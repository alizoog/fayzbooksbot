package uz.fayz.model;
import lombok.*;
import uz.fayz.enums.RoleEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String password;
    private RoleEnum roleEnum;
}
