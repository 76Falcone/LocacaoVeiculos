package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitário para hash de senhas usando SHA-256.
 *
 * @author 76Falcone
 */
public class HashUtil {

    /**
     * Gera o hash SHA-256 de uma string (ex: senha).
     *
     * @param texto texto a ser hasheado
     * @return hash em formato hexadecimal (64 caracteres)
     */
    public static String sha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo SHA-256 não disponível.", e);
        }
    }
}
