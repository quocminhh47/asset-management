package com.nashtech.assetmanagement.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nashtech.assetmanagement.dto.request.EditAssetRequest;
import com.nashtech.assetmanagement.dto.response.*;
import com.nashtech.assetmanagement.entities.*;
import com.nashtech.assetmanagement.repositories.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.service.AssetService;
import com.nashtech.assetmanagement.utils.GenerateRandomNumber;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {

	private final AssetRepository assetRepository;

	private final CategoryRepository categoryRepository;

	private final UserRepository userRepository;

	private final LocationRepository locationRepository;

	private final AssetMapper assetMapper;

	private final AssignmentRepository assignmentRepository;

	public String generateAssetCode(String prefix) {
		String code;
		do {
			code = prefix + GenerateRandomNumber.randomNumber();
		} while (assetRepository.existsAssetByCode(code));
		return code;
	}

	@Override
	public ResponseAssetDTO createAsset(RequestCreateAsset requestCreateAsset) {
		Asset asset = assetMapper.RequestAssetToAsset(requestCreateAsset);
		asset.setCode(generateAssetCode(requestCreateAsset.getCategoryId()));
		Category category = categoryRepository.findById(requestCreateAsset.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Can not find category has code: " + requestCreateAsset.getCategoryId()));
		asset.setCategory(category);
		Location location = locationRepository.findById(requestCreateAsset.getLocationId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Can not find " + "category has code: " + requestCreateAsset.getLocationId()));
		asset.setLocation(location);
		Users users = userRepository.findById(requestCreateAsset.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Can not find " + "user has id: " + requestCreateAsset.getUserId()));
		asset.setUser(users);
		asset = assetRepository.save(asset);
		return assetMapper.assetToResponseAssetDTO(asset);
	}

	@Override
	public ListAssetResponseDto getListAsset(String userId, List<String> categoryId, List<String> state, String keyword,
			String sortBy, String sortDirection, Integer page, Integer size) {
		Sort.Direction sort = Sort.Direction.ASC;
		if(sortDirection.equals("DESC")) {
			sort = Sort.Direction.DESC;
		}
		Pageable pageable = PageRequest.of(page - 1, size , Sort.by(sort, sortBy));
		Page<Asset> pageAsset = null;
		long totalItems = 0;
		Optional<Users> optionalUsers = userRepository.findById(userId);
		if (!optionalUsers.isPresent()) {
			throw new ResourceNotFoundException(String.format("user.not.found.with.code:%s", userId));
		}
		Users user = optionalUsers.get();
		List<AssetState> assetState = new ArrayList<>();

		if (state.size() == 0 && categoryId.size() == 0) {
			pageAsset = assetRepository.findByUser(user, pageable);
			totalItems = pageAsset.getTotalPages();
		} else if (state.size() > 0) {
			for (int i = 0; i < state.size(); i++) {
				assetState.add(AssetState.valueOf(state.get(i)));
			}
			if (categoryId.size() == 0) {
				pageAsset = assetRepository.getListAssetByState(userId, assetState, keyword, pageable);
				totalItems = pageAsset.getTotalPages();
			} else {
				pageAsset = assetRepository.getListAsset(userId, categoryId, assetState, keyword, pageable);
				totalItems = pageAsset.getTotalPages();
			}
		} else if (state.size() == 0) {
			pageAsset = assetRepository.getListAssetByCategory(userId, categoryId, keyword, pageable);
			totalItems = pageAsset.getTotalPages();
		}

		List<Asset> dto = pageAsset.getContent();
		List<AssetResponseDto> list = assetMapper.mapperListAsset(dto);
		ListAssetResponseDto result = new ListAssetResponseDto(list, totalItems);
		return result;
	}

	@Override
	public List<ResponseAssetAndCategory> getAssetByCodeOrNameAndLocationCode(String text, String locationCode) {
		Optional<Location> locationOptional = locationRepository.findById(locationCode);
		if (locationOptional.isEmpty()) {
			throw new ResourceNotFoundException("Location code not found");
		}
		List<Asset> assetList = assetRepository.findAssetByNameOrCodeAndLocationCode(text.toLowerCase(), locationCode);
		List<ResponseAssetAndCategory> responseList = assetMapper.getAssetListToResponseAssetDTOList(assetList);
		return responseList;
	}

	@Override
	public EditAssetResponse editAsset(EditAssetRequest editAssetRequest, String assetCode) {
		Asset asset = assetRepository.findById(assetCode).orElseThrow(
				() -> new ResourceNotFoundException("Asset." + assetCode + ".not.found"));

		if(asset.getState().equals(AssetState.ASSIGNED))
			throw new IllegalStateException("Asset."+ assetCode + ".is.being.assigned.Cannot modify");

		Asset mappedAsset = assetMapper.mapEditAssetRequestToEntity(editAssetRequest, asset);
		Asset savedAsset = assetRepository.save(mappedAsset);
		return assetMapper.mapToEditAssetResponse(savedAsset);
	}

	//582 - Delete asset
	@Override
	public ResponseMessage deleteAssetByAssetCode(String assetCode) {
		Asset asset = assetRepository.findById(assetCode).orElseThrow(
				() -> new ResourceNotFoundException("Cannot find asset with asset code: " + assetCode));
		List<Assignment> assignmentList = assignmentRepository.findByAsset(asset);
		if (!assignmentList.isEmpty()) {
			return new ResponseMessage(HttpStatus.CONFLICT, "Asset belongs to one or more historical assignments", new Date());
		}
		assetRepository.delete(asset);
		return new ResponseMessage(HttpStatus.OK, "Delete asset successfully!", new Date());
	}
}
