package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.gob.seguropopulartlax.bitacora_vehicular_repss.entidades.Usuario;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.tools.base64;

public class Login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    Button btn_login;
    EditText edt_user, edt_pass;
    CheckBox saveLoginCheckBox;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";
    Boolean saveLogin;

    RelativeLayout relativeLayout;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    private View view_imagenLogin;
    private View view_cardViewLogin;
    private View view_BotonLogin;
    private View view_Lottie;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        btn_login = findViewById(R.id.btn_login);
        edt_user = findViewById(R.id.edt_user);
        edt_pass = findViewById(R.id.edt_pass);
        relativeLayout = findViewById(R.id.relativelayout);
        saveLoginCheckBox = findViewById(R.id.checkboxLogin);

        //Vistas loading
        view_imagenLogin = findViewById(R.id.view_logo_login);
        view_cardViewLogin = findViewById(R.id.view_card_login);
        view_BotonLogin = findViewById(R.id.view_btn_login);
        view_Lottie = findViewById(R.id.view_lottie);

        //Carga de usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Boolean valor = sharedPreferences.getBoolean("savelogin", false);

        if (valor) {
            saveLoginCheckBox.setChecked(true);
            saveLogin = sharedPreferences.getBoolean("savelogin", true);
            edt_user.setText(sharedPreferences.getString("curp", null));
            edt_pass.setText(sharedPreferences.getString("password", null));

            Intent layout_menu = new Intent(Login.this, MenuPrincipal.class);
            startActivity(layout_menu);
            finish();
        } else {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password = edt_pass.getText().toString().trim();
                    String usuario = edt_user.getText().toString().trim().toUpperCase();
                    View focusView = null;
                    boolean cancel = false;

                    if (TextUtils.isEmpty(password)) {
                        edt_pass.setError("El campo contraseña es requerido");
                        focusView = edt_pass;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(usuario)) {
                        edt_user.setError("El campo usuario es requerido");
                        focusView = edt_user;
                        cancel = true;
                    }
                    if (cancel) {
                        focusView.requestFocus();
                    } else {
                        if (isOnlineNet()) {
                            cargarWebService(usuario, password);
                        } else {
                            cuadroDialogo(Login.this);
                        }
                    }
                }
            });
        }
    }

    private void cargarWebService(String usuario, String contrasenia) {

        mostrarProgreso(true);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String url = "http://sp.saludtlax.gob.mx:8084/Service_bitacora/webresources/user/login?CURP=" +
                usuario + "&PASSWORD=" + base64.encode(contrasenia).trim();


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, Login.this, Login.this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mostrarProgreso(false);
        Snackbar.make(relativeLayout, "No se pudo conectar al servidor", Snackbar.LENGTH_LONG).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        String password = edt_pass.getText().toString().trim();

        Usuario usuario = new Usuario();

        JSONArray jsonArray = response.optJSONArray("usuarios");
        JSONObject jsonObject = null;

        try {
            jsonObject = jsonArray.getJSONObject(0);
            usuario.setCurp(jsonObject.optString("curp"));
            usuario.setPassword(jsonObject.optString("password"));
            usuario.setNombre(jsonObject.optString("nombre"));
            usuario.setApaterno(jsonObject.optString("apaterno"));
            usuario.setAmaterno(jsonObject.optString("amaterno"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (usuario.getCurp().equals(edt_user.getText().toString().trim()) && usuario.getPassword().equals(base64.encode(password).trim())) {
            //Insertar valores al shared
            editor.putBoolean("savelogin", true);
            editor.putString("curp", usuario.getCurp());
            editor.putString("password", usuario.getPassword());
            editor.putString("nombre", usuario.getNombre());
            editor.putString("apaterno", usuario.getApaterno());
            editor.putString("amaterno", usuario.getAmaterno());
            editor.commit();

            mostrarProgreso(false);
            Intent intent = new Intent(Login.this, MenuPrincipal.class);
            startActivity(intent);
            finish();
        } else {
            mostrarProgreso(false);
            Snackbar.make(relativeLayout, "Error en usuario o contraseña", Snackbar.LENGTH_LONG).show();
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

    private void mostrarProgreso(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        view_imagenLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        view_imagenLogin.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view_imagenLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        view_cardViewLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        view_cardViewLogin.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view_cardViewLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        view_BotonLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        view_BotonLogin.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view_BotonLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        view_Lottie.setVisibility(show ? View.VISIBLE : View.GONE);
        view_Lottie.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view_Lottie.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public Boolean isOnlineNet() {

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("¿Estás seguro?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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