package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // After successful login, start MainActivity (with the navigation drawer)
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();  // Close LoginActivity so the user cannot go back
                        } else if (user != null) {
                            // If email is not verified, send a verification email and log out
                            user.sendEmailVerification()
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(LoginActivity.this, "Verification email sent. Please check your inbox.", Toast.LENGTH_LONG).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(LoginActivity.this, "Error sending verification email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            mAuth.signOut();
                            Toast.makeText(LoginActivity.this, "Please verify your email to log in.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle login failure
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(LoginActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
