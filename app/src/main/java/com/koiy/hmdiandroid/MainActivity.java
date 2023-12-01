package com.koiy.hmdiandroid;

import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //    TvView view;
    TextView tv;
    StringBuilder sb;


    public void reportState(final String state) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                sb.append(state);
                tv.setText(sb.toString());
            }
        });
    }

    private void openActionView(TvInputInfo tvInputInfo) {

        if (tvInputInfo != null) {
//
//            TvInputInfo tvInputInfo;
//            tvInputInfo = inputInfoList.get(index);
            Uri uri;

            if (tvInputInfo.isPassthroughInput()) {
                uri = TvContract.buildChannelUriForPassthroughInput(tvInputInfo.getId());
            } else {
                uri = TvContract.buildChannelsUriForInput(tvInputInfo.getId());
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TvInputManager mTvInputManager = (TvInputManager) getSystemService(Context.TV_INPUT_SERVICE);
        sb = new StringBuilder();
        List<TvInputInfo> inputs = mTvInputManager.getTvInputList();
        tv = findViewById(R.id.mytextfield);

        LinearLayout buttonCollention = findViewById(R.id.buttonCollention);

        for (TvInputInfo tvInputInfo : inputs) {

            String inputName = extractName(tvInputInfo.getId());
            // Creating a new Button
            Button newButton = new Button(this);
            newButton.setText(inputName); // Set the text for the button

            // You can also set other properties programmatically, such as layout parameters
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

            newButton.setLayoutParams(layoutParams);

            // Adding the button to the LinearLayout
            buttonCollention.addView(newButton);

            // Adding an OnClickListener to the button
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click event here
                    Toast.makeText(MainActivity.this, inputName, Toast.LENGTH_SHORT).show();
                    openActionView(tvInputInfo);
                }
            });
        }


    }


    private String extractName(String input) {

        // Find the last index of '/'
        int lastIndex = input.lastIndexOf('/');
        if (lastIndex != -1) {
            String lastPart = input.substring(lastIndex + 1);
            return lastPart;
        } else {
            return "";
        }
    }
}