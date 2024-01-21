package algonquin.cst2335.ju000013.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import algonquin.cst2335.ju000013.data.MainViewModel;
import algonquin.cst2335.ju000013.databinding.ActivityMainBinding;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        ImageView myImageView = variableBinding.myImageView;
        TextView textView = variableBinding.textview;

        model.isSelected.observe(this, selected -> {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switchButton.setChecked(selected);
        });

        variableBinding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("CheckBox: " + isChecked);
        });

        variableBinding.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("Switch: " + isChecked);
        });

        variableBinding.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("RadioButton: " + isChecked);
        });

        variableBinding.mybutton.setOnClickListener(v -> {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
            model.editString.observe(this, s -> textView.setText("Your edit text has " + s));
        });

        variableBinding.myImageButton.setOnClickListener(v -> {
            int width = myImageView.getWidth();
            int height = myImageView.getHeight();
            showToast("The width = " + width + " and height = " + height);
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
