package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        if (fromPrevious != null) {
            String emailAddress = fromPrevious.getStringExtra("EmailAddress");
            TextView textViewTop = findViewById(R.id.textViewTop);
            textViewTop.setText("Welcome back " + emailAddress);
        }

        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(v -> {
            EditText phoneEditText = findViewById(R.id.phoneEditText);
            String phoneNumber = phoneEditText.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            if (!phoneNumber.isEmpty()) {
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });

        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getExtras() != null) {
                                Bitmap thumbnail = data.getParcelableExtra("data");
                                ImageView cameraImageView = findViewById(R.id.cameraImageView); // Correct ID
                                cameraImageView.setImageBitmap(thumbnail);
                            }
                        }
                    }
                });

        Button changePictureButton = findViewById(R.id.changePictureButton);
        changePictureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResultLauncher.launch(cameraIntent);
        });
    }
}
