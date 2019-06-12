package com.company.repository;

import com.company.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static Map<String,User> users = new HashMap<>();
    public User getUserById(String id)
    {
        return users.get(id);
    }
    public boolean addUser(String id,User user){
        if(users.containsKey(id))
        {
            return false;
        }
        users.put(id,user);
        return true;
    }
    public boolean updateUser(String id,User user)
    {
        if(!users.containsKey(id))
        {
            //无该user
            return false;
        }
        users.put(id,user);
        return true;
    }
}
