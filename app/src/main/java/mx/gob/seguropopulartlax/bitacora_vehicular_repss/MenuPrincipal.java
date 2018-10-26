package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.gob.seguropopulartlax.bitacora_vehicular_repss.adaptadores.VehiculoAdaptador;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.entidades.Vehiculo;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.tools.base64;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";
    private RecyclerView recyclerVehiculo;
    private ArrayList<Vehiculo> listaVehiculos;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ConstraintLayout constraintLayout;
    private View view_lottie, view_recyclerAutos;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obetener usuario
        String nombre, apaterno, amaterno;
        TextView textView_nav;

        //Preferencias para obtener usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nombre = sharedPreferences.getString("nombre", null);
        apaterno = sharedPreferences.getString("apaterno", null);
        amaterno = sharedPreferences.getString("amaterno", null);

        setContentView(R.layout.activity_menu_principal);

        //Inicia el recyclerview
        listaVehiculos = new ArrayList<>();

        recyclerVehiculo = findViewById(R.id.rv_automoviles);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MenuPrincipal.this,2);
        recyclerVehiculo.setLayoutManager(gridLayoutManager);
        requestQueue = Volley.newRequestQueue(this);
        cargarWerbService();


        //Inicia el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Vistas
        constraintLayout = findViewById(R.id.id_constraintUsuario);

        //Progress Dialog
        progressDialog = new ProgressDialog(MenuPrincipal.this);
        progressDialog.show();
        progressDialog.setCancelable(true);
        progressDialog.setContentView(R.layout.cargando);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        textView_nav = view.findViewById(R.id.nav_tvUsuario);
        textView_nav.setText(nombre + " " + apaterno + " " + amaterno);
        navigationView.setNavigationItemSelectedListener(this);

        Snackbar.make(constraintLayout, "Bienvenido " + nombre, Snackbar.LENGTH_LONG).show();
    }

    private void cargarWerbService() {
        mostrarProgreso(true);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String url = "http://sp.saludtlax.gob.mx:8084/Service_bitacora/webresources/vehiculos/all";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mostrarProgreso(false);
        progressDialog.dismiss();
        Snackbar.make(constraintLayout, "No se pudo conectar al servidor", Snackbar.LENGTH_LONG).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Vehiculo vehiculo;

        JSONArray jsonArray = response.optJSONArray("vehiculos");

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                vehiculo = new Vehiculo();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                vehiculo.setNo_economico(jsonObject.optString("no_economico"));
                vehiculo.setMarca(jsonObject.optString("marca"));
                vehiculo.setTipo(jsonObject.optString("tipo"));
                vehiculo.setModelo(jsonObject.optInt("modelo"));
                vehiculo.setCapacidad_tanque(jsonObject.optInt("capacidad_tanque"));
                vehiculo.setCilindraje(jsonObject.optInt("cilindraje"));
                vehiculo.setTipo_combustible(jsonObject.optString("tipo_combustible"));
                vehiculo.setNo_serie(jsonObject.optString("no_serie"));
                vehiculo.setNo_motor(jsonObject.optString("no_motor"));
                vehiculo.setPlacas(jsonObject.optString("placas"));
                vehiculo.setArea_resguardante(jsonObject.optString("area_resguardante"));
                vehiculo.setResguardante(jsonObject.optString("resguardante"));
                vehiculo.setDisponibilidad(jsonObject.optBoolean("disponibilidad"));
                vehiculo.setImagen(jsonObject.optInt("imagen"));
                listaVehiculos.add(vehiculo);
            }
            mostrarProgreso(false);
            progressDialog.dismiss();
            VehiculoAdaptador adaptador = new VehiculoAdaptador(listaVehiculos, this);
            recyclerVehiculo.setAdapter(adaptador);
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar.make(constraintLayout, "No se pudo establecer conexión con el servidor", Snackbar.LENGTH_LONG).show();
            mostrarProgreso(false);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recorrido) {
            Intent intent = new Intent(MenuPrincipal.this, Recorridos.class);
            startActivity(intent);

        } else if (id == R.id.nav_recargas) {

        } else if (id == R.id.nav_rutas) {

        } else if (id == R.id.nav_configurar) {

        } else if (id == R.id.nav_logout){
            editor.clear();
            editor.commit();
            finish();
            Intent intent = new Intent(MenuPrincipal.this, Login.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mostrarProgreso(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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
                            MenuPrincipal.this.finish();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
