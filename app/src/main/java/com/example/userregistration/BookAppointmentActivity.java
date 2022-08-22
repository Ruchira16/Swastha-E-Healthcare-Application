package com.example.userregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userregistration.databinding.ActivityBookAppointmentBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity{

    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    ActivityBookAppointmentBinding binding;
    String userName, email, dob,gender, Aptdate, doctorName, phoneNumber, location;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    RadioButton male, female;
    FirebaseDatabase db;
    DatabaseReference reference;
    int i = 0;
    Book book;
    private int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Booked Appointments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    i = (int)snapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(BookAppointmentActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });




        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        binding.LocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLastLocation();

            }
        });

        final Calendar calendar=Calendar.getInstance();
        binding.inputDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(BookAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.inputDOB.setText(dayOfMonth+"-" + (month+1)+"-"+year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        binding.inputDateofAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(BookAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.inputDateofAppointment.setText(dayOfMonth+"-" + (month+1)+"-"+year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });




        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = binding.inputName.getText().toString();
                email = binding.inputEmail.getText().toString();
                dob = binding.inputDOB.getText().toString();
                gender = binding.inputGender.getText().toString();
                Aptdate = binding.inputDateofAppointment.getText().toString();
                doctorName = binding.inputDoctorName.getText().toString();
                phoneNumber = binding.inputPhone.getText().toString();
                location = binding.inputLocation.getText().toString();


                if (!email.matches(emailPattern) || userName.isEmpty() || dob.isEmpty() || Aptdate.isEmpty() || doctorName.isEmpty()
                        || location.isEmpty()) {
                    Toast.makeText(BookAppointmentActivity.this, "Enter valid details", Toast.LENGTH_SHORT).show();
                } else if (binding.inputPhone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(BookAppointmentActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                } else if (binding.inputPhone.getText().toString().trim().length() != 10) {
                    Toast.makeText(BookAppointmentActivity.this, "Please Type valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    Book book = new Book(userName, email, dob,gender, Aptdate, doctorName,phoneNumber, location);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Booked Appointments");

                    reference.child(userName).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            binding.inputName.setText("");
                            binding.inputEmail.setText("");
                            binding.inputDOB.setText("");
                            binding.inputGender.setText("");
                            binding.inputDateofAppointment.setText("");
                            binding.inputDoctorName.setText("");
                            binding.inputPhone.setText("");
                            binding.inputLocation.setText("");
                            Toast.makeText(BookAppointmentActivity.this, "Your Appointment is Booked", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }


    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                try {
                                    Geocoder geocoder = new Geocoder(BookAppointmentActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    binding.inputLocation.setText(addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else {
            askPermission();
        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(BookAppointmentActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getLastLocation();

            }else {
                Toast.makeText(BookAppointmentActivity.this,"Please provide the required permission",Toast.LENGTH_SHORT).show();

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}