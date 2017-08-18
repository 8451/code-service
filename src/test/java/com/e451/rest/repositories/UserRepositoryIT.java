package com.e451.rest.repositories;

import com.e451.rest.domains.user.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.util.Arrays;
import java.util.List;


@TestPropertySource("application-test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private List<User> createdUsers;
    private List<User> users;

    @Before
    public void setup() {
        users = Arrays.asList(
                new User("id1", "Liz", "Conrad", "zil@darnoc.moc", "passw0rd"),
                new User("id2", "Jacob", "Tucker", "bocaj@reckut.moc", "dr0wssap")
        );
        createdUsers = userRepository.insert(users);
    }

    @After
    public void teardown() {
        userRepository.deleteAll();
    }

    @Test
    public void createUsers_returnAllUsers() {
        Assert.assertEquals(users, createdUsers);
    }
}
