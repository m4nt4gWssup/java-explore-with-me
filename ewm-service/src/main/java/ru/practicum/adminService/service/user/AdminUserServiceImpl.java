package ru.practicum.adminService.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.baseService.dao.UserRepository;
import ru.practicum.baseService.dto.user.NewUserRequest;
import ru.practicum.baseService.dto.user.UserDto;
import ru.practicum.baseService.exception.ConflictException;
import ru.practicum.baseService.exception.ValidationException;
import ru.practicum.baseService.mapper.UserMapper;
import ru.practicum.baseService.model.User;
import ru.practicum.baseService.util.page.CustomPageRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        CustomPageRequest pageable = new CustomPageRequest(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, pageable);
        }
        log.info("Количество пользователей: {}", users.size());
        return UserMapper.toUserDtoList(users);
    }

    @Transactional
    @Override
    public UserDto save(NewUserRequest dto) {
        User user = UserMapper.toUser(dto);
        Optional<User> existingUser = userRepository.findByName(dto.getName());
        if (existingUser.isPresent()) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Данный email уже занят");
        }
        log.info("Добавлен пользователь: {}", user.getEmail());
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        if (userRepository.existsById(userId)) {
            log.info("Удален пользователь с id = {}", userId);
            userRepository.deleteById(userId);
        }
    }
}