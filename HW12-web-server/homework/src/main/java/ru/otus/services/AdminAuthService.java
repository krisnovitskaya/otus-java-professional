package ru.otus.services;

import lombok.SneakyThrows;

import java.util.Properties;

public class AdminAuthService implements AuthService {

    private final String adminLogin;
    private final String adminPassword;

    @SneakyThrows
    public AdminAuthService() {
        Properties prop = new Properties();
        try(var is =this.getClass().getClassLoader().getResourceAsStream("auth.properties")){
            prop.load(is);
            adminLogin = prop.getProperty("login");
            adminPassword = prop.getProperty("password");
        }
    }

    @Override
    public boolean authenticate(String login, String password) {
        return adminLogin.equals(login) && adminPassword.equals(password);
    }

}
