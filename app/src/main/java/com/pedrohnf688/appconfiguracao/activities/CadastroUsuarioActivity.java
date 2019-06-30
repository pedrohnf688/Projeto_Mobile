package com.pedrohnf688.appconfiguracao.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pedrohnf688.appconfiguracao.R;
import com.pedrohnf688.appconfiguracao.modelo.Usuario;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroUsuarioActivity extends AppCompatActivity {

    Button cancelar, cadastrarUser;
    EditText nome,data;
    CircleImageView circleImageView;
    ImageButton imageButton;
    DatePickerDialog datePickerDialog;
    RadioGroup radioGroup;
    RadioButton masculino, feminino;
    DatabaseReference databaseUsuario;
    private FirebaseDatabase mFirebase;
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private static final int CODIGO_FOTO = 56;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.nomeUsuario);
        data = findViewById(R.id.dataNascimento);
        radioGroup = findViewById(R.id.radioGroupSexo);
        masculino = findViewById(R.id.radioButtonMasculino);
        feminino = findViewById(R.id.radioButtonFeminino);
        cancelar = findViewById(R.id.buttonCancelar);
        cadastrarUser = findViewById(R.id.buttonCadastrar);
        imageButton = findViewById(R.id.imageButton);
        circleImageView = findViewById(R.id.fotoUser);



        mFirebase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference().child("perfil_photos");

        if(mFirebase == null) {

            mFirebase.setPersistenceEnabled(true);
            databaseUsuario = mFirebase.getReference("Usuario");

        }else{

            databaseUsuario = mFirebase.getReference("Usuario");
            databaseUsuario.keepSynced(true);
        }


        Calendar calendar = Calendar.getInstance();

        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        final int mes = calendar.get(Calendar.MONTH);
        final int ano = calendar.get(Calendar.YEAR);


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cadastrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filePath != null){
                    StorageReference reference = storageReference.child("images"+UUID.randomUUID().toString());
                    reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String sexo = "";

                                    if (masculino.isChecked()) {
                                        sexo = "Masculino";
                                    }
                                    if (feminino.isChecked()) {
                                        sexo = "Feminino";
                                    }

                                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                    if (!TextUtils.isEmpty(nome.getText().toString()) && !TextUtils.isEmpty(data.getText().toString()) &&
                                            radioGroup.getCheckedRadioButtonId() != -1 && !TextUtils.isEmpty(sexo) && !TextUtils.isEmpty(email)) {


                                        String id = databaseUsuario.push().getKey();

                                        Usuario p = new Usuario(id, nome.getText().toString(), email, data.getText().toString(), uri.toString(), sexo);

                                        databaseUsuario.child(id).setValue(p);

                                        Toast.makeText(CadastroUsuarioActivity.this, p.toString(), Toast.LENGTH_SHORT).show();


                                        nome.setText("");
                                        data.setText("");
                                        masculino.setChecked(false);
                                        feminino.setChecked(false);
                                        sexo = "";
                                        email = "";
                                        circleImageView.setImageResource(R.drawable.user);

                                        startActivity(new Intent(CadastroUsuarioActivity.this, InicioActivity.class));
                                    } else {
                                        Toast.makeText(CadastroUsuarioActivity.this, "Preencha os Campos Corretamente", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CadastroUsuarioActivity.this, "Erro 1", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog  = new DatePickerDialog(CadastroUsuarioActivity.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String d = dayOfMonth + "/" + (monthOfYear+1) +"/" + year;
                        data.setText(d);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),CODIGO_FOTO);
            }
        });


    }


    private void uploadImagem(){

        if(filePath != null){

            StorageReference reference = storageReference.child("images"+UUID.randomUUID().toString());
            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CODIGO_FOTO && resultCode == RESULT_OK && data != null && data.getData() != null){

            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                circleImageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
