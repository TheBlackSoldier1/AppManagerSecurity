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

    // Método para eliminar la contraseña de Firebase y de la lista local
    private void deletePassword(String idTarjeta, int position) {
        // Obtenemos la referencia a la base de datos de Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("pass");

        // Eliminamos el nodo correspondiente a la tarjeta usando su idTarjeta
        database.child(idTarjeta).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Si se elimina con éxito en Firebase, eliminamos también de la lista local
                    passwordList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Contraseña eliminada correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Si falla la eliminación, mostramos un error
                    Toast.makeText(context, "Error al eliminar la contraseña", Toast.LENGTH_SHORT).show();
                });
    }
}
