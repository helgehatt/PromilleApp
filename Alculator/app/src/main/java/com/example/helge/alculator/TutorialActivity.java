package com.example.helge.alculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialActivity extends Activity {

    private int state = 1;
    ImageView image;
    TextView text;
    TextView counter;
    Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        image = (ImageView) findViewById(R.id.tutorial_imageView);
        text = (TextView) findViewById(R.id.tutorial_text);
        counter = (TextView) findViewById(R.id.tutorial_counter);
        quitButton = (Button) findViewById(R.id.tutorial_button);
        ImageButton arrowLeft = (ImageButton) findViewById(R.id.arrow_left);
        ImageButton arrowRight = (ImageButton) findViewById(R.id.arrow_right);
        quitButton.setVisibility(View.GONE);

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 < state) {
                    state--;
                    updateState();
                }
            }
        });
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state < 6){
                    state++;
                    updateState();
                }
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateState() {
        switch(state){
            case 1:
                image.setImageResource(R.drawable.tutorial_1);
                text.setText(R.string.tutorial_1);
                counter.setText("1/6");
                break;
            case 2:
                image.setImageResource(R.drawable.tutorial_2);
                text.setText(R.string.tutorial_2);
                counter.setText("2/6");
                break;
            case 3:
                image.setImageResource(R.drawable.tutorial_3);
                text.setText(R.string.tutorial_3);
                counter.setText("3/6");
                break;
            case 4:
                image.setImageResource(R.drawable.tutorial_4);
                text.setText(R.string.tutorial_4);
                counter.setText("4/6");
                break;
            case 5:
                image.setImageResource(R.drawable.tutorial_5);
                text.setText(R.string.tutorial_5);
                counter.setText("5/6");
                break;
            case 6:
                image.setImageResource(R.drawable.tutorial_6);
                text.setText(R.string.tutorial_6);
                counter.setText("6/6");
                quitButton.setVisibility(View.VISIBLE);
                break;
        }
    }
}
