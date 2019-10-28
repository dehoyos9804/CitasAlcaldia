package co.com.learn.code.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import co.com.learn.code.Adapter.AdaptadorListaCitas;
import co.com.learn.code.Adapter.AdaptadorListaDependencia;
import co.com.learn.code.Models.Dependencias;
import co.com.learn.code.Models.ListaCitas;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

/**
 * clase que permite obtener las citas del usuario*/
public class ListaCitasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    //Etiqueta de depuracion
    private static final String TAG = ListaCitasActivity.class.getSimpleName();
    //adaptador del RecicleView
    private AdaptadorListaCitas adapter;
    //instancia global de RecicleView
    private RecyclerView recyclerView;
    //private TextView emptyView;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //Instancia GSON
    private Gson gson = new Gson();
    //instancia del progress dialog
    private static ProgressDialog loading = null;

    private TextView txtDiaActual;
    private TextView txtHoraActual;
    private TextView txtDiaSemana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_citas);

        setToolbar(); //añadimos el toolbar

        init();//inicio las instancias

        recyclerView = (RecyclerView) findViewById(R.id.reciclador_lista_cita);
        recyclerView.setHasFixedSize(true);

        //Usar el Administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);

        //cargardatos en el adaptador
        cargarDatosAdaptador();
    }

    private void init(){
        txtDiaActual = (TextView) findViewById(R.id.txtDiaActual);
        txtHoraActual = (TextView) findViewById(R.id.txtHoraActual);
        txtDiaSemana = (TextView) findViewById(R.id.txtDiaSemana);

        //colocar la fecha y hora actual
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        //txtDiaActual.setText((currentYear + "-" + currentMonth + "-" + currentDay));
        SimpleDateFormat fechaactual = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat horaActual = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat diaSemana = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayofTheWeek = diaSemana.format(d);
        String formatoFecha = fechaactual.format(localCalendar.getTime());
        String formatoHora = horaActual.format(localCalendar.getTime());

        int currenAp = localCalendar.get(Calendar.AM_PM);
        String meridiano = "";
        if(currenAp != 0){
            meridiano = "P.M";
        }else{
            meridiano = "A.M";
        }

        txtDiaActual.setText(formatoFecha);
        txtHoraActual.setText((formatoHora+ " " + meridiano));
        txtDiaSemana.setText(dayofTheWeek);
    }

    //Agrego mi toolbar
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lista_cita);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            // Poner ícono del drawer toggle
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_dark);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
     * Carga el adaptador con las Consultas obtenidas
     * en la respuesta
     */
    public void cargarDatosAdaptador() {

        loading = ProgressDialog.show(this,"Cargando.","Espere por favor...",false,false);
        String usuario = Preferences.getPreferenceString(this, Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);
        String newURL = Constantes.GET_LISTA_CITAS_USUARIO + "?codusuario=" + usuario;

        // Petición GET
        VolleySingleton.
                getInstance(this).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                newURL,
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
                                        showSnackBar(("Error de red:" + error.getLocalizedMessage()));
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
                        JSONArray mensaje = response.getJSONArray("tblusuarios");
                        // Parsear con Gson
                        ListaCitas[] citas = gson.fromJson(mensaje.toString(), ListaCitas[].class);
                        // Inicializar adaptador
                        adapter = new AdaptadorListaCitas(Arrays.asList(citas), this);
                        // Setear adaptador a la lista
                        recyclerView.setAdapter(adapter);
                        loading.dismiss();

                    } catch (JSONException e) {
                        showSnackBar(("Aun No Tiene Citas"));
                        Log.i(TAG, "Error al llenar Adaptador " + e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    loading.dismiss();
                    showSnackBar(mensaje2);
                    break;
                case "3":
                    String mensaje3 = response.getString("mensaje");
                    loading.dismiss();
                    showSnackBar(mensaje3);
                    break;
            }
        } catch (JSONException je) {
            Log.d(TAG, je.getMessage());
        }
    }

    //Agrego mi menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search_initial, menu);
        MenuItem searchItem = menu.findItem(R.id.men_action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinadorLista), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        //el usuario presionó el botón de búsqueda
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //el usuario cambió el texto
        return false;
    }
}
