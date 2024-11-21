package activities;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class encryptPassword {

    // Método para cifrar la contraseña usando AES
    public static String encryptPassword2(String password, String userId) {
        try {
            // Usar el UID del usuario como parte de la clave (se puede hacer más seguro generando una clave separada)
            String key = generateKey(userId); // Generar una clave a partir del userId

            // Cifrar la contraseña con AES
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Cifrar el password
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());

            // Convertir el array de bytes a un string codificado en Base64 para poder almacenarlo
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Si ocurre un error, devuelve null
        }
    }

    // Método para generar una clave a partir del userId
    private static String generateKey(String userId) {
        // En un caso real, es recomendable generar una clave de 16 bytes (128 bits) o 32 bytes (256 bits)
        // Aquí se usa una versión simple que toma el primer 16 bytes del userId
        // Puedes utilizar un método más robusto si lo deseas.
        return userId.substring(0, Math.min(userId.length(), 16));  // Asegúrate de que la clave sea de 16 bytes
    }
}
