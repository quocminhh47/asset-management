package com.nashtech.assetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCategoryDTO {

    @NotEmpty(message = "Category code must not be empty.")
    @Size(min = 2,max = 2, message = "Length of category code  must equals 2")
    private String id;

    @NotEmpty(message = "Category name must not be empty.")
    private String name;
}
