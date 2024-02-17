package uz.fayz.service;

import uz.fayz.enums.StepEnum;
import uz.fayz.model.User;
import uz.fayz.repository.UserRepository;

public class UserService {
    private static final UserRepository userRepository = new UserRepository();
    public boolean isUserAlreadyExist(String chatId) {
        return userRepository.existUserByChatId(chatId);
    }

    public User getUserByChatId(String chatId) {
        return userRepository.getUserByChatId(chatId);
    }

    public void editUser(User newUser, StepEnum step) {
        userRepository.editUser(newUser, step);
    }

    public void save(User newUser, StepEnum step) {
        userRepository.save(newUser, step);
    }

    public void userChangeStatus(String chatId, StepEnum stepEnum) {
        userRepository.userChangeStatus(chatId,stepEnum);
    }
}
