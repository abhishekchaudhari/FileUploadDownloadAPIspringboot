package com.alzion.alzion.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    private static final String ALGORITHM = "AES";

    public byte[] encrypt(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPasscode(key));
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPasscode(key));
        return cipher.doFinal(data);
    }

    private SecretKey getKeyFromPasscode(String passcode) {
        byte[] keyBytes = passcode.getBytes();
        byte[] validKey = new byte[16]; // AES-128 requires a 16-byte key
        System.arraycopy(keyBytes, 0, validKey, 0, Math.min(keyBytes.length, validKey.length));
        return new SecretKeySpec(validKey, ALGORITHM);
    }
}
