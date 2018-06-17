package com.dongyang.dev.foodary;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FirebaseAuth auth;
    private ImageButton imgBtnWrite;
    private ImageButton imgBtnMenu;
    private EditText et_search;
    private List<ImageDTO> imageDTOs = new ArrayList<>( );
    private FirebaseDatabase database;
    private FirebaseDatabase database2;
    private List<String> frnd = new ArrayList<>( );
    private BoardRecyclerViewAdapter boardRecyclerViewAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액션바
        android.support.v7.app.ActionBar actionBar = getSupportActionBar( );

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.`
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 255, 255, 255)));

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.layout_actionbar, null);

        actionBar.setCustomView(actionbar);
        //액션바끝
        database = FirebaseDatabase.getInstance( );
        database2 = FirebaseDatabase.getInstance( );
        auth = FirebaseAuth.getInstance( );

        imgBtnMenu = findViewById(R.id.imgBtnMenu);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        boardRecyclerViewAdapter = new BoardRecyclerViewAdapter( );
        mRecyclerView.setAdapter(boardRecyclerViewAdapter);
        boardRecyclerViewAdapter.notifyDataSetChanged( );



        imgBtnWrite = findViewById(R.id.imgBtnWrite);
        et_search = findViewById(R.id.et_search);
        getDefaultData();


        imgBtnMenu.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext( ), FindFriend.class);
                startActivity(intent);
                finish();
            }
        });

        imgBtnWrite.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
                finish();
            }
        });


        getData();
        getDefaultData();
        et_search.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                database.getReference( ).child("images").addValueEventListener(new ValueEventListener( ) {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {   //실시간으로 데이터새로고침

                        imageDTOs.clear( );

                        if (TextUtils.isEmpty(charSequence.toString( ))) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren( )) {
                                if (frnd.contains(snapshot.child("userId").getValue( ).toString( ))) {
                                    Log.d("태그", "onDataChange: 엠티일때");
                                    ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                                    imageDTOs.add(imageDTO);
                                }
                            }
                        } else {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren( )) {
                                Log.d("태그", "onDataChange: 움직일때");

                                if (frnd.contains(snapshot.child("userId").getValue( ).toString( ))) {
                                    if ((snapshot.child("title").getValue( ) + "").contains(charSequence.toString( ))) {
                                        ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                                        imageDTOs.add(imageDTO);
                                    }
                                }
                            }
                        }
                        boardRecyclerViewAdapter.notifyDataSetChanged( );
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    void getDefaultData() {
        database2.getReference( ).child("images").addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {   //실시간으로 데이터새로고침

                imageDTOs.clear( );


                for (DataSnapshot snapshot : dataSnapshot.getChildren( )) {
                    Log.d("태그", "onDataChange: 내부");
                    if (frnd.contains(snapshot.child("userId").getValue( ).toString( ))) {
                        ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                        imageDTOs.add(imageDTO);

                    }
                }
                boardRecyclerViewAdapter.notifyDataSetChanged( );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    void getData() {
        database.getReference( ).child("friends").orderByChild("friend").addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                frnd.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren( )) {

                    Log.i("child : email", snapshot.child("email").getValue( ).toString( ));
                    Log.i("child : friend", snapshot.child("friend").getValue( ).toString( ));
                    if (snapshot.child("email").getValue( ).equals(auth.getCurrentUser( ).getEmail( ))) {
                        frnd.add(snapshot.child("friend").getValue( ).toString( ));
                        Log.i("추가된것", snapshot.child("friend").getValue( ).toString( ));
                        System.out.println("안녕");
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.my_view, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder) holder).item_tvid.setText(imageDTOs.get(position).title);
            ((CustomViewHolder) holder).item_tvtitle.setText(imageDTOs.get(position).content);

            Glide.with(holder.itemView.getContext( )).load(imageDTOs.get(position).imageUrl).into(((CustomViewHolder) holder).item_imageView);
        }

        @Override
        public int getItemCount() {
            return imageDTOs.size( );
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView item_imageView;
            TextView item_tvid;
            TextView item_tvtitle;


            public CustomViewHolder(View view) {
                super(view);
                item_imageView = view.findViewById(R.id.item_imageview);
                item_tvid = view.findViewById(R.id.item_id);
                item_tvtitle = view.findViewById(R.id.item_title);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
