package uz.fayz.repository.mapper;

import uz.fayz.enums.StepEnum;
import uz.fayz.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyMapper {
    public User userMapper(ResultSet rs) throws SQLException {
        return User.builder()
                .age(rs.getInt("age"))
                .step(StepEnum.valueOf(rs.getString("step_enum")))
                .chatId(rs.getString("chat_id"))
                .school(rs.getString("school"))
                .classNumber(rs.getInt("class_number"))
                .fullName(rs.getString("full_name"))
                .phoneNumber(rs.getString("phone_number"))
                .teacherName(rs.getString("teacher_name"))
                .testNumber(rs.getInt("test_number"))
                .build();
    }
}
