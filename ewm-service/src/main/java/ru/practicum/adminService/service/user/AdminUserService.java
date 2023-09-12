package ru.practicum.adminService.service.user;

import ru.practicum.baseService.dto.user.NewUserRequest;
import ru.practicum.baseService.dto.user.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);
}