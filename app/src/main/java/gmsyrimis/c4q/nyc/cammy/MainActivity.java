package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {
    // ELEMENTS
    ImageView gotoCamera;
    ImageView gotoGallery;
    ImageView gotoPop;
    // KEY VALUE PAIRS
    private Uri imageUri;
    public static String IMAGE_URI_KEY = "uri";
    // FLAGS
    private static final int CAMERA_REQUEST = 1;
    private static final int SELECT_SINGLE_PICTURE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // FINDING VIEWS
        gotoGallery = (ImageView) findViewById(R.id.iv_gallery);
        gotoCamera = (ImageView) findViewById(R.id.iv_camera);
        gotoPop = (ImageView) findViewById(R.id.iv_pop);
        // GALLERY BUTTON
        gotoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(galleryIntent, SELECT_SINGLE_PICTURE);
                }
            }
        });
        // CAMERA BUTTON
        gotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (capture.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(capture, CAMERA_REQUEST);
                }
            }
        });
        // POPULAR MEMES BUTTON
        gotoPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pop = new Intent(MainActivity.this, PopMemes.class);
                startActivity(pop);
            }
        });
    }

    private void gotoSelectStyle() {
        Intent selectStyle = new Intent(MainActivity.this, SelectStyleActivity.class);
        selectStyle.putExtra(IMAGE_URI_KEY, imageUri);
        startActivity(selectStyle);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                imageUri = data.getData();
                gotoSelectStyle();
            } else if (requestCode == SELECT_SINGLE_PICTURE && resultCode == RESULT_OK && null != data) {
                imageUri = data.getData();
                gotoSelectStyle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

