package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmanagerpassword.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

import models.UserPass;

public class AddEditPasswordActivity extends AppCompatActivity {

    private EditText sitioEditText, usuarioEditText, contraseñaEditText, apuntesEditText;
    private Button savePasswordButton, backButton;
    private FirebaseDatabase database = FirebaseDatabase.getInstance(); // Instancia de Realtime Database
    private DatabaseReference myRef = database.getReference("contraseñas"); // Referencia a la colección de contraseñas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_password);

        sitioEditText = findViewById(R.id.sitioEditText);
        usuarioEditText = findViewById(R.id.usuarioEditText);
        contraseñaEditText = findViewById(R.id.contraseñaEditText);
        apuntesEditText = findViewById(R.id.apuntesEditText);
        savePasswordButton = findViewById(R.id.savePasswordButton);
        backButton = findViewById(R.id.backButton);


        backButton.setOnClickListener(v -> onBackPressed());
        savePasswordButton.setOnClickListener(v -> savePassword());
    }

    private void savePassword() {
        String sitio = sitioEditText.getText().toString();
        String usuario = usuarioEditText.getText().toString();
        String contraseña = contraseñaEditText.getText().toString();
        String apuntes = apuntesEditText.getText().toString();

        // Verifica si los campos no están vacíos
        if (sitio.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Genera una clave de cifrado basada en el UID del usuario
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String encryptedPassword = encryptPassword(contraseña, userId);  // Cifra la contraseña

        // Crea el objeto UserPass
        UserPass userpass = new UserPass(userId, UUID.randomUUID().toString(), apuntes, encryptedPassword, sitio, usuario);

        // Guardar en Firebase Realtime Database
        String key = myRef.push().getKey();  // Genera una clave única para este objeto
        if (key != null) {
            myRef.child(key).setValue(userpass)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Contraseña guardada", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar contraseña", Toast.LENGTH_SHORT).show());
        }
    }

    // Método para cifrar la contraseña con AES
    private String encryptPassword(String password, String userId) {
        try {
            // Usar el UID del usuario para generar la clave
            String key = generateKey(userId);  // Generar una clave a partir del userId

            // Cifrar la contraseña con AES
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Cifrar la contraseña
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());

            // Convertir el array de bytes a un string codificado en Base64 para poder almacenarlo
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Si ocurre un error, devuelve null
        }
    }

    // Método para generar una clave a partir del userId
    private String generateKey(String userId) {
        // En un caso real, es recomendable usar un algoritmo de derivación de claves (KDF) como PBKDF2, Scrypt o Argon2.
        // Sin embargo, para simplificar, tomaremos el userId y lo ajustamos a 16 bytes.

        // Si el userId es más corto de 16 caracteres, lo completamos con ceros (o cualquier valor que prefieras).
        if (userId.length() < 16) {
            while (userId.length() < 16) {
                userId += "0";  // Añadimos ceros hasta que sea de 16 caracteres
            }
        } else {
            // Si el userId es más largo, recortamos a los primeros 16 caracteres
            userId = userId.substring(0, 16);
        }

        return userId;  // Regresamos la clave de 16 caracteres
    }

}
