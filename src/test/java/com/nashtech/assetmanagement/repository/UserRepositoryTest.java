package com.nashtech.assetmanagement.repository;

import com.nashtech.assetmanagement.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(scripts = {"file:src/main/resources/data_test.sql"})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByStaffCodeAndName_ShouldReturnListUser_WhenStaffCodeAndNameExist() {
        assertEquals(1, userRepository.findByStaffCodeOrNameAndLocationCode("p ", "HN").size());
        assertEquals(7, userRepository.findByStaffCodeOrNameAndLocationCode("sd", "HCM").size());
    }

    @Test
    void findStaffCodeList_ShouldReturnStaffCodeList_WhenStaffCodeBeginWithSDExist() {
        List<String> staffCode = userRepository.findAllStaffCode();
        assertEquals(9, staffCode.size());
        for (int i = 0; i < staffCode.size(); i++) {
            String result = staffCode.get(i);
            assertEquals("SD", result.substring(0, 2));
        }
    }

}
