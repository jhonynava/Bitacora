package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Recarga_combustible extends AppCompatActivity {


    private TextInputLayout iedt_tipo_combustible, iedt_kilometraje_carga, iedt_suministro_gasolina, iedt_precio_gasolina, iedt_importe;
    private EditText edt_tipo_combustible, edt_kilometraje_carga, edt_suministro_gasolina, edt_precio_gasolina, edt_importe;
    private String texto_tipo_combustible, texto_kiometra_carga, texto_suministro_gasolina, texto_precio_gasolina, texto_importe;
    private ImageView icono_tipo_combustible, icono_kilometraje_carga, icono_suministro_gas, icono_precio_gasolina, icono_importe;
    private Drawable d_tipoCombustible, d_kilometrajeCarga, d_suministroGas, d_precioGasolina, d_importe;
    private Button btn_calcular_importe;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";
    private Boolean recarga_gasolina = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga_combustible);

        //Preferencias de sharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        iedt_tipo_combustible = findViewById(R.id.iedt_tipo_combustible);
        iedt_kilometraje_carga = findViewById(R.id.iedt_kilometraje_carga);
        iedt_suministro_gasolina = findViewById(R.id.iedt_suministro_gasolina);
        iedt_precio_gasolina = findViewById(R.id.iedt_precio_gasolina);
        iedt_importe = findViewById(R.id.iedt_importe);

        edt_tipo_combustible = findViewById(R.id.edt_tipo_combustible);
        edt_kilometraje_carga = findViewById(R.id.edt_kilometraje_carga);
        edt_suministro_gasolina = findViewById(R.id.edt_suministro_gasolina);
        edt_precio_gasolina = findViewById(R.id.edt_precio_gasolina);
        edt_importe = findViewById(R.id.edt_importe);

        icono_tipo_combustible = findViewById(R.id.icono_tipo_combustible);
        icono_kilometraje_carga = findViewById(R.id.icono_kilometraje_carga);
        icono_suministro_gas = findViewById(R.id.icono_suministro);
        icono_precio_gasolina = findViewById(R.id.icono_precio_gasolina);
        icono_importe = findViewById(R.id.icono_importe);

        btn_calcular_importe = findViewById(R.id.btn_calcular_importe);

        d_tipoCombustible = icono_tipo_combustible.getDrawable();
        d_kilometrajeCarga = icono_kilometraje_carga.getDrawable();
        d_suministroGas = icono_suministro_gas.getDrawable();
        d_precioGasolina = icono_precio_gasolina.getDrawable();
        d_importe = icono_importe.getDrawable();


        edt_tipo_combustible.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    d_tipoCombustible = DrawableCompat.wrap(d_tipoCombustible);
                    DrawableCompat.setTint(d_tipoCombustible,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.naranja_dark));

                    d_kilometrajeCarga = DrawableCompat.wrap(d_kilometrajeCarga);
                    DrawableCompat.setTint(d_kilometrajeCarga,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_suministroGas = DrawableCompat.wrap(d_suministroGas);
                    DrawableCompat.setTint(d_suministroGas,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_precioGasolina = DrawableCompat.wrap(d_precioGasolina);
                    DrawableCompat.setTint(d_precioGasolina,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_importe = DrawableCompat.wrap(d_importe);
                    DrawableCompat.setTint(d_importe,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));
                }
            }
        });

        edt_kilometraje_carga.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    d_tipoCombustible = DrawableCompat.wrap(d_tipoCombustible);
                    DrawableCompat.setTint(d_tipoCombustible,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_kilometrajeCarga = DrawableCompat.wrap(d_kilometrajeCarga);
                    DrawableCompat.setTint(d_kilometrajeCarga,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.naranja_dark));

                    d_suministroGas = DrawableCompat.wrap(d_suministroGas);
                    DrawableCompat.setTint(d_suministroGas,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_precioGasolina = DrawableCompat.wrap(d_precioGasolina);
                    DrawableCompat.setTint(d_precioGasolina,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_importe = DrawableCompat.wrap(d_importe);
                    DrawableCompat.setTint(d_importe,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));
                }
            }
        });

        edt_suministro_gasolina.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    d_tipoCombustible = DrawableCompat.wrap(d_tipoCombustible);
                    DrawableCompat.setTint(d_tipoCombustible,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_kilometrajeCarga = DrawableCompat.wrap(d_kilometrajeCarga);
                    DrawableCompat.setTint(d_kilometrajeCarga,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_suministroGas = DrawableCompat.wrap(d_suministroGas);
                    DrawableCompat.setTint(d_suministroGas,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.naranja_dark));

                    d_precioGasolina = DrawableCompat.wrap(d_precioGasolina);
                    DrawableCompat.setTint(d_precioGasolina,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_importe = DrawableCompat.wrap(d_importe);
                    DrawableCompat.setTint(d_importe,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));
                }
            }
        });

        edt_precio_gasolina.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    d_tipoCombustible = DrawableCompat.wrap(d_tipoCombustible);
                    DrawableCompat.setTint(d_tipoCombustible,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_kilometrajeCarga = DrawableCompat.wrap(d_kilometrajeCarga);
                    DrawableCompat.setTint(d_kilometrajeCarga,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_suministroGas = DrawableCompat.wrap(d_suministroGas);
                    DrawableCompat.setTint(d_suministroGas,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                    d_precioGasolina = DrawableCompat.wrap(d_precioGasolina);
                    DrawableCompat.setTint(d_precioGasolina,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.naranja_dark));

                    d_importe = DrawableCompat.wrap(d_importe);
                    DrawableCompat.setTint(d_importe,
                            ContextCompat.getColor(Recarga_combustible.this, R.color.gris));
                }
            }
        });

        edt_importe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                d_tipoCombustible = DrawableCompat.wrap(d_tipoCombustible);
                DrawableCompat.setTint(d_tipoCombustible,
                        ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                d_kilometrajeCarga = DrawableCompat.wrap(d_kilometrajeCarga);
                DrawableCompat.setTint(d_kilometrajeCarga,
                        ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                d_suministroGas = DrawableCompat.wrap(d_suministroGas);
                DrawableCompat.setTint(d_suministroGas,
                        ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                d_precioGasolina = DrawableCompat.wrap(d_precioGasolina);
                DrawableCompat.setTint(d_precioGasolina,
                        ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

                d_importe = DrawableCompat.wrap(d_importe);
                DrawableCompat.setTint(d_importe,
                        ContextCompat.getColor(Recarga_combustible.this, R.color.naranja_dark));
            }
        });

        btn_calcular_importe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto_suministro_gasolina = edt_suministro_gasolina.getText().toString().trim();
                texto_precio_gasolina = edt_precio_gasolina.getText().toString().trim();
                Boolean cancel = false;
                if (TextUtils.isEmpty(texto_suministro_gasolina)) {
                    iedt_suministro_gasolina.setError("El suministro en litros es requerido.");
                    iedt_suministro_gasolina.requestFocus();
                    cancel = true;
                } else {
                    iedt_suministro_gasolina.setError(null);
                }
                if (TextUtils.isEmpty(texto_precio_gasolina)) {
                    iedt_precio_gasolina.setError("El precio del combustible es requerido.");
                    iedt_precio_gasolina.requestFocus();
                    cancel = true;
                } else {
                    iedt_precio_gasolina.setError(null);
                }
                if (!cancel) {
                    edt_importe.setText(String.valueOf(Double.valueOf(texto_suministro_gasolina) * Double.valueOf(texto_precio_gasolina)));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_terminar_accion, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_terminar_accion:
                accion_btn_terminar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void accion_btn_terminar() {
        //Ocultar teclado
        View view = Recarga_combustible.this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        //Cambiar color a iconos
        d_tipoCombustible = DrawableCompat.wrap(d_tipoCombustible);
        DrawableCompat.setTint(d_tipoCombustible,
                ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

        d_kilometrajeCarga = DrawableCompat.wrap(d_kilometrajeCarga);
        DrawableCompat.setTint(d_kilometrajeCarga,
                ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

        d_suministroGas = DrawableCompat.wrap(d_suministroGas);
        DrawableCompat.setTint(d_suministroGas,
                ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

        d_precioGasolina = DrawableCompat.wrap(d_precioGasolina);
        DrawableCompat.setTint(d_precioGasolina,
                ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

        d_importe = DrawableCompat.wrap(d_importe);
        DrawableCompat.setTint(d_importe,
                ContextCompat.getColor(Recarga_combustible.this, R.color.gris));

        //Obtener texto de los campos
        texto_tipo_combustible = "Gasolina";
        texto_kiometra_carga = edt_kilometraje_carga.getText().toString().trim();
        texto_suministro_gasolina = edt_suministro_gasolina.getText().toString().trim();
        texto_precio_gasolina = edt_precio_gasolina.getText().toString().trim();

        //Verificar llenado de campos
        Boolean cancel = false;
        if (TextUtils.isEmpty(texto_kiometra_carga)) {
            iedt_kilometraje_carga.setError("El kilometraje cuando se realiza la carga es requerido.");
            iedt_kilometraje_carga.requestFocus();
            cancel = true;
        } else {
            iedt_kilometraje_carga.setError(null);
        }
        if (TextUtils.isEmpty(texto_suministro_gasolina)) {
            iedt_suministro_gasolina.setError("El suministro en litros es requerido.");
            iedt_suministro_gasolina.requestFocus();
            cancel = true;
        } else {
            iedt_suministro_gasolina.setError(null);
        }
        if (TextUtils.isEmpty(texto_precio_gasolina)) {
            iedt_precio_gasolina.setError("El precio del combustible es requerido.");
            iedt_precio_gasolina.requestFocus();
            cancel = true;
        } else {
            iedt_precio_gasolina.setError(null);
        }
        if (!cancel) {
            if (isOnlineNet()) {
                edt_importe.setText(String.valueOf(Double.valueOf(texto_suministro_gasolina) * Double.valueOf(texto_precio_gasolina)));
                editor.putBoolean("recarga_gasolina", true);
                editor.commit();
                Intent intent = new Intent(Recarga_combustible.this, MenuPrincipal.class);
                startActivity(intent);
            } else {
                cuadroDialogo(Recarga_combustible.this);
            }
        }
    }

    private void cuadroDialogo(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View dialogLayout = layoutInflater.inflate(R.layout.cuadro_dialogo_internet, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        Button btn_irConfiguraciones = dialogLayout.findViewById(R.id.btn_ir_configuraciones);
        Button btn_aceptar = dialogLayout.findViewById(R.id.btn_aceptar);

        btn_irConfiguraciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                show.dismiss();
            }
        });

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }

    private boolean isOnlineNet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info_wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo info_datos = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (info_wifi != null && info_wifi.isConnected()) {
            return true;
        } else if (info_datos != null && info_datos.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
