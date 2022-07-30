package com.nashtech.assetmanagement.mapper;

import com.nashtech.assetmanagement.dto.request.RequestCategoryDTO;
import com.nashtech.assetmanagement.dto.response.ResponseCategoryDTO;
import com.nashtech.assetmanagement.entities.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.modelmapper.TypeToken;

import java.util.List;

@Component
public class CategoryMapper {

    private final ModelMapper mapper;


    public CategoryMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ResponseCategoryDTO categoryToResponseCategoryDTO(Category category){
        return mapper.map(category,ResponseCategoryDTO.class);
    }

    public List<ResponseCategoryDTO> ListCategoriesToListResponseCategories(List<Category> categories){
        return  mapper.map(categories,
                new TypeToken<List<ResponseCategoryDTO>>() {
                }.getType());
    }
    public Category RequestCategoryToCategory(RequestCategoryDTO requestCategoryDTO){
        return mapper.map(requestCategoryDTO,Category.class);
    }
}
