package com.hotel.hotelcaribe.ui.sucursales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotel.hotelcaribe.R;
import com.hotel.hotelcaribe.objetos_bd.sucursal;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterSucursal extends RecyclerView.Adapter<AdapterSucursal.ViewHolder> implements View.OnClickListener {

    Context context;
    List<sucursal> datos;
    private  View.OnClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(sucursal item);
    }

    public  AdapterSucursal(Context context, List<sucursal> datos){
        this.context = context;
        this.datos = datos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sucursales, parent, false);

        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bing the data
        holder.nombre.setText(datos.get(position).getNombre());
        Picasso.get()
                .load(datos.get(position).getUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_cloud_sync_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(holder.imagen);

    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }
    }

    public void setOnClickListener (View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre;
        private ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreSucursal);
            imagen = itemView.findViewById(R.id.imageViewSucursal);


        }
    }
}
