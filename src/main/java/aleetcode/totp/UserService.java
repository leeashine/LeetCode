package aleetcode.totp;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class UserService {
    private Map<String, String> userSecrets = new HashMap<>();

    public String generateAndStoreUserSecret(String userId) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
        SecretKey secretKey = keyGenerator.generateKey();
        String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        userSecrets.put(userId, secretKeyString);
        return secretKeyString;
    }

    public String getUserSecret(String userId) {
        return userSecrets.get(userId);
    }
}