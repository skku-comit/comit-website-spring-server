package com.example.comitserver.service;

import com.example.comitserver.domain.Member;
import com.example.comitserver.dto.LoginDto;
import com.example.comitserver.dto.MemberDto;
import com.example.comitserver.dto.RegisterDto;
import com.example.comitserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member saveEntity(Member member) {
        return (Member) memberRepository.save(member);
    }

    @Override
    public Member saveDto(MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .build();
        return saveEntity(member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    //로그인 구현
    // dto를 통해 넘어온 정보가 db랑 일치하면 true
    public boolean login(LoginDto loginDto){
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        Member byUsername = memberRepository.findByUsername(username);
        if(byUsername != null) {
            return byUsername.getPassword().equals(password);
        }
        return false;
    }

    public Member register(RegisterDto registerDto) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        Member member = Member.builder()
                .username(registerDto.getUsername())
                .password(registerDto.getPassword())
                .build();
        return saveEntity(member);
    }
}
