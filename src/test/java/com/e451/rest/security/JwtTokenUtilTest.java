package com.e451.rest.security;

import com.e451.rest.domains.user.User;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;


@TestPropertySource("application-test.properties")
@RunWith(MockitoJUnitRunner.class)
public class JwtTokenUtilTest {

    private User user;
    private JwtTokenUtil tokenUtil;
    private String token;

    @Before
    public void setup() {
        user = new User("1", "f1", "l1", "email@test.com", "password");
        tokenUtil = new JwtTokenUtil("codePassword", 604800L, "", "");
        token = tokenUtil.generateToken(user);
    }


    @Test
    public void generateToken_ReturnsToken() {
        Assert.assertNotNull(token);
    }

    @Test
    public void getUserNameFromToken_ReturnsUserName() {
        String username = tokenUtil.getUsernameFromToken(token);
        Assert.assertNotNull(username);
        Assert.assertEquals(user.getUsername(), username);
    }

    @Test
    public void getCreatedDateFromToken_ReturnDate() {
        Date createdDate = tokenUtil.getCreatedDateFromToken(token);
        Date now = new Date();
        Assert.assertTrue("Dates not within five seconds", Math.abs(createdDate.getTime() - now.getTime()) < 5000);
    }

    @Test
    public void getExpirationDateFromToken_ReturnsExpiration() {
        Date expirationDate = tokenUtil.getExpirationDateFromToken(token);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        calendar.add(Calendar.MINUTE, -5);
        Date targetDate = calendar.getTime();
        Assert.assertTrue("Expiration date not far enough in the future", expirationDate.after(targetDate));
    }

    @Test
    public void validateToken_returnsTrueIfBelongsToCurrentUser() {
        Assert.assertTrue(tokenUtil.validateToken(token, user));
    }

    @Test
    public void validateToken_returnsFalseIfTokenDoesNotBelongToCurrentUser() {
        Assert.assertFalse(tokenUtil.validateToken(token, new User("2", "first", "last", "email", "pass")));
    }

    @Test
    public void refreshToken_returnsUpdatedValidToken() throws Exception {
        // Need enough of a time buffer to make sure the tokens dates will be different.
        Thread.sleep(1000);
        String newToken = tokenUtil.refreshToken(token);

        Assert.assertNotNull(newToken);
        Assert.assertNotEquals(newToken, token);
        Assert.assertTrue(tokenUtil.validateToken(newToken, user));
    }
}
