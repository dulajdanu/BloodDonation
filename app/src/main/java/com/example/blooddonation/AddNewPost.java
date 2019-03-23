package com.example.blooddonation;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class AddNewPost extends AppCompatActivity
{
    private Button btnChoose,btnUpload;
    private ImageView imageview;
    private EditText descriptionImg;
    private Uri filepath; //imageurl
    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseStorage storage;
    private  StorageReference storageReference;
    private DatabaseReference databaseReference;

    private ProgressBar uploadProgress;
    private StorageTask mUploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
        descriptionImg = findViewById(R.id.editText);
        uploadProgress = findViewById(R.id.uploadProgress);


        btnChoose=(Button)findViewById(R.id.btnChoose);
        btnUpload =(Button)findViewById(R.id.btnUploadImage);
        imageview = (ImageView) findViewById(R.id.imgView);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ChooseImage();

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UploadImages();

            }
        });

    }

    private void UploadImages()
    {
//        if (filepath != null)
//        {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();
//
//            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
//            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
//            {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
//                {
//                    progressDialog.dismiss();
//                    Toast.makeText(AddNewPost.this,"Uploaded",Toast.LENGTH_SHORT).show();
//
//                }
//            })
//
//                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
//                        {
//                            progressDialog.dismiss();
//                            Toast.makeText(AddNewPost.this,"Failed",Toast.LENGTH_SHORT).show();
//
//                        }
//                    })
//
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
//                        {   Double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                            progressDialog.setMessage("Uploaded"+(double) progress + "%");
//
//                        }
//                    });
//
//
//
//
//        }

        if(mUploadTask != null && mUploadTask.isInProgress())
        {
            Toast.makeText(AddNewPost.this,"Upload in progress",Toast.LENGTH_LONG).show();
        }
        else
        {
            uploadImage();
        }

    }


    private void uploadImage() {

        if(filepath != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+""+getFileExtension(filepath));
            mUploadTask = fileReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadProgress.setProgress(0);
                        }
                    },500);
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Upload upload = new Upload(descriptionImg.getText().toString().trim(),uri.toString());
                            String uploadID = databaseReference.push().getKey();
                            databaseReference.child(uploadID).setValue(upload);
                            Toast.makeText(AddNewPost.this,"Upload Successful",Toast.LENGTH_LONG).show();
                            imageview.setImageResource(android.R.drawable.ic_menu_camera);
                            descriptionImg.setText("");
                        }
                    });


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewPost.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int)progress);

                        }
                    });
        }
        else
        {
            Toast.makeText(AddNewPost.this,"No file selected",Toast.LENGTH_LONG).show();
        }

    }

    private String getFileExtension(Uri filepath) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(filepath));
    }

    private void ChooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null)
        {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageview.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();

            }





        }
    }
}
