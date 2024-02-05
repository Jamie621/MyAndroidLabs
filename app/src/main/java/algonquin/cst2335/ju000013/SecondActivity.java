package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Retrieve the Intent that started this Activity
        Intent fromPrevious = getIntent();

        // Now retrieve the email address string that was passed with putExtra()
        if (fromPrevious != null) {
            String emailAddress = fromPrevious.getStringExtra("EmailAddress");

            // Find the TextView by its ID and set the email address as its text
            TextView textViewTop = findViewById(R.id.textViewTop);
            textViewTop.setText(emailAddress); // Set the email address as text

            // In NextPage, retrieving data
//            Intent fromPrevious = getIntent();
//            int age = fromPrevious.getIntExtra("Age", -1); // -1 is a default value in case "Age" is not found
//            String name = fromPrevious.getStringExtra("Name");
//            String pCode = fromPrevious.getStringExtra("PostalCode");


        }
    }
}
