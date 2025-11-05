package com.exam.pm2examengrupo1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private ImageView imgFoto;
    private EditText etNombre, etTelefono, etLatitud, etLongitud;
    private Button btnTomarFoto, btnGuardar, btnContactos;
    private LocationManager locationManager;
    private boolean fotoTomada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlazar elementos de la interfaz (XML)
        imgFoto = findViewById(R.id.imgFoto);
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnContactos = findViewById(R.id.btnContactos);

        // Configurar GPS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        solicitarPermisosUbicacion();

        // Botón: tomar fotografía
        btnTomarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });

        // Botón: guardar contacto
        btnGuardar.setOnClickListener(v -> {
            if (!fotoTomada) {
                mostrarAlerta("No se ha tomado fotografía");
                return;
            }

            if (etLatitud.getText().toString().isEmpty() || etLongitud.getText().toString().isEmpty()) {
                mostrarAlerta("Debe describir la ubicación");
                return;
            }

            Toast.makeText(this, "Contacto guardado correctamente", Toast.LENGTH_SHORT).show();
        });

        // Botón: ver contactos guardados
        btnContactos.setOnClickListener(v ->
                Toast.makeText(this, "Mostrando contactos guardados...", Toast.LENGTH_SHORT).show()
        );
    }

    private void solicitarPermisosUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            iniciarUbicacion();
        }
    }

    private void iniciarUbicacion() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1f, this);
        } catch (SecurityException e) {
            mostrarAlerta("GPS no está activo");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        etLatitud.setText(String.valueOf(location.getLatitude()));
        etLongitud.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap foto = (Bitmap) data.getExtras().get("data");
            imgFoto.setImageBitmap(foto);
            fotoTomada = true;
        }
    }

    private void mostrarAlerta(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Alerta")
                .setMessage(mensaje)
                .setPositiveButton("Ok", null).show();

    }
}