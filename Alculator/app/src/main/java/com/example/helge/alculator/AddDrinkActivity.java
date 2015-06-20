package com.example.helge.alculator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddDrinkActivity extends Activity {

    public static final int NEW_DRINK = 1;
    public static final String NAME = "DRINK_NAME";
    public static final String ALCOHOL = "DRINK_ALCOHOL";
    public static final String VOLUME = "DRINK_VOLUME";
    public static final String CALORIES = "DRINK_CALORIES";
    public static final String IMAGE = "DRINK_IMAGE";

    private static final String MISSING_FIELDS = "Required fields are missing.";

    private EditText nameField;
    private EditText percentageField;
    private EditText volumeField;
    private EditText caloriesField;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_drink);
        
        nameField = (EditText) findViewById(R.id.editText_drink_name);
        percentageField = (EditText) findViewById(R.id.editText_drink_percentage);
        volumeField = (EditText) findViewById(R.id.editText_drink_volume);
        caloriesField = (EditText) findViewById(R.id.editText_drink_calories);
        image = (ImageView) findViewById(R.id.image);

        Button okButton = (Button) findViewById(R.id.button_ok);
        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        Button deleteButton = (Button) findViewById(R.id.button_delete);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                String nameString = nameField.getText().toString();
                String alcoholString = percentageField.getText().toString();
                String volumeString = volumeField.getText().toString();
                String caloriesString = caloriesField.getText().toString();
                
                data.putExtra(NAME, nameField.getText().toString());
                data.putExtra(ALCOHOL, percentageField.getText().toString());
                data.putExtra(VOLUME, volumeField.getText().toString());
                data.putExtra(CALORIES, caloriesField.getText().toString());

                //Get current image from imageview, compress it and put it as extra.
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                data.putExtra(IMAGE, byteArray);

                setResult(RESULT_OK, data);
                finish();
            }
        });
        
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Delete current image?
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: give image choices.
            }
        });
    }
}
