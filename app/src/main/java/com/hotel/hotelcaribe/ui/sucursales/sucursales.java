package com.hotel.hotelcaribe.ui.sucursales;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hotel.hotelcaribe.R;
import com.hotel.hotelcaribe.objetos_bd.sucursal;
import com.hotel.hotelcaribe.servicios_sucursales;

import java.util.ArrayList;
import java.util.List;

public class sucursales extends Fragment{

    private SucursalesViewModel mViewModel;
    private RecyclerView recyclerView;
    private AdapterSucursal adapterSucursal;

    private  DatabaseReference databaseReference;
    private List<sucursal> mUploads;

    public static sucursales newInstance() {
        return new sucursales();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sucursales_fragment, container, false);
        recyclerView = root.findViewById(R.id.RecyclerViewSucursal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        mUploads = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("sucursales");
        
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    sucursal sucursal = postSnapshot.getValue(sucursal.class);
                    mUploads.add(sucursal);
                }
                adapterSucursal = new AdapterSucursal(root.getContext(),mUploads);
                adapterSucursal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = String.valueOf(mUploads.get(recyclerView.getChildAdapterPosition(view)).getId());
                        Toast.makeText(root.getContext(), mUploads.get(recyclerView.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(view.getContext(), servicios_sucursales.class);
                        intent.putExtra("id", id.replace(" ",""));
                        startActivity(intent);

                    }
                });
                recyclerView.setAdapter(adapterSucursal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(root.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SucursalesViewModel.class);
        // TODO: Use the ViewModel
    }

}