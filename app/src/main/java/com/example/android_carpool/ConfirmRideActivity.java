package com.example.android_carpool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConfirmRideActivity extends AppCompatActivity {

    TextView originText;
    TextView destinationText;
    TextView costText;
    EditText phoneNumberText;

    String origin;
    String destination;
    String cost;
    String phoneNumber;

    String key;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, keyReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ride);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Ticket");

        originText();
        destinationText();
        costText();

        initBack();
        confirmRide();
    }

    private void originText() {
        originText = (TextView) findViewById(R.id.origin_text_view_activity_confirm_ride);
        Bundle bundle = getIntent().getExtras();
        origin = bundle.getString("ORIGIN_LOCATION_STRING_KEY");
        originText.setText(origin);
    }

    private void destinationText() {
        destinationText = (TextView) findViewById(R.id.destination_text_view_activity_confirm_ride);
        Bundle bundle = getIntent().getExtras();
        destination = bundle.getString("DESTINATION_LOCATION_STRING_KEY");
        destinationText.setText(destination);
    }

    private void costText() {
        costText = (TextView) findViewById(R.id.cost_text_view);
        Bundle bundle = getIntent().getExtras();
        cost = bundle.getString("COST_STRING_KEY");
        costText.setText(cost);
    }

    private void saveData() {
        key = reference.push().getKey();
        HashMap<String, Object> ticketKey = new HashMap<>();
        reference.updateChildren(ticketKey);
        keyReference = reference.child(key);

        phoneNumberText = (EditText) findViewById(R.id.phone_number_activity_confirm_ride);
        phoneNumber = phoneNumberText.getText().toString();

        HashMap<String, Object> map = new HashMap<>();
        map.put("Origin", origin);
        map.put("Destination", destination);
        map.put("Cost", cost);
        map.put("PhoneNumber", phoneNumber);

        keyReference.updateChildren(map);
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
        RelativeLayout confirm = (RelativeLayout) findViewById(R.id.confirm_click);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();

                Intent intent = new Intent(ConfirmRideActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
