package com.dragomir.internship_managment.service;

import com.dragomir.internship_managment.domain.Student;
import com.dragomir.internship_managment.dto.CompanyRegistrationDTO;
import com.dragomir.internship_managment.dto.StudentRegistrationDTO;
import com.dragomir.internship_managment.exceptions.UserAlreadyExistsException;
import com.dragomir.internship_managment.repository.CompanyRepository;
import com.dragomir.internship_managment.repository.StudentRepository;
import com.dragomir.internship_managment.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterStudentSuccessfully() {
        StudentRegistrationDTO dto = new StudentRegistrationDTO();
        dto.setFirstName("Drago");
        dto.setLastName("Vlacic");
        dto.setBio("blablablabla");
        dto.setStudyYear("5");
        dto.setPhoneNumber("063123123");
        dto.setIndexNumber("2018/414");
        dto.setPassword("password123");
        dto.setEmail("drago@gmail.com");

        // 'podesavam' stanje koje mi treba u ovom scenariju
        when(userRepository.existsByEmail("drago@gmail.com")).thenReturn(false);
        when(studentRepository.existsByIndexNumber("2018/414")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        Student savedStudent = new Student();
        savedStudent.setEmail("drago@gmail.com");

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = userService.registerStudent(dto);

        assertNotNull(result);
        assertEquals("drago@gmail.com", result.getEmail());

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenEmailAlreadyInUse()
    {
        StudentRegistrationDTO dto = new StudentRegistrationDTO();
        dto.setFirstName("Drago");
        dto.setLastName("Vlacic");
        dto.setBio("blablablabla");
        dto.setStudyYear("5");
        dto.setPhoneNumber("063123123");
        dto.setIndexNumber("2018/414");
        dto.setPassword("password123");
        dto.setEmail("drago@gmail.com");
        when(userRepository.existsByEmail("drago@gmail.com")).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () ->{
            userService.registerStudent(dto);
        });


    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenStudentIndexAlreadyInUser () {
        {
            StudentRegistrationDTO srdto = new StudentRegistrationDTO();
            srdto.setFirstName("Drago");
            srdto.setLastName("Vlacic");
            srdto.setBio("blablablabla");
            srdto.setStudyYear("5");
            srdto.setPhoneNumber("063123123");
            srdto.setIndexNumber("2018/414");
            srdto.setPassword("password123");
            srdto.setEmail("drago@gmail.com");

            when(studentRepository.existsByIndexNumber("2018/414")).thenReturn(true);
            assertThrows(UserAlreadyExistsException.class, () ->{
                userService.registerStudent(srdto);
            });

        }

    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenPIBAlreadyExists () {

        CompanyRegistrationDTO crdto = new CompanyRegistrationDTO();
        crdto.setAddress("Ugrinovacka 39");
        crdto.setDescription("blablabal");
        crdto.setPib("1234567");
        crdto.setEmail("@mail.com");
        crdto.setPhoneNumber("063123123");
        crdto.setCompanySize("100");
        crdto.setContactPerson("SLAVKO");
        crdto.setWebsite("www.company.com");
        crdto.setPassword("password123");

        when(companyRepository.existsByPib("1234567")).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () ->{
            userService.registerCompany(crdto);
        });








    }
}

