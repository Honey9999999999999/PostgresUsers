package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.UserDTO;
import org.example.exception.NotFoundException;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return convertToDTO(user);
    }

    public UserDTO save(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        User updatingUser = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getAge());
        updatingUser.setId(id);
        updatingUser.setRole(Role.DEFAULT_ROLE);
        return convertToDTO(userRepository.save(updatingUser));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // Вспомогательные методы конвертации
    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}