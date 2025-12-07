package com.conner.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;
    private String login;
    private String password;
    private String phone;
    private String email;

    private Date created;
    private Date modified;
}
