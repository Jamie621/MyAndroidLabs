package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "MyData";
    private static final String EMAIL_KEY = "LoginName";
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "onCreata() - Loading Widgets");

        // Initialize the EditText and Button
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        // Load the email from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String email = prefs.getString(EMAIL_KEY, "");
        emailEditText.setText(email);

        loginButton.setOnClickListener(v -> {
            String emailToSave = emailEditText.getText().toString();
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra(EMAIL_KEY, emailToSave);
            startActivity(nextPage);

            // Save the email to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(EMAIL_KEY, emailToSave);
            editor.apply();


            @Override
            public void onClick(View v) {
                EditText emailEditText = findViewById(R.id.emailEditText); // Replace with your actual EditText ID
                String email = emailEditText.getText().toString();

                Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
                nextPage.putExtra( "EmailAddress", emailEditText.getText().toString());
                startActivity(nextPage);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart() - The application is now visible on screen.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume() - The application is now responding to user input.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause() - Another activity is taking focus, this activity is about to be paused.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop() - The application is no longer visible to the user, it's now stopped.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy() - Any memory used by the application is freed.");
    }
}