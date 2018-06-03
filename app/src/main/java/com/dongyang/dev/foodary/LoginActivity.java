package com.dongyang.dev.foodary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "두번 수정한 결과", Toast.LENGTH_SHORT).show( );
        setContentView(R.layout.activity_main);
    }
}
x