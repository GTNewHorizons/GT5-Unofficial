package gtPlusPlus.core.util.data;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private final String secret;
    ;

    private final SecretKeySpec secretKey;

    private final byte[] key;

    public AES() {

        this("Darkness In Their Hearts");
    }

    public AES(String aSecret) {

        secret = aSecret;

        key = getBytes(getHashedString(secret));

        secretKey = generateKey(key);
    }

    private static final String getHashedString(String aString) {

        return toHexString(getSHA(aString));
    }

    private static final byte[] getSHA(String input) {

        MessageDigest md;

        try {

            md = MessageDigest.getInstance("SHA-256");

            return md.digest(input.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        return new byte[] {};
    }

    private static final String toHexString(byte[] hash) {

        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32) {

            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    private final byte[] getBytes(String aKey) {

        byte[] aKeyData;

        MessageDigest sha;

        try {

            sha = MessageDigest.getInstance("SHA-256");

            return sha.digest(aKey.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e1) {

            e1.printStackTrace();

            try {

                aKeyData = aKey.getBytes("UTF-8");

                sha = MessageDigest.getInstance("SHA-1");

                aKeyData = sha.digest(key);

                aKeyData = Arrays.copyOf(key, 16);

                return aKeyData;

            } catch (NoSuchAlgorithmException e) {

                e.printStackTrace();

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }

        return new byte[] {};
    }

    private final SecretKeySpec generateKey(byte[] aKey) {

        return new SecretKeySpec(aKey, "AES");
    }

    public String encode(String strToEncrypt) {

        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

        } catch (Exception e) {

            System.out.println("Error while encrypting: " + e.toString());
        }

        return null;
    }

    public String decode(String strToDecrypt) {

        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

        } catch (Exception e) {

        }

        return null;
    }
}
