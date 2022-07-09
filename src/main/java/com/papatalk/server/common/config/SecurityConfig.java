package com.papatalk.server.common.config;

import com.papatalk.server.common.jwt.JwtAccessDeniedHandler;
import com.papatalk.server.common.jwt.JwtAuthenticationEntryPoint;
import com.papatalk.server.common.jwt.JwtSecurityConfig;
import com.papatalk.server.common.jwt.TokenProvider;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()  //유저 이름과 비밀번호로 로그인하는게 default
                //token 사용하므로 disable
                .csrf().disable()
                //예외처리를 위해 만들었던 코드를 지정


                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() //해당 요청은 권한검증을 하지 않고 접근 가능

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                //세션을 사용하지 않기 때문에 세션 설정을 STATELESS로 지정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }

    @Override
    public void configure(WebSecurity web){
        //원하는 호출 URI 에 권한허용
        web.ignoring()
                .antMatchers(
                        "/postgresql-console/**"
                        ,"/favicon.ico"
                );
        //static 디렉토리의 하위 파일 목록은 인증 무시 예
        // web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");

    }
}
