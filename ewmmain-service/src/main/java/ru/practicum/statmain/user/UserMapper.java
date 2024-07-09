package ru.practicum.statmain.user;

import ru.practicum.statmain.user.dto.UserCreateRequest;
import ru.practicum.statmain.user.dto.UserResponse;
import ru.practicum.statmain.user.dto.UserShortResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toEntity(UserCreateRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }

    public static List<UserResponse> toResponse(List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static UserShortResponse toUserShortDto(User user) {
        return new UserShortResponse(user.getId(), user.getName());
    }

    public static List<UserShortResponse> toUserShortDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserShortDto)
                .collect(Collectors.toList());
    }

}
