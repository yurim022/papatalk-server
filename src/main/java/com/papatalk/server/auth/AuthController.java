package com.papatalk.server.auth;

import com.papatalk.server.auth.dto.LoginDto;
import com.papatalk.server.auth.dto.TokenDto;
import com.papatalk.server.common.jwt.JwtFilter;
import com.papatalk.server.common.jwt.TokenProvider;
import com.papatalk.server.common.format.ApiResponse;
import com.papatalk.server.common.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    /**
     * 회원가입
     * @return
     */
    @PostMapping(value = "/register")
    public ApiResponse register(@RequestBody HashMap requestParam) {
        // TODO: @Valid 이용해서 requestParam 검증
        ApiResponse result = new ApiResponse();
        result.setResultCode(Constants.RESULT_CODE_OK);
        result.setResultMsg(Constants.RESULT_MSG_OK);

        return result;
    }

    /**
     * 로그인
     * Authorization 토큰 발급
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        //TODO: AuthService로 빼기

        log.info("in authenticate controller");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getPhoneNumber(), loginDto.getPassword());
        log.info("userNamePasswordAutenticationToken 발급");

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication builder 로 authentication 생성");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("set Authentication");

        String jwt = tokenProvider.createToken(authentication);
        log.info("create token success");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
