package com.dongyang.dev.foodary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FindFriend extends AppCompatActivity {
    private EditText email_find;
    private Button bt_find;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance( );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);




        email_find = findViewById(R.id.email_find);
        bt_find = findViewById(R.id.bt_find);

        bt_find.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                FriendDTO friendDTO = new FriendDTO( );
                friendDTO.friend = email_find.getText()+"";
                friendDTO.email = mAuth.getCurrentUser().getEmail();
                database.getReference().child("friends").push().setValue(friendDTO);

                Toast.makeText(FindFriend.this, "추가 되었습니다.", Toast.LENGTH_SHORT).show( );
                Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
