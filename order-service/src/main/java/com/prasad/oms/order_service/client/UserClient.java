package com.prasad.oms.order_service.client;

import com.prasad.oms.order_service.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class UserClient {

    @Autowired
   private RestTemplate restTemplate;


    @Value("${user.service.url:http://user-service:8081/users/}")
    private String userUrl;

    public UserResponse getUserById(Long userId) {
        return restTemplate.getForObject(userUrl + userId, UserResponse.class);
    }





}
