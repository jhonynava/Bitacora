package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button btn_login;
    EditText edt_user, edt_pass;
    CheckBox saveLoginCheckBox;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";
    Boolean saveLogin;
    private String nombre_usuario;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        edt_user = findViewById(R.id.edt_user);
        edt_pass = findViewById(R.id.edt_pass);
        relativeLayout = findViewById(R.id.relativelayout);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();


        Boolean valor = sharedPreferences.getBoolean("savelogin", false);
        if (valor){
            saveLoginCheckBox.setChecked(true);

            saveLogin = sharedPreferences.getBoolean("savelogin", true);
            edt_user.setText(sharedPreferences.getString("username",null));
            edt_pass.setText(sharedPreferences.getString("password",null));
            nombre_usuario = sharedPreferences.getString("nombre",null);

            Snackbar snackbar = Snackbar.make(relativeLayout, "Bienvenido" + nombre_usuario,Snackbar.LENGTH_LONG);
            snackbar.show();
            Intent intent = new Intent(Login.this, MenuPrincipal.class);
            startActivity(intent);
        } else {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = Login.this.getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    iniciarSesion();
                    Intent intent = new Intent(Login.this, MenuPrincipal.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void iniciarSesion() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("¿Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Login.this.finish();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}