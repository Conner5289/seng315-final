package com.conner.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.conner.config.CogntioConfig;
import com.conner.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserBuilderHelper {
    private static final String ID = "sub";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone_number";
    private static final String EMAIL_VERIFIED = "email_verified";
    private static final String PHONE_VERIFIED = "phone_number_verified";
    private final CogntioConfig config;

    public User build(UserType userType) {

        var user = new User();
        user.setLogin(userType.getUsername());
        var map = userType.getAttributes().stream()
                .collect(Collectors.toMap(AttributeType::getName, AttributeType::getValue));

        user.setId(map.get(ID));
        user.setEmail(map.get(EMAIL));
        user.setPhone(map.get(PHONE));
        user.setCreated(userType.getUserCreateDate());
        user.setModified(userType.getUserLastModifiedDate());

        return user;
    }

    public User build(AdminGetUserResult userResult) {

        var user = new User();
        user.setLogin(userResult.getUsername());
        var map = userResult.getUserAttributes().stream()
                .collect(Collectors.toMap(AttributeType::getName, AttributeType::getValue));
        user.setId(map.get(ID));
        user.setEmail(map.get(EMAIL));
        user.setPhone(map.get(PHONE));
        user.setCreated(userResult.getUserCreateDate());
        user.setModified(userResult.getUserLastModifiedDate());

        return user;
    }

    public AdminCreateUserRequest adminCreateUserRequest(User user) {
        return new AdminCreateUserRequest()
                .withUserPoolId(config.getPoolid())
                .withUsername(user.getLogin())
                .withUserAttributes(
                        new AttributeType().withName(EMAIL_VERIFIED).withValue("true"),
                        new AttributeType().withName(EMAIL).withValue(user.getEmail()),
                        new AttributeType().withName(PHONE).withValue(user.getPhone()),
                        new AttributeType().withName(PHONE_VERIFIED).withValue("true"));

    }

}
