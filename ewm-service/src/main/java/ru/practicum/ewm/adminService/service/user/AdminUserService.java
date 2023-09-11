package ru.practicum.ewm.adminService.service.user;

import ru.practicum.ewm.baseService.dto.user.NewUserRequest;
import ru.practicum.ewm.baseService.dto.user.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);
}