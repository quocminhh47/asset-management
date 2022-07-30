package com.nashtech.assetmanagement.controller.rest.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.request.RequestCategoryDTO;
import com.nashtech.assetmanagement.dto.response.ResponseCategoryDTO;
import com.nashtech.assetmanagement.service.CategoryService;

@RestController
@RequestMapping("/admin/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseCategoryDTO createCategory(@Valid @RequestBody RequestCategoryDTO requestCategoryDTO){
        return categoryService.createCategory(requestCategoryDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseCategoryDTO> getAllCategories(){
        return categoryService.getAllCategory();
    }
}
