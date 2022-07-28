package com.nashtech.assetmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.AssetState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.mapper.AssetMapper;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import com.nashtech.assetmanagement.repositories.CategoryRepository;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.service.AssetService;
import com.nashtech.assetmanagement.utils.TotalPages;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AssetMapper assetMapper;

	@Override
	public ListAssetResponseDto getListAsset(String userId, int pageNumber, int size, String categoryId,
			String assetCode, String assetName, List<String> state) {
		Pageable pageable = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "assetCode"));
		Page<Asset> pageAsset = null;
		long totalItems = 0;
		Optional<Users> optionalUsers = userRepository.findById(userId);

		if (!optionalUsers.isPresent()) {
			throw new ResourceNotFoundException(String.format("User not found with staff code : %s", userId));
		}

		Users user = optionalUsers.get();

		List<AssetState> assetState = new ArrayList<>();
		if (state.size() > 0) {
			for (int i = 0; i < state.size(); i++) {
				assetState.add(AssetState.valueOf(state.get(i)));
			}
		}

		if (categoryId.equals("")) {
			if (state.size() == 0) {
				pageAsset = assetRepository.findByUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
						user, assetCode, assetName, pageable);
				totalItems = assetRepository
						.countByUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(user, assetCode,
								assetName);
			} else {
				pageAsset = assetRepository
						.findByStateInAndUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
								assetState, user, assetCode, assetName, pageable);
				totalItems = assetRepository
						.countByStateInAndUserAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
								assetState, user, assetCode, assetName);
			}
		} else {
			Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
			if (!optionalCategory.isPresent()) {
				throw new ResourceNotFoundException(
						String.format("Category not found with category code : %s", categoryId));
			} else {
				Category category = optionalCategory.get();
				if (state.size() != 0) {
					pageAsset = assetRepository
							.findByStateInAndUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
									assetState, user, category, assetCode, assetName, pageable);

					totalItems = assetRepository
							.countByStateInAndUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
									assetState, user, category, assetCode, assetName);
				} else {
					pageAsset = assetRepository
							.findByUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(user,
									category, assetCode, assetName, pageable);
					totalItems = assetRepository
							.countByUserAndCategoryAndAssetCodeContainingIgnoreCaseOrAssetNameContainingIgnoreCase(
									user, category, assetCode, assetName);
				}
			}
		}

		List<Asset> dto = pageAsset.getContent();
		List<AssetResponseDto> list = assetMapper.mapperListAsset(dto);
		ListAssetResponseDto result = new ListAssetResponseDto(list, TotalPages.totalPages(totalItems, size));
		return result;
	}

	@Override
	public AssetResponseDto getOne(String assetId) {
		Optional<Asset> optional = assetRepository.findById(assetId);
		if (!optional.isPresent()) {
			throw new ResourceNotFoundException(String.format("Asset not found with asset code : %s", assetId));
		}
		Asset asset = optional.get();
		AssetResponseDto dto = modelMapper.map(asset, AssetResponseDto.class);
		return dto;
	}
}
