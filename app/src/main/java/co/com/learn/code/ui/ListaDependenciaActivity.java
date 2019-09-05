package co.com.learn.code.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import co.com.learn.code.Adapter.AdaptadorListaDependencia;
import co.com.learn.code.Models.Dependencias;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class ListaDependenciaActivity extends AppCompatActivity {

    //Etiqueta de depuracion
    private static final String TAG = ListaDependenciaActivity.class.getSimpleName();
    //adaptador del RecicleView
    private AdaptadorListaDependencia adapter;
    //instancia global de RecicleView
    private RecyclerView recyclerView;
    //private TextView emptyView;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //Instancia GSON
    private Gson gson = new Gson();
    //instancia del progress dialog
    private static ProgressDialog loading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dependencia);

        setToolbar(); //añadimos el toolbar

        recyclerView = (RecyclerView) findViewById(R.id.reciclador);
        recyclerView.setHasFixedSize(true);

        //Usar el Administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);

        //cargardatos en el adaptador
        cargarDatosAdaptador();
    }

    //Agrego mi toolbar
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            // Poner ícono del drawer toggle
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //permite implementar la logica cuando se seleccionan los items del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Carga el adaptador con las Consultas obtenidas
     * en la respuesta
     */
    public void cargarDatosAdaptador() {

        loading = ProgressDialog.show(this,"Cargando.","Espere por favor...",false,false);

        // Petición GET
        VolleySingleton.
                getInstance(this).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                Constantes.GET_DEPENDENCIAS,
                                 null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuestaHTTP(response);
                                        Log.i(TAG, "processanddo respuesta..." + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismiss();
                                        Utilidades.showToast(ListaDependenciaActivity.this, "Error al cargar los datos: " + error.toString());
                                        Log.d(TAG, "Error Volley: " + error.toString());
                                    }
                                }

                        )
                );
    }


    private void procesarRespuestaHTTP(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            switch (estado) {
                case "1":// EXITO
                    try {
                        // Obtener array "consulta" Json
                        JSONArray mensaje = response.getJSONArray("tbldependencias");
                        // Parsear con Gson
                        Dependencias[] tbldependencia = gson.fromJson(mensaje.toString(), Dependencias[].class);
                        // Inicializar adaptador
                        adapter = new AdaptadorListaDependencia(Arrays.asList(tbldependencia), this);
                        // Setear adaptador a la lista
                        recyclerView.setAdapter(adapter);
                        loading.dismiss();

                    } catch (JSONException e) {
                        Log.i(TAG, "Error al llenar Adaptador " + e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    loading.dismiss();
                    Utilidades.showToast(this, mensaje2);
                    break;
            }
        } catch (JSONException je) {
            Log.d(TAG, je.getMessage());
        }
    }
}
