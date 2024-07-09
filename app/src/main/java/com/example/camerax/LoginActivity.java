package com.example.camerax;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();


        emailEditText = findViewById(R.id.userName_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();


                            String rollno = user.getEmail().substring(0, 8);

                            // Check student login format
                            String checkStudent = "^\\d{2}[A-Za-z]{2}\\d{4}$";
                            Pattern patternStudent = Pattern.compile(checkStudent);
                            Matcher matcherStudent = patternStudent.matcher(rollno);

                            // Check guard login format
                            String checkGuard = "^[A-Za-z]{3}\\d{4}[A-Za-z]{1}$";
                            Pattern patternGuard = Pattern.compile(checkGuard);
                            Matcher matcherGuard = patternGuard.matcher(rollno);

                            // check for admin format
                            String checkAdmin="^[A-Za-z]{5}\\d{2}[a-z]{1}$";
                            Pattern patternAdmin=Pattern.compile(checkAdmin);
                            Matcher matcherAdmin=patternAdmin.matcher(rollno);


                            if (matcherStudent.matches()) {
                                Toast.makeText(LoginActivity.this, "Student login", Toast.LENGTH_SHORT).show();
                                Intent studentIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(studentIntent);
                                finish();
                            }

                            else if (matcherGuard.matches()) {
                                Toast.makeText(LoginActivity.this, "Admin login", Toast.LENGTH_SHORT).show();
                                Intent adminIntent = new Intent(LoginActivity.this, GateKeeperActivity.class);
                                startActivity(adminIntent);
                                finish();
                            }

                            else if(matcherAdmin.matches()) {

                                Toast.makeText(LoginActivity.this, "Admin login", Toast.LENGTH_SHORT).show();
                                Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(adminIntent);
                                finish();
                            }
                        }

                        else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
