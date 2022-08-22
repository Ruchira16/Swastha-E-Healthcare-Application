package com.example.userregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SearchDoctorActivity extends AppCompatActivity implements RecyclerViewInterface{

    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    Button btnBook;
    //TextView Book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        recyclerView = (RecyclerView) findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<SearchModel> options =
                new FirebaseRecyclerOptions.Builder<SearchModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Doctors"), SearchModel.class)
                .build();

        searchAdapter = new SearchAdapter(options, this);
        recyclerView.setAdapter(searchAdapter);

    }



    @Override
    protected void onStart() {
        super.onStart();
        searchAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SearchDoctorActivity.this, BookAppointmentActivity.class);
        startActivity(intent);

    }
}