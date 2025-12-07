package com.conner.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.conner.config.CogntioConfig;
import com.conner.model.User;

@Service
public class CognitoUserService implements UserService {
    private final CogntioConfig config;
    private final UserBuilderHelper userBuilderHelper;
    private AWSCognitoIdentityProvider provider;

    public CognitoUserService(CogntioConfig config, UserBuilderHelper userBuilderHelper) {
        this.config = config;
        this.userBuilderHelper = userBuilderHelper;
        this.provider = constructProvider(config);
    }

    private AWSCognitoIdentityProvider constructProvider(CogntioConfig config) {

        return AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(config.gCredentialsProvider())
                .withRegion(config.getRegion()).build();
    }

    @Override
    public User create(User user) {

        var cratedUser = createDefaultPasswordUser(user);
        updateWithPermenatPassword(user.getLogin(), user.getPassword());

        return cratedUser;
    }

    private void updateWithPermenatPassword(String login, String password) {
        var passwordRequest = new AdminSetUserPasswordRequest()
                .withUsername(login)
                .withPassword(password)
                .withPermanent(true)
                .withUserPoolId(config.getPoolid());

        provider.adminSetUserPassword(passwordRequest);
    }

    private User createDefaultPasswordUser(User user) {

        return Optional.of(user).map(userBuilderHelper::adminCreateUserRequest).map(provider::adminCreateUser)
                .map(AdminCreateUserResult::getUser)
                .map(userBuilderHelper::build).orElseThrow();
    }

}
