package com.dongyang.dev.foodary;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;
    private TextView input_email;
    private TextView input_password;
    private Button btn_login;
    private TextView et_signin;
    private TextView find_password;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance( );
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);
        et_signin = findViewById(R.id.et_signin);
        find_password = findViewById(R.id.find_password);


        find_password.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FindPassword.class);
            startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(input_email.getText()) || TextUtils.isEmpty(input_password.getText())){
                    new AlertDialog
                            .Builder(LoginActivity.this)
                            .setTitle("Error")
                            .setMessage("이메일과 비밀번호를 입력해주세요")
                            .setPositiveButton("Ok",null)
                            .show();
                    return;
                }
                final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this,"","로그인중입니다.",true);
                mAuth.signInWithEmailAndPassword(input_email.getText( )+"", input_password.getText( )+"").addOnCompleteListener(new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss( );
                        if (task.isSuccessful( )) {

                                    //select * from name where email = "song@.....";
                            database.getReference( ).child("name").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener( ) {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren( )) {
                                        Log.d("---------MainActivity", "ValueEventListener : " + snapshot.child("name").getValue());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });



                            Intent intent = new Intent(getApplicationContext(),MyActivity.class);
                            startActivity(intent);
                            finish();
                        } else{
                            new AlertDialog
                                    .Builder(LoginActivity.this)
                                    .setTitle("Error")
                                    .setMessage("로그인실패")
                                    .setPositiveButton("Ok",null)
                                    .show();

                        }
                    }
                });

            }
        });


        et_signin.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

}