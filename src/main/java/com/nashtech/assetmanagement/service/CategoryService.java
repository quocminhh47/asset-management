package com.nashtech.assetmanagement.service;

import java.util.List;

import com.nashtech.assetmanagement.dto.request.CategoryRequestDto;
import com.nashtech.assetmanagement.dto.response.CategoryResponseDto;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategory();

    CategoryResponseDto createCategory(CategoryRequestDto requestCategoryDTO);
}
