package aleetcode;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class RSABackend {

    /**
     * 生成RSA密钥对
     */
    public static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 使用RSA公钥加密对称密钥
     */
    public static byte[] encryptSymmetricKeyWithRSA(PublicKey publicKey, SecretKey symmetricKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(symmetricKey.getEncoded());
    }

    /**
     * 使用RSA私钥解密对称密钥
     */
    public static SecretKey decryptSymmetricKeyWithRSA(PrivateKey privateKey, byte[] encryptedSymmetricKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedKeyBytes = cipher.doFinal(encryptedSymmetricKey);
        return new SecretKeySpec(decryptedKeyBytes, 0, decryptedKeyBytes.length, "AES");
    }

    /**
     * 使用对称密钥加密数据
     */
    public static byte[] encryptWithSymmetricKey(SecretKey symmetricKey, byte[] data, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, ivParameterSpec);
        return cipher.doFinal(data);
    }

    /**
     * 使用对称密钥解密数据
     */
    public static byte[] decryptWithSymmetricKey(SecretKey symmetricKey, byte[] encryptedData, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey, ivParameterSpec);
        return cipher.doFinal(encryptedData);
    }

    // 生成随机的16字节初始向量
    public static byte[] generateRandomIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static void main(String[] args) throws Exception {
        // 生成RSA密钥对
//        KeyPair rsaKeyPair = generateRSAKeyPair();
//        PublicKey rsaPublicKey = rsaKeyPair.getPublic();
//        PrivateKey rsaPrivateKey = rsaKeyPair.getPrivate();

//        byte[] bytes = generateRandomIV();
        byte[] bytes = Base64.getDecoder().decode("o9Gn6Ln+BGdDSrQmlYrUwA==");

        System.out.println("IV: " + Base64.getEncoder().encodeToString(bytes));

        // 加载公钥
        String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgMTbz3l/vcM3NiAIm6/nTB5UCoxyKMGSkqtAt4TQ8q+Iii1tee8xlfG+ts9HtuKDwTSrfxDPggMJSiXuyKZkQAnRsvaLVqwFqsIV0sj79Irt45G+9pHfy2UvzuSZx/JtbnbKQz8U1SNmuWiy5UagXJElYdcPl7pCCjWKtR9/ORD5WHR0hOA3UmGhqrsYWTw1q2scEq+Vuy8lNorSL+PDt+oqBpT8BgT4oV7VHz6i7qPhiXEKR2gE/fWTVvMkDperjlH5fXpyEv38A9BX/l063Cv/J4euOeM27SHboDKQ2WLEuzmf4u+ODQGFjpusWlfiWaz8J13/jXX7B7Nqh/n6twIDAQAB"; // 将你的公钥字符串放在这里
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
        PublicKey rsaPublicKey = keyFactory1.generatePublic(x509EncodedKeySpec);

        // 加载私钥
        String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCAxNvPeX+9wzc2IAibr+dMHlQKjHIowZKSq0C3hNDyr4iKLW157zGV8b62z0e24oPBNKt/EM+CAwlKJe7IpmRACdGy9otWrAWqwhXSyPv0iu3jkb72kd/LZS/O5JnH8m1udspDPxTVI2a5aLLlRqBckSVh1w+XukIKNYq1H385EPlYdHSE4DdSYaGquxhZPDWraxwSr5W7LyU2itIv48O36ioGlPwGBPihXtUfPqLuo+GJcQpHaAT99ZNW8yQOl6uOUfl9enIS/fwD0Ff+XTrcK/8nh6454zbtIdugMpDZYsS7OZ/i744NAYWOm6xaV+JZrPwnXf+NdfsHs2qH+fq3AgMBAAECggEAMBVSXy7nXFzBCRz9GeWdYPcSOr4nAsEFkm8QQ3b87yJMwgwkjRucUHls1aahCNGckVCxmUkVs16e4Wk4uQQo+yCvhTw626pwk0rWC1exfadDHW8W98uQ3cpzB7alFdyYsF77OBEeHliRyzolUygESYyJgPdlgyqx7k3/9ZJFV3FNfklSvTIQHW3jRe5zN0jaJpSmZB206moRTG3WyRItZ8jIi0uoBeOLQnHmFk4txtT5C6uKpv+pQsIZaYdrB/UdkqMjgOwAs+TdNcLSaKW42VNMVcMs7uzvETHl4CFDgHb4unAkhYCcb3bqhDeJXCNikYRsQU9okMg9twMlOegpQQKBgQDYP5UD2dNOZSlArr8qYN1dTpKoswsYwNVYQaKzVdKvspKZCi3KhXYLwJIejfyag3z7bOWXc4SuuqDbu0YFZsFZHRnaJ59f6/+ZyXNkSPi7CzQpLUUuwiIWnKkOxXfh6Pwc17/oRYJKCDK87XWK6H0xr5hCEK72kp0Tyec4fg5uXQKBgQCYcJb0vhS4j4LadWWH+pZmDTAE+1eRkFRSdDRTgK2sZS7kC4GwBYAZwXJTOm3tFYLJ/FxZnHZ083U6X6nkVPwss6jeb2K2GOA+oiKeur9I1/RYb21mwgyiIhlSkNeZ6lmBIGQNli0OoBi+IsJmiDudHSlrx+U+tIqWaDfbXo40IwKBgA/CUnG9ZhDIfoEgOv4zoO4z06xHn/9lziZoYhr/niL/uH8+pqejoj6pAf0IC+iWvHz2KqUm3fx9zUs2EpxCKMT9weyMBqlO4YmrjKU6TCYVYu+9RNVrgGB1ejyBV4M3H2hasLmUlzId9tSd2XsOBlGgok/uN1HwzVGr3RXY2eRhAoGBAJKOFCNiJ4u0tWoP2bgci8G7RgJUHkFdECRhgfkRymbmtGkcQueWsiyQ1bf8gbigqKhGTgb9LMmNUpP3at+RcQwNAfov/ifxzI2K1VvjUInYzRrErdpQD1NFEzFU/WMFcl15ZZfGXLUl6APMaTeoHCAV7D+p49UgP+kQczsdMfwfAoGAHzgMT2OaRNE50/hbArGS31ul1/7amF0IPVZJ5RrnlbAqS/hmu5zcZhCihOrjOIcCmzPrXBE3GrkoR0Nm8aQ2NJiQvc3OMfudjyeUJ6LxPcvIPnAwW9+5BA6vhexq35D8E0dWUryr3p01vHxZOG2U3uOLccVw5n/ji8MBGD9rdPI="; // 将你的公钥字符串放在这里
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey rsaPrivateKey = keyFactory.generatePrivate(keySpec);

        System.out.println("rsaPublicKey:" + Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()));
        System.out.println("rsaPrivateKey:" + Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()));
        // 生成对称密钥
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(256);
//        SecretKey symmetricKey = keyGenerator.generateKey();
        String symmetricKeyStr = "Mh9q2BDt16S/PZzE3l1nTlx6PrZu7sK9WB3zEzn44IA=";
        SecretKey symmetricKey = new SecretKeySpec(Base64.getDecoder().decode(symmetricKeyStr), "AES");
        System.out.println("Symmetric Key: " + Base64.getEncoder().encodeToString(symmetricKey.getEncoded()));

        // 使用RSA公钥加密对称密钥
        byte[] encryptedSymmetricKey = encryptSymmetricKeyWithRSA(rsaPublicKey, symmetricKey);
