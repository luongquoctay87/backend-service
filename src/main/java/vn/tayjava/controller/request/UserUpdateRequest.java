package vn.tayjava.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.tayjava.common.Gender;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class UserUpdateRequest implements Serializable {

    @NotNull(message = "id must be not null")
    @Min(value = 1, message = "userId must be equals or greater than 1")
    private Long id;

    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "firstName must be not blank")
    private String lastName;
    private Gender gender;
    private Date birthday;
    private String username;

    @Email(message = "Email invalid")
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
}
