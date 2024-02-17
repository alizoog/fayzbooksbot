package uz.fayz.repository;

import uz.fayz.enums.StepEnum;
import uz.fayz.model.User;
import uz.fayz.repository.mapper.MyMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository extends BaseDatabase {
    private final MyMapper mapper = new MyMapper();

    public boolean existUserByChatId(String chatId) {
        String id = null;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT chat_id FROM users WHERE chat_id = ?");
            ps.setString(1,chatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

            id = rs.getString("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id != null;
    }

    public User getUserByChatId(String chatId) {
        User user = null;
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE chat_id = ?");
            ps.setString(1,chatId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user = mapper.userMapper(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void editUser(User newUser, StepEnum step) {
        try (Connection connection = connection()) {
            int age = newUser.getAge() == null ? 0 :newUser.getAge();
            String query = makeQuery(newUser);
            int classNumber = newUser.getClassNumber() == null ? 0 :newUser.getClassNumber();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,newUser.getFullName());
            ps.setString(2,newUser.getSchool());
            ps.setInt(3,classNumber);
            ps.setString(4,newUser.getTeacherName());
            ps.setString(5,step.toString());
            ps.setInt(6,age);
            ps.setString(7,newUser.getPhoneNumber());
            ps.setInt(8,newUser.getTestNumber());
            if (newUser.getAddressId() != null){
                ps.setInt(9,newUser.getAddressId());
                ps.setString(10,newUser.getChatId());
            }
            else ps.setString(9,newUser.getChatId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String makeQuery(User newUser) {
        StringBuilder sb = new StringBuilder("UPDATE users set full_name = ?, school = ?, class_number = ?, teacher_name = ?, step_enum = ?, age = ?, phone_number = ?, test_number = ?");
        if (newUser.getAddressId() != null) sb.append(", address_id = ? ");
        sb.append(" where chat_id = ?");
        return sb.toString();
    }

    public void save(User newUser, StepEnum step) {
        try (Connection connection = connection()) {
            int age = newUser.getAge() == null ? 0 :newUser.getAge();
            int classNumber = newUser.getClassNumber() == null ? 0 :newUser.getClassNumber();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (full_name, school, class_number, teacher_name, step_enum, age, phone_number, chat_id) values (?,?,?,?,?,?,?,?);");
            ps.setString(1,newUser.getFullName());
            ps.setString(2,newUser.getSchool());
            ps.setInt(3,classNumber);
            ps.setString(4,newUser.getTeacherName());
            ps.setString(5,step.toString());
            ps.setInt(6,age);
            ps.setString(7,newUser.getPhoneNumber());
            ps.setString(8,newUser.getChatId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userChangeStatus(String chatId, StepEnum stepEnum) {
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE users set  step_enum = ? where chat_id = ?");
            ps.setString(1,stepEnum.toString());
            ps.setString(2,chatId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
