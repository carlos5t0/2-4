package com.example.ejercicio2_4;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ejercicio2_4.Models.FirmaImag;
import com.example.ejercicio2_4.Models.SQLiteConexion;
import com.example.ejercicio2_4.Models.Transacciones;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityFirma extends AppCompatActivity {

    private FirmaImag firmaImag;
    EditText Descripcion;
    Button btnGuardar,btnListarFirmas;
    LinearLayout fContent;
    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);

        Descripcion = ( EditText) findViewById(R.id.txtDescripcion);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnListarFirmas = (Button) findViewById(R.id.btnMostrarFirmas);
        fContent = (LinearLayout) findViewById(R.id.firmaLayout);
        firmaImag = new FirmaImag(this, null);
        fContent.addView(firmaImag, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { guardarDatos();}
        });


        btnListarFirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ActivityListarFirma.class);
                startActivity(intent);
            }
        });
    }

    private void guardarDatos() {
        try {
            firmas(firmaImag.getBitmap());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            Intent intent = new Intent(this, ActivityFirma.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            LimpiarPantalla();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error a guardar Datos ",Toast.LENGTH_LONG).show();
        }


    }

    private void firmas( Bitmap bitmap) {

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.descripcion,Descripcion.getText().toString());
        valores.put(String.valueOf(Transacciones.firma),ArrayFoto);

        Long resultado = db.insert(Transacciones.TablaSignaturess, null, valores);

        Toast.makeText(getApplicationContext(), "Registro ingreso con exito: " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    }



    private void LimpiarPantalla()
    {
        Descripcion.setText("");
        firmaImag.ClearCanvas();

    }

}