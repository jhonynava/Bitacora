package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Toast;

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
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.gob.seguropopulartlax.bitacora_vehicular_repss.adaptadores.VehiculoAdaptador;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.entidades.Vehiculo;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Response.Listener<JSONObject>, Response.ErrorListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";
    private RecyclerView recyclerVehiculo;
    private ArrayList<Vehiculo> listaVehiculos;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ConstraintLayout constraintLayout;
    private View view_lottie, view_recyclerAutos, contenedor;
    private ProgressDialog progressDialog = null;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obetener usuario
        String nombre, apaterno, amaterno;
        Boolean recorrido;
        TextView textView_nav;

        //Preferencias para obtener usuario
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nombre = sharedPreferences.getString("nombre", null);
        apaterno = sharedPreferences.getString("apaterno", null);
        amaterno = sharedPreferences.getString("amaterno", null);
        recorrido = sharedPreferences.getBoolean("recorrido", false);

        setContentView(R.layout.activity_menu_principal);

        //Inicia el recyclerview
        listaVehiculos = new ArrayList<>();

        recyclerVehiculo = findViewById(R.id.rv_automoviles);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MenuPrincipal.this, 2);
        recyclerVehiculo.setLayoutManager(gridLayoutManager);
        requestQueue = Volley.newRequestQueue(this);
        cargarWerbService();

        //Inicia el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rfaLayout = findViewById(R.id.activity_main_rfal);
        rfaBtn = findViewById(R.id.activity_main_rfab);

        //Vistas
        constraintLayout = findViewById(R.id.id_constraintUsuario);
        contenedor = findViewById(R.id.view_appbar);

        //Progress Dialog
        progressDialog = new ProgressDialog(MenuPrincipal.this);
        progressDialog.show();
        progressDialog.setCancelable(true);
        progressDialog.setContentView(R.layout.cargando);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //FloatingActionButton
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Rutas")
                .setResId(R.drawable.map_float)
                .setIconNormalColor(getResources().getColor(R.color.azul_primary))
                .setIconPressedColor(getResources().getColor(R.color.azul_dark))
                .setLabelColor(getResources().getColor(R.color.azul_dark))
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Recarga")
                .setResId(R.drawable.gas_station_float)
                .setIconNormalColor(getResources().getColor(R.color.naranja_primary))
                .setIconPressedColor(getResources().getColor(R.color.naranja_dark))
                .setLabelColor(getResources().getColor(R.color.naranja_dark))
                .setLabelSizeSp(14)
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Recorrido")
                .setResId(R.drawable.car_float)
                .setIconNormalColor(getResources().getColor(R.color.verde_primary))
                .setIconPressedColor(getResources().getColor(R.color.verde_dark))
                .setLabelColor(getResources().getColor(R.color.verde_dark))
                .setWrapper(2)
        );
        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (recorrido) {
            rfaLayout.setVisibility(View.GONE);
            Fragment fragment = new content_recorrido();
            getSupportFragmentManager().beginTransaction().replace(R.id.view_appbar, fragment).commit();
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        textView_nav = view.findViewById(R.id.nav_tvUsuario);
        textView_nav.setText(nombre + " " + apaterno + " " + amaterno);
        navigationView.setNavigationItemSelectedListener(this);

        //Snackbar.make(constraintLayout, "Bienvenido " + nombre, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        switch (position){
            case 0:
                Toast.makeText(this, "Proximamente..." , Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();
                break;
            case 1:
                Intent recarga = new Intent(MenuPrincipal.this, Recarga_combustible.class);
                startActivity(recarga);
                rfabHelper.toggleContent();
                break;
            case 2:
                Intent intent = new Intent(MenuPrincipal.this, Recorridos.class);
                startActivity(intent);
                rfabHelper.toggleContent();
                break;
        }
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        switch (position){
            case 0:
                Toast.makeText(this, "Proximamente..." , Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();
                break;
            case 1:
                Intent recarga = new Intent(MenuPrincipal.this, Recarga_combustible.class);
                startActivity(recarga);
                rfabHelper.toggleContent();
                break;
            case 2:
                Intent intent = new Intent(MenuPrincipal.this, Recorridos.class);
                startActivity(intent);
                rfabHelper.toggleContent();
                break;
        }
    }

    private void cargarWerbService() {
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
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(constraintLayout, "No se pudo conectar", Snackbar.LENGTH_INDEFINITE).setAction("REINTENTAR", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWerbService();
            }
        });
        snackbar.show();
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
            progressDialog.dismiss();
            VehiculoAdaptador adaptador = new VehiculoAdaptador(listaVehiculos, this);
            recyclerVehiculo.setAdapter(adaptador);
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar.make(constraintLayout, "No se pudo establecer conexión con el servidor", Snackbar.LENGTH_LONG).show();
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
            Intent intent = new Intent(MenuPrincipal.this, Recarga_combustible.class);
            startActivity(intent);

        } else if (id == R.id.nav_rutas) {

        } else if (id == R.id.nav_configurar) {

        } else if (id == R.id.nav_logout) {
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
