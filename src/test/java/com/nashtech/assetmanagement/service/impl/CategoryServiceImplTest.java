package com.nashtech.assetmanagement.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nashtech.assetmanagement.dto.request.RequestCategoryDTO;
import com.nashtech.assetmanagement.dto.response.ResponseCategoryDTO;
import com.nashtech.assetmanagement.entities.Category;
import com.nashtech.assetmanagement.exception.NotUniqueException;
import com.nashtech.assetmanagement.mapper.CategoryMapper;
import com.nashtech.assetmanagement.repositories.CategoryRepository;

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
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void createCategory_WhenRequestValid_Expect_ReturnCategory(){
        RequestCategoryDTO requestCategoryDTO=mock(RequestCategoryDTO.class);
        when(categoryMapper.RequestCategoryToCategory(requestCategoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        ResponseCategoryDTO expected=mock(ResponseCategoryDTO.class);
        when(categoryMapper.categoryToResponseCategoryDTO(category)).thenReturn(expected);
        ResponseCategoryDTO actual=categoryServiceImpl.createCategory(requestCategoryDTO);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void createCategory_WhenCategoryPrefixNotUnique_Expect_ReturnCategory(){
        RequestCategoryDTO requestCategoryDTO=new RequestCategoryDTO("LT","Laptop");
        when(categoryRepository.existsCategoriesById("LT")).thenReturn(true);
        NotUniqueException exception= assertThrows(NotUniqueException.class,
                () -> categoryServiceImpl.createCategory(requestCategoryDTO));
        assertThat(exception.getMessage()).isEqualTo("Please enter a different category. " +
                "prefix is already existed.");
    }
}
