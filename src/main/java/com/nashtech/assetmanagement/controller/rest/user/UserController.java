package com.nashtech.assetmanagement.controller.rest.user;

import com.nashtech.assetmanagement.dto.request.ChangePassRequestDto;
import com.nashtech.assetmanagement.dto.request.FirstLoginRequestDto;
import com.nashtech.assetmanagement.dto.request.UserRequestDto;
import com.nashtech.assetmanagement.dto.response.LocationResponseDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
import com.nashtech.assetmanagement.dto.response.UserResponseDto;
import com.nashtech.assetmanagement.dto.response.UserContentResponseDto;
import com.nashtech.assetmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RequestMapping("")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/admin/api/create")
    @ResponseStatus(HttpStatus.OK)
    public UserContentResponseDto createNewUser(@RequestBody @Valid UserRequestDto user){
       return userService.createNewUser(user);
    }

    @PutMapping("/admin/api/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserContentResponseDto editUser(@RequestBody @Valid UserRequestDto user, @PathVariable("id") String staffCode){
        return userService.editUser(user,staffCode);
    }

    @GetMapping("/admin/api/location/{staffCode}")
    public ResponseEntity<LocationResponseDto> getLocationByStaffCode(@PathVariable("staffCode")String id){
        return ResponseEntity.ok(this.userService.getLocationByStaffCode(id));
    }

    @GetMapping("admin/api/searchUser/{location}")
    public ResponseEntity<HashMap> getUserListByStaffCodeOrName(@RequestParam("text")String text,@PathVariable("location")String locationCode){
        HashMap hashMap = new HashMap();
        List<UserContentResponseDto> result = this.userService.getUsersByStaffCodeOrNameAndLocationCode(text,locationCode);
        hashMap.put("list_user",result);
        hashMap.put("total",result.size());
        return ResponseEntity.ok(hashMap);
    }

    @PostMapping("/user/api/first-login")
    public ResponseEntity<?> changePasswordFirstLogin(@Valid @RequestBody FirstLoginRequestDto requestFirstLogin) {
        MessageResponse responseMessage=
                userService.changePasswordFirstLogin(requestFirstLogin.getUserName(),
                        requestFirstLogin.getNewPassword());

        return new ResponseEntity<>(responseMessage,responseMessage.getStatus());
    }
    @PostMapping("/user/api/change-password")
    public ResponseEntity<UserResponseDto> changePassword(@Valid @RequestBody ChangePassRequestDto requestChangePassDto){
        return ResponseEntity.ok(userService.changePassword(requestChangePassDto));
    }
}
