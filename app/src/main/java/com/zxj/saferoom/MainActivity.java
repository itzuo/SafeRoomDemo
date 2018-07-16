package com.zxj.saferoom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zxj.saferoom.bean.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initView();
    }

    private void init() {
        for(int i=0;i<10;i++){
            User user = new User();
            user.setName("张三:"+i);
            user.setAge(10+i);
            user.setGender(i%2==0?"男":"女");
            App.getInstance().getDatabase().getUserDao().insert(user);
        }
    }

    private void initView() {
        List<User> allUser = App.getInstance().getDatabase().getUserDao().getAllUser();
        StringBuilder sb = new StringBuilder();
        for (User user : allUser) {
            sb.append("\n\t 姓名："+user.getName()+"  年龄："+user.getAge()+"  性别："+user.getGender());
        }
        TextView tv = findViewById(R.id.tv);
        tv.setText(sb.toString());
    }
}
