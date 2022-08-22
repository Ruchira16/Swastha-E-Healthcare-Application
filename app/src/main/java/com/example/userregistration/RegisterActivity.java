package com.example.userregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    TextView alreadyHaveAccount;
    EditText inputEmail, inputPassword, inputGender, inputConfirmPassword, inputName, inputBirthDate;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    private int year,month,day;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        inputName = findViewById(R.id.inputName);
        inputBirthDate = findViewById(R.id.inputBirthDate);
        inputEmail = findViewById(R.id.inputEmail);
        inputGender = findViewById(R.id.inputGender);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



        final Calendar calendar=Calendar.getInstance();

        inputBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        inputBirthDate.setText(dayOfMonth+"-" + (month+1)+"-"+year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = inputName.getText().toString();
                String email=inputEmail.getText().toString();
                String dob = inputBirthDate.getText().toString();
                String gender = inputGender.getText().toString();
                String password=inputPassword.getText().toString();
                String confirmPassword=inputConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(RegisterActivity.this, "Enter your full name", Toast.LENGTH_SHORT).show();
                    inputName.setError("Full name is required");
                    inputName.requestFocus();
                }else if(!email.matches(emailPattern)){
                    inputEmail.setError("Enter Correct Email");
                    inputEmail.requestFocus();
                }else if(password.isEmpty() || password.length()<6)
                {
                    inputPassword.setError("Password should be 6 characters");

                }else if(TextUtils.isEmpty(dob)) {
                    Toast.makeText(RegisterActivity.this, "Enter your date of birth", Toast.LENGTH_SHORT).show();
                    inputBirthDate.setError("Enter date of birth");
                    inputBirthDate.requestFocus();
                }
                else if (!password.equals(confirmPassword))
                {
                    inputConfirmPassword.setError("Passwords do not match");
                }else {

                    registerUser(name, email, dob, gender, password);
                }

            }


        });
    }

    private void registerUser(String name, String email, String dob, String gender, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registration Successsful", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    // Enter User Data into the firebase realtime database

                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(name, dob, email, gender);

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegisterActivity.this, "Registration Successful! Please verify your email and phone number", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, PhoneVerificationActivity.class);
                                startActivity(intent);
                                finish();

                            } else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed. Please try again", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}