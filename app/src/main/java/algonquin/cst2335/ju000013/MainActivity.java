package algonquin.cst2335.ju000013;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "onStart()-The application is niw visible on screen");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy()-Activity is about to be destroyed, releasing any resources.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop()-Activity is no longer visible to the user, it's now stopped.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause()-Another activity is taking focus, this activity is about to be paused.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume()-The application is now responding to user");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "In onCreate()-Activity is being created, setting up the initial state");
    }
}