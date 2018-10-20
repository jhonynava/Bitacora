package mx.gob.seguropopulartlax.bitacora_vehicular_repss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    private String nombre_usuario;
    RelativeLayout relativeLayout;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

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



            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargarWebService();
                }
            });
    }

    private void cargarWebService() {

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String password = edt_pass.getText().toString().trim();

        String url="http://sp.saludtlax.gob.mx:8084/Service_bitacora/webresources/user/login?CURP=" +
                edt_user.getText().toString().trim().toUpperCase() + "&PASSWORD=" + base64.encode(password).trim();


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,Login.this,Login.this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se pudo conectar al servidor02" + error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        String password = edt_pass.getText().toString().trim();

        Usuario usuario =  new Usuario();

        JSONArray jsonArray =  response.optJSONArray("usuarios");
        JSONObject jsonObject=null;

        try{
            jsonObject = jsonArray.getJSONObject(0);
            usuario.setCurp(jsonObject.optString("curp"));
            usuario.setPassword(jsonObject.optString("password"));
            usuario.setNombre(jsonObject.optString("nombre"));
            usuario.setApaterno(jsonObject.optString("apaterno"));
            usuario.setAmaterno(jsonObject.optString("amaterno"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        if (usuario.getCurp().equals(edt_user.getText().toString().trim()) && usuario.getPassword().equals(base64.encode(password).trim())){
            Intent intent = new Intent(Login.this, MenuPrincipal.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, "Error en la contraseña", Toast.LENGTH_SHORT).show();
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