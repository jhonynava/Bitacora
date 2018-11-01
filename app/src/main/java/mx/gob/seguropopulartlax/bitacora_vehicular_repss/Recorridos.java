package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Recorridos extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextInputLayout edt_fecha, edt_no_economico, edt_kilometraje_inicial, edt_tanque_inicial,
            edt_recorrido, edt_usuario;
    private Button btn_enviar_recorrido;
    private String texto_fecha, texto_no_economico, texto_kilo_inicial, texto_tanque_inicial, texto_recorrido, texto_usuario;
    private EditText edt_fecha2, edt_no_economico2, edt_kilometraje_inicial2, edt_tanque_inicial2,
            edt_recorrido2, edt_usuario2;
    private Calendar myCalendar = Calendar.getInstance();
    private View view_recorrido, view_lottie;
    private long min;

    private ImageView iconoCalendario, iconoNoEcono, iconoKilInicial, iconoTanqueInicial, iconoRecorrido;

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private StringRequest stringRequest;
    private CoordinatorLayout coordinatorLayout;

    TableRow row_fecha, row_no_economico, row_kilometraje_inicial, row_kilometraje_final, row_tanque_inicial,
            row_tanque_final, row_recorrido, row_nombre;
    TextView titulo, subtitulo, textView_fecha, textView_no_economico, textView_kilo_inicial,
            textView_tanque_inicial, textView_recorrido, textView_nombre;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorridos);

        //Obetener usuario
        String nombre, apaterno, amaterno, nombre_completo;
        final Boolean recorrido;

        //Preferencias para obtener usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nombre = sharedPreferences.getString("nombre", null);
        apaterno = sharedPreferences.getString("apaterno", null);
        amaterno = sharedPreferences.getString("amaterno", null);
        recorrido = sharedPreferences.getBoolean("recorrido", false);
        nombre_completo = nombre + " " + apaterno + " " + amaterno;

        edt_fecha = findViewById(R.id.edt_fecha);
        edt_no_economico = findViewById(R.id.edt_no_economico);
        edt_kilometraje_inicial = findViewById(R.id.edt_kilometraje_inicial);
        edt_tanque_inicial = findViewById(R.id.edt_tanque_inicial);
        edt_recorrido = findViewById(R.id.edt_recorrido);
        edt_usuario = findViewById(R.id.edt_usuario);
        btn_enviar_recorrido = findViewById(R.id.btn_recorrido);
        coordinatorLayout = findViewById(R.id.coordinator_recorrido);

        edt_fecha2 = findViewById(R.id.edt_fecha2);
        edt_no_economico2 = findViewById(R.id.edt_no_economico2);
        edt_kilometraje_inicial2 = findViewById(R.id.edt_kilometraje_inicial2);
        edt_tanque_inicial2 = findViewById(R.id.edt_tanque_inicial2);
        edt_recorrido2 = findViewById(R.id.edt_recorrido2);
        edt_usuario2 = findViewById(R.id.edt_usuario2);

        iconoCalendario = findViewById(R.id.icono_calendario);
        iconoNoEcono = findViewById(R.id.icono_no_economico);
        iconoKilInicial = findViewById(R.id.icono_kilometraje);
        iconoTanqueInicial = findViewById(R.id.icono_tanque_inicial);
        iconoRecorrido = findViewById(R.id.icono_recorrido);

        view_recorrido = findViewById(R.id.view_recorrido);
        view_lottie = findViewById(R.id.view__recorrido_lottie);

        //Datos del no_economico
        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            String valor = parametros.getString("no_economico");
            if (valor != null)
                edt_no_economico2.setText(valor);
        }

        //Asiganr nombre del conductor
        edt_usuario2.setText(nombre_completo);
        edt_usuario2.setTextColor(getResources().getColor(R.color.negro));

        edt_no_economico2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable d = iconoCalendario.getDrawable();
                    d = DrawableCompat.wrap(d);
                    DrawableCompat.setTint(d,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable e = iconoNoEcono.getDrawable();
                    e = DrawableCompat.wrap(e);
                    DrawableCompat.setTint(e,
                            ContextCompat.getColor(Recorridos.this, R.color.colorAccent));

                    Drawable f = iconoKilInicial.getDrawable();
                    f = DrawableCompat.wrap(f);
                    DrawableCompat.setTint(f,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable g = iconoTanqueInicial.getDrawable();
                    g = DrawableCompat.wrap(g);
                    DrawableCompat.setTint(g,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable h = iconoRecorrido.getDrawable();
                    h = DrawableCompat.wrap(h);
                    DrawableCompat.setTint(h,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));
                }
            }
        });

        edt_kilometraje_inicial2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable d = iconoCalendario.getDrawable();
                    d = DrawableCompat.wrap(d);
                    DrawableCompat.setTint(d,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable e = iconoNoEcono.getDrawable();
                    e = DrawableCompat.wrap(e);
                    DrawableCompat.setTint(e,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable f = iconoKilInicial.getDrawable();
                    f = DrawableCompat.wrap(f);
                    DrawableCompat.setTint(f,
                            ContextCompat.getColor(Recorridos.this, R.color.colorAccent));

                    Drawable g = iconoTanqueInicial.getDrawable();
                    g = DrawableCompat.wrap(g);
                    DrawableCompat.setTint(g,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable h = iconoRecorrido.getDrawable();
                    h = DrawableCompat.wrap(h);
                    DrawableCompat.setTint(h,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));
                }
            }
        });

        edt_recorrido2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable d = iconoCalendario.getDrawable();
                    d = DrawableCompat.wrap(d);
                    DrawableCompat.setTint(d,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable e = iconoNoEcono.getDrawable();
                    e = DrawableCompat.wrap(e);
                    DrawableCompat.setTint(e,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable f = iconoKilInicial.getDrawable();
                    f = DrawableCompat.wrap(f);
                    DrawableCompat.setTint(f,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable g = iconoTanqueInicial.getDrawable();
                    g = DrawableCompat.wrap(g);
                    DrawableCompat.setTint(g,
                            ContextCompat.getColor(Recorridos.this, R.color.gris));

                    Drawable h = iconoRecorrido.getDrawable();
                    h = DrawableCompat.wrap(h);
                    DrawableCompat.setTint(h,
                            ContextCompat.getColor(Recorridos.this, R.color.colorAccent));
                }
            }
        });

        edt_fecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                min = myCalendar.getTimeInMillis();
                DatePickerDialog dialog = new DatePickerDialog(Recorridos.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker();
                dialog.show();
            }
        });

        btn_enviar_recorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto_fecha = edt_fecha.getEditText().getText().toString().trim();
                texto_no_economico = edt_no_economico.getEditText().getText().toString().trim();
                texto_kilo_inicial = edt_kilometraje_inicial.getEditText().getText().toString().trim();
                texto_tanque_inicial = edt_tanque_inicial.getEditText().getText().toString().trim();
                texto_recorrido = edt_recorrido.getEditText().getText().toString().trim();
                texto_usuario = edt_usuario.getEditText().getText().toString().trim();

                Drawable d = iconoCalendario.getDrawable();
                d = DrawableCompat.wrap(d);
                DrawableCompat.setTint(d,
                        ContextCompat.getColor(Recorridos.this, R.color.gris));

                Drawable e = iconoNoEcono.getDrawable();
                e = DrawableCompat.wrap(e);
                DrawableCompat.setTint(e,
                        ContextCompat.getColor(Recorridos.this, R.color.gris));

                Drawable f = iconoKilInicial.getDrawable();
                f = DrawableCompat.wrap(f);
                DrawableCompat.setTint(f,
                        ContextCompat.getColor(Recorridos.this, R.color.gris));

                Drawable g = iconoTanqueInicial.getDrawable();
                g = DrawableCompat.wrap(g);
                DrawableCompat.setTint(g,
                        ContextCompat.getColor(Recorridos.this, R.color.gris));

                Drawable h = iconoRecorrido.getDrawable();
                h = DrawableCompat.wrap(h);
                DrawableCompat.setTint(h,
                        ContextCompat.getColor(Recorridos.this, R.color.gris));

                boolean cancel = false;

                if (TextUtils.isEmpty(texto_fecha)) {
                    edt_fecha.setError("La fecha es requerida");
                    edt_fecha.requestFocus();
                    cancel = true;
                } else {
                    edt_fecha.setError(null);
                }
                if (TextUtils.isEmpty(texto_no_economico)) {
                    edt_no_economico.setError("El número economico es requerido");
                    edt_no_economico.requestFocus();
                    cancel = true;
                } else {
                    edt_no_economico.setError(null);
                }
                if (TextUtils.isEmpty(texto_kilo_inicial)) {
                    edt_kilometraje_inicial.setError("El kilometraje inicial es requerido");
                    edt_kilometraje_inicial.requestFocus();
                    cancel = true;
                } else {
                    edt_kilometraje_inicial.setError(null);
                }
                if (TextUtils.isEmpty(texto_tanque_inicial)) {
                    edt_tanque_inicial.setError("El tanque inicial es requerido");
                    edt_tanque_inicial.requestFocus();
                    cancel = true;
                } else {
                    edt_tanque_inicial.setError(null);
                }
                if (TextUtils.isEmpty(texto_recorrido)) {
                    edt_recorrido.setError("El recorrido es requerido");
                    edt_recorrido.requestFocus();
                    cancel = true;
                } else {
                    edt_recorrido.setError(null);
                }
                if (!cancel) {
                    if (isOnlineNet()) {
                        if (!recorrido)
                            cuadroDialogoResumen(Recorridos.this);
                        else
                            Toast.makeText(Recorridos.this, "Tienes un recorrido actualmente", Toast.LENGTH_SHORT).show();
                    } else {
                        cuadroDialogo(Recorridos.this);
                    }
                }
            }
        });
    }

    private void cargarWebservice(boolean b, final String texto_no_economico) {
        mostrarProgreso(true);

        final String disponibilidad = String.valueOf(b);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String url = "http://sp.saludtlax.gob.mx:8084/Service_bitacora/webresources/vehiculos/update?NO_ECONOMICO=" + texto_no_economico + "&DISPONIBILIDAD=" + disponibilidad;

        stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarProgreso(false);
                if (response.equalsIgnoreCase("actualizado")) {
                    Intent intent = new Intent(Recorridos.this, MenuPrincipal.class);
                    editor.putBoolean("recorrido", true);
                    editor.putString("fecha", texto_fecha);
                    editor.putString("no_economico", texto_no_economico);
                    editor.putString("kilometraje_inicial", texto_kilo_inicial);
                    editor.putString("tanque_inicial", texto_tanque_inicial);
                    editor.putString("lugar_recorrido", texto_recorrido);
                    editor.commit();
                    startActivity(intent);
                    Recorridos.this.finish();
                } else
                    Toast.makeText(Recorridos.this, "Error, los datos no se enviaron", Toast.LENGTH_LONG).show();

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void mostrarProgreso(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        view_recorrido.setVisibility(show ? View.GONE : View.VISIBLE);
        view_recorrido.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view_recorrido.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        view_lottie.setVisibility(show ? View.VISIBLE : View.GONE);
        view_lottie.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view_lottie.setVisibility(show ? View.VISIBLE : View.GONE);
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

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "YYYY-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.forLanguageTag("ES"));

        edt_fecha2.setText(sdf.format(myCalendar.getTime()));
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
                edt_tanque_inicial2.setText("LLENO");
                return true;
            case R.id.id_popup_mas_3:
                edt_tanque_inicial2.setText("MÁS DE 3/4");
                return true;
            case R.id.id_popup_3:
                edt_tanque_inicial2.setText("3/4");
                return true;
            case R.id.id_popup_mas_2:
                edt_tanque_inicial2.setText("MÁS DE 2/4");
                return true;
            case R.id.id_popup_2:
                edt_tanque_inicial2.setText("2/4");
                return true;
            case R.id.id_popup_mas_1:
                edt_tanque_inicial2.setText("MÁS DE 1/4");
                return true;
            case R.id.id_popup_1:
                edt_tanque_inicial2.setText("1/4");
                return true;
            case R.id.id_popup_reserva:
                edt_tanque_inicial2.setText("RESERVA");
            default:
                return false;
        }
    }

    private void cuadroDialogoResumen(Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View dialogLayout = layoutInflater.inflate(R.layout.cuadro_dialogo_revisar_recorrido, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        row_fecha = dialogLayout.findViewById(R.id.row_fecha);
        row_kilometraje_inicial = dialogLayout.findViewById(R.id.row_kilometraje_inicial);
        row_kilometraje_final = dialogLayout.findViewById(R.id.row_kilometraje_final);
        row_tanque_inicial = dialogLayout.findViewById(R.id.row_tanque_inicial);
        row_tanque_final = dialogLayout.findViewById(R.id.row_tanque_final);
        row_recorrido = dialogLayout.findViewById(R.id.row_recorrido);
        row_nombre = dialogLayout.findViewById(R.id.row_nombre);
        row_no_economico = dialogLayout.findViewById(R.id.row_no_economico);

        Button btn_cancelar_cuadro = dialogLayout.findViewById(R.id.btn_cuadrodialog_cancelar);
        Button btn_aceptar_cuadro = dialogLayout.findViewById(R.id.btn_cuadrodialog_aceptar);

        textView_fecha = dialogLayout.findViewById(R.id.texto_fecha);
        textView_kilo_inicial = dialogLayout.findViewById(R.id.texto_kilometraje_inicial);
        textView_tanque_inicial = dialogLayout.findViewById(R.id.texto_tanque_inicial);
        textView_recorrido = dialogLayout.findViewById(R.id.texto_recorrido);
        textView_nombre = dialogLayout.findViewById(R.id.texto_nombre);
        textView_no_economico = dialogLayout.findViewById(R.id.texto_no_economico);


        textView_fecha.setText(texto_fecha);
        textView_no_economico.setText(texto_no_economico);
        textView_kilo_inicial.setText(texto_kilo_inicial);
        textView_tanque_inicial.setText(texto_tanque_inicial);
        textView_recorrido.setText(texto_recorrido);
        textView_nombre.setText(texto_usuario);

        btn_cancelar_cuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });

        btn_aceptar_cuadro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebservice(false, texto_no_economico);
                show.dismiss();
            }
        });


    }
}
