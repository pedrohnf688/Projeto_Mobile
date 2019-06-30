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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedrohnf688.appconfiguracao.R;

public class CadastrarActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebase;
    DatabaseReference databaseUsuario;
    private FirebaseAuth firebaseAuth;
    EditText emailCad,senhaCad;
    Button cadastrarUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);


        cadastrarUser = findViewById(R.id.cadastrarUser);
        emailCad = findViewById(R.id.emailCad);
        senhaCad = findViewById(R.id.senhaCad);

        mFirebase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        cadastrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailCad.getText().toString()) && !TextUtils.isEmpty(senhaCad.getText().toString())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailCad.getText().toString(),
                        senhaCad.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                              if(task.isSuccessful()){
                                  firebaseAuth.getCurrentUser().sendEmailVerification()
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if(task.isSuccessful()){
                                              startActivity(new Intent(CadastrarActivity.this, MainActivity.class));
                                              Toast.makeText(CadastrarActivity.this, "Cadastrado Com Sucesso", Toast.LENGTH_SHORT).show();
                                              emailCad.setText("");
                                              senhaCad.setText("");
                                          }else{
                                              Toast.makeText(CadastrarActivity.this, "Erro ao Enviar Email de Verificação", Toast.LENGTH_SHORT).show();
                                          }

                                      }
                                  });

                              }else{
                                  Toast.makeText(CadastrarActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              }


                            }
                        });

                }else{
                    Toast.makeText(CadastrarActivity.this, "Preencha os Campos Corretamente", Toast.LENGTH_SHORT).show();
                }

            }
        });





    }
}
