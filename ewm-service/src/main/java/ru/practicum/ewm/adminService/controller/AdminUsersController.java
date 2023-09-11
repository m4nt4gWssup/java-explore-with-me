package ru.practicum.ewm.adminService.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminService.service.user.AdminUserService;
import ru.practicum.ewm.baseService.dto.user.NewUserRequest;
import ru.practicum.ewm.baseService.dto.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final AdminUserService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /admin/users");
        return service.getAll(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Получен запрос POST /admin/users c новым пользователем: {}", newUserRequest.getEmail());
        return service.save(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Получен запрос DELETE /admin/users/{}", userId);
        service.delete(userId);
    }
}