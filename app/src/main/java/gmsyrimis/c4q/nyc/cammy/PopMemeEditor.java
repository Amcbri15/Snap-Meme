package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PopMemeEditor extends Activity {
    // VIEW ELEMENTS
    private ImageView ivCustomPopular;
    private Bitmap bitmap;
    private EditText topRow;
    private EditText bottomRow;
    private Button shareBt;
    private Button saveBt;
    private LinearLayout linearPopLayout;
    // KEY VALUE PAIRS
    private String imageUri = "";
    public static String IMAGE_URI_KEY = "uri";
    private String topText = "";
    public static String TOP_TEXT_KEY = "top";
    private String bottomText = "";
    public static String BOTTOM_TEXT_KEY = "bottom";
    // Anthony's suggested variable
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_meme_editor);
        // Setting views to ids
        ivCustomPopular = (ImageView) findViewById(R.id.iv_custom_popular);
        bottomRow = (EditText) findViewById(R.id.bottom_pop);
        topRow = (EditText) findViewById(R.id.top_pop);
        shareBt = (Button) findViewById(R.id.bt_share_popo);
        saveBt = (Button) findViewById(R.id.bt_save_popo);
        linearPopLayout = (LinearLayout) findViewById(R.id.linear_pop_layout);
        // Loading image uri from previous activity
        if (savedInstanceState == null) {
            imageUri = getIntent().getExtras().getString(IMAGE_URI_KEY);
            topText = "";
            bottomText = "";
        } else {
            imageUri = savedInstanceState.getString(IMAGE_URI_KEY);
            topText = savedInstanceState.getString(TOP_TEXT_KEY);
            bottomText = savedInstanceState.getString(BOTTOM_TEXT_KEY);
        }
        // Setting text to image
        topRow.setText(topText);
        bottomRow.setText(bottomText);
        int popId = Integer.parseInt(imageUri);
        ivCustomPopular.setImageDrawable(getResources().getDrawable(popId));
        // OLD SHARE BUTTON
        shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));

            }
        });

        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIt(linearPopLayout, linearPopLayout.getWidth(), linearPopLayout.getHeight());
            }
        });

        // TODO SHARE BUTTON AND SAVE BUTTON

    }
    // Create an image file name for use when saving image for sharing
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_SEND intent
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    // Method to share an image via social networks
    public void shareVia(Bitmap mBitmap) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f;
        try {
            f = createImageFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(mCurrentPhotoPath));
        startActivity(Intent.createChooser(share, "Share Image"));
    }


    public static Bitmap screenView(View v, int width, int height) {
        Bitmap screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return screenshot;
    }

    public void saveIt(View memeView, int width, int height) {
        Bitmap sharable = screenView(memeView, width, height);
        // FILE SETUP
        String imageFileName = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());
        String filename = "Snapmeme" + imageFileName + ".jpeg";
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File outputFile = null;
        try {
            outputFile = File.createTempFile(filename, ".jpg", picDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // STREAM
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            sharable.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // URI TO BE PASSED
        Uri resultUri = Uri.fromFile(outputFile);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(resultUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_URI_KEY, imageUri);
        outState.putString(TOP_TEXT_KEY, topText);
        outState.putString(BOTTOM_TEXT_KEY, bottomText);
    }

}
