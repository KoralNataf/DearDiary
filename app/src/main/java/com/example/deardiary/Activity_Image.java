package com.example.deardiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;

public class Activity_Image extends Activity_Base {

    private ImageView image_IMG_background;
    private CircularImageView image_IMG_user;
    private TextView image_LBL_save;
    private TextView image_LBL_back;
    private boolean isSuccess= false;
    private Uri downloadUri;

    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;

    private StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        findViews();
        initViews();
    }

    private void findViews() {
        image_IMG_background = findViewById(R.id.image_IMG_background);
        image_IMG_user = findViewById(R.id.image_IMG_user);
        image_LBL_save = findViewById(R.id.image_LBL_save);
        image_LBL_back = findViewById(R.id.image_LBL_back);
    }

    private void initViews() {

        updateImage(this.getResources().getIdentifier("background",
                "drawable", this.getPackageName()), image_IMG_background);
        readProfileFromDB();

        image_LBL_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSuccess){
                    final ProgressDialog progressDialog = new ProgressDialog(Activity_Image.this);
                    String mUri = downloadUri.toString();
                    Log.d("pttt", mUri);

                    myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("imageURL", mUri);
                    myRef.updateChildren(map);

                    Toast.makeText(Activity_Image.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        image_LBL_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Image.this, Activity_Main.class);
                startActivity(intent);
                finish();
            }
        });

        //Profile Image ref in storage;
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(firebaseUser.getUid());

        image_IMG_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimePermission();
            }
        });
    }

    private void runTimePermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void selectImage() {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        this.startActivityForResult(chooserIntent, IMAGE_REQUEST);
    }

    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadMyImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtention(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful())
                        throw task.getException();

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        isSuccess = true;
                        downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        Glide.with(Activity_Image.this).load(mUri).into(image_IMG_user);

                        progressDialog.dismiss();
                    }else {

                        Toast.makeText(Activity_Image.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Activity_Image.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });
        }else{
            Toast.makeText(Activity_Image.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){

            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(Activity_Image.this, "Upload In Progress..", Toast.LENGTH_SHORT).show();
            }else {
                uploadMyImage();
            }
        }
    }

    private void readProfileFromDB(){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class); //load user from DB
                if (!user.getImageURL().equals("default")) {
                    Glide.with(Activity_Image.this).load(user.getImageURL()).into(image_IMG_user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}