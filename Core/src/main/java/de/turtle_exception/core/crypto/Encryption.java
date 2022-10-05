package de.turtle_exception.core.crypto;

import org.jetbrains.annotations.NotNull;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@SuppressWarnings("SameParameterValue")
public class Encryption {
    private static final int KEY_SPEC_PASS_ITERATIONS = 65536;
    private static final int KEY_SPEC_KEY_LENGTH = 256;

    private static final int IV_LENGTH = 16;
    private static final int SALT_LENGTH = 16;

    private static @NotNull SecretKey generateKeyFromPassword(@NotNull String pass, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, KEY_SPEC_PASS_ITERATIONS, KEY_SPEC_KEY_LENGTH);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static @NotNull IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static @NotNull String doEncrypt(@NotNull String algorithm, @NotNull String input, @NotNull SecretKey key, @NotNull IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] txtByte = input.getBytes();
        byte[] encrypt = cipher.doFinal(txtByte);
        byte[] encoded = Base64.getEncoder().encode(encrypt);

        return new String(encoded);
    }

    private static @NotNull String doDecrypt(@NotNull String algorithm, @NotNull String cipherText, @NotNull SecretKey key, @NotNull IvParameterSpec iv)
            throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] txtByte = cipherText.getBytes();
        byte[] decoded = Base64.getDecoder().decode(txtByte);
        byte[] decrypt = cipher.doFinal(decoded);

        return new String(decrypt);
    }

    public static @NotNull String encrypt(@NotNull String input, @NotNull String pass)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] sa = generateSalt();

        IvParameterSpec ivParameterSpec = generateIv();
        SecretKey key = generateKeyFromPassword(pass, sa);

        String cipherText = doEncrypt("AES/CBC/PKCS5Padding", input, key, ivParameterSpec);

        byte[] iv  = ivParameterSpec.getIV();
        byte[] ct  = cipherText.getBytes();
        byte[] out = new byte[iv.length + sa.length + ct.length];

        System.arraycopy(iv, 0, out, 0, iv.length);
        System.arraycopy(sa, 0, out, IV_LENGTH, sa.length);
        System.arraycopy(ct, 0, out, SALT_LENGTH + IV_LENGTH, ct.length);

        return new String(out, StandardCharsets.ISO_8859_1);
    }

    public static @NotNull String decrypt(@NotNull String input, @NotNull String pass)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] in = input.getBytes(StandardCharsets.ISO_8859_1);
        byte[] iv = Arrays.copyOfRange(in, 0, IV_LENGTH);
        byte[] sa = Arrays.copyOfRange(in, IV_LENGTH, IV_LENGTH + SALT_LENGTH);
        byte[] ct = Arrays.copyOfRange(in, IV_LENGTH + SALT_LENGTH, in.length);
        String cipherText = new String(ct);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        SecretKey key = generateKeyFromPassword(pass, sa);

        return doDecrypt("AES/CBC/PKCS5Padding", cipherText, key, ivParameterSpec);
    }
}
