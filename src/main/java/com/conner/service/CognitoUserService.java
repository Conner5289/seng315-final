package com.conner.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
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

    @Override
    public User create(User user) {

        var cratedUser = createDefaultPasswordUser(user);
        updateWithPermenatPassword(user.getLogin(), user.getPassword());

        return cratedUser;
    }

    public List<User> getAll() {
        var reqeust = new ListUsersRequest()
                .withUserPoolId(config.getPoolid());

        return provider.listUsers(reqeust).getUsers().stream().map(userBuilderHelper::build)
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(String userId) {
        var reqeust = new ListUsersRequest()
                .withUserPoolId(config.getPoolid())
                .withFilter(String.format("sub=\"%s\"", userId));

        return provider.listUsers(reqeust)
                .getUsers()
                .stream()
                .findFirst()
                .map(userBuilderHelper::build)
                .orElseThrow();
    }

    @Override
    public List<User> getAllUsers() {
        var reqeust = new ListUsersRequest()
                .withUserPoolId(config.getPoolid());

        var userResults = new ListUsersResult();
        List<User> allUsers = new ArrayList<>();
        do {
            reqeust.setPaginationToken(userResults.getPaginationToken());
            userResults = provider.listUsers(reqeust);
            allUsers.addAll(userResults.getUsers().stream().map(userBuilderHelper::build).toList());
        } while (Objects.nonNull(userResults.getPaginationToken()));

        return allUsers;

    }

    private AWSCognitoIdentityProvider constructProvider(CogntioConfig config) {

        return AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(config.gCredentialsProvider())
                .withRegion(config.getRegion()).build();
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
