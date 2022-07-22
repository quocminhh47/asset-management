package com.nashtech.assetmanagement.sercurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.assetmanagement.dto.response.ResponseErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error(authException.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseErrorMessage responseErrorMessage=
                new ResponseErrorMessage(HttpStatus.FORBIDDEN,"Access denied",
                        authException.getMessage(),new Date());
        String accessDeniedResponse = objectMapper.writeValueAsString(responseErrorMessage);
        log.warn(responseErrorMessage.toString());
        response.getWriter().print(accessDeniedResponse);
    }
}
