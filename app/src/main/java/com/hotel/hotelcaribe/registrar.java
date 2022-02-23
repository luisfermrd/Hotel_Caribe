package com.hotel.hotelcaribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hotel.hotelcaribe.objetos_bd.usuario;

public class registrar extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button btniniciar, btnregistrar;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private EditText nombres, apellidos, cedula, telefono, correo, password, confirmarPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("usuario");

        mAuth = FirebaseAuth.getInstance();

        nombres = findViewById(R.id.txtNombres);
        apellidos = findViewById(R.id.txtApellidos);
        cedula = findViewById(R.id.txtCedula);
        telefono = findViewById(R.id.txtTelefono);
        correo = findViewById(R.id.txtCorreo);
        password = findViewById(R.id.txtPassword);
        confirmarPassword = findViewById(R.id.txtPasswordC);
        progressBar = findViewById(R.id.progressBar2);



        btniniciar = findViewById(R.id.Iniciar_sesion);
        btnregistrar = findViewById(R.id.Registrar);

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()){
                    registrar();
                }
            }
        });
        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registrar.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validar(){
        if (nombres.getText().toString().isEmpty()){
            nombres.setError("Ingrese un nombre");
            return false;
        }else if (apellidos.getText().toString().isEmpty()){
            apellidos.setError("Ingrese un apellido");
            return false;
        }else if (cedula.getText().toString().isEmpty()){
            cedula.setError("Ingrese una cedula");
            return false;
        }else if (telefono.getText().toString().isEmpty()){
            telefono.setError("Ingrese un telefono");
            return false;
        }else if (correo.getText().toString().isEmpty()){
            correo.setError("Ingrese un correo");
            return false;
        }else if(password.getText().toString().isEmpty()){
            password.setError("Ingrese una contraseña");
            return false;
        }else if(confirmarPassword.getText().toString().isEmpty()){
            confirmarPassword.setError("Ingrese una contraseña");
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    private void registrar(){
        progressBar.setVisibility(View.VISIBLE);

        if (password.getText().toString().equals(confirmarPassword.getText().toString())){

            String nom = nombres.getText().toString();
            String ape = apellidos.getText().toString();
            int ced = Integer.parseInt(cedula.getText().toString());
            long tel = Long.parseLong(telefono.getText().toString());
            String cor = correo.getText().toString();

            mAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();

                                if(mAuth.getCurrentUser() != null){
                                    usuario Usuario = new usuario(nom, ape, ced, tel, cor);
                                    myRef.child(mAuth.getCurrentUser().getUid()).setValue(Usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Datos guardados.",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error al guardar datos."+e.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                Toast.makeText(getApplicationContext(), "Sesion iniciada.",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), inicio.class);
                                startActivity(intent);

                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error al registrar.",Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            password.setError("Las contraseñas no coinciden");
        }
    }
}