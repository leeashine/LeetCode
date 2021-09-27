package work.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 注册为bean
 */
public class CryptUtil {

    private static final Logger logger = LoggerFactory.getLogger(CryptUtil.class);

    private static ThreadLocal<AesHelper> threadLocal = new ThreadLocal<>();

    private String prefix;
    private String key;

    public CryptUtil(String key, String prefix) {
        this.key = key;
        this.prefix = prefix;
    }

    public AesHelper getAesHelper() {
        AesHelper aesHelper = threadLocal.get();
        if (aesHelper == null) {
            aesHelper = new AesHelper(Base64.getDecoder().decode(key), null);
            threadLocal.set(aesHelper);
        }
        return aesHelper;
    }

    /**
     * 加密
     *
     * @param toEncrypt 原始数据
     * @return 加密后数据
     */
    public String encrypt(String toEncrypt) {
        if (StringUtils.isEmpty(toEncrypt)) {
            return toEncrypt;
        }
        try {
            return prefix + Base64.getEncoder().encodeToString(getAesHelper().encrypt(toEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error("CryptUtil.encrypt error,toEncrypt :{}", toEncrypt);
            return toEncrypt;
        } finally {
            threadLocal.remove();
        }
    }

    /**
     * 解密
     *
     * @param toDecrypt
     * @return
     */
    public String decrypt(String toDecrypt) {
        if (StringUtils.isEmpty(toDecrypt)) {
            return toDecrypt;
        }
        if (!isEncrypted(toDecrypt)) {
            return toDecrypt;
        }
        try {
            toDecrypt = toDecrypt.substring(prefix.length());
            return new String(getAesHelper().decrypt(Base64.getDecoder().decode(toDecrypt)));
        } catch (Exception e) {
            logger.error("CryptUtils.decrypt error,toDecrypt :{}", toDecrypt);
            return toDecrypt;
        } finally {
            threadLocal.remove();
        }
    }

    /**
     * 判断是否加密过
     *
     * @param toDecrypt 需要判断的字符串
     * @return boolean
     */
    private boolean isEncrypted(String toDecrypt) {
        return StringUtils.startsWith(toDecrypt, prefix);
    }

    public static void main(String[] args) {
        CryptUtil cipher = new CryptUtil("7MdDaXxfGhtSdGq0zU2/jw==", "cipher");
        String encrypt = cipher.encrypt("15150215899");
        System.out.println("加密数据: " + encrypt);
        String decrypt = cipher.decrypt(encrypt);
        System.out.println("解密数据: " + decrypt);
    }

}
