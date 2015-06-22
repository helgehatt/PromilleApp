package com.example.helge.alculator;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddDrinkActivity extends Activity {

    public static final String NAME = "DRINK_NAME";
    public static final String ALCOHOL = "DRINK_ALCOHOL";
    public static final String VOLUME = "DRINK_VOLUME";
    public static final String CALORIES = "DRINK_CALORIES";
    public static final String IMAGE = "DRINK_IMAGE";
    public static final String IMAGE_PATH = "DRINK_IMAGE_PATH";

    private static final String MISSING_FIELDS = "Required fields are missing.";

    private EditText nameField;
    private EditText percentageField;
    private EditText volumeField;
    private EditText caloriesField;
    private ImageView image;

    private Intent drinkData;

    static final int SELECT_PICTURE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drinkData = new Intent();

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
                String nameString = nameField.getText().toString();
                String alcoholString = percentageField.getText().toString();
                String volumeString = volumeField.getText().toString();
                String caloriesString = caloriesField.getText().toString();

                if (nameString.isEmpty() || alcoholString.isEmpty() || volumeString.isEmpty()){
                    Toast.makeText(getApplicationContext(), MISSING_FIELDS, Toast.LENGTH_LONG).show();
                    return;
                }
                
                drinkData.putExtra(NAME, nameString);
                drinkData.putExtra(ALCOHOL, alcoholString);
                drinkData.putExtra(VOLUME, volumeString);
                drinkData.putExtra(CALORIES, caloriesString);

                //Get current image from imageview, compress it and put it as extra.
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                drinkData.putExtra(IMAGE, byteArray);

                setResult(RESULT_OK, drinkData);
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE_REQUEST);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE_REQUEST) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            drinkData.putExtra(IMAGE_PATH, filePath);
            // TODO change image in view
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        // can post image
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(contentUri,
                proj,
                null,
                null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

}
