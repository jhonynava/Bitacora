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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class Finalizar_recorrido extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextInputLayout edt_kilometraje_final, edt_tanque_final;
    private EditText edt_kilometraje_final2, edt_tanque_final2;
    private String texto_kilometraje_final, texto_tanque_final;
    private ImageView icono_kilometraje, icono_tanque;
    private Button btn_finalizar_recorrido;
    private TableRow row_fecha, row_no_economico, row_kilometraje_inicial, row_kilometraje_final, row_tanque_inicial,
            row_tanque_final, row_recorrido, row_nombre;
    private TextView titulo, subtitulo, textView_fecha, textView_no_economico, textView_kilo_inicial, textView_kilo_final,
            textView_tanque_inicial, textView_tanque_final, textView_recorrido, textView_nombre;
    private String texto_fecha, texto_kilometraje_inicial, texto_tanque_inicial, texto_suministro,
            texto_diferencia, texto_importe, texto_recorrido, texto_nombre, texto_firma, texto_observaciones,
            texto_no_economico, texto_curp;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_recorrido);

        edt_kilometraje_final = findViewById(R.id.edt_kilometraje_final);
        edt_tanque_final = findViewById(R.id.edt_tanque_final);

        edt_kilometraje_final2 = findViewById(R.id.edt_kilometraje_final2);
        edt_tanque_final2 = findViewById(R.id.edt_tanque_final2);

        icono_kilometraje = findViewById(R.id.icono_kilometraje_final);
        icono_tanque = findViewById(R.id.icono_tanque_final);

        btn_finalizar_recorrido = findViewById(R.id.btn_finalizar_recorrido);

        //Preferencias para obtener acceso
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Obtención de valores guardados en SharedPreferences
        texto_fecha = sharedPreferences.getString("fecha", null);
        texto_kilometraje_inicial = sharedPreferences.getString("kilometraje_inicial", null);
        texto_tanque_inicial = sharedPreferences.getString("tanque_inicial", null);
        texto_recorrido = sharedPreferences.getString("lugar_recorrido", null);
        texto_nombre = sharedPreferences.getString("nombre", null) + " " + sharedPreferences.getString("apaterno", null) + " " + sharedPreferences.getString("amaterno", null);
        texto_no_economico = sharedPreferences.getString("no_economico", null);
        texto_curp = sharedPreferences.getString("curp", null);


        edt_kilometraje_final2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable d = icono_kilometraje.getDrawable();
                    d = DrawableCompat.wrap(d);
                    DrawableCompat.setTint(d,
                            ContextCompat.getColor(Finalizar_recorrido.this, R.color.colorPrimaryDark));

                    Drawable e = icono_tanque.getDrawable();
                    e = DrawableCompat.wrap(e);
                    DrawableCompat.setTint(e,
                            ContextCompat.getColor(Finalizar_recorrido.this, R.color.gris));
                }
            }
        });

        edt_tanque_final2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable d = icono_kilometraje.getDrawable();
                    d = DrawableCompat.wrap(d);
                    DrawableCompat.setTint(d,
                            ContextCompat.getColor(Finalizar_recorrido.this, R.color.gris));

                    Drawable e = icono_tanque.getDrawable();
                    e = DrawableCompat.wrap(e);
                    DrawableCompat.setTint(e,
                            ContextCompat.getColor(Finalizar_recorrido.this, R.color.colorPrimaryDark));
                }
            }
        });

        btn_finalizar_recorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto_kilometraje_final = edt_kilometraje_final.getEditText().getText().toString();
                texto_tanque_final = edt_tanque_final.getEditText().getText().toString();

                Drawable d = icono_kilometraje.getDrawable();
                d = DrawableCompat.wrap(d);
                DrawableCompat.setTint(d,
                        ContextCompat.getColor(Finalizar_recorrido.this, R.color.gris));

                Drawable e = icono_tanque.getDrawable();
                e = DrawableCompat.wrap(e);
                DrawableCompat.setTint(e,
                        ContextCompat.getColor(Finalizar_recorrido.this, R.color.gris));

                boolean cancel = false;

                if (TextUtils.isEmpty(texto_kilometraje_final)) {
                    edt_kilometraje_final.setError("El tanque final es requerido");
                    edt_kilometraje_final.requestFocus();
                    cancel = true;
                } else {
                    edt_kilometraje_final.setError(null);
                }
                if (TextUtils.isEmpty(texto_tanque_final)) {
                    edt_tanque_final.setError("El recorrido final es requerido");
                    edt_tanque_final.requestFocus();
                    cancel = true;
                } else {
                    edt_tanque_final.setError(null);
                }
                if (!cancel) {
                    if (isOnlineNet()) {
                        cuadroDialogoRevisar(Finalizar_recorrido.this);
                    } else {
                        cuadroDialogoRevisar(Finalizar_recorrido.this);
                    }
                }
            }
        });
    }

    private void cuadroDialogoRevisar(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View dialogLayout = layoutInflater.inflate(R.layout.cuadro_dialogo_revisar_recorrido, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        row_fecha = dialogLayout.findViewById(R.id.row_fecha);
        row_no_economico = dialogLayout.findViewById(R.id.row_no_economico);
        row_kilometraje_inicial = dialogLayout.findViewById(R.id.row_kilometraje_inicial);
        row_kilometraje_final = dialogLayout.findViewById(R.id.row_kilometraje_final);
        row_tanque_inicial = dialogLayout.findViewById(R.id.row_tanque_inicial);
        row_tanque_final = dialogLayout.findViewById(R.id.row_tanque_final);
        row_recorrido = dialogLayout.findViewById(R.id.row_recorrido);
        row_nombre = dialogLayout.findViewById(R.id.row_nombre);

        Button btn_cancelar_cuadro = dialogLayout.findViewById(R.id.btn_cuadrodialog_cancelar);
        Button btn_aceptar_cuadro = dialogLayout.findViewById(R.id.btn_cuadrodialog_aceptar);
        textView_kilo_final = dialogLayout.findViewById(R.id.texto_kilometraje_final);
        textView_tanque_final = dialogLayout.findViewById(R.id.texto_tanque_final);

        textView_kilo_final.setText(texto_kilometraje_final);
        textView_tanque_final.setText(texto_tanque_final);

        row_fecha.setVisibility(View.GONE);
        row_no_economico.setVisibility(View.GONE);
        row_tanque_inicial.setVisibility(View.GONE);
        row_kilometraje_inicial.setVisibility(View.GONE);
        row_recorrido.setVisibility(View.GONE);
        row_nombre.setVisibility(View.GONE);
        row_kilometraje_final.setVisibility(View.VISIBLE);
        row_tanque_final.setVisibility(View.VISIBLE);

        btn_cancelar_cuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });

        btn_aceptar_cuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuadroDialogo_resumenDia(Finalizar_recorrido.this);
                show.dismiss();
            }
        });
    }

    private void cuadroDialogo_resumenDia(Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View dialogLayout = layoutInflater.inflate(R.layout.cuadro_dialogo_revisar_recorrido, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        TextView titulo = dialogLayout.findViewById(R.id.titulo_cuadro_dialogo_infomacion);
        TextView subtitulo = dialogLayout.findViewById(R.id.subtitulo_cuadro_dialogo_informacion);

        titulo.setText("Résumen del recorrido");
        subtitulo.setVisibility(View.GONE);

        row_fecha = dialogLayout.findViewById(R.id.row_fecha);
        row_kilometraje_inicial = dialogLayout.findViewById(R.id.row_kilometraje_inicial);
        row_kilometraje_final = dialogLayout.findViewById(R.id.row_kilometraje_final);
        row_tanque_inicial = dialogLayout.findViewById(R.id.row_tanque_inicial);
        row_tanque_final = dialogLayout.findViewById(R.id.row_tanque_final);
        row_recorrido = dialogLayout.findViewById(R.id.row_recorrido);
        row_nombre = dialogLayout.findViewById(R.id.row_nombre);

        Button btn_cancelar_cuadro = dialogLayout.findViewById(R.id.btn_cuadrodialog_cancelar);
        Button btn_aceptar_cuadro = dialogLayout.findViewById(R.id.btn_cuadrodialog_aceptar);

        textView_fecha = dialogLayout.findViewById(R.id.texto_fecha);
        textView_kilo_final = dialogLayout.findViewById(R.id.texto_kilometraje_final);
        textView_kilo_inicial = dialogLayout.findViewById(R.id.texto_kilometraje_inicial);
        textView_tanque_inicial = dialogLayout.findViewById(R.id.texto_tanque_inicial);
        textView_tanque_final = dialogLayout.findViewById(R.id.texto_tanque_final);
        textView_recorrido = dialogLayout.findViewById(R.id.texto_recorrido);
        textView_nombre = dialogLayout.findViewById(R.id.texto_nombre);
        textView_no_economico = dialogLayout.findViewById(R.id.texto_no_economico);

        textView_fecha.setText(texto_fecha);
        textView_nombre.setText(texto_nombre);
        textView_no_economico.setText(texto_no_economico);
        textView_recorrido.setText(texto_recorrido);
        textView_tanque_inicial.setText(texto_tanque_inicial);
        textView_kilo_inicial.setText(texto_kilometraje_inicial);
        textView_kilo_final.setText(texto_kilometraje_final);
        textView_tanque_final.setText(texto_tanque_final);

        row_fecha.setVisibility(View.VISIBLE);
        row_no_economico.setVisibility(View.VISIBLE);
        row_tanque_inicial.setVisibility(View.VISIBLE);
        row_kilometraje_inicial.setVisibility(View.VISIBLE);
        row_recorrido.setVisibility(View.VISIBLE);
        row_nombre.setVisibility(View.VISIBLE);
        row_kilometraje_final.setVisibility(View.VISIBLE);
        row_tanque_final.setVisibility(View.VISIBLE);

        btn_cancelar_cuadro.setVisibility(View.GONE);

        btn_aceptar_cuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebservice();
                show.dismiss();
            }
        });
    }

    private void cargarWebservice() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String url = "http://sp.saludtlax.gob.mx:8084/Service_bitacora/webresources/vehiculos/add?"
                + "ID_BITACORA=0"
                + "&FECHA=" + texto_fecha
                + "&KILOMETRAJE_FINAL=" + texto_kilometraje_final
                + "&IMPORTE"
                + "&TANQUE_INICIAL=" + texto_tanque_inicial.replace(" ", "%")
                + "&SUMINISTRO"
                + "&TANQUE_FINAL=" + texto_tanque_final.replace(" ", "%")
                + "&DIFERENCIA="
                + "&NOMBRE_USUARIO=" + texto_nombre.replace(" ", "%")
                + "&FIRMA"
                + "&RECORRIDO" + texto_recorrido.replace(" ", "%")
                + "&OBSERVACIONES"
                + "&NO_ECONOMICO=" + texto_no_economico
                + "&CURP=" + texto_curp
                + "&KILOMETRAJE_INICIAL=" + texto_tanque_inicial;

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarProgreso(false);
                if (response.equalsIgnoreCase("Agregado correctamente")) {
                    editor.putBoolean("recorrido", false);
                    editor.commit();
                    Intent intent = new Intent(Finalizar_recorrido.this, MenuPrincipal.class);
                    startActivity(intent);
                    Finalizar_recorrido.this.finish();
                } else
                    Toast.makeText(Finalizar_recorrido.this, "Error, los datos no se enviaron", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarProgreso(false);
                error.printStackTrace();
                Log.i("ERROR", error.toString());
            }
        }) {
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

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_popup_lleno:
                edt_tanque_final2.setText("LLENO");
                return true;
            case R.id.id_popup_mas_3:
                edt_tanque_final2.setText("MÁS DE 3/4");
                return true;
            case R.id.id_popup_3:
                edt_tanque_final2.setText("3/4");
                return true;
            case R.id.id_popup_mas_2:
                edt_tanque_final2.setText("MÁS DE 2/4");
                return true;
            case R.id.id_popup_2:
                edt_tanque_final2.setText("2/4");
                return true;
            case R.id.id_popup_mas_1:
                edt_tanque_final2.setText("MÁS DE 1/4");
                return true;
            case R.id.id_popup_1:
                edt_tanque_final2.setText("1/4");
                return true;
            case R.id.id_popup_reserva:
                edt_tanque_final2.setText("RESERVA");
            default:
                return false;
        }
    }
}
