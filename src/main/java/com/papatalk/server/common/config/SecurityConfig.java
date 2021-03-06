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
                .httpBasic().disable()  //?????? ????????? ??????????????? ?????????????????? default
                //token ??????????????? disable
                .csrf().disable()
                //??????????????? ?????? ???????????? ????????? ??????


                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() //?????? ????????? ??????????????? ?????? ?????? ?????? ??????

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                //????????? ???????????? ?????? ????????? ?????? ????????? STATELESS??? ??????
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }

    @Override
    public void configure(WebSecurity web){
        //????????? ?????? URI ??? ????????????
        web.ignoring()
                .antMatchers(
                        "/postgresql-console/**"
                        ,"/favicon.ico"
                );
        //static ??????????????? ?????? ?????? ????????? ?????? ?????? ???
        // web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");

    }
}
