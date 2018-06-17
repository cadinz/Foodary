package com.dongyang.dev.foodary;

import android.*;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class WriteActivity extends AppCompatActivity {
    private static final int GALLERY_CODE = 10;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    //private ImageView imageView;
    private EditText title;
    private EditText content;
    private Button bt_upload;
    private Button bt_gallery;
    private String imagePath;
    private FirebaseDatabase database;
    private ImageButton btn_back;
    private TextView imguri;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        btn_back = findViewById(R.id.btn_back);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        title = findViewById(R.id.et_title);
        content = findViewById(R.id.et_content);
        bt_upload = findViewById(R.id.button4);    //업로드 버튼
        bt_gallery = findViewById(R.id.button3);
        imguri = findViewById(R.id.imguri);

        btn_back.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
        //업로드 버튼 누르면
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload(imagePath);




            }
        });

        //겔러리 버튼 누르면
        bt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });
    }

    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GALLERY_CODE){

            imagePath = getPath(data.getData());
//            File f = new File(getPath(data.getData()));    //경로넘어옴
            //imageView.setImageURI(Uri.fromFile(f));
            String uri = imagePath.toString();
            String path = uri.substring(uri.lastIndexOf("/")+1);
           imguri.setText(path);

        }
    }
    private void upload(String uri){
        final ProgressDialog dialog = ProgressDialog.show(WriteActivity.this,"","업로드 중입니다..",true);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://foodary-project.appspot.com");

        Uri file = Uri.fromFile(new File(uri));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                dialog.dismiss();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();


                    ImageDTO imageDTO = new ImageDTO();
                imageDTO.imageUrl = downloadUrl.toString();
                imageDTO.title = title.getText().toString();
                imageDTO.content = content.getText().toString();
                imageDTO.uid = auth.getCurrentUser().getUid();
                imageDTO.userId = auth.getCurrentUser().getEmail();
                database.getReference().child("images").push().setValue(imageDTO);
                Toast toast = Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_LONG); toast.show();
                Intent intent = new Intent(WriteActivity.this,MyActivity.class);
                startActivity(intent);
            }
        });
    }


}
