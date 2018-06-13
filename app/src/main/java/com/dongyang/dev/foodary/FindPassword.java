package com.dongyang.dev.foodary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_SendEmail;
    private TextView input_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        final FirebaseAuth auth = FirebaseAuth.getInstance( );

        btn_SendEmail = findViewById(R.id.btn_SendEmail);
        input_email = findViewById(R.id.input_Email);

        btn_SendEmail.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(input_email.getText( ))) {
                    new AlertDialog
                            .Builder(FindPassword.this)
                            .setTitle("Error")
                            .setMessage("이메일을 입력해주세요")
                            .setPositiveButton("Ok", null)
                            .show( );
                    return;
                }
                auth.sendPasswordResetEmail(String.valueOf(input_email.getText( )))
                        .addOnCompleteListener(new OnCompleteListener<Void>( ) {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful( )) {
                                    new AlertDialog
                                            .Builder(FindPassword.this)
                                            .setTitle("메세지")
                                            .setMessage("입력된 이메일로 메일을 전송했습니다.")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener( ) {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(getApplicationContext( ), LoginActivity.class);
                                                    startActivity(intent);
                                                    finish( );
                                                }
                                            }).show( );


                                }
                            }
                        });
            }
        });


    }
}
