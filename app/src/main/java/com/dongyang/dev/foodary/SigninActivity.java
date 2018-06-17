package com.dongyang.dev.foodary;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends Activity {
    private FirebaseAuth mAuth;
    private TextView input_email;
    private TextView input_password;
    private TextView input_name;
    private Button btn_signin;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);




        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance( );
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        btn_signin = findViewById(R.id.btn_signin);
        input_name = findViewById(R.id.input_name);
        btn_signin.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(input_email.getText()) || TextUtils.isEmpty(input_password.getText())){
                    new AlertDialog
                            .Builder(SigninActivity.this)
                            .setTitle("Error")
                            .setMessage("가입할 이메일과 비밀번호를 입력해주세요")
                            .setPositiveButton("Ok",null)
                            .show();
                    return;
                }
                final ProgressDialog dialog = ProgressDialog.show(SigninActivity.this,"","회원가입중입니다.",true);
                mAuth.createUserWithEmailAndPassword(input_email.getText( ) + "", input_password.getText( ) + "").addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            NameDTO dto = new NameDTO();
                            dto.name = input_name.getText().toString();
                            dto.email = mAuth.getCurrentUser().getEmail();
                            database.getReference().child("name").push().setValue(dto);

                            new AlertDialog
                                    .Builder(SigninActivity.this)
                                    .setTitle("메세지")
                                    .setMessage("회원가입에 성공하셨습니다.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener( ) {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                        } else{
                            new AlertDialog
                                    .Builder(SigninActivity.this)
                                    .setTitle("메세지")
                                    .setMessage("회원가입에 실패했습니다.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener( ) {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();

                        }
                    }
                });

            }
        });

    }

}