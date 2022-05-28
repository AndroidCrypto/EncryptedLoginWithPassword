package de.androidcrypto.encryptedloginwithpassword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
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

        // enable buttons on status of app password storage
        boolean appPasswordAvailable = EncryptedSharedPreferencesUtils.getAppPasswordStatus();
        if (appPasswordAvailable) { setButtonsEnabledForUsing(); } else
        { setButtonsDisabledForUsing(); }

        setAppPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePassword(view);
            }
        });

        resetAppPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword(view);
            }
        });

        changeAppPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { changePassword(view); }
        });

    }

    private void savePassword(View view) {
        Context context = view.getContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Neues Programm Passwort vergeben");
        String message = "\nBitte geben Sie ein mindestens\n8-stelliges Passwort ein und drücken\nSie auf SPEICHERN, um alle\nProgrammfunktionen nutzen\nzu können.";
        alertDialog.setMessage(message);
        final EditText oldPassphrase = new EditText(context);
        oldPassphrase.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.round_rect_shape, null));
        //oldPassphrase.setBackground(getResources().getDrawable(R.drawable.round_rect_shape));
        oldPassphrase.setHint("  neues Passwort eingeben");
        oldPassphrase.setPadding(50, 20, 50, 20);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(36, 36, 36, 36);
        oldPassphrase.setLayoutParams(lp1);
        RelativeLayout container = new RelativeLayout(context);
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
                    Snackbar snackbar = Snackbar.make(view, "Das Passwort ist zu kurz", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                    snackbar.show();
                    return;
                }

                // storing the data
                boolean result = EncryptedSharedPreferencesUtils.saveNewAppPassword(oldPassword);
                if (result) {
                    Snackbar snackbar = Snackbar.make(view, "The App password was stored.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green));
                    snackbar.show();
                    setButtonsEnabledForUsing();
                    return;
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Something went wrong, sorry.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                    snackbar.show();
                    setButtonsDisabledForUsing();
                    return;
                }
            }
        });
        alertDialog.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar snackbar = Snackbar.make(view, "Passwort speichern abgebrochen", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                snackbar.show();
            }
        });
        alertDialog.show();
    }

    private void changePassword(View view) {
        Context context = view.getContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Passwort ändern");
        String message = "\nGeben Sie zuerst das alte Passwort,\ndann das neue Passwort ein\nund drücken Sie auf ÄNDERN.";
        alertDialog.setMessage(message);

        final EditText oldPassphrase = new EditText(context);
        oldPassphrase.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_rect_shape, null));
        oldPassphrase.setHint("  altes Passwort");
        oldPassphrase.setPadding(50, 20, 50, 20);
        final EditText newPassphrase = new EditText(context);
        newPassphrase.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_rect_shape, null));
        newPassphrase.setHint("  neues Passwort");
        newPassphrase.setPadding(50, 20, 50, 20);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(36, 36, 36, 36);
        oldPassphrase.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(36, 200, 36, 36);
        newPassphrase.setLayoutParams(lp2);
        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(oldPassphrase);
        container.addView(newPassphrase);
        //now set view to dialog
        alertDialog.setView(container);
        alertDialog.setPositiveButton("ändern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int oldPassphraseLength = oldPassphrase.length();
                char[] oldPassword = new char[oldPassphraseLength];
                oldPassphrase.getText().getChars(0, oldPassphraseLength, oldPassword, 0);
                int newPassphraseLength = newPassphrase.length();
                char[] newPassword = new char[newPassphraseLength];
                newPassphrase.getText().getChars(0, newPassphraseLength, newPassword, 0);
                // check for password length => MINIMAL_PASSWORD_LENGTH
                if (newPassphraseLength < MINIMAL_PASSWORD_LENGTH) {
                    Snackbar snackbar = Snackbar.make(view, "Das neue Passwort ist zu kurz", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                    snackbar.show();
                    return;
                }
                boolean result = false;
                result = EncryptedSharedPreferencesUtils.changeAppPassword(oldPassword, newPassword);
                if (result) {
                    Snackbar snackbar = Snackbar.make(view, "Das Passwort wurde geändert", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green));
                    snackbar.show();
                    return;
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Passwort ändern funktioniert nicht", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                    snackbar.show();
                }
            }
        });
        alertDialog.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar snackbar = Snackbar.make(view, "Passwort ändern abgebrochen", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                snackbar.show();
            }
        });
        alertDialog.show();
    }

    private void resetPassword(View view) {
        Context context = view.getContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Programm Passwort zurücksetzen");
        String message = "\nMit dem Passwort-Reset\nwerden alle Daten gelöscht.\nDrücken Sie auf RESET, um \ndas Passwort zurückzusetzen.";
        alertDialog.setMessage(message);
        RelativeLayout container = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        alertDialog.setView(container);
        alertDialog.setPositiveButton("reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EncryptedSharedPreferencesUtils.resetAppPassword();
                setButtonsDisabledForUsing();
                Snackbar snackbar = Snackbar.make(view, "Die App wurde zurückgesetzt und alle Daten gelöscht.", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green));
                snackbar.show();
            }
        });
        alertDialog.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar snackbar = Snackbar.make(view, "Es wurde kein Reset durchgeführt", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.orange));
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