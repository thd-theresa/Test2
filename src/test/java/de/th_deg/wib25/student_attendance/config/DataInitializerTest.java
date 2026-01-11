package de.th_deg.wib25.student_attendance.config;

import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataInitializer = new DataInitializer(userRepository, passwordEncoder);
    }

    @Test
    void testAdminAccountCreatedWhenNoUsersExist() throws Exception {
        // Given
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        dataInitializer.run();

        // Then
        verify(userRepository, times(1)).count();
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testAdminAccountNotCreatedWhenUsersExist() throws Exception {
        // Given
        when(userRepository.count()).thenReturn(1L);

        // When
        dataInitializer.run();

        // Then
        verify(userRepository, times(1)).count();
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
