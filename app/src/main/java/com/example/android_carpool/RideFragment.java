package com.example.android_carpool;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RideFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_ride, container, false);
        initRouteActivity();
        return  view;
    }

    private void initRouteActivity() {
        RelativeLayout routeRelativeLayout = (RelativeLayout) view.findViewById(R.id.route_relative_layout_fragment_ride);
        routeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RouteActivity.class);
                startActivity(intent);
            }
        });
    }
}
