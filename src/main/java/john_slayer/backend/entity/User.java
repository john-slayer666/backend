package john_slayer.backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Data
public class User {
    @Id
    private String id;
    private String email;
    private String passwordHash;
    private String fullName;
    private String avatarUrl;
}
