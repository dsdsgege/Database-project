package com.teamsportsdb.utils;

public class LoginManager {
    private static String password = null;
    private static String username = null;
    private static String name = null;

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        LoginManager.password = password;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LoginManager.username = username;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        LoginManager.name = name;
    }
}
