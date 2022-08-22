package com.example.userregistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageProfileActivity extends AppCompatActivity {

    CircleImageView img;
    FloatingActionButton fab;
    
    private TextView welcomeText, profileName, profileEmail, profileGender, profileDOB, logout;
    private String fullname, email, doB, gender;
    private FirebaseAuth authProfile;

    FirebaseStorage storage;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        img = findViewById(R.id.profile_image);
        fab = findViewById(R.id.floatingActionButton);
        welcomeText = findViewById(R.id.textViewWelcome);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profileGender = findViewById(R.id.profile_gender);
        profileDOB = findViewById(R.id.profile_dob);
        logout = findViewById(R.id.logout);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        database.getReference().child("Registered Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                        Picasso.get().load(readWriteUserDetails.getProfileImg()).into(img);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        
        if(firebaseUser == null){
            Toast.makeText(ManageProfileActivity.this, "Something Went Wrong! User details not available at the moment", Toast.LENGTH_SHORT).show();
        }else{
            showUserProfile(firebaseUser);
        }
        

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ManageProfileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID =firebaseUser.getUid();

        //Extracting User Reference from Database for "Registered Users"

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    if(readUserDetails != null){
                        fullname = readUserDetails.fullName;
                        email = firebaseUser.getEmail();
                        doB = readUserDetails.dob;
                        gender = readUserDetails.gender;

                        welcomeText.setText("Welcome, "+ fullname +"!");
                        profileName.setText(fullname);
                        profileEmail.setText(email);
                        profileDOB.setText(doB);
                        profileGender.setText(gender);
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ManageProfileActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        img.setImageURI(uri);

        final StorageReference reference = storage.getReference().child("profile_pictures")
                .child(FirebaseAuth.getInstance().getUid());

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ManageProfileActivity.this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show();

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        database.getReference().child("Registered Users").child(FirebaseAuth.getInstance().getUid())
                                .child("profileImg").setValue(uri.toString());

                        Toast.makeText(ManageProfileActivity.this, "Profile Image uploaded", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        authProfile = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                Intent intent = new Intent(ManageProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
