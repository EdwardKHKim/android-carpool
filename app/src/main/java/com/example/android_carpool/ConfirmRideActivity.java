package com.example.android_carpool;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ConfirmRideActivity extends AppCompatActivity {

    TextView originText;
    TextView destinationText;
    TextView costText;

    String originStr;
    String destinationStr;
    String costStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ride);

        originText();
        destinationText();
        costText();

        initBack();
        confirmRide();
    }

    private void originText() {
        originText = (TextView) findViewById(R.id.origin_text_view_activity_confirm_ride);
        Bundle bundle = getIntent().getExtras();
        originStr = bundle.getString("ORIGIN_LOCATION_STRING_KEY");
        originText.setText(originStr);
    }

    private void destinationText() {
        destinationText = (TextView) findViewById(R.id.destination_text_view_activity_confirm_ride);
        Bundle bundle = getIntent().getExtras();
        destinationStr = bundle.getString("DESTINATION_LOCATION_STRING_KEY");
        destinationText.setText(destinationStr);
    }

    private void costText() {
        costText = (TextView) findViewById(R.id.cost_text_view);
        Bundle bundle = getIntent().getExtras();
        costStr = bundle.getString("COST_STRING_KEY");
        costText.setText(costStr);
    }

    private void initBack() {
        ImageView back = (ImageView) findViewById(R.id.back_activity_confirm_ride);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void confirmRide() {

    }
}
