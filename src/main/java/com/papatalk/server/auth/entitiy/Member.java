package com.papatalk.server.auth.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "mbr_bas")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbr_seq")
    private Long memberSeq;

    @Column(length = 50)
    private String email;
    @Column(length = 256,nullable = false)
    private String password;
    @Column(name = "phone_no",length = 15,nullable = false, unique = true)
    private String phoneNumber;
    @Column(name = "fcm_token",columnDefinition = "TEXT")
    private String fcmTocken;
    private Long failCnt; //로그인 실패 횟수
    @CreatedDate @Column(name = "cret_date")
    private LocalDateTime createdDate;
    @LastModifiedDate @Column(name = "updt_date")
    private LocalDateTime updatedDate;
    @Column(name = "use_yn",nullable = false)
    private boolean useYn; //사용여부

    //jwt 를 위해 추가된 필드
    @ManyToMany
    @JoinTable(
            name = "mbr_authority",
            joinColumns = {@JoinColumn(name = "mbr_seq", referencedColumnName = "mbr_seq")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


}
