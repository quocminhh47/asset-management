package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.CreateAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.EditAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.GetAssetListRequestDto;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.*;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.AssignmentRepository;
import com.nashtech.assetmanagement.repositories.CategoryRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AssetService;
import com.nashtech.assetmanagement.service.AuthenticationServices;
import com.nashtech.assetmanagement.utils.GenerateRandomNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {

	private final AssetRepository assetRepository;

	private final CategoryRepository categoryRepository;

	private final UserRepository userRepository;

	private final AssetMapper assetMapper;

	private final AssignmentRepository assignmentRepository;

	private final AuthenticationServices authenticationService;

	public String generateAssetCode(String prefix) {
		String code;
		do {
			code = prefix + GenerateRandomNumber.randomNumber();
		} while (assetRepository.existsAssetByCode(code));
		return code;
	}

	@Override
	public ResponseAssetDto createAsset(CreateAssetRequestDto requestCreateAsset) {
		Users users = authenticationService.getUser();
		Asset asset = assetMapper.requestAssetToAsset(requestCreateAsset);
		asset.setCode(generateAssetCode(requestCreateAsset.getCategoryId()));
		Category category = categoryRepository.findById(requestCreateAsset.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Can not find category has code: " + requestCreateAsset.getCategoryId()));
		asset.setCategory(category);
		asset.setLocation(users.getLocation());
		asset.setUser(users);
		asset = assetRepository.save(asset);
		return assetMapper.assetToResponseAssetDTO(asset);
	}

	@Override
	public ListAssetResponseDto getListAsset(GetAssetListRequestDto dto) {
		Sort.Direction sort = Sort.Direction.ASC;
		if (dto.getSortDirection().equals("DESC")) {
			sort = Sort.Direction.DESC;
		}
		Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by(sort, dto.getSortBy()));
		Page<Asset> pageAsset = null;
		long totalItems = 0;
		Optional<Users> optionalUsers = userRepository.findById(dto.getUserId());
		if (optionalUsers.isEmpty()) {
			throw new ResourceNotFoundException(String.format("user.not.found.with.code:%s", dto.getUserId()));
		}
		if (dto.getStates().size() == 0 && dto.getCategoryIds().size() == 0) {
			pageAsset = assetRepository.getListAssetBySearchs(dto.getUserId(), dto.getKeyword(), pageable);
			totalItems = pageAsset.getTotalPages();
		} else if (dto.getStates().size() > 0) {
			List<AssetState> assetState = assetMapper.mapperListStates(dto.getStates());
			if (dto.getCategoryIds().size() == 0) {
				pageAsset = assetRepository.getListAssetByState(dto.getUserId(), assetState, dto.getKeyword(),
						pageable);
				totalItems = pageAsset.getTotalPages();
			} else {
				pageAsset = assetRepository.getListAsset(dto.getUserId(), dto.getCategoryIds(), assetState,
						dto.getKeyword(), pageable);
				totalItems = pageAsset.getTotalPages();
			}
		} else if (dto.getStates().size() == 0) {
			pageAsset = assetRepository.getListAssetByCategory(dto.getUserId(), dto.getCategoryIds(), dto.getKeyword(),
					pageable);
			totalItems = pageAsset.getTotalPages();
		}

		List<Asset> listAsset = pageAsset.getContent();
		List<AssetResponseDto> list = assetMapper.mapperListAsset(listAsset);
		return new ListAssetResponseDto(list, totalItems);
	}

	@Override
	public ListSearchingAssetResponseDto getAssetByCodeOrNameAndLocationCode(String text) {
		Users users = authenticationService.getUser();
		Location location = users.getLocation();
		List<Asset> assetList = assetRepository.findAssetByNameOrCodeAndLocationCode(text.toLowerCase(),
				location.getCode());
		return assetMapper.getAssetListToResponseAssetDTOList(assetList);
	}

	@Override
	public EditAssetResponseDto editAsset(EditAssetRequestDto editAssetRequest, String assetCode) {
		Asset asset = assetRepository.findById(assetCode)
				.orElseThrow(() -> new ResourceNotFoundException("Asset." + assetCode + ".not.found"));

		if (asset.getState().equals(AssetState.ASSIGNED)) {
			throw new IllegalStateException("Asset." + assetCode + ".is.being.assigned.Cannot modify");
		}
		Asset mappedAsset = assetMapper.mapEditAssetRequestToEntity(editAssetRequest, asset);
		Asset savedAsset = assetRepository.save(mappedAsset);
		return assetMapper.mapToEditAssetResponse(savedAsset);
	}

	// 582 - Delete asset
	@Override
	public MessageResponse deleteAssetByAssetCode(String assetCode) {
		Asset asset = assetRepository.findById(assetCode)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot find asset with asset code: " + assetCode));
		List<Assignment> assignmentList = assignmentRepository.findByAsset(asset);
		if (!assignmentList.isEmpty()) {
			return new MessageResponse(HttpStatus.CONFLICT, "Asset belongs to one or more historical assignments",
					new Date());
		}
		assetRepository.delete(asset);
		return new MessageResponse(HttpStatus.OK, "Delete asset successfully!", new Date());
	}

	@Override
	public AssetReportResponseDto getAssetReportList(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<IAssetReportResponseDto> assetReportPage = assetRepository.getAssetReportList(pageable);
		return assetMapper.mapToAssetReportDto(assetReportPage);
	}

	@Override
	public List<IAssetReportResponseDto> getAllAssetReport() {
		return assetRepository.getAssetReportList();
	}

}
