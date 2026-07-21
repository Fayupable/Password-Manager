package PasswordManager;

/*
 * Educational example of the Java AES API — NOT a real password manager.
 * The security flaws below are left in deliberately and labeled with FLAW:
 * comments; the README explains the production-grade fix for each.
 */

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordManager {
    // FLAW: "AES" alone means AES/ECB/PKCS5Padding. ECB leaks plaintext
    // patterns. Real fix: AES/GCM/NoPadding with a random IV per entry.
    private static final String ALGORITHM = "AES";
    // FLAW: hardcoded key — anyone with this source can decrypt every entry.
    // Real fix: derive the key from a master password via PBKDF2/Argon2.
    private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
    // FLAW: in-memory only — every entry is lost when the program exits.
    private Map<String, String> passwordStore = new HashMap<>();

    public static void main(String[] args) {
        PasswordManager manager = new PasswordManager();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Password");
            System.out.println("2. Retrieve Password");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter site: ");
                    String site = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    manager.addPassword(site, password);
                    break;
                case 2:
                    System.out.print("Enter site: ");
                    site = scanner.nextLine();
                    String retrievedPassword = manager.getPassword(site);
                    System.out.println("Password: " + retrievedPassword);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public void addPassword(String site, String password) {
        try {
            String encryptedPassword = encrypt(password);
            passwordStore.put(site, encryptedPassword);
            System.out.println("Password added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPassword(String site) {
        try {
            String encryptedPassword = passwordStore.get(site);
            return encryptedPassword != null ? decrypt(encryptedPassword) : "No password found for this site.";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    private String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }
}

