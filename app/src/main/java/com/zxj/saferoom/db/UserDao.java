package com.zxj.saferoom.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.zxj.saferoom.bean.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user")
    List<User> getAllUser();

    @Delete
    void deleteAll(List<User> users);

    @Update
    void update(User user);

}
