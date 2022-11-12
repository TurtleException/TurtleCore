package de.turtle_exception.client.internal.util.crypto;

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
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final int KEY_SPEC_PASS_ITERATIONS = 65536;
    private static final int KEY_SPEC_KEY_LENGTH      =   256;

    private static final int   IV_LENGTH = 16;
    private static final int SALT_LENGTH = 16;

    private Encryption() { }

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

    private static byte[] doEncrypt(@NotNull String algorithm, byte[] bytes, @NotNull SecretKey key, @NotNull IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypt = cipher.doFinal(bytes);
        return Base64.getEncoder().encode(encrypt);
    }

    private static byte[] doDecrypt(@NotNull String algorithm, byte[] bytes, @NotNull SecretKey key, @NotNull IvParameterSpec iv)
            throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] decoded = Base64.getDecoder().decode(bytes);
        return cipher.doFinal(decoded);
    }

    /* - - - */

    public static @NotNull String encrypt(final @NotNull String input, @NotNull String pass)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return new String(encrypt(input.getBytes(), pass), StandardCharsets.ISO_8859_1);
    }

    public static byte[] encrypt(final byte[] bytes, @NotNull String pass)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] sa = generateSalt();

        IvParameterSpec ivParameterSpec = generateIv();
        SecretKey key = generateKeyFromPassword(pass, sa);


        byte[] iv  = ivParameterSpec.getIV();
        byte[] ct = doEncrypt(ALGORITHM, bytes, key, ivParameterSpec);
        byte[] out = new byte[iv.length + sa.length + ct.length];

        System.arraycopy(iv, 0, out, 0, iv.length);
        System.arraycopy(sa, 0, out, IV_LENGTH, sa.length);
        System.arraycopy(ct, 0, out, SALT_LENGTH + IV_LENGTH, ct.length);

        return out;
    }

    /* - - - */

    public static @NotNull String decrypt(final @NotNull String input, @NotNull String pass)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return new String(decrypt(input.getBytes(StandardCharsets.ISO_8859_1), pass));
    }

    public static byte[] decrypt(final byte[] bytes, @NotNull String pass)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] iv = Arrays.copyOfRange(bytes, 0, IV_LENGTH);
        byte[] sa = Arrays.copyOfRange(bytes, IV_LENGTH, IV_LENGTH + SALT_LENGTH);
        byte[] ct = Arrays.copyOfRange(bytes, IV_LENGTH + SALT_LENGTH, bytes.length);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        SecretKey key = generateKeyFromPassword(pass, sa);

        return doDecrypt(ALGORITHM, ct, key, ivParameterSpec);
    }
}