//        byte[] encryptedSymmetricKey = Base64.getDecoder().decode("FyxBhaf+rn9w74PMKn1LXALkm3FfPQUGMyUDSYIEqcdJlvWhw+fswX25B1TV0RRbBxFfu+Qgwu/kz0pU8kBmoQw3hfgU86zpGWbVCuPR7A2AW7qbaDz9Quxx7ZXt/euuvOv6uK7jVg6gv/qRpJRM/0GrYRjrA8DzXPLv9OimMRt2dJxLimKHv03+TeQmUXk7yc/JvexPTeOQ9JR2wFC0g2X59ycrd1LB9tIXwDu7Pa1RFlRKB5Ehpx0NdrvrgnZU0szSX7c7m1k2MvbDNyHNovk+tcP0efKsRUq4E4meHeia437qMM4ECRl2HbPsdwtKaxSbMqqIdZ5OwSyysJAHaw==");
        System.out.println("Encrypted symmetric key: " + Base64.getEncoder().encodeToString(encryptedSymmetricKey));

        // 假设这是后端接收到的数据
        byte[] originalData = "Hello, this is a secret message.".getBytes();

        // 使用对称密钥加密数据
        byte[] encryptedData = encryptWithSymmetricKey(symmetricKey, originalData, bytes);
        System.out.println("Encrypted data: " + Base64.getEncoder().encodeToString(encryptedData));

        // 使用对称密钥解密数据
        byte[] decryptedData = decryptWithSymmetricKey(symmetricKey, encryptedData, bytes);
        System.out.println("Decrypted data: " + new String(decryptedData));

        // 使用RSA私钥解密对称密钥
        SecretKey decryptedSymmetricKey = decryptSymmetricKeyWithRSA(rsaPrivateKey, encryptedSymmetricKey);
        System.out.println("Decrypted symmetric key: " + Base64.getEncoder().encodeToString(decryptedSymmetricKey.getEncoded()));
    }
}