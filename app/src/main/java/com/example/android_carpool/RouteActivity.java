package com.example.android_carpool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapboxDirections client;
    private DirectionsRoute currentRoute;

    Point originPoint;
    Double originLatitude;
    Double originLongitude;
    TextView originTextView;

    Point destinationPoint;
    Double destinationLatitude;
    Double destinationLongitude;
    TextView destinationTextView;

    private static final int REQUEST_CODE_AUTOCOMPLETE_ORIGIN = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_DESTINATION = 2;
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID_ORIGIN = "route-source-id-origin";
    private static final String ROUTE_SOURCE_ID_DESTINATION = "route-source-id-destination";
    private static final String ICON_LAYER_ID_ORIGIN = "icon-layer-id-origin";
    private static final String ICON_LAYER_ID_DESTINATION = "icon-layer-id-destination";
    private static final String ICON_ID_ORIGIN = "icon-id-origin";
    private static final String ICON_ID_DESTINATION = "icon-id-destination";
    private static final String ORIGIN_LATITUDE = "origin-latitude";
    private static final String ORIGIN_LONGITUDE = "origin-longitude";
    private static final String DESTINATION_LATITUDE = "destination-latitude";
    private static final String DESTINATION_LONGITUDE = "destination-longitude";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_route);
        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        initBack();
        initOriginSearch();
        initDestinationSearch();
        initBack();
        initNextActivity();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri(getString(R.string.mapbox_style)),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        initOriginSearch();
                        initDestinationSearch();
                        initSourceOrigin(style);
                        initLayerOrigin(style);
                        initSourceDestination(style);
                        initLayerDestination(style);
                    }
                });
    }

    private void sharedPreferences() {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        originLatitude = Double.longBitsToDouble(sharedPreferences.getLong(ORIGIN_LATITUDE, 0));
        originLongitude = Double.longBitsToDouble(sharedPreferences.getLong(ORIGIN_LONGITUDE, 0));
        destinationLatitude = Double.longBitsToDouble(sharedPreferences.getLong(DESTINATION_LATITUDE, 0));
        destinationLongitude = Double.longBitsToDouble(sharedPreferences.getLong(DESTINATION_LONGITUDE, 0));
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

    private void initSourceRoute(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{})));
    }

    private void initLayerRoute(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#000000"))
        );
        loadedMapStyle.addLayer(routeLayer);
    }

    private void initSourceOrigin(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID_ORIGIN));
    }

    private void initSourceDestination(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID_DESTINATION));
    }

    private void initLayerOrigin(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage(ICON_ID_ORIGIN, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_square))));

        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID_ORIGIN, ROUTE_SOURCE_ID_ORIGIN).withProperties(
                iconImage(ICON_ID_ORIGIN), iconOffset(new Float[]{0f, -8f})
        ));
    }

    private void initLayerDestination(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage(ICON_ID_DESTINATION, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_oval))));

        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID_DESTINATION, ROUTE_SOURCE_ID_DESTINATION).withProperties(
                iconImage(ICON_ID_DESTINATION), iconOffset(new Float[]{0f, -8f})
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            CarmenFeature carmenFeatureOrigin = PlaceAutocomplete.getPlace(data);
            CarmenFeature carmenFeatureDestination = PlaceAutocomplete.getPlace(data);

            if (requestCode == REQUEST_CODE_AUTOCOMPLETE_ORIGIN) {

                if (mapboxMap != null) {
                    Style style = mapboxMap.getStyle();

                    if (style != null) {
                        GeoJsonSource geoJsonSource = style.getSourceAs(ROUTE_SOURCE_ID_ORIGIN);

                        if (geoJsonSource != null) {
                            geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(
                                    new Feature[]{Feature.fromJson(carmenFeatureOrigin.toJson())}
                            ));

                            originPoint = com.mapbox.geojson.Point.fromLngLat(((Point) carmenFeatureOrigin.geometry())
                                    .latitude(), ((Point) carmenFeatureOrigin.geometry()).longitude());

                            SharedPreferences sharedPreferencesOrigin = this.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorOrigin = sharedPreferencesOrigin.edit();
                            editorOrigin.putLong(ORIGIN_LATITUDE, Double.doubleToLongBits(originPoint.latitude()));
                            editorOrigin.putLong(ORIGIN_LONGITUDE, Double.doubleToLongBits(originPoint.longitude()));
                            editorOrigin.apply();
                        }

                        mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder().target(new LatLng(((Point) carmenFeatureOrigin.geometry())
                                .latitude(), ((Point) carmenFeatureOrigin.geometry()).longitude()))
                                .zoom(14).build()), 4000);
                    }

                    originTextView = (TextView) findViewById(R.id.origin_text_view);
                    originTextView.setText(carmenFeatureOrigin.text());
                    destinationTextView = (TextView) findViewById(R.id.destination_text_view);

                    style.removeLayer(ROUTE_LAYER_ID);
                    style.removeSource(ROUTE_SOURCE_ID);

                    sharedPreferences();

                    initSourceRoute(style);
                    initLayerRoute(style);

                    if (destinationTextView.getText().length() > 0) {
                        Point origin = Point.fromLngLat(originLatitude, originLongitude);
                        Point destination = Point.fromLngLat(destinationLatitude, destinationLongitude);

                        getRoute(style, origin, destination);
                    }
                }
            }

            if (requestCode == REQUEST_CODE_AUTOCOMPLETE_DESTINATION) {

                if (mapboxMap != null) {
                    Style style = mapboxMap.getStyle();

                    if (style != null) {
                        GeoJsonSource geoJsonSource = style.getSourceAs(ROUTE_SOURCE_ID_DESTINATION);

                        if (geoJsonSource != null) {
                            geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(
                                    new Feature[]{Feature.fromJson(carmenFeatureDestination.toJson())}
                            ));

                            destinationPoint = Point.fromLngLat(((Point) carmenFeatureDestination
                            .geometry()).latitude(), ((Point) carmenFeatureDestination.geometry()).longitude());

                            SharedPreferences sharedPreferencesDestination = this.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorDestination = sharedPreferencesDestination.edit();
                            editorDestination.putLong(DESTINATION_LATITUDE, Double.doubleToLongBits(destinationPoint.latitude()));
                            editorDestination.putLong(DESTINATION_LONGITUDE, Double.doubleToLongBits(destinationPoint.longitude()));
                            editorDestination.apply();
                        }

                        mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder().target(new LatLng(((Point) carmenFeatureDestination.geometry()).latitude(),
                                        ((Point) carmenFeatureDestination.geometry()).longitude()))
                                .zoom(14).build()), 4000);
                    }

                    destinationTextView = (TextView) findViewById(R.id.destination_text_view);
                    destinationTextView.setText(carmenFeatureDestination.text());

                    originTextView = (TextView) findViewById(R.id.origin_text_view);

                    style.removeLayer(ROUTE_LAYER_ID);
                    style.removeSource(ROUTE_SOURCE_ID);

                    sharedPreferences();

                    initSourceRoute(style);
                    initLayerRoute(style);

                    if (originTextView.getText().length() > 0) {

                        Point origin = Point.fromLngLat(originLatitude, originLongitude);
                        Point destination = Point.fromLngLat(destinationLatitude, destinationLongitude);

                        LatLng originLocation = new LatLng(49.2838, -122.7932);
                        LatLng destinationLocation = new LatLng(49.2765, -123.2177);

                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .include(originLocation)
                                .include(destinationLocation)
                                .build();

                        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150), 5000);

                        getRoute(style, origin, destination);
                    }
                }
            }
        }
    }

    private void getRoute(@NonNull final  Style style, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(call.request().url().toString());
                Timber.d("Response code:" + response.code());
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.");

                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.e("No routes found");

                    return;
                }

                currentRoute = response.body().routes().get(0);

                if (style.isFullyLoaded()) {
                    GeoJsonSource geoJsonSource = style.getSourceAs(ROUTE_SOURCE_ID);

                    if (geoJsonSource != null) {
                        Timber.d("onResponse: source != null");

                        geoJsonSource.setGeoJson(FeatureCollection.fromFeature(
                                Feature.fromGeometry(LineString.fromPolyline(
                                        currentRoute.geometry(), PRECISION_6))));
                    }
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
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

    private void initNextActivity() {
        RelativeLayout nextActivity = (RelativeLayout) findViewById(R.id.continue_activity_route);
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteActivity.this, ConfirmRideActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
