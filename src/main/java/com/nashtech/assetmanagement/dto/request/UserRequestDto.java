package com.nashtech.assetmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.assetmanagement.constraint.BirthDay;
import com.nashtech.assetmanagement.constraint.JoinedDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@JoinedDate(before = "birthDate", after = "joinedDate",message = "joinedDate must be after birthDate")
public class UserRequestDto {
    @Size(max = 128)
    private String firstName;
    @Size(max = 128)
    private String lastName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date joinedDate;
    @NotNull(message = "birthDate must not be null")
    @BirthDay(message = "birthDate must greater than 18")
    @Past(message = "birthDate must be in the past")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate;
    @NotNull
    private Boolean gender;
    @NotEmpty
    private String locationName;
    @NotEmpty
    private String roleName;
}