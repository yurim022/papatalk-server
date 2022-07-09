package com.papatalk.server.auth.service;

import com.papatalk.server.auth.entitiy.Member;
import com.papatalk.server.auth.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository userRepository) {
        this.memberRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String phoneNumber) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByPhoneNumber(phoneNumber)
                .map(member -> createUser(phoneNumber, member))
                .orElseThrow(()-> new UsernameNotFoundException(phoneNumber + "에 해당하는 멤버가 없습니다."));
    }

    private User createUser(String phoneNumber, Member user){
        if (!user.isUseYn()){
            throw new RuntimeException(phoneNumber + " -> 활성화되어 있지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(),
                user.getPassword(),
                grantedAuthorities);
    }

}

