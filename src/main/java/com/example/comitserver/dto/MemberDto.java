package com.example.comitserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;

    @NotBlank(message = "이름을 입력하세요") // null, empty, whitespace check
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
}
