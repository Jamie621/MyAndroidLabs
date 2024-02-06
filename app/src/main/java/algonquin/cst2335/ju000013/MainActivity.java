package algonquin.cst2335.ju000013;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "MyData"; // Change to public
    public static final String EMAIL_KEY = "LoginName"; // Change to public
    private EditText emailEditText;
    private static final int CAMERA_PERMISSION_CODE = 123; // You can choose any integer value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }



        Log.w(TAG, "onCreate() - Loading Widgets");

        // Initialize the EditText and Button
        emailEditText = findViewById(R.id.emailEditText);
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
        });
    }





    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart() - The application is now visible on the screen.");
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
