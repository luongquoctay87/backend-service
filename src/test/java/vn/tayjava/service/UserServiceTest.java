package vn.tayjava.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.tayjava.common.Gender;
import vn.tayjava.common.UserStatus;
import vn.tayjava.common.UserType;
import vn.tayjava.controller.request.AddressRequest;
import vn.tayjava.controller.request.UserCreationRequest;
import vn.tayjava.controller.request.UserPasswordRequest;
import vn.tayjava.controller.request.UserUpdateRequest;
import vn.tayjava.controller.response.UserPageResponse;
import vn.tayjava.controller.response.UserResponse;
import vn.tayjava.exception.ResourceNotFoundException;
import vn.tayjava.model.UserEntity;
import vn.tayjava.repository.AddressRepository;
import vn.tayjava.repository.UserRepository;
import vn.tayjava.service.impl.UserServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Unit Test cho Service Layer
@ExtendWith(MockitoExtension.class) // Sử dụng Mockito
@MockitoSettings(strictness = Strictness.LENIENT)
@Slf4j(topic = "UserServiceTest")
public class UserServiceTest {

    private UserService userService;

    private @Mock UserRepository userRepository;
    private @Mock AddressRepository addressRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @Mock EmailService emailService;

    private static UserEntity mockUser1;
    private static UserEntity mockUser2;

    @BeforeAll
    public static void beforeAll() {
        // Dữ liệu giả lập
        mockUser1 = new UserEntity();
        mockUser1.setId(1L);
        mockUser1.setFirstName("Tay");
        mockUser1.setLastName("Java");
        mockUser1.setGender(Gender.MALE);
        mockUser1.setBirthday(new Date());
        mockUser1.setEmail("quoctay87@gmail.com");
        mockUser1.setPhone("0975118228");
        mockUser1.setUsername("tayjava");
        mockUser1.setPassword("password");
        mockUser1.setType(UserType.USER);
        mockUser1.setStatus(UserStatus.ACTIVE);

        mockUser2 = new UserEntity();
        mockUser2.setId(2L);
        mockUser2.setFirstName("John");
        mockUser2.setLastName("Doe");
        mockUser2.setGender(Gender.FEMALE);
        mockUser2.setBirthday(new Date());
        mockUser2.setEmail("johndoe@gmail.com");
        mockUser2.setPhone("0123456789");
        mockUser2.setUsername("johndoe");
        mockUser2.setPassword("password");
        mockUser2.setType(UserType.USER);
        mockUser2.setStatus(UserStatus.INACTIVE);
    }

    @BeforeEach
    public void beforeEach() {
        // Khởi tạo lớp triển khai của UserService
        userService = new UserServiceImpl(userRepository, addressRepository, passwordEncoder, emailService);
    }

    @Test
    public void testGetUserList_Success() {
        Page<UserEntity> userPage = new PageImpl<>(List.of(mockUser1, mockUser2));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        UserPageResponse result = userService.findAll(null, null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(2, result.totalElements);
    }

    @Test
    public void testSearchUser_Success() {
        Page<UserEntity> userPage = new PageImpl<>(List.of(mockUser1, mockUser2));

        // Giả lập phương thức search của UserRepository
        when(userRepository.searchByKeyword(any(), any(Pageable.class))).thenReturn(userPage);

        UserPageResponse result = userService.findAll("tay", null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(2, result.totalElements);
        assertEquals("tayjava", result.getUsers().get(0).getUsername());
    }

    @Test
    public void testGetUserList_Empty() {
        Page<UserEntity> userPage = new PageImpl<>(List.of());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        UserPageResponse result = userService.findAll(null, null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(0, result.getUsers().size());
    }

    @Test
    public void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));

        // Gọi hàm và kiểm tra kết quả
        UserResponse result = userService.findById(1L);
        Assertions.assertNotNull(result);
        assertEquals("tayjava", result.getUsername());
    }

    @Test
    public void testGetUserById_Failure() {
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.findById(10L));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testFindByUsername_Success() {
        when(userRepository.findByUsername("tayjava")).thenReturn(mockUser1);
        UserResponse result = userService.findByUsername("tayjava");
        Assertions.assertNotNull(result);
        assertEquals("tayjava", result.getUsername());
    }

