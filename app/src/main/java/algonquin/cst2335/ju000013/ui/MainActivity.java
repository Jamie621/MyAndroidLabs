package algonquin.cst2335.ju000013.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import algonquin.cst2335.ju000013.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // calls parent onCreate()

        model = new ViewModelProvider(this).get(MainViewModel.class);

        VariableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot()); // loads XML on screen

        variableBinding.myText.setText(model.editString);
        variableBinding.mybutton.setOnClickListner(click ->
        {
            model.editString.postValue(variableBinding.myEditText.getText().toString());
            model.editString.observe(this, s -> {
                variableBinding.myText.setText("Your edit text has "+ s);
            });
//            model.editString = variableBinding.myEditText.getText().toString();
//            variableBinding.myText.setText("Your edit text has: " + model.editString);
        });
    }
}

//        TextView mytext = VariableBinding.texttView;
//        Button btn = VariableBinding.mybutton;
//        EditText myedit = VariableBinding.myedittext;

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Code to execute when the button is clicked
//                mybutton.setOnClickListener(   vw  ->  mytext.setText("Your edit text has: " + editString)    );
//
//                // Get text from EditText
//                String editString = myedit.getText().toString();
//
//                // Update TextView with the new string
//                mytext.setText("Your edit text has: " + editString);
//            }
//        });
//    }
//}