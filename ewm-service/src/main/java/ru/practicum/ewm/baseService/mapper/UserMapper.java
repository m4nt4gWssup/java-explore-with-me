package ru.practicum.ewm.baseService.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.baseService.dto.user.NewUserRequest;
import ru.practicum.ewm.baseService.dto.user.UserDto;
import ru.practicum.ewm.baseService.dto.user.UserShortDto;
import ru.practicum.ewm.baseService.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class UserMapper {

    public User toEntity(NewUserRequest dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserDto toUserDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    public UserShortDto toUserShortDto(User entity) {
        return UserShortDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}