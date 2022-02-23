package com.hotel.hotelcaribe.ui.reservas;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hotel.hotelcaribe.R;
import com.hotel.hotelcaribe.objetos_bd.servicios;
import com.hotel.hotelcaribe.ui.metodo_pago.AdapterCarrito;

import java.util.ArrayList;
import java.util.List;

public class reservas extends Fragment {

    private ReservasViewModel mViewModel;
    private List<servicios> Elementos;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private AdapterCarrito listAdapter;
    private RecyclerView recyclerView;

    public static reservas newInstance() {
        return new reservas();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.reservas_fragment, container, false);
        recyclerView = root.findViewById(R.id.recyclerViewReservas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        Elementos = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        cargarReservas();


        return root;
    }

    private void cargarReservas(){
        if(mAuth.getCurrentUser() != null) {
            myRef.child("reservasPagadas").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        servicios ser = postSnapshot.getValue(servicios.class);
                        Elementos.add(ser);
                    }
                    listAdapter = new AdapterCarrito(getContext(), Elementos, false);

                    recyclerView.setAdapter(listAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReservasViewModel.class);
        // TODO: Use the ViewModel
    }

}