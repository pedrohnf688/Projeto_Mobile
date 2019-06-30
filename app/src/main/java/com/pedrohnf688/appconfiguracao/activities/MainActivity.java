package com.pedrohnf688.appconfiguracao.activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pedrohnf688.appconfiguracao.R;
import com.pedrohnf688.appconfiguracao.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebase;
    DatabaseReference databaseUsuario;
    private FirebaseAuth firebaseAuth;
    EditText emailUser,senhaUser;
    Button entrar,cadastrarUser,visitanteUser,recuperarSenha;
    List<Usuario> usuarioList = new ArrayList<Usuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cadastrarUser = findViewById(R.id.cadastrar);
        visitanteUser = findViewById(R.id.visitante);
        emailUser = findViewById(R.id.emailUser);
        senhaUser = findViewById(R.id.senhaUser);
        entrar = findViewById(R.id.logar);
        recuperarSenha = findViewById(R.id.resetarSenha);

        mFirebase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signOut();

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailUser.getText().toString()) && !TextUtils.isEmpty(senhaUser.getText().toString())) {
                    firebaseAuth.signInWithEmailAndPassword(emailUser.getText().toString(),
                        senhaUser.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            if(firebaseAuth.getCurrentUser().isEmailVerified()) {

                                Query query = mFirebase.getReference().child("Usuario").orderByChild("email").equalTo(emailUser.getText().toString());

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                                        //for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            //if(d.getValue(Usuario.class).getEmail().equals(emailUser.getText().toString())){

                                        if(dataSnapshot.exists()) {
                                            startActivity(new Intent(MainActivity.this, InicioActivity.class));
                                            Toast.makeText(MainActivity.this, "Bem-vindo", Toast.LENGTH_SHORT).show();
                                        }else{
                                            startActivity(new Intent(MainActivity.this, CadastroUsuarioActivity.class));
                                            Toast.makeText(MainActivity.this, "Completar Cadastro", Toast.LENGTH_SHORT).show();
                                        }

                                        emailUser.setText("");
                                        senhaUser.setText("");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }else{
                                Toast.makeText(MainActivity.this, "Por Favor Verifique seu Email", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(MainActivity.this, "Usuário e/ou senha inválidos", Toast.LENGTH_SHORT).show();
                        }

                    }});
                }else{
                    Toast.makeText(MainActivity.this, "Preencha os Campos Corretamente", Toast.LENGTH_SHORT).show();
                }


            }
        });

        visitanteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Manutenção...", Toast.LENGTH_SHORT).show();
            }
        });

        cadastrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CadastrarActivity.class));
            }
        });

        recuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ResetarSenhaActivity.class));
            }
        });

    }


}
