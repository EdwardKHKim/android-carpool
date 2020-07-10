package com.example.android_carpool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import org.w3c.dom.Text;


public class RouteActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_AUTOCOMPLETE_ORIGIN = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_DESTINATION = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        initOriginSearch();
        initDestinationSearch();
        initBack();
        initSelectRide();
    }

    private void initOriginSearch() {
        RelativeLayout originSearch = (RelativeLayout) findViewById(R.id.origin_relative_layout_activity_route);
        originSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .build(PlaceOptions.MODE_CARDS))
                        .build(RouteActivity.this);

                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_ORIGIN);
            }
        });
    }

    private void initDestinationSearch() {
        RelativeLayout destinationSearch = (RelativeLayout) findViewById(R.id.destination_relative_layout_activity_route);
        destinationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .build(PlaceOptions.MODE_CARDS))
                        .build(RouteActivity.this);

                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_DESTINATION);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            CarmenFeature carmenFeatureOrigin = PlaceAutocomplete.getPlace(data);
            CarmenFeature carmenFeatureDestination = PlaceAutocomplete.getPlace(data);

            if (requestCode == REQUEST_CODE_AUTOCOMPLETE_ORIGIN) {
                TextView originTextView = (TextView) findViewById(R.id.origin_text_view);
                originTextView.setText(carmenFeatureOrigin.text());
            }

            if (requestCode == REQUEST_CODE_AUTOCOMPLETE_DESTINATION) {
                TextView destinationTextView = (TextView) findViewById(R.id.destination_text_view);
                destinationTextView.setText(carmenFeatureDestination.text());
            }
        }
    }

    private void initBack() {
        ImageView back = (ImageView) findViewById(R.id.back_activity_route);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initSelectRide() {
        RelativeLayout continueActivity = (RelativeLayout) findViewById(R.id.continue_activity_route);
        continueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteActivity.this, SelectRideActivity.class);
                startActivity(intent);
            }
        });
    }
}
