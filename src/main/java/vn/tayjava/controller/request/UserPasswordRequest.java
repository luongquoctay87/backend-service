package vn.tayjava.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserPasswordRequest implements Serializable {
    @NotNull(message = "id must be not null")
    @Min(value = 1, message = "userId must be equals or greater than 1")
    private Long id;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;
}
