package com.pedrohnf688.appconfiguracao.activities;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedrohnf688.appconfiguracao.R;

public class ResetarSenhaActivity extends AppCompatActivity {

    EditText email;
    Button enviar;
    private FirebaseDatabase mFirebase;
    DatabaseReference databaseUsuario;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha);

        email = findViewById(R.id.emailRecuperar);
        enviar = findViewById(R.id.recuperarSenha);

        firebaseAuth = FirebaseAuth.getInstance();


        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString())) {
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetarSenhaActivity.this, "Senha Enviada para seu Email", Toast.LENGTH_SHORT).show();
                                        email.setText("");
                                    } else {
                                        Toast.makeText(ResetarSenhaActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(ResetarSenhaActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
