package com.e451.rest.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by j747951 on 6/15/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class HealthControllerTest {

    private HealthController healthController;

    @Before
    public void setup() {
        healthController = new HealthController();
    }

    @Test
    public void healthOk_shouldReturnOKStatus() {
        ResponseEntity response = healthController.healthCheck();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
