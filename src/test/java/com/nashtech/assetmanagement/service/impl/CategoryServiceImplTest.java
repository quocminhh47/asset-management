package com.nashtech.assetmanagement.service.impl;

import com.nashtech.assetmanagement.dto.request.RequestCategoryDTO;
import com.nashtech.assetmanagement.dto.response.ResponseCategoryDTO;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.mapper.CategoryMapper;
import com.nashtech.assetmanagement.repositories.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private Category category;

    private CategoryServiceImpl categoryServiceImpl;

    @BeforeEach
    void setUp(){
        category=mock(Category.class);
        categoryRepository = mock(CategoryRepository.class);
        categoryMapper = mock(CategoryMapper.class);
        categoryServiceImpl=new CategoryServiceImpl(categoryRepository,categoryMapper);
    }

    @Test
    public void getAllCategory_WhenRequestValid_Expect_ReturnListCategory(){
        List<Category> categories=mock(List.class);
        List<ResponseCategoryDTO> expected=mock(List.class);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.ListCategoriesToListResponseCategories(categories)).thenReturn(expected);
        List<ResponseCategoryDTO> actual =categoryServiceImpl.getAllCategory();
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void createCategory_WhenRequestValid_Expect_ReturnCategory(){
        RequestCategoryDTO requestCategoryDTO=mock(RequestCategoryDTO.class);
        when(categoryMapper.RequestCategoryToCategory(requestCategoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        ResponseCategoryDTO expected=mock(ResponseCategoryDTO.class);
        when(categoryMapper.categoryToResponseCategoryDTO(category)).thenReturn(expected);
        ResponseCategoryDTO actual=categoryServiceImpl.createCategory(requestCategoryDTO);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}
