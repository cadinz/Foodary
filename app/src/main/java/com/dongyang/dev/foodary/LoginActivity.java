package com.dongyang.dev.foodary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView input_email;
    private TextView input_password;
    private Button btn_login;
    private TextView et_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "세번 수정한 결과", Toast.LENGTH_SHORT).show( );
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance( );

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        btn_login = findViewById(R.id.btn_login);
        et_signin = findViewById(R.id.et_signin);

        btn_login.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(input_email.getText( )+"", input_password.getText( )+"").addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show( );
                        }
                    }
                });

            }
        });


        et_signin.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(input_email.getText( ) + "", input_password.getText( ) + "").addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful( )){
                            Toast.makeText(LoginActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show( );
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show( );
                        }

                    }
                });
            }
        });

    }
}

