package activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanagerpassword.R;

import java.util.List;

import models.UserPass;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private List<UserPass> passwordList;

    public PasswordAdapter(List<UserPass> passwordList) {
        this.passwordList = passwordList;
    }

    @Override
    public PasswordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PasswordViewHolder holder, int position) {
        UserPass userpass = passwordList.get(position);
        holder.siteNameTextView.setText(userpass.getNombrePagina());
        holder.usernameTextView.setText(userpass.getNombreUsuario());
        holder.passwordTextView.setText(userpass.getPass());
        holder.notesTextView.setText(userpass.getApuntes());
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public static class PasswordViewHolder extends RecyclerView.ViewHolder {

        TextView siteNameTextView, usernameTextView, passwordTextView, notesTextView;

        public PasswordViewHolder(View itemView) {
            super(itemView);
            siteNameTextView = itemView.findViewById(R.id.siteNameTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
        }
    }
}
