package work.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesHelper {
    private SecretKeySpec keySpec;
    private IvParameterSpec iv;
    private boolean useCFB = false;

    public AesHelper(byte[] aesKey, byte[] iv) {
        if (aesKey == null || aesKey.length < 16 && (iv != null && iv.length < 16)) {
            throw new RuntimeException("错误的初始密钥");
        }
        if (iv == null) {
            iv = Md5Util.compute(aesKey);
        }
        keySpec = new SecretKeySpec(aesKey, "AES");
        this.iv = new IvParameterSpec(iv);
    }

    public byte[] encrypt(byte[] data) {
        Cipher cipher = null;
        try {
            if (useCFB) {
                cipher = Cipher.getInstance("AES/CFB/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            } else {
                cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            }
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] secret) {
        Cipher cipher = null;
        try {
            if (useCFB) {
                cipher = Cipher.getInstance("AES/CFB/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            } else {
                cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            }
            return cipher.doFinal(secret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
