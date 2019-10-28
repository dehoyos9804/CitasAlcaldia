package co.com.learn.code.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import co.com.learn.code.Adapter.AdaptadorHorarioDisponibleUpdate;
import co.com.learn.code.Adapter.AdaptadorHorariosDisponibles;
import co.com.learn.code.Models.DetalleCita;
import co.com.learn.code.Models.HorariosDisponibles;
import co.com.learn.code.Models.IniciarSesion;
import co.com.learn.code.Models.Temas;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.DateDialog;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class DetalleCitaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    //etiqueta para la depuracion
    private static final String TAG = DetalleCita.class.getSimpleName();
    private Gson gson = new Gson();
    private CollapsingToolbarLayout collapser;
    private String codigoCita;

    private TextView txtDependenciaElegida;
    private TextView txtTemaElegido;
    private TextView txtFechaDetalleCita;
    private TextView txtHoraDetalleCita;
    private TextView txtFuncionarioCargo;

    private String duracionTema;
    private String codigoFuncionario;
    private int tiempoNotificacion = 0;

    //atributos del bottom sheet
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private TextView txtFecha;
    private Spinner spinnerNotificacion;
    //adaptador del RecicleView
    private AdaptadorHorarioDisponibleUpdate adapter;
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
    public static void launch(Activity activity, String consecutivo) {
        Intent intent = getLaunchIntent(activity, consecutivo);
        activity.startActivityForResult(intent, Constantes.CITA_DETALLE);
    }

    /**
     * Construye un Intent a partir del contexto y la actividad
     * de detalle.
     *
     * @param context Contexto donde se inicia
     * @param consecutivo  Identificador de las consultas
     * @return Intent listo para usar
     */
    public static Intent getLaunchIntent(Context context, String consecutivo){
        Intent intent = new Intent(context, DetalleCitaActivity.class);
        intent.putExtra(Constantes.EXTRA_CITA_DETALLE_ID, consecutivo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cita);

        setToolbar();

        init();//iniciar componentes
    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalle_cita);
        setSupportActionBar(toolbar);
    }

    private void init(){
        if(getSupportActionBar()!=null)//habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retener instancia
        if(getIntent().getStringExtra(Constantes.EXTRA_CITA_DETALLE_ID) != null){
             codigoCita = getIntent().getStringExtra(Constantes.EXTRA_CITA_DETALLE_ID);
        }

        collapser = (CollapsingToolbarLayout) findViewById(R.id.collapser_detalle);
        collapser.setTitle(Preferences.getPreferenceString(this, Constantes.PREFERENCIA_NOMBRES_CLAVE) + " " + Preferences.getPreferenceString(this, Constantes.PREFERENCIA_APELLIDOS_CLAVE)); // Cambiar título

        txtDependenciaElegida = (TextView) findViewById(R.id.txtDependenciaElegida);
        txtTemaElegido = (TextView) findViewById(R.id.txtTemaElegido);
        txtFechaDetalleCita = (TextView) findViewById(R.id.txtFechaDetalleCita);
        txtHoraDetalleCita = (TextView) findViewById(R.id.txtHoraDetalleCita);
        txtFuncionarioCargo = (TextView) findViewById(R.id.txtFuncionarioCargo);


        //botton sheet
        bottom_sheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottom_sheet.setVisibility(View.GONE);//oculto el bottom sheet

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        bottom_sheet.setVisibility(View.VISIBLE);//colocando visible el botton sheet
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        bottom_sheet.setVisibility(View.GONE);//oculto el botton sheet
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        txtFecha = (TextView) findViewById(R.id.txtFecha);
        spinnerNotificacion = (Spinner) findViewById(R.id.spinnerNotificacion);

        //colocar la fecha y hora actual
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat fechaactual = new SimpleDateFormat("yyyy-MM-dd");
        String formatoFecha = fechaactual.format(localCalendar.getTime());

        txtFecha.setText(formatoFecha);

        //array para llenar la notificacion
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.notificacion ,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //lleno el spinner
        spinnerNotificacion.setAdapter(arrayAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.reciclador_add);
        recyclerView.setHasFixedSize(true);

        //Usar el Administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);

        llenarDatos();
        spinnerNotificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //valido las la seleccion del spinner
                switch (position){
                    case 0:
                        tiempoNotificacion = 0;
                        break;
                    case 1:
                        tiempoNotificacion = 30;
                        break;
                    case 2:
                        tiempoNotificacion = 60;
                        break;
                    case 3:
                        tiempoNotificacion = (24*60);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtFecha.setOnClickListener(this);
    }

    //Agrego mi menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_cita, menu);
        return true;
    }

    //permite implementar la logica cuando se seleccionan los items del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_discard:
                bottom_sheet.setVisibility(View.VISIBLE);//coloco visible el bottom sheet
                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    spinnerNotificacion.setSelection(1);
                    cargarDatosAdaptador((txtFecha.getText().toString()), Integer.parseInt(duracionTema), codigoFuncionario);//cargo los horarios disponibles
                }
                return true;
            case R.id.action_delete:
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle("Eliminar Cita");
                dialogo.setMessage("¿Esta seguro que quieres eliminar la cita?");
                dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCita();
                        dialog.dismiss();
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogo.show();//mostrar el dialogo
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void llenarDatos(){

        //Añadir parametros a la URL de webservice
        String newURL = Constantes.GET_DETALLE_CITA_ID + "?codcita=" + codigoCita;

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
                                        //descartar el diálogo de progreso
                                        Utilidades.showToast(DetalleCitaActivity.this, "Error de red: " + error.getLocalizedMessage());
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
                        JSONObject datos = response.getJSONObject("tblusuarios");
                        //Parsear objeto
                        DetalleCita is = gson.fromJson(datos.toString(),DetalleCita.class);
                        Log.i("TAG","tag-->"+is.getNumerocedula());

                        txtDependenciaElegida.setText(is.getNombre());
                        txtTemaElegido.setText(is.getTema());
                        txtFechaDetalleCita.setText(is.getFechareal());
                        txtHoraDetalleCita.setText((is.getHorareali() + " - " + is.getHorarealf()));
                        txtFuncionarioCargo.setText((is.getNombres() + " " + is.getApellidos()));
                        duracionTema = is.getDuracion();
                        codigoFuncionario = is.getNumerocedula();

                    }catch (JSONException e){
                        Log.i(TAG,"Error al llenar Adaptador " +e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
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

        cargarDatosAdaptador((txtFecha.getText().toString()), Integer.parseInt(duracionTema), codigoFuncionario);//cargo los horarios disponibles
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
                                        //Utilidades.showToast(AgendarCitaActivity.this, "Error al cargar los datos: " + error.toString());
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
                        adapter = new AdaptadorHorarioDisponibleUpdate(Arrays.asList(horarios), this, codigoCita, tiempoNotificacion, txtFecha.getText().toString());

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

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator_detalle_cita), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    private void deleteCita(){
        HashMap<String, String> map = new HashMap<>();// MAPEO

        map.put("idcita", codigoCita);// Identificador

        JSONObject jobject = new JSONObject(map);// Objeto Json

        // Eliminar datos en el servidor
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.DELETE_CITA,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta
                                procesarRespuestaEliminar(response);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                showSnackBar("Error Volley: " + error.getLocalizedMessage());
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    /**
     * Procesa la respuesta de eliminación obtenida desde el sevidor
     */
    private void procesarRespuestaEliminar(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Utilidades.showToast(this, mensaje);
                    Intent intent = new Intent(this, InitialUsuarioActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP));//finaliza las actividades en pila
                    break;

                case "2":
                    // Mostrar mensaje
                    showSnackBar(mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
