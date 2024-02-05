package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SecondActivity extends AppCompatActivity {

    private static final String IMAGE_FILE_NAME = "profile_picture.png";
    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ImageView cameraImageView;
    private EditText phoneEditText;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        cameraImageView = findViewById(R.id.cameraImageView);
        phoneEditText = findViewById(R.id.phoneEditText);
        loadProfileImage();

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra(MainActivity.EMAIL_KEY);
        TextView textViewTop = findViewById(R.id.textViewTop);
        textViewTop.setText(getString(R.string.welcome_back, emailAddress));

        String phoneNumber = prefs.getString("PhoneNumber", "");
        phoneEditText.setText(phoneNumber);

        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(v -> {
            String phone = phoneEditText.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            if (!phone.isEmpty()) {
                callIntent.setData(Uri.parse("tel:" + phone));
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
                                if (thumbnail != null) {
                                    cameraImageView.setImageBitmap(thumbnail);
                                    saveProfileImage(thumbnail);
                                }
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

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneEditText.getText().toString());
        editor.apply();
    }

    private void loadProfileImage() {
        File file = new File(getFilesDir(), IMAGE_FILE_NAME);
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            cameraImageView.setImageBitmap(theImage);
        }
    }

    private void saveProfileImage(Bitmap bitmap) {
        try (FileOutputStream fOut = openFileOutput(IMAGE_FILE_NAME, MODE_PRIVATE)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
