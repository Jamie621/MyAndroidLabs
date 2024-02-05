package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
//    private static final String EMAIL_KEY = "EmailAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "onCreata() - Loading Widgets");
        Button loginButton = findViewById(R.id.loginButton);

//         Use a lambda expression

//        loginButton.setOnClickListener( clk -> {
//            EditText emailEditText = findViewById(R.id.emailEditText);
//            String email = emailEditText.getText().toString();
//
//            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
//            nextPage.putExtra("EmailAddress", email);
//            startActivity(nextPage);
//        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = findViewById(R.id.emailEditText); // Replace with your actual EditText ID
                String email = emailEditText.getText().toString();

                Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
                nextPage.putExtra( "EmailAddress", emailEditText.getText().toString());
                startActivity(nextPage);

// In FirstActivity, sending data
//                Intent next = new Intent(FirstPage.this, NextPage.class);
//                next.putExtra("Age", 30); // Assuming you want to pass the age as an int
//                next.putExtra("Name", "Jason"); // Passing a String
//                next.putExtra("PostalCode", "12345"); // Another String
//                startActivity(next);

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