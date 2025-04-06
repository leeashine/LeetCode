package aleetcode.totp;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * 基于时间的一次性密码（TOTP） 遵循的是 RFC 6238 标准
 * 也叫动态令牌
 */
public class TOTPService {
    private static final int TIME_STEP_SECONDS = 30;
    private static final int TOTP_DIGITS = 6;
    private static final String HMAC_ALGO = "HmacSHA1";

    public String generateTOTP(String secretKeyString) throws NoSuchAlgorithmException, InvalidKeyException {
        Base32 base32 = new Base32();
        byte[] secretKey = base32.decode(secretKeyString);

        long timeIndex = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;

        byte[] data = new byte[8];
        for (int i = 7; i >= 0; i--) {
            data[i] = (byte) (timeIndex & 0xFF);
            timeIndex >>= 8;
        }

        SecretKeySpec signKey = new SecretKeySpec(secretKey, HMAC_ALGO);
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[hash.length - 1] & 0xF;
        int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16) | ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);

        int otp = binary % (int) Math.pow(10, TOTP_DIGITS);

        return String.format("%0" + TOTP_DIGITS + "d", otp);
    }

    public boolean validateTOTP(String secretKeyString, String totp) throws NoSuchAlgorithmException, InvalidKeyException {
        String generatedTotp = generateTOTP(secretKeyString);
        return generatedTotp.equals(totp);
    }
}