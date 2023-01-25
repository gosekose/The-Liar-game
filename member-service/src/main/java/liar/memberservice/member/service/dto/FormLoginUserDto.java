package liar.memberservice.member.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormLoginUserDto {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @Builder
    public FormLoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
