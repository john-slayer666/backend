package john_slayer.backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDTO {
    private String id;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name should not exceed 100 characters")
    private String fullName;

    private String avatarUrl;

    @NotBlank(message = "Password is required")
    private String password;
}



