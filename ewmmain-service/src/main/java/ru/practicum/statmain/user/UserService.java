package ru.practicum.statmain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.user.dto.UserCreateRequest;
import ru.practicum.statmain.user.dto.UserResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse addUser(UserCreateRequest request) {
        User user = UserMapper.toEntity(request);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.toResponse(users);
    }
}
