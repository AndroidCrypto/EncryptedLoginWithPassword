package de.androidcrypto.encryptedloginwithpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    TextView loginStatus;

    //public boolean isAppPasswordAccepted() {
      //  return appPasswordAccepted;
    //}

    private boolean appPasswordAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginStatus = findViewById(R.id.tvLoginLoginStatus);

        loadPassword();
    }

    //public void loadPasswordPressed(View v) {
    public void loadPassword() {
        ConstraintLayout constraintLayout = findViewById(R.id.loginConstraintLayout);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(constraintLayout.getContext());
        alertDialog.setTitle("Programm Passwort eingeben");
        String message = "\nBitte geben Sie ein mindestens\n8-stelliges Passwort ein und drücken\nSie auf LADEN, um alle\nProgrammfunktionen nutzen\nzu können.";
        alertDialog.setMessage(message);
        final EditText oldPassphrase = new EditText(constraintLayout.getContext());
        oldPassphrase.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_rect_shape, null));
        //oldPassphrase.setBackground(getResources().getDrawable(R.drawable.round_rect_shape));
        oldPassphrase.setHint("  Passwort");
        oldPassphrase.setPadding(50, 20, 50, 20);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(36, 36, 36, 36);
        oldPassphrase.setLayoutParams(lp1);
        RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(rlParams);
        container.addView(oldPassphrase);
        alertDialog.setView(container);
        alertDialog.setPositiveButton("laden", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get password as char array
                int oldPassphraseLength = oldPassphrase.length();
                char[] oldPassword = new char[oldPassphraseLength];
                oldPassphrase.getText().getChars(0, oldPassphraseLength, oldPassword, 0);
                // test on password length
                if (oldPassphraseLength < SettingsActivity.MINIMAL_PASSWORD_LENGTH) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, "Das Passwort ist zu kurz", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(constraintLayout.getContext(), R.color.red));
                    snackbar.show();
                    return;
                }
                // verify the entered hashed password with the stored one
                boolean result = EncryptedSharedPreferencesUtils.verifyAppPassword(oldPassword);
                if (result) {
                    appPasswordAccepted = true;
                    loginStatus.setText("*** the verification was SUCCESSFUL");
                    System.out.println("*** the verification was TRUE");
                    Snackbar snackbar = Snackbar.make(constraintLayout, "The App password was verified", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(constraintLayout.getContext(), R.color.green));
                    snackbar.show();
                    return;
                } else {
                    appPasswordAccepted = false;
                    loginStatus.setText("*** the verification was NOT SUCCESSFUL");
                    System.out.println("*** the verification was FALSE");
                    Snackbar snackbar = Snackbar.make(constraintLayout, "Something went wrong, sorry.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(constraintLayout.getContext(), R.color.red));
                    snackbar.show();
                    //return;
                }
            }
        });
        alertDialog.setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar snackbar = Snackbar.make(constraintLayout, "Passwort laden abgebrochen", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(constraintLayout.getContext(), R.color.red));
                snackbar.show();
            }
        });
        alertDialog.show();
    }
}