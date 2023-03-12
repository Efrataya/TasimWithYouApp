package com.example.tasimwithyouapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Passwords implements Serializable {
    public List<String> innerPasswords = new ArrayList<>();
    public Passwords() {}
    public void fill() {
        innerPasswords.add("pass11");
        innerPasswords.add("pass22");
        innerPasswords.add("pass33");
        innerPasswords.add("pass44");
    }
     public static void create(Passwords passwords) {
        passwords.fill();
    }

}
