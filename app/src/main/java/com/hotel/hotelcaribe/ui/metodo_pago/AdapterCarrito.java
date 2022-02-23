package com.hotel.hotelcaribe.ui.metodo_pago;

import android.content.Context;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hotel.hotelcaribe.R;
import com.hotel.hotelcaribe.objetos_bd.bd_local.metodos_bd;
import com.hotel.hotelcaribe.objetos_bd.servicios;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCarrito extends RecyclerView.Adapter<AdapterCarrito.ViewHolder> {

    Context context;
    List<servicios> datos;
    private FirebaseAuth mAuth;
    private boolean esCarrito;
    private FirebaseDatabase database;
    private DatabaseReference myRef2;

    public  AdapterCarrito(Context context, List<servicios> datos, boolean esCarrito){
        this.context = context;
        this.datos = datos;
        mAuth = FirebaseAuth.getInstance();
        this.esCarrito = esCarrito;
        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference("reservasPagadas");
    }

    @NonNull
    @Override
    public AdapterCarrito.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_carrito, parent, false);
        return new AdapterCarrito.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCarrito.ViewHolder holder, int position) {
        int id = datos.get(position).getId();
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

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (esCarrito){
                    eliminar(id, context);
                }else{
                    eliminarReserva(id, context);
                }
            }
        });
    }

    private void eliminarReserva(int id, Context context) {
        if(mAuth.getCurrentUser() != null) {
            myRef2.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(id)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Reserva eliminada.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Error al eliminar reserva." + e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void eliminar(int id, Context context) {
        metodos_bd admin = new metodos_bd(context,"carrito_servicio", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        bd.delete("servicios", "id = '" + id +"' and uid = '"+mAuth.getCurrentUser().getUid()+"'", null);
        Toast.makeText(context, "Elemento eliminado, si el cambio no se ve reflejado porfavor actualice la ventana", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre, info, precio, n_personas;
        private ImageView imagen;
        private Button btnEliminar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtservicioCarrito);
            info = itemView.findViewById(R.id.txtdescripcionCarrito);
            precio = itemView.findViewById(R.id.txtprecioCarrito);
            n_personas = itemView.findViewById(R.id.txtnPersonasCarrito);
            imagen = itemView.findViewById(R.id.imageViewServicioCarrito);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCarrito);
        }
    }
}
