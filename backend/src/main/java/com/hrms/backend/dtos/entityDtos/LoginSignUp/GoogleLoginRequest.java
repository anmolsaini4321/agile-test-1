package com.hrms.backend.dtos.entityDtos.LoginSignUp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleLoginRequest {
    @NotNull(message = "token is required")
    @NotBlank(message = "token cannot be empty")
    private String idToken;
}
