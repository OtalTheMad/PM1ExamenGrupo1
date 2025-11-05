package com.exam.pm2examengrupo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.exam.pm2examengrupo1.Contact;
import java.util.List;
import com.bumptech.glide.Glide;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private final OnContactActionListener listener;


    public interface OnContactActionListener {
        void onContactClick(Contact contact);
        void onDeleteClick(Contact contact);
        void onUpdateClick(Contact contact);
    }

    public ContactAdapter(List<Contact> contactList, OnContactActionListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.nameTextView.setText(contact.getName());


        holder.itemView.setOnClickListener(v -> listener.onContactClick(contact));

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void filterList(List<Contact> filteredList) {
        this.contactList = filteredList;
        notifyDataSetChanged();
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;
        TextView nameTextView;


        public ContactViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewContactName);
            imageViewPhoto = itemView.findViewById(R.id.imageViewContactPhoto);
        }
    }
}
