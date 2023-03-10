package com.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Passwords implements Serializable {
    public static Passwords passwords;
    public List<String> innerPasswords = new ArrayList<>();
    public Passwords() {}
    public void fill() {
        innerPasswords.add("pass11");
        innerPasswords.add("pass22");
        innerPasswords.add("pass33");
        innerPasswords.add("pass44");
    }
    static public void create() {
        passwords = new Passwords();
        passwords.fill();
    }
    public static boolean passwordOK(String password) {
        return (passwords != null) && passwords.innerPasswords.contains(password);
    }
}
