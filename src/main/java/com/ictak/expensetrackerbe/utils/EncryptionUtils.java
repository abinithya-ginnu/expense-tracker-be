package com.ictak.expensetrackerbe.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class EncryptionUtils {
    String myEncryptionKey = "ThisIsMyFinalICTAKProject";

    public String encrypt(String email) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(myEncryptionKey);
        return encryptor.encrypt(email);
    }

    public String decrypt(String encryptedEmail) {
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(myEncryptionKey);
        return decryptor.decrypt(encryptedEmail);
    }
}
