package uz.fayz.model;

import lombok.*;
import uz.fayz.enums.StepEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

//    private Integer id;

    private String chatId;

    private String fullName;

    private String phoneNumber;

    private Integer age;

    private String teacherName;

    private String school;

    private Integer classNumber;

    private Integer testNumber;

    private Integer addressId;

    private StepEnum step;
}
