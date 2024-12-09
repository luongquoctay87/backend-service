package vn.tayjava.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.tayjava.common.Gender;
import vn.tayjava.common.UserStatus;
import vn.tayjava.common.UserType;
import vn.tayjava.controller.response.UserPageResponse;
import vn.tayjava.controller.response.UserResponse;
import vn.tayjava.model.UserEntity;
import vn.tayjava.service.UserService;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private static UserResponse mockUser1;
    private static UserResponse mockUser2;

    @BeforeAll
    public static void beforeAll() {
        // Dữ liệu giả lập
        mockUser1 = new UserResponse();
        mockUser1.setId(1L);
        mockUser1.setFirstName("Tay");
        mockUser1.setLastName("Java");
        mockUser1.setGender(Gender.MALE);
        mockUser1.setBirthday(new Date());
        mockUser1.setEmail("quoctay87@gmail.com");
        mockUser1.setPhone("0975118228");
        mockUser1.setUsername("tayjava");

        mockUser2 = new UserResponse();
        mockUser2.setId(2L);
        mockUser2.setFirstName("John");
        mockUser2.setLastName("Doe");
        mockUser2.setGender(Gender.FEMALE);
        mockUser2.setBirthday(new Date());
        mockUser2.setEmail("johndoe@gmail.com");
        mockUser2.setPhone("0123456789");
        mockUser2.setUsername("johndoe");
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUserDetail_Success() throws Exception {
        // Chuẩn bị dữ liệu
        Long userId = 1L;

        // Giả lập hành vi của service
        when(userService.findById(userId)).thenReturn(mockUser1);

        // Thực hiện kiểm tra API
        mockMvc.perform(get("/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("user"));
    }

    @Test
    public void testGetAllUsers_Success() throws Exception {
        // Chuẩn bị dữ liệu
        List<UserResponse> userList = List.of(mockUser1, mockUser2);

        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setPageNumber(0);
        userPageResponse.setPageSize(20);
        userPageResponse.setTotalPages(1);
        userPageResponse.setTotalElements(2);
        userPageResponse.setUsers(userList);

        // Giả lập hành vi của service
        when(userService.findAll(any(), any(), 0, 20)).thenReturn(userPageResponse);

        // Thực hiện kiểm tra API
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("user list"));


    }
}
