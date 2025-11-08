package com.exam.pm2examengrupo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText etNombre, etTelefono, etLatitud, etLongitud;
    private ImageView imgFoto;
    private Button btnGuardar, btnContactos, btnTomarFoto;
    private ArrayList<Contacto> listaContactos = new ArrayList<>();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap fotoTomada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Referencias
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        imgFoto = findViewById(R.id.imgFoto);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnContactos = findViewById(R.id.btnContactos);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Guardar contacto y mostrar en mapa
        btnGuardar.setOnClickListener(v -> guardarContacto());

        // Mostrar lista de contactos
        btnContactos.setOnClickListener(v -> {
            if (listaContactos.isEmpty()) {
                Toast.makeText(this, "No hay contactos guardados", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder sb = new StringBuilder();
                for (Contacto c : listaContactos) {
                    sb.append(c.nombre).append(" - ").append(c.telefono)
                            .append(" (").append(c.lat).append(", ").append(c.lng).append(")\n");
                }
                Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // Tomar foto
        btnTomarFoto.setOnClickListener(v -> tomarFoto());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ubicacionInicial = new LatLng(15.50, -88.00);
        agregarMarcador(ubicacionInicial, "José Escalante");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionInicial, 16));

        etNombre.setText("José Escalante");
        etTelefono.setText("9876-5432");
        etLatitud.setText(String.valueOf(ubicacionInicial.latitude));
        etLongitud.setText(String.valueOf(ubicacionInicial.longitude));
    }

    private void agregarMarcador(LatLng ubicacion, String nombre) {
        mMap.clear();
        BitmapDescriptor iconoPersona = BitmapDescriptorFactory.fromBitmap(
                redimensionarIcono(R.drawable.persona, 100, 100)
        );
        mMap.addMarker(new MarkerOptions().position(ubicacion).title(nombre).icon(iconoPersona));
    }

    private void guardarContacto() {
        try {
            String nombre = etNombre.getText().toString();
            String telefono = etTelefono.getText().toString();
            double lat = Double.parseDouble(etLatitud.getText().toString());
            double lng = Double.parseDouble(etLongitud.getText().toString());

            Contacto c = new Contacto(nombre, telefono, lat, lng, fotoTomada);
            listaContactos.add(c);

            agregarMarcador(new LatLng(lat, lng), nombre);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));

            Toast.makeText(this, "Contacto guardado y ubicación mostrada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Verifica que los campos y coordenadas sean válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            fotoTomada = (Bitmap) extras.get("data");
            imgFoto.setImageBitmap(fotoTomada);
        }
    }

    private Bitmap redimensionarIcono(int resId, int ancho, int alto) {
        Bitmap original = BitmapFactory.decodeResource(getResources(), resId);
        return Bitmap.createScaledBitmap(original, ancho, alto, false);
    }

    // Clase interna para guardar contactos
    private static class Contacto {
        String nombre, telefono;
        double lat, lng;
        Bitmap foto;

        Contacto(String nombre, String telefono, double lat, double lng, Bitmap foto) {
            this.nombre = nombre;
            this.telefono = telefono;
            this.lat = lat;
            this.lng = lng;
            this.foto = foto;
        }
    }

}

