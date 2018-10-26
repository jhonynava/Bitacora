package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.accounts.AuthenticatorException;
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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Recorridos extends AppCompatActivity{

    private EditText edt_fecha, edt_no_economico, edt_kilometraje_inicial, edt_tanque_inicial,
            edt_recorrido, edt_usuario;
    private Button btn_enviar_recorrido;
    private String texto_fecha, texto_no_economico, texto_kilo_inicial, texto_tanque_inicial, texto_recorrido,
            texto_usuario;

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private StringRequest stringRequest;
    private CoordinatorLayout coordinatorLayout;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "recorrido_inicial";
    Boolean disponible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorridos);

        edt_fecha = findViewById(R.id.edt_fecha);
        edt_no_economico = findViewById(R.id.edt_no_economico);
        edt_kilometraje_inicial = findViewById(R.id.edt_kilometraje_inicial);
        edt_tanque_inicial = findViewById(R.id.edt_tanque_inicial);
        edt_recorrido = findViewById(R.id.edt_recorrido);
        edt_usuario = findViewById(R.id.edt_usuario);
        btn_enviar_recorrido = findViewById(R.id.btn_recorrido);
        coordinatorLayout = findViewById(R.id.coordinator_recorrido);

        //Carga de usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btn_enviar_recorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto_fecha = edt_fecha.getText().toString().trim();
                texto_no_economico = edt_no_economico.getText().toString().trim();
                texto_kilo_inicial = edt_kilometraje_inicial.getText().toString().trim();
                texto_tanque_inicial = edt_tanque_inicial.getText().toString().trim();
                texto_recorrido = edt_recorrido.getText().toString().trim();
                texto_usuario = edt_usuario.getText().toString().trim();

                boolean cancel = false;

                cargarWebservice(false);

                /*
                if (TextUtils.isEmpty(texto_fecha)) {
                    edt_fecha.setError("El campo de fecha es requerido");
                    edt_fecha.requestFocus();
                    cancel = true;
                }
                if (TextUtils.isEmpty(texto_no_economico)) {
                    edt_no_economico.setError("El campo de fecha es requerido");
                    edt_no_economico.requestFocus();
                    cancel = true;
                }
                if (TextUtils.isEmpty(texto_kilo_inicial)) {
                    edt_kilometraje_inicial.setError("El campo de fecha es requerido");
                    edt_kilometraje_inicial.requestFocus();
                    cancel = true;
                }
                if (TextUtils.isEmpty(texto_tanque_inicial)) {
                    edt_tanque_inicial.setError("El campo de fecha es requerido");
                    edt_tanque_inicial.requestFocus();
                    cancel = true;
                }
                if (TextUtils.isEmpty(texto_recorrido)) {
                    edt_recorrido.setError("El campo de fecha es requerido");
                    edt_recorrido.requestFocus();
                    cancel = true;
                }
                if (!cancel) {
                    if (isOnlineNet()) {
                        Boolean disponible = false;
                        cargarWebservice(false);
                    } else {
                        cuadroDialogo(Recorridos.this);
                    }
                }*/

            }
        });

    }

    private void cargarWebservice(boolean b) {
        mostrarProgreso(true);

        final String disponibilidad = String.valueOf(b);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String url = "http://sp.saludtlax.gob.mx:8084/Service_bitacora/webresources/vehiculos/updates";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*String usuario = "Jhonattan Romano Nava";
                if (response.trim().equalsIgnoreCase("actualizado")) {
                    editor.putString("fecha", texto_fecha);
                    editor.putString("no_economico", texto_no_economico);
                    editor.putString("kilometraje_inicial", texto_kilo_inicial);
                    editor.putString("tanque_inicial", texto_tanque_inicial);
                    editor.putString("recorrido", texto_recorrido);
                    editor.putString("conductor", usuario);
                } else {
                    mostrarProgreso(false);
                    Snackbar.make(coordinatorLayout, "No se pudo conectar al servidor", Snackbar.LENGTH_LONG).show();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarProgreso(false);
                Snackbar.make(coordinatorLayout, "No se pudo conectar", Snackbar.LENGTH_LONG).show();
                error.printStackTrace();
                Log.i("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("NO_ECONOMICO", "302");
                params.put("DISPONIBILIDAD", "false");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void mostrarProgreso(boolean b) {

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
}
