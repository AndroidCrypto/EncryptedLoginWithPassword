package de.androidcrypto.encryptedloginwithpassword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {

    public static final int MINIMAL_PASSWORD_LENGTH = 3;
    Button setAppPassword, changeAppPassword, resetAppPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setAppPassword = findViewById(R.id.btnSettingsSetAppPassword);
        changeAppPassword = findViewById(R.id.btnSettingsChangeAppPassword);
        resetAppPassword = findViewById(R.id.btnSettingsResetAppPassword);

        setAppPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePassword(view);
            }
        });

        resetAppPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsDisabledForUsing();
            }
        });

    }

    private void savePassword(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Neues Programm Passwort vergeben");
        String message = "\nBitte geben Sie ein mindestens\n8-stelliges Passwort ein und drücken\nSie auf SPEICHERN, um alle\nProgrammfunktionen nutzen\nzu können.";
        alertDialog.setMessage(message);
        final EditText oldPassphrase = new EditText(v.getContext());
        oldPassphrase.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.round_rect_shape, null));
        //oldPassphrase.setBackground(getResources().getDrawable(R.drawable.round_rect_shape));
        oldPassphrase.setHint("  neues Passwort eingeben");
        oldPassphrase.setPadding(50, 20, 50, 20);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(36, 36, 36, 36);
        oldPassphrase.setLayoutParams(lp1);
        RelativeLayout container = new RelativeLayout(v.getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(oldPassphrase);
        alertDialog.setView(container);
        alertDialog.setPositiveButton("speichern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int oldPassphraseLength = oldPassphrase.length();
                char[] oldPassword = new char[oldPassphraseLength];
                oldPassphrase.getText().getChars(0, oldPassphraseLength, oldPassword, 0);
                // test on password length
                if (oldPassphraseLength < MINIMAL_PASSWORD_LENGTH) {
                    Snackbar snackbar = Snackbar.make(v, "Das Passwort ist zu kurz", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.red));
                    snackbar.show();
                    return;
                }

                // storing the data
                boolean result = EncryptedSharedPreferencesUtils.saveNewAppPassword(oldPassword);

                if (result) {
                    Snackbar snackbar = Snackbar.make(v, "The App password was stored.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.green));
                    snackbar.show();
                    setButtonsEnabledForUsing();
                    return;
                } else {
                    Snackbar snackbar = Snackbar.make(v, "Soething got wrong, sorry.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.red));
                    snackbar.show();
                    return;
                }





                // get the new generated and encryptedMasterKey from Encryption
                /*
                String encryptedMasterKey = Encryption.encryptMasterKeyAesGcmPbkdf2InternallyToBase64(oldPassword);
                // check for length
                if (encryptedMasterKey.length() < 1) {
                    Snackbar snackbar = Snackbar.make(v, "Die Verschlüsselung funktioniert nicht.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.red));
                    snackbar.show();
                    return;
                }
*/
                /*
                Snackbar snackbar2 = Snackbar.make(v, "encryptedMasterKey: " + encryptedMasterKey, Snackbar.LENGTH_LONG);
                snackbar2.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.blue));
                snackbar2.show();

                // store the key in internal storage
                boolean success = saveEncryptedMasterKey(encryptedMasterKeyFileName, encryptedMasterKey.getBytes(StandardCharsets.UTF_8));
                if (!success) {
                    Snackbar snackbar = Snackbar.make(v, "Die Speicherung funktioniert nicht.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.red));
                    snackbar.show();
                    return;
                }
                // now reload the encryptedMasterkey and store encrypted with session key in app
                // loading the encrypted masterKey from internal storage
                byte[] encryptedMasterKeyByte = loadEncryptedMasterKey(encryptedMasterKeyFileName);
                boolean internalEncryptedMasterKeyAvailable = Encryption.decryptMasterKeyAesGcmPbkdf2InternallyFromBase64(encryptedMasterKeyByte, oldPassword);
                if (!internalEncryptedMasterKeyAvailable) {
                    Snackbar snackbar = Snackbar.make(v, "Das Passwort ist falsch.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.red));
                    snackbar.show();
                    return;
                }*/
                /*
                // activate work buttons
                allWorkButtonsEnabled();
                disableSavePasswordButton();
                enableLoadPasswordButton();
                enableResetPasswordButton();
                // start des count down timers
                startCountdownTimer(v);*/
              /*
                Snackbar snackbar = Snackbar.make(v, "Die App ist nun bereit", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.green));
                snackbar.show();
                setButtonsEnabledForUsing();

               */
            }
        });
        alertDialog.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar snackbar = Snackbar.make(v, "Passwort speichern abgebrochen", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(v.getContext(), R.color.red));
                snackbar.show();
            }
        });
        alertDialog.show();
    }

    public void loadPasswordPressed(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
        alertDialog.setTitle("Programm Passwort eingeben");
        String message = "\nBitte geben Sie ein mindestens\n8-stelliges Passwort ein und drücken\nSie auf LADEN, um alle\nProgrammfunktionen nutzen\nzu können.";
        alertDialog.setMessage(message);
        final EditText oldPassphrase = new EditText(SettingsActivity.this);
        oldPassphrase.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_rect_shape, null));
        //oldPassphrase.setBackground(getResources().getDrawable(R.drawable.round_rect_shape));
        oldPassphrase.setHint("  Passwort");
        oldPassphrase.setPadding(50, 20, 50, 20);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(36, 36, 36, 36);
        oldPassphrase.setLayoutParams(lp1);
        RelativeLayout container = new RelativeLayout(SettingsActivity.this);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(oldPassphrase);
        alertDialog.setView(container);
        alertDialog.setPositiveButton("laden", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int oldPassphraseLength = oldPassphrase.length();
                char[] oldPassword = new char[oldPassphraseLength];
                oldPassphrase.getText().getChars(0, oldPassphraseLength, oldPassword, 0);
                // test on password length
                if (oldPassphraseLength < MINIMAL_PASSWORD_LENGTH) {
                    Snackbar snackbar = Snackbar.make(v, "Das Passwort ist zu kurz", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(SettingsActivity.this, R.color.red));
                    snackbar.show();
                    return;
                }
                /*
                // loading the encrypted masterKey from internal storage
                byte[] encryptedMasterKey = loadEncryptedMasterKey(encryptedMasterKeyFileName);
                boolean internalEncryptedMasterKeyAvailable = Encryption.decryptMasterKeyAesGcmPbkdf2InternallyFromBase64(encryptedMasterKey, oldPassword);
                if (!internalEncryptedMasterKeyAvailable) {
                    Snackbar snackbar = Snackbar.make(v, "Das Passwort ist falsch.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(SettingsActivity.this, R.color.red));
                    snackbar.show();
                    return;
                }
                // activate work buttons
                allWorkButtonsEnabled();
                enableChangePasswordButton();
                enableResetPasswordButton();
                // start des count down timers
                startCountdownTimer(v);*/
                Snackbar snackbar = Snackbar.make(v, "Die App ist nun bereit", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(SettingsActivity.this, R.color.green));
                snackbar.show();
            }
        });
        alertDialog.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar snackbar = Snackbar.make(v, "Passwort laden abgebrochen", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(SettingsActivity.this, R.color.red));
                snackbar.show();
            }
        });
        alertDialog.show();
    }

    // disables the set app password button and enables change & reset password button
    private void setButtonsEnabledForUsing() {
        setAppPassword.setEnabled(false);
        changeAppPassword.setEnabled(true);
        resetAppPassword.setEnabled(true);
    }

    // enables the set app password button and disables change & reset password button
    private void setButtonsDisabledForUsing() {
        setAppPassword.setEnabled(true);
        changeAppPassword.setEnabled(false);
        resetAppPassword.setEnabled(false);
    }
}