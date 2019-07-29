package com.example.driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {


    FirebaseAuth mAuth;

    private ProgressBar progressBar;

    Button log_in ;
    EditText email;
    EditText pas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_in = findViewById(R.id.login);
        email = findViewById(R.id.emailAddress);
        pas = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

    }


    public void login(View view) {

        final String email_text = email.getText().toString().trim();
        final String pas_text = pas.getText().toString().trim();



        if (email_text.isEmpty()) {
            email.setError(getString(R.string.input_error_email));
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
            email.setError(getString(R.string.input_error_email_invalid));
            email.requestFocus();
            return;
        }
        if (pas_text.isEmpty()) {
            pas.setError(getString(R.string.input_error_password));
            pas.requestFocus();
            return;
        }
        if (pas_text.length() < 6) {
            pas.setError(getString(R.string.input_error_password_length));
            pas.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email_text, pas_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(
                            login.this,
                            "your email or password my be wrong !, please try login again",
                            Toast.LENGTH_LONG).show();

                } else {

                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(login.this, navigation.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

}