    @Test
    void testFindByEmail_Success() {
        when(userRepository.findByEmail("quoctay87@gmail.com")).thenReturn(mockUser1);
        UserResponse result = userService.findByEmail("quoctay87@gmail.com");
        Assertions.assertNotNull(result);
        assertEquals("quoctay87@gmail.com", result.getEmail());
    }

    @Test
    void testSave_Success() {
        // Giả lập hành vi của UserRepository
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser1);

        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Tay");
        userCreationRequest.setLastName("Java");
        userCreationRequest.setGender(Gender.MALE);
        userCreationRequest.setBirthday(new Date());
        userCreationRequest.setEmail("quoctay87@gmail.com");
        userCreationRequest.setPhone("0975118228");
        userCreationRequest.setUsername("tayjava");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("ApartmentNumber");
        addressRequest.setFloor("Floor");
        addressRequest.setBuilding("Building");
        addressRequest.setStreetNumber("StreetNumber");
        addressRequest.setStreet("Street");
        addressRequest.setCity("City");
        addressRequest.setCountry("Country");
        addressRequest.setAddressType(1);
        userCreationRequest.setAddresses(List.of(addressRequest));

        // Gọi phương thức cần kiểm tra
        long result = userService.save(userCreationRequest);

        // Kiểm tra kết quả
        assertNotNull(result);
        assertEquals(1L, result);
    }

    @Test
    void testUpdate_Success() {
        Long userId = 2L;

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userId);
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Smith");
        updatedUser.setGender(Gender.FEMALE);
        updatedUser.setBirthday(new Date());
        updatedUser.setEmail("janesmith@gmail.com");
        updatedUser.setPhone("0123456789");
        updatedUser.setUsername("janesmith");
        updatedUser.setType(UserType.USER);
        updatedUser.setStatus(UserStatus.ACTIVE);

        // Giả lập hành vi của UserRepository
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser2));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setId(userId);
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Smith");
        updateRequest.setGender(Gender.MALE);
        updateRequest.setBirthday(new Date());
        updateRequest.setEmail("janesmith@gmail.com");
        updateRequest.setPhone("0123456789");
        updateRequest.setUsername("janesmith");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("ApartmentNumber");
        addressRequest.setFloor("Floor");
        addressRequest.setBuilding("Building");
        addressRequest.setStreetNumber("StreetNumber");
        addressRequest.setStreet("Street");
        addressRequest.setCity("City");
        addressRequest.setCountry("Country");
        addressRequest.setAddressType(1);
        updateRequest.setAddresses(List.of(addressRequest));

        // Gọi phương thức cần kiểm tra
        userService.update(updateRequest);

        UserResponse result = userService.findById(userId);

        assertEquals("janesmith", result.getUsername());
        assertEquals("janesmith@gmail.com", result.getEmail());
    }

    @Test
    void testChangePassword_Success() {
        Long userId = 2L;

        UserPasswordRequest request = new UserPasswordRequest();
        request.setId(userId);
        request.setPassword("newPassword");
        request.setConfirmPassword("newPassword");

        // Giả lập hành vi của repository và password encoder
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser2));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedNewPassword");

        // Gọi phương thức cần kiểm tra
        userService.changePassword(request);

        // Kiểm tra mật khẩu được mã hóa và lưu
        assertEquals("encodedNewPassword", mockUser2.getPassword());
        verify(userRepository, times(1)).save(mockUser2);
        verify(passwordEncoder, times(1)).encode(request.getPassword());
    }

    @Test
    public void testDeleteUser_Success() {
        // Chuẩn bị dữ liệu
        Long userId = 1L;

        // Giả lập hành vi repository
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser1));

        // Gọi phương thức cần kiểm tra
        userService.delete(userId);

        // Kiểm tra kết quả
        assertEquals(UserStatus.INACTIVE, mockUser1.getStatus());
        verify(userRepository, times(1)).save(mockUser1);
    }

    @Test
    public void testUserNotFound_ThrowsException() {
        // Chuẩn bị dữ liệu
        Long userId = 1L;

        // Giả lập hành vi repository
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Gọi phương thức và kiểm tra ngoại lệ
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.delete(userId));

        // Kiểm tra nội dung ngoại lệ
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }
}
