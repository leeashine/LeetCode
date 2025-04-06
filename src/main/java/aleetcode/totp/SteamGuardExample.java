package aleetcode.totp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SteamGuardExample {
    public static void main(String[] args) {
        try {
            UserService userService = new UserService();
            TOTPService totpService = new TOTPService();

            // 假设我们有一个用户ID
            String userId = "user123";

            // 生成并存储用户的密钥
            String secretKey = userService.generateAndStoreUserSecret(userId);
            System.out.println("User secret generated and stored: " + secretKey);

            // 生成TOTP
            String totp = totpService.generateTOTP(secretKey);
            System.out.println("Generated TOTP: " + totp);

            for (int i = 0; i < 60; i++) {
                // 生成TOTP
                String totp2 = totpService.generateTOTP(secretKey);
                System.out.println("Generated TOTP:" + i + ": " + totp2);
                Thread.sleep(1000);
            }

            // 生成TOTP
            String totp2 = totpService.generateTOTP(secretKey);
            System.out.println("Generated TOTP2: " + totp2);

            // 验证TOTP
            boolean isValid = totpService.validateTOTP(secretKey, totp);
            System.out.println("Is the TOTP valid? " + isValid);

            // 模拟用户输入错误的TOTP
            String invalidTotp = String.valueOf(Integer.parseInt(totp) + 1);
            boolean isInvalidValid = totpService.validateTOTP(secretKey, invalidTotp);
            System.out.println("Is the invalid TOTP valid? " + isInvalidValid);

        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}