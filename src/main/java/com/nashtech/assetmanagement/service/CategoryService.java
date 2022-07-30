package com.nashtech.assetmanagement.service;

import com.nashtech.assetmanagement.dto.request.RequestCategoryDTO;
import com.nashtech.assetmanagement.dto.response.ResponseCategoryDTO;

import java.util.List;

public interface CategoryService {
    List<ResponseCategoryDTO> getAllCategory();

    ResponseCategoryDTO createCategory(RequestCategoryDTO requestCategoryDTO);
}
