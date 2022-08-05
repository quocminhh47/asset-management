package com.nashtech.assetmanagement.repository;

import com.nashtech.assetmanagement.entities.Users;
import com.nashtech.assetmanagement.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = {"file:src/main/resources/data_test.sql"})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findStaffCodeList_ShouldReturnStaffCodeList_WhenStaffCodeBeginWithSDExist() {
        List<String> staffCode = userRepository.findAllStaffCode();
        assertEquals(9, staffCode.size());
        for (int i = 0; i < staffCode.size(); i++) {
            String result = staffCode.get(i);
            assertEquals("SD", result.substring(0, 2));
        }
    }

    @Test
    void findByStaffCodeOrNameAndLocationCode_ShouldReturnUserList_WhenNameOrCodeAndLocationCodeExist() {
        List<Users> userList = userRepository.findByStaffCodeOrNameAndLocationCode(" p", "HCM");
        String pattern = "(.* [pP].*)";
        for (int i = 0; i < userList.size(); i++) {
            String fullname = userList.get(i).getFirstName() + ' ' + userList.get(i).getLastName();
            assertTrue(fullname.matches(pattern));
            assertEquals("HCM", userList.get(i).getLocation().getCode());
        }
    }

}
