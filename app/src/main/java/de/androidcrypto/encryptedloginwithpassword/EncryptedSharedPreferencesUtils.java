package de.androidcrypto.encryptedloginwithpassword;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptedSharedPreferencesUtils {

    /**
     * This class bundles all methods to work with EncryptedSharedPreferences
     */

    private static String masterKeyAlias;
    private static SharedPreferences sharedPreferences;
    private static final String encryptedSharedPreferencesFilename = "esp.dat";
    private static final String encryptedSharedPreferencesDefaultValue = "no data stored";
    private static final boolean encryptedSharedPreferencesDefaultValueBoolean = false;
    private static Context context;
    private static boolean isAppPasswordSet = false;
    private static boolean isAppPasswordVerified = false;
    private static final String HASHED_APP_PASSWORD = "hashed_app_password";
    private static final String SALT_APP_PASSWORD = "salt_app_password";

    public static boolean setupEncryptedSharedPreferences(Context myContext) {
        try {
            context = myContext;
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            sharedPreferences = EncryptedSharedPreferences.create(
                    encryptedSharedPreferencesFilename,
                    masterKeyAlias,
                    myContext,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return isAppPasswordSet;
        }
        return true;
    }

    public static boolean saveNewAppPassword(char[] password) {
        // uses EncryptionUtilities to hash the password with PBKDF2
        // stores the hashed password and the random salt for later verification
        // return true if successful
        boolean result = false;
        EncryptionNewAppPasswordUtil encryptionUtil = new EncryptionNewAppPasswordUtil();
        encryptionUtil.setPassword(password);
        Thread thread = new Thread(encryptionUtil);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return result;
        }
        String hashedPasswordBase64 = encryptionUtil.getHashedPasswordBase64();
        String saltBase64 = encryptionUtil.getSaltBase64();
        if (hashedPasswordBase64.equals("")) {
            return result;
        }
        // store the password and salt
        saveEncryptedSharedPreferences(HASHED_APP_PASSWORD, hashedPasswordBase64);
        saveEncryptedSharedPreferences(SALT_APP_PASSWORD, saltBase64);
        return true;
    }

    public static boolean verifyAppPassword(char[] passwordToVerify) {
        // uses EncryptionUtilities to hash the password with PBKDF2
        // stores the hashed password and the random salt for later verification
        // return true if successful
        EncryptionVerifyAppPasswordUtil encryptionUtil = new EncryptionVerifyAppPasswordUtil();
        encryptionUtil.setPassword(passwordToVerify);
        String storedSaltBase64 = getEncryptedSharedPreferences(SALT_APP_PASSWORD);
        String storedAppPassword = getEncryptedSharedPreferences(HASHED_APP_PASSWORD);
        encryptionUtil.setStoredSaltBase64(storedSaltBase64);
        encryptionUtil.setStoredHashedPasswordBase64(storedAppPassword);
        Thread thread = new Thread(encryptionUtil);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        boolean result = encryptionUtil.getVerificationResult();
        isAppPasswordVerified = result;
        return result;
    }

    public static boolean getAppPasswordStatus() {
        boolean result = false;
        String saltBase64 = getEncryptedSharedPreferences(SALT_APP_PASSWORD);
        if (saltBase64.equals(encryptedSharedPreferencesDefaultValue)) {
            return result;
        } else {
            System.out.println("*** saltBase64: " + saltBase64);
            return true;
        }
    }

    public static boolean getAppPasswordVerificationStatus() {
        return isAppPasswordVerified;
    }

    /**
     * private methods follow
     */

    private static boolean checkEncryptedSharedPreferencesStored(String key) {
        String decryptedData = sharedPreferences
                .getString(key, encryptedSharedPreferencesDefaultValue);
        if (decryptedData.equals(encryptedSharedPreferencesDefaultValue)) {
            return false;
        } else {
            return true;
        }
    }

    private static String getEncryptedSharedPreferences(String key) {
        return sharedPreferences
                .getString(key, encryptedSharedPreferencesDefaultValue);
    }

    private static void saveEncryptedSharedPreferences(String key, String value) {
        sharedPreferences
                .edit()
                .putString(key, value)
                .apply();
    }

    private static boolean getEncryptedSharedPreferencesBoolean(String key) {
        return sharedPreferences
                .getBoolean(key, encryptedSharedPreferencesDefaultValueBoolean);
    }

    private static void saveEncryptedSharedPreferencesBoolean(String key, boolean value) {
        sharedPreferences
                .edit()
                .putBoolean(key, value)
                .apply();
    }
}
