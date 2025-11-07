package com.exam.pm2examengrupo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.exam.pm2examengrupo1.api.ContactApi;
import com.exam.pm2examengrupo1.api.RetrofitClient;
import com.exam.pm2examengrupo1.Contact; // Asegúrate que esta ruta es correcta
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactListActivity extends AppCompatActivity implements ContactAdapter.OnContactActionListener {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> contactList = new ArrayList<>();
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list); // El layout XML de tu Pantalla 2

        recyclerView = findViewById(R.id.recyclerViewContacts);
        searchEditText = findViewById(R.id.editTextSearch);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(contactList, this); // 'this' como listener
        recyclerView.setAdapter(adapter);

        // Lógica de Buscar texto
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> filterList(searchEditText.getText().toString()));

        // Obtener la lista inicial al crear la actividad
        fetchContactsList();
    }

    // --- CRUD: OBTENER LISTA (GET)  ---
    private void fetchContactsList() {
        ContactApi apiService = RetrofitClient.getApiService();
        Call<List<Contact>> call = apiService.getContacts();

        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contactList.clear();
                    contactList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ContactListActivity.this, "Error al obtener lista: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(ContactListActivity.this, "Error de red al obtener contactos.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // --- LÓGICA DE BÚSQUEDA  ---
    private void filterList(String text) {
        List<Contact> filteredList = new ArrayList<>();
        for (Contact contact : contactList) {
            if (contact.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(contact);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
        // El adaptador debe tener un método para actualizar su lista interna
        adapter.filterList(filteredList);
    }

    // --- Implementación de Acciones del Adapter (al hacer click en un contacto) ---

    // Al hacer click en un elemento de la lista
    @Override
    public void onContactClick(Contact contact) {
        showActionDialog(contact); // Mostrar el diálogo "Acción: Desea ir a la ubicación..."
    }

    // Al hacer click en "Eliminar Contacto"
    @Override
    public void onDeleteClick(Contact contact) {
        // Asumiendo que el modelo Contact tiene un método getId()
        deleteContactFromApi(contact.getId());
    }

    // Al hacer click en "Actualizar Contacto"
    @Override
    public void onUpdateClick(Contact contact) {
        // Iniciar la Activity principal (Pantalla 1) y pasar el objeto Contacto para editar
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CONTACT_TO_EDIT", contact); // Necesitarás hacer que Contact sea Parcelable o Serializable
        startActivity(intent);
    }

    // --- Lógica del Diálogo "Acción" (Ir al Mapa)  ---
    private void showActionDialog(Contact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Acción")
                .setMessage("Desea ir a la ubicación de " + contact.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Ir al Mapa según lat/lng
                    Intent mapIntent = new Intent(ContactListActivity.this, MapsActivity.class);
                    mapIntent.putExtra("LATITUDE", contact.getLatitude());
                    mapIntent.putExtra("LONGITUDE", contact.getLongitude());
                    mapIntent.putExtra("NAME", contact.getName());
                    startActivity(mapIntent);
                })
                .setNegativeButton("No", null)
                .show();
    }

    // --- CRUD: ELIMINAR (DELETE)  ---
    private void deleteContactFromApi(int contactId) {
        ContactApi apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.deleteContact(contactId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ContactListActivity.this, "Contacto Eliminado Exitosamente", Toast.LENGTH_SHORT).show();
                    fetchContactsList(); // Refrescar la lista
                } else {
                    Toast.makeText(ContactListActivity.this, "Error al eliminar: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ContactListActivity.this, "Error de conexión al eliminar.", Toast.LENGTH_LONG).show();
            }
        });
    }
}