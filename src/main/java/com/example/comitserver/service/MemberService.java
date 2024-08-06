package com.example.comitserver.service;

import com.example.comitserver.domain.Member;
import com.example.comitserver.dto.MemberDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public interface MemberService {
    Member saveEntity(Member member);

    Member saveDto(MemberDto memberDto);

    Member findByUsername(String username);

    List<Member> findAll();

}
