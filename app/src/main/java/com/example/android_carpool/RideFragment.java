package com.example.android_carpool;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RideFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_ride, container, false);
        initFields();
        initRouteActivity();
        return  view;
    }

    private void initFields() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Confirm");

        recyclerView = (RecyclerView) view.findViewById(R.id.ride_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = reference.limitToLast(50);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Ticket>()
                .setQuery(query, Ticket.class)
                .build();

        FirebaseRecyclerAdapter<Ticket, TicketViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Ticket, TicketViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull TicketViewHolder ticketViewHolder, int i, @NonNull Ticket ticketRide) {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ticketViewHolder.origin.setText(ticketRide.getOrigin());
                                ticketViewHolder.destination.setText(ticketRide.getDestination());
                                ticketViewHolder.cost.setText(ticketRide.getCost());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_ride, parent, false);
                        RideFragment.TicketViewHolder viewHolder = new RideFragment.TicketViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        private TextView origin, destination, cost;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            origin = itemView.findViewById(R.id.origin_cardview_ride_text);
            destination = itemView.findViewById(R.id.destination_cardview_ride_text);
            cost = itemView.findViewById(R.id.cost_text_view_cardview_ride);
        }
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
