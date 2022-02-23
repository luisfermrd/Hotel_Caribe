package com.hotel.hotelcaribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hotel.hotelcaribe.objetos_bd.servicios;

import java.util.ArrayList;
import java.util.List;

public class servicios_sucursales extends AppCompatActivity {

    private String id;
    private RecyclerView recyclerView;
    private AdapterServicios adapterServicios;

    private DatabaseReference databaseReference;
    private List<servicios> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_sucursales);
        Bundle datos = this.getIntent().getExtras();
        id = datos.getString("id");
        recyclerView = findViewById(R.id.recyclerViewServicios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    servicios servicios = postSnapshot.getValue(servicios.class);
                    mUploads.add(servicios);
                }
                adapterServicios = new AdapterServicios(getApplicationContext(),mUploads);

                recyclerView.setAdapter(adapterServicios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}