package com.hotel.hotelcaribe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hotel.hotelcaribe.objetos_bd.servicios;
import com.hotel.hotelcaribe.objetos_bd.bd_local.metodos_bd;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterServicios extends RecyclerView.Adapter<AdapterServicios.ViewHolder> {
    private FirebaseAuth mAuth;
    Context context;
    List<servicios> datos;

    public  AdapterServicios(Context context, List<servicios> datos){
        this.context = context;
        this.datos = datos;
        mAuth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public AdapterServicios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_servicio, parent, false);


        return new AdapterServicios.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterServicios.ViewHolder holder, int position) {
        //bing the data
        servicios servicios = datos.get(position);
        holder.nombre.setText(datos.get(position).getNombre());
        holder.info.setText(datos.get(position).getInfo());
        holder.precio.setText(String.valueOf(datos.get(position).getPrecio()));
        holder.n_personas.setText(String.valueOf(datos.get(position).getN_personas()));
        Picasso.get()
                .load(datos.get(position).getUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_cloud_sync_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.imagen);
        holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarServicio(servicios, holder.itemView.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre, info, precio, n_personas;
        private ImageView imagen;
        private Button btnAgregar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtservicio);
            info = itemView.findViewById(R.id.txtdescripcion);
            precio = itemView.findViewById(R.id.txtprecio);
            n_personas = itemView.findViewById(R.id.txtnPersonas);
            imagen = itemView.findViewById(R.id.imageViewServicio);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);

        }
    }

    private void guardarServicio(servicios servicios, Context context) {
        metodos_bd admin = new metodos_bd(context,"carrito_servicio", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery("SELECT * FROM servicios WHERE id = '"+servicios.getId()+"' AND uid = '"+mAuth.getCurrentUser().getUid()+"'", null);

        if (fila.moveToFirst()){
            Toast.makeText(context, "Este servicio ya esta agregado al carrito", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("id",servicios.getId());
            contentValues.put("info",servicios.getInfo());
            contentValues.put("n_personas",servicios.getN_personas());
            contentValues.put("nombre",servicios.getNombre());
            contentValues.put("precio",servicios.getPrecio());
            contentValues.put("url",servicios.getUrl());
            contentValues.put("uid",mAuth.getCurrentUser().getUid());

            bd.insert("servicios", null, contentValues);

            Toast.makeText(context, "Servicio agregado al carrito", Toast.LENGTH_SHORT).show();
        }
    }
}
