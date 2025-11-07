package com.exam.pm2examengrupo1.api;

import com.exam.pm2examengrupo1.Contact;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface ContactApi {

    // 1. CREATE (Salvar Contacto)
    @POST("contacts")
    Call<Contact> createContact(@Body Contact contact);

    // 2. GET (Obtener lista)
    @GET("contacts")
    Call<List<Contact>> getContacts();

    // 3. UPDATE (Actualizar Contacto)
    @PUT("contacts/{id}")
    Call<Contact> updateContact(@Path("id") int contactId, @Body Contact contact);

    // 4. DELETE (Eliminar Contacto)
    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") int contactId);
}
