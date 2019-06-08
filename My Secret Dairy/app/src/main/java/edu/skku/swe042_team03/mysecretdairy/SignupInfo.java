package edu.skku.swe042_team03.mysecretdairy;

import java.util.HashMap;
import java.util.Map;

public class SignupInfo {
    public String ID;
    public String Password;
    public  String Email;


    public SignupInfo () { }

    public SignupInfo(String id, String password, String email) {
        this.ID = id;
        this.Password = password;
        this.Email = email;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", ID);
        result.put("Password", Password);
        result.put("Email", Email);
        return result;
    }
}
