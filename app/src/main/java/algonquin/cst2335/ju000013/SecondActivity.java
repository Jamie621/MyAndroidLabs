package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        cameraImageView = findViewById(R.id.cameraImageView);
        loadProfileImage();

        Intent fromPrevious = getIntent();
        if (fromPrevious != null) {
            String emailAddress = fromPrevious.getStringExtra(MainActivity.EMAIL_KEY);
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
                                if (thumbnail != null) {
                                    cameraImageView.setImageBitmap(thumbnail);
                                    saveProfileImage(thumbnail);
                                }
                            }
                        }
                    }
                }
        );

        Button changePictureButton = findViewById(R.id.changePictureButton);
        changePictureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResultLauncher.launch(cameraIntent);
        });

        // Detect long-press gesture on the profile picture
        cameraImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Clear the saved profile image (delete the image file)
                clearProfileImage();

                // Display the default picture (e.g., a placeholder image)
                cameraImageView.setImageResource(R.drawable.ic_menu_camera); // Replace with your default image resource

                // Return true to consume the long-press event
                return true;
            }
        });
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

    // Method to clear the saved profile image (delete the image file)
    private void clearProfileImage() {
        File file = new File(getFilesDir(), IMAGE_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
