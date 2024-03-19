package algonquin.cst2335.ju000013;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * MainActivity serves as the primary user interface for the application.
 * It initializes the application's views and handles user interactions.
 * Users can input and validate password strength based on predefined criteria
 * such as the inclusion of uppercase letters, lowercase letters, numbers,
 * and special characters.
 *
 * @author Jungmin Ju
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the TextView that displays text at the center of the screen */
    private TextView tv = null;

    /** This holds the EditText where the user inputs their password */
    private EditText et = null;

    /** This holds the Button that the user presses to initiate the password check */
    private Button btn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editText);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener( clk ->{

            String password = et.getText().toString();
            boolean isComplex = checkPasswordComplexity(password);
            if (isComplex) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }

        });

    }

    /** This function checks the complexity of a password
     *
     * @param pw The String object that we are checking.
     * @return Return true if the password meet the requirements.
     *
     */
    boolean checkPasswordComplexity( String pw)
    {
        // Function implementation goes here

        // Initialize the boolean variables
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        // Iterate over each character in the password string
        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        // Check if all conditions are met
        if (!foundUpperCase) {
            // Toast.makeText(getApplicationContext(), "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            // Toast.makeText(getApplicationContext(), "Your password does not have a lower case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            // Toast.makeText(getApplicationContext(), "Your password does not have a number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            // Toast.makeText(getApplicationContext(), "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Determines if the given character is one of the specified special characters.
     *
     * @param c The character to check.
     * @return true if the character is a special character, false otherwise.
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
                return true;
            default:
                return false;
        }
    }


}