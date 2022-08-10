package com.nashtech.assetmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListSearchingUserResponseDto {
    private int total;
    @JsonProperty("list_user")
    private List<UserContentResponseDto> listUser;
}
