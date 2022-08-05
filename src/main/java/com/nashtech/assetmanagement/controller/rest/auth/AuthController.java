package com.nashtech.assetmanagement.controller.rest.auth;

import com.nashtech.assetmanagement.dto.request.LoginRequestDto;
import com.nashtech.assetmanagement.dto.response.JwtResponseDto;
import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.enums.UserState;
import com.nashtech.assetmanagement.exception.ResourceNotFoundException;
import com.nashtech.assetmanagement.exception.UnauthorizedException;
import com.nashtech.assetmanagement.repositories.UserRepository;
import com.nashtech.assetmanagement.sercurity.jwt.JwtUtils;
import com.nashtech.assetmanagement.sercurity.userdetail.UserPrinciple;
import com.nashtech.assetmanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/api/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {
    @Autowired
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequestDto requestLoginDTO) {
        Users user = userRepository.findByUserName(requestLoginDTO.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException(("Username is not found")));

        if(user.getState().equals(UserState.INACTIVE))  throw new UnauthorizedException(("Account is not active"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestLoginDTO.getUserName(),
                        requestLoginDTO.getPassword()));

        // if go there, the user/password is correct
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // generate jwt to return to client
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        List<String> roles = userPrinciple.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDto(
                jwt,
                "Bearer",
                userPrinciple.getStaffCode(),
                userPrinciple.getUsername(),
                userPrinciple.getState(),
                roles,
                user.getLocation().getCode()
        ));
    }
}
