package activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanagerpassword.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import models.UserPass;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private List<UserPass> passwordList;
    private Context context;

    public PasswordAdapter(List<UserPass> passwordList, Context context) {
        this.passwordList = passwordList;
        this.context = context;
    }

    @Override
    public PasswordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PasswordViewHolder holder, int position) {
        UserPass userPass = passwordList.get(position);

        holder.siteNameTextView.setText(userPass.getNombrePagina());
        holder.usernameTextView.setText(userPass.getNombreUsuario());
        holder.passwordTextView.setText("**********"); // Mostramos la contraseña como asteriscos
        holder.notesTextView.setText(userPass.getApuntes());

        // Configuramos el botón de eliminar
        holder.deleteButton.setOnClickListener(v -> {
            deletePassword(userPass.getIdTarjeta(), position);
        });
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public static class PasswordViewHolder extends RecyclerView.ViewHolder {

        TextView siteNameTextView, usernameTextView, passwordTextView, notesTextView;
        Button deleteButton;

        public PasswordViewHolder(View itemView) {
            super(itemView);
            siteNameTextView = itemView.findViewById(R.id.siteNameTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void deletePassword(String idTarjeta, int position) {
        // Verificamos que la lista no esté vacía y que la posición sea válida
        if (passwordList != null && !passwordList.isEmpty() && position >= 0 && position < passwordList.size()) {
            // Obtenemos la referencia a la base de datos de Firebase
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("pass");

            // Eliminamos el nodo correspondiente a la tarjeta usando su idTarjeta
            database.child(idTarjeta).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Si la eliminación fue exitosa en Firebase, eliminamos también de la lista local
                        if (position < passwordList.size()) {
                            passwordList.remove(position); // Eliminamos de la lista local
                            notifyItemRemoved(position);    // Notificamos al adaptador que se eliminó un item
                            Toast.makeText(context, "Contraseña eliminada correctamente", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Si hay un error al eliminar, mostramos un mensaje
                        Toast.makeText(context, "Error al eliminar la contraseña", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Si la lista está vacía o la posición es inválida, mostramos un mensaje
            Toast.makeText(context, "No hay contraseñas para eliminar o la posición es incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}