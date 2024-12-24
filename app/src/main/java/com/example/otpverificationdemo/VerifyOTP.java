package com.example.otpverificationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTP extends AppCompatActivity {

    EditText inputOtp1, inputOtp2, inputOtp3, inputOtp4, inputOtp5, inputOtp6;
    Button verifyOtpButton;
    ProgressBar progressBar;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);

        inputOtp1 = findViewById(R.id.input_otp1);
        inputOtp2 = findViewById(R.id.input_otp2);
        inputOtp3 = findViewById(R.id.input_otp3);
        inputOtp4 = findViewById(R.id.input_otp4);
        inputOtp5 = findViewById(R.id.input_otp5);
        inputOtp6 = findViewById(R.id.input_otp6);
        verifyOtpButton = findViewById(R.id.button_VerifyOtp);
        progressBar = findViewById(R.id.progressbar);

        verificationId = getIntent().getStringExtra("verificationId");
        String mobileNumber = getIntent().getStringExtra("mobile");

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputOtp1.getText().toString().trim().isEmpty()
                    || inputOtp2.getText().toString().trim().isEmpty()
                    || inputOtp3.getText().toString().trim().isEmpty()
                    || inputOtp4.getText().toString().trim().isEmpty()
                    || inputOtp5.getText().toString().trim().isEmpty()
                    || inputOtp6.getText().toString().trim().isEmpty()){

                    Toast.makeText(VerifyOTP.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                String code = inputOtp1.getText().toString() + inputOtp2.getText().toString() + inputOtp3.getText().toString()
                         + inputOtp4.getText().toString() + inputOtp5.getText().toString() + inputOtp6.getText().toString();

                if(verificationId != null){
                    progressBar.setVisibility(View.VISIBLE);
                    verifyOtpButton.setVisibility(View.INVISIBLE);

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            verifyOtpButton.setVisibility(View.VISIBLE);
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(VerifyOTP.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(VerifyOTP.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}