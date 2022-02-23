package com.hotel.hotelcaribe.ui.metodo_pago;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hotel.hotelcaribe.R;
import com.hotel.hotelcaribe.objetos_bd.pago;
import com.hotel.hotelcaribe.objetos_bd.servicios;
import com.hotel.hotelcaribe.objetos_bd.bd_local.metodos_bd;

import java.util.ArrayList;
import java.util.List;

public class metodo_pago extends Fragment {

    private MetodoPagoViewModel mViewModel;
    private List<servicios> Elementos;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef, myRef2;
    private LinearLayout pagos;
    private Button btnPagar, btnRealizarPago;
    private TextView totalAPagar;
    private EditText txtTarjeta, txtNombre, txtFecha, txtCodigo, txtDireccion, txtNumIdentificacion;
    private Spinner tipoID;
    private int total = 0;
    private ProgressBar progressBar;

    public static metodo_pago newInstance() {
        return new metodo_pago();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.metodo_pago_fragment, container, false);
        Elementos = new ArrayList<>();
        pagos = root.findViewById(R.id.linearLayoutPagos);
        btnPagar = root.findViewById(R.id.btnPagar);
        totalAPagar = root.findViewById(R.id.txtTotalAPagar);

        txtTarjeta = root.findViewById(R.id.txtTarjeta);
        txtNombre = root.findViewById(R.id.txtNombreTarjeta);
        txtFecha = root.findViewById(R.id.txtFechaExp);
        txtCodigo = root.findViewById(R.id.txtCodigoCVV);
        txtDireccion = root.findViewById(R.id.txtDireccion);
        txtNumIdentificacion = root.findViewById(R.id.txtDocumento);
        tipoID = root.findViewById(R.id.spinnerTipoDocumento);
        btnRealizarPago = root.findViewById(R.id.btnRealizarPago);
        progressBar = root.findViewById(R.id.progressBar3);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("pagos");
        myRef2 = database.getReference("reservasPagadas");

        buscarServicios();

        totalAPagar.setText(String.valueOf(total));

        AdapterCarrito listAdapter = new AdapterCarrito(root.getContext(), Elementos, true);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCarrito);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(listAdapter);


        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Elementos.size() >0){
                    cargarDatosDePago();
                    pagos.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getContext(), "No hay servicios para pagar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRealizarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()){
                    pagar();
                    pagos.setVisibility(View.GONE);
                }
            }
        });


        return root;
    }

    private void pagar(){
        progressBar.setVisibility(View.VISIBLE);

        String numero = txtTarjeta.getText().toString();
        String nombre = txtNombre.getText().toString();
        String fecha = txtFecha.getText().toString();
        int codigo = Integer.parseInt(txtCodigo.getText().toString());
        String direccion = txtDireccion.getText().toString();
        long cedula = Long.parseLong(txtNumIdentificacion.getText().toString());
        String ID = (String) tipoID.getSelectedItem();

        if(mAuth.getCurrentUser() != null){
            pago Pago = new pago(numero, nombre, fecha, codigo, direccion, cedula, ID);
            myRef.child(mAuth.getCurrentUser().getUid()).setValue(Pago).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    if(subirServiciosPagados()){
                        Toast.makeText(getContext(), "Pago exitoso.",Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                    pagos.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al pagar."+e.toString(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private boolean subirServiciosPagados(){

        if(mAuth.getCurrentUser() != null){
            for (servicios ser: Elementos) {
                myRef2.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(ser.getId())).setValue(ser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al cargar Servicio."+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            metodos_bd admin = new metodos_bd (getContext(),"carrito_servicio", null, 1);

            SQLiteDatabase bd = admin.getWritableDatabase();

            int cant = bd.delete("servicios", "uid = '" + mAuth.getCurrentUser().getUid() +"'", null);

            return true;

        }
        return false;
    }

    private void buscarServicios() {
        metodos_bd admin = new metodos_bd(getContext(), "carrito_servicio", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery("SELECT * FROM servicios WHERE uid = '"+mAuth.getCurrentUser().getUid()+"'", null);
        while (fila.moveToNext()){
            servicios elementos = new servicios();
            elementos.setId(Integer.parseInt(fila.getString(0)));
            elementos.setInfo(fila.getString(1));
            elementos.setN_personas(Integer.parseInt(fila.getString(2)));
            elementos.setNombre(fila.getString(3));
            elementos.setPrecio((int) Double.parseDouble(fila.getString(4)));
            elementos.setUrl(fila.getString(5));

            total += (int) Double.parseDouble(fila.getString(4));

            Elementos.add(elementos);
        }

        bd.close();
    }

    private void cargarDatosDePago(){
        if(mAuth.getCurrentUser() != null) {
            myRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pago datosUsuario = snapshot.getValue(pago.class);
                    if (datosUsuario != null){
                        txtTarjeta.setText(datosUsuario.getNumero());
                        txtNombre.setText(datosUsuario.getNombre());
                        txtFecha.setText(datosUsuario.getFecha());
                        txtCodigo.setText(String.valueOf(datosUsuario.getCodigo()));
                        txtDireccion.setText(datosUsuario.getDireccion());
                        txtNumIdentificacion.setText(String.valueOf(datosUsuario.getCedula()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private boolean validar(){
        if (txtTarjeta.getText().toString().isEmpty()){
            txtTarjeta.setError("Ingrese el numero de tarjeta");
            return false;
        }else if (txtNombre.getText().toString().isEmpty()){
            txtNombre.setError("Ingrese su nombre");
            return false;
        }else if (txtFecha.getText().toString().isEmpty()){
            txtFecha.setError("Ingrese la fecha de expiracion");
            return false;
        }else if (txtCodigo.getText().toString().isEmpty()){
            txtCodigo.setError("Ingrese el codigo");
            return false;
        }else if (txtDireccion.getText().toString().isEmpty()){
            txtDireccion.setError("Ingrese la direccion");
            return false;
        }else if(txtNumIdentificacion.getText().toString().isEmpty()){
            txtNumIdentificacion.setError("Ingrese su numero de documento");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MetodoPagoViewModel.class);
        // TODO: Use the ViewModel
    }

}