package gmsyrimis.c4q.nyc.cammy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;


public class SelectStyleActivity extends Activity {

    ImageView chooseDemotivational;
    ImageView chooseVanilla;

    // TODO: Change imgUri to Uri type and not String type. Fix any code that breaks as a result.
    private String imageUri;

    public static String IMAGE_URI_KEY = "uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_style);

        Intent getFromMain = getIntent();

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString(IMAGE_URI_KEY) != null) {
                imageUri = bundle.getString(IMAGE_URI_KEY);
            }
        } else {
            imageUri = savedInstanceState.getString(IMAGE_URI_KEY);
        }

        chooseDemotivational = (ImageView) findViewById(R.id.choose_demotivational);
        chooseDemotivational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDemotivational = new Intent(SelectStyleActivity.this, DemotivationalActivity.class);
                gotoDemotivational.putExtra(IMAGE_URI_KEY, imageUri);
                startActivity(gotoDemotivational);
            }
        });
        chooseVanilla = (ImageView) findViewById(R.id.choose_vanilla);
        chooseVanilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoVanilla = new Intent(SelectStyleActivity.this, VanillaActivity.class);
                gotoVanilla.putExtra(IMAGE_URI_KEY, imageUri);
                startActivity(gotoVanilla);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_URI_KEY, imageUri);
    }
}
