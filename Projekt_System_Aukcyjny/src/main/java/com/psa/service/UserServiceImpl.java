package com.psa.service;

import com.psa.dto.UserRequestDto;
import com.psa.exception.ResourceNotFoundException;
import com.psa.model.User;
import com.psa.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserRequestDto userRequestDto) {
        validateUniqueFields(userRequestDto, null);

        User user = new User();
        mapUserFields(user, userRequestDto);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uzytkownik o id " + id + " nie istnieje"));

        validateUniqueFields(userRequestDto, id);
        mapUserFields(user, userRequestDto);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uzytkownik o id " + id + " nie istnieje"));

        userRepository.delete(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uzytkownik o id " + id + " nie istnieje"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByUsernameAsc();
    }

    private void validateUniqueFields(UserRequestDto userRequestDto, Long currentUserId) {
        String username = normalize(userRequestDto.getUsername());
        String email = normalize(userRequestDto.getEmail());

        boolean usernameTaken = userRepository.findAllByOrderByUsernameAsc().stream()
                .anyMatch(user -> normalize(user.getUsername()).equalsIgnoreCase(username)
                        && !user.getId().equals(currentUserId));

        boolean emailTaken = userRepository.findAllByOrderByUsernameAsc().stream()
                .anyMatch(user -> normalize(user.getEmail()).equalsIgnoreCase(email)
                        && !user.getId().equals(currentUserId));

        if (usernameTaken) {
            throw new IllegalArgumentException("Taki login jest juz zajety");
        }

        if (emailTaken) {
            throw new IllegalArgumentException("Taki email jest juz zajety");
        }
    }

    private void mapUserFields(User user, UserRequestDto userRequestDto) {
        user.setUsername(normalize(userRequestDto.getUsername()));
        user.setPassword(normalize(userRequestDto.getPassword()));
        user.setEmail(normalize(userRequestDto.getEmail()));
        user.setFirstName(normalizeNullable(userRequestDto.getFirstName()));
        user.setLastName(normalizeNullable(userRequestDto.getLastName()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeNullable(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}