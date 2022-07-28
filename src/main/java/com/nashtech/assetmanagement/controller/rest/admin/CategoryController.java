package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.request.RequestCategoryDTO;
import com.nashtech.assetmanagement.dto.response.ResponseCategoryDTO;
import com.nashtech.assetmanagement.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseCategoryDTO createCategory(@Valid @RequestBody RequestCategoryDTO requestCategoryDTO){
        return categoryService.createCategory(requestCategoryDTO);
    }

    @GetMapping
    public List<ResponseCategoryDTO> getAllCategories(){
        return categoryService.getAllCategory();
    }
}
