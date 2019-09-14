package co.com.learn.code.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import co.com.learn.code.Adapter.AdaptadorHorariosDisponibles;
import co.com.learn.code.Adapter.AdaptadorListaDependencia;
import co.com.learn.code.Models.Dependencias;
import co.com.learn.code.Models.Funcionario_Id;
import co.com.learn.code.Models.HorariosDisponibles;
import co.com.learn.code.Models.Temas;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.DateDialog;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class AgendarCitaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener{

    //etiqueta para la depuracion
    private static final String TAG = AgendarCitaActivity.class.getSimpleName();
    private Gson gson = new Gson();
    private CollapsingToolbarLayout collapser;
    //FloatingActionButton fab;

    private TextView txtFecha;
    private String codigo_dependencia;
    private String nombre_dependencia;
    private int duracion;
    private String codigo_funcionario;
    private Spinner spinnerTema;

    //atributos para llenar el spinner de temas
    private ArrayList<Temas> listaTema;

    //adaptador del RecicleView
    private AdaptadorHorariosDisponibles adapter;
    //instancia global de RecicleView
    private RecyclerView recyclerView;
    //private TextView emptyView;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //instancia del progress dialog
    private static ProgressDialog loading = null;


    /**
     * Inicia una nueva instancia de la actividad
     *
     * @param activity Contexto desde donde se lanzará
     * @param consecutivo   Identificador de las consultas a detallar
     */
    public static void launch(Activity activity, String consecutivo, String nombredependencia) {
        Intent intent = getLaunchIntent(activity, consecutivo, nombredependencia);
        activity.startActivityForResult(intent, Constantes.DEPENDENCIA);
    }

    /**
     * Construye un Intent a partir del contexto y la actividad
     * de detalle.
     *
     * @param context Contexto donde se inicia
     * @param consecutivo  Identificador de las consultas
     * @return Intent listo para usar
     */
    public static Intent getLaunchIntent(Context context, String consecutivo, String nombredependencia){
        Intent intent = new Intent(context, AgendarCitaActivity.class);
        intent.putExtra(Constantes.EXTRA_CODIGO_DEPENDENCIA, consecutivo);
        intent.putExtra(Constantes.EXTRA_NOMBRE_DEPENDENCIA, nombredependencia);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_add_cita);

        setToolbar();//Añadir action bar

        init();//inicio las dependencias

    }

    private void init(){
        if(getSupportActionBar()!=null)//habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retener instancia
        if(getIntent().getStringExtra(Constantes.EXTRA_CODIGO_DEPENDENCIA) != null && getIntent().getStringExtra(Constantes.EXTRA_NOMBRE_DEPENDENCIA) != null){
            codigo_dependencia = getIntent().getStringExtra(Constantes.EXTRA_CODIGO_DEPENDENCIA);
            nombre_dependencia = getIntent().getStringExtra(Constantes.EXTRA_NOMBRE_DEPENDENCIA);
        }

        getCodigoFuncionario();

        txtFecha = (TextView) findViewById(R.id.txtFecha);
        collapser = (CollapsingToolbarLayout) findViewById(R.id.collapser);
        collapser.setTitle(nombre_dependencia); // Cambiar título


        spinnerTema = (Spinner) findViewById(R.id.spinnerTema);
        llenarSpinnerTema();

        //fab = (FloatingActionButton) findViewById(R.id.fab);
        //Object item = spinnerTema.getSelectedItem();
        //int idseleccionado = ((Temas) item).getIdtema();

        recyclerView = (RecyclerView) findViewById(R.id.reciclador_add);
        recyclerView.setHasFixedSize(true);

        //Usar el Administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);

        txtFecha.setOnClickListener(this);
        spinnerTema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                duracion = ((Temas) item).getDuracion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
        Date date = null;

        final int mesActual = month + 1;
        String diaFormat = (dayOfMonth < 10) ? 0 + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
        String mesFormat = (mesActual< 10) ? 0 + String.valueOf(mesActual) : String.valueOf(mesActual);
        try{
            date = dateFormat.parse(year + "-" + mesFormat + "-" + diaFormat);
        }catch (ParseException e){
            e.printStackTrace();
        }

        String outDate = dateFormat.format(date);
        txtFecha.setText(outDate);

        cargarDatosAdaptador((txtFecha.getText().toString()), duracion, codigo_funcionario);//cargo los horarios disponibles
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtFecha:
                String tag = "DatePicker";
                new DateDialog().show(getSupportFragmentManager(),tag);
                break;
        }
    }

    private void llenarSpinnerTema(){
        String newURL = Constantes.GET_ALL_TEMAS + "?coddependencia=" + codigo_dependencia;
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
                                        respuestaHTTPSpinner(response);
                                        Log.i(TAG, "processanddo respuesta..." + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //loading.dismiss();
                                        Utilidades.showToast(AgendarCitaActivity.this, "Error al cargar los datos: " + error.toString());
                                        Log.d(TAG, "Error Volley: " + error.toString());
                                    }
                                }

                        )
                );
    }

    private void respuestaHTTPSpinner(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            switch (estado) {
                case "1":// EXITO
                    try {
                        // Obtener array "consulta" Json
                        JSONArray tbltema = response.getJSONArray("tbltemas");
                        listaTema = new ArrayList<Temas>();
                        listaTema.add(new Temas(0, "Seleccionar Temas", 0));
                        for (int i = 0; i < tbltema.length(); i++){
                            JSONObject object = (JSONObject) tbltema.get(i);
                            listaTema.add(new Temas(object.getInt("idtema"), object.getString("tema"), object.getInt("duracion")));
                        }

                        //llenar el adaptador
                        if(listaTema != null){
                            ArrayAdapter<Temas> adapter = new ArrayAdapter<Temas>(this, android.R.layout.simple_spinner_item, listaTema);
                            spinnerTema.setAdapter(adapter);
                        }else{
                            Log.i(TAG,"Datos Vacios"+listaTema);
                        }

                        /*spinnerTema.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position != 0 && id != 0){
                                    Object item = parent.getItemAtPosition(position);
                                }
                            }
                        });*/

                    } catch (JSONException e) {
                        Log.i(TAG, "Error al llenar La Lista " + e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    //loading.dismiss();
                    Utilidades.showToast(this, mensaje2);
                    break;
            }
        } catch (JSONException je) {
            Log.d(TAG, je.getMessage());
        }
    }

    /*
     * Carga el adaptador con las Consultas obtenidas
     * en la respuesta
     */
    public void cargarDatosAdaptador(String fecha, int duracion, String codfuncionario) {

        loading = ProgressDialog.show(this,"Cargando.","Espere por favor...",false,false);
        String newURL = Constantes.GET_HORAS_DISPONIBLES + "?fecha=" + fecha + "&duracion=" + duracion + "&codfuncionario=" + codfuncionario;
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
                                        Utilidades.showToast(AgendarCitaActivity.this, "Error al cargar los datos: " + error.toString());
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
                        HorariosDisponibles[] horarios = gson.fromJson(mensaje.toString(), HorariosDisponibles[].class);
                        // Inicializar adaptador
                        adapter = new AdaptadorHorariosDisponibles(Arrays.asList(horarios), this);
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

    private void getCodigoFuncionario(){

        //Añadir parametros a la URL de webservice
        String newURL = Constantes.GET_CODIGO_FUNCIONARIO_DEPENDENCIA + "?coddependencia=" + codigo_dependencia;

        //petición GET
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
                                        procesarRespuesta(response);
                                        Log.i(TAG, "processanddo respuesta..." + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.toString());
                                    }
                                }
                        )
                );
    }

    private void procesarRespuesta(JSONObject response){
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            switch (estado){
                case "1":// EXITO
                    try {
                        // Obtener array "consulta" Json
                        JSONObject datos = response.getJSONObject("tbldependencias");
                        //Parsear objeto
                        Funcionario_Id is = gson.fromJson(datos.toString(),Funcionario_Id.class);
                        Log.i("TAG","tag-->"+is.getIdfuncionario());
                        codigo_funcionario = is.getIdfuncionario();

                    }catch (JSONException e){
                        Log.i(TAG,"Error al llenar Adaptador " +e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    //limpiar();
                    Utilidades.showToast(this, mensaje2);
                    break;
                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Utilidades.showToast(this, mensaje3);
                    break;
            }
        }catch (JSONException je){
            Log.d(TAG, je.getMessage());
        }
    }

}
