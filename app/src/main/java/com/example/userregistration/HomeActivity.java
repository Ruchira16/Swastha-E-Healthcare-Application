package com.example.userregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity{

    CardView cardBook;
    CardView cardSearch;
    CardView cardProfile;
    CardView cardAbout;
    CardView cardLogout;
    CardView cardNotify;
    CardView cardChat;


    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardBook = findViewById(R.id.cardBook);
        cardSearch = findViewById(R.id.cardSearch);
        cardProfile = findViewById(R.id.cardProfile);
        cardAbout = findViewById(R.id.cardAbout);
        cardLogout = findViewById(R.id.cardLogout);
        cardNotify = findViewById(R.id.cardNotify);
        cardChat = findViewById(R.id.cardChat);

        cardBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BookAppointmentActivity.class);
                startActivity(intent);
            }
        });
        cardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchDoctorActivity.class);
                startActivity(intent);
            }
        });
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ManageProfileActivity.class);
                startActivity(intent);
            }
        });



        cardAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        cardNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotifyActivity.class);
                startActivity(intent);
            }
        });

        cardChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChatbotActivity.class);
                startActivity(intent);
            }
        });



        mAuth = FirebaseAuth.getInstance();

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


}