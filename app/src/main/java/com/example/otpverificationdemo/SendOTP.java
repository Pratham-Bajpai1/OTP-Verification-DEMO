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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {

    EditText inputPhoneNumber;
    Button buttonSendOtp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_otp);

        inputPhoneNumber = findViewById(R.id.input_PhoneNumber);
        buttonSendOtp = findViewById(R.id.button_SendOtp);
        progressBar = findViewById(R.id.progress_bar);

        buttonSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputPhoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(SendOTP.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonSendOtp.setEnabled(false);

                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + inputPhoneNumber.getText().toString(),
                        60, TimeUnit.SECONDS, SendOTP.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttonSendOtp.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                buttonSendOtp.setVisibility(View.VISIBLE);
                                Toast.makeText(SendOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                //super.onCodeSent(s, forceResendingToken);
                                progressBar.setVisibility(View.GONE);
                                buttonSendOtp.setVisibility(View.VISIBLE);
                                Toast.makeText(SendOTP.this, "OTP sent", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                                intent.putExtra("mobile", inputPhoneNumber.getText().toString());
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                            }
                        });
            }
        });

    }
}