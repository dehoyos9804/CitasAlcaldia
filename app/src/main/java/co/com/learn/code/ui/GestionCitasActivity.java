package co.com.learn.code.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import co.com.learn.code.Models.GestionCitas;
import co.com.learn.code.Models.IniciarSesion;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class GestionCitasActivity extends AppCompatActivity implements View.OnClickListener {

    //Etiqueta de depuracion
    private static final String TAG = GestionCitasActivity.class.getSimpleName();

    //Instancia GSON
    private Gson gson = new Gson();
    //instancia del progress dialog
    private ProgressDialog loading = null;

    private TextView txtDiaActual;
    private TextView txtHoraActual;
    private TextView txtDiaSemana;

    private String codigoCita;

    private TextView txtEstadoCita;
    private RadioButton rb_en_espera;
    private RadioButton rb_no_atendida;
    private RadioButton rb_atendida;
    private TextView txtUsuario;
    private TextView txtTema;
    private TextView txtFechaCitaGestion;
    private TextView txtHoraCita;
    private TextView txtDuracionTema;

    /**
     * Inicia una nueva instancia de la actividad
     *
     * @param activity Contexto desde donde se lanzará
     * @param consecutivo   Identificador de las consultas a detallar
     */
    public static void launch(Activity activity, String consecutivo) {
        Intent intent = getLaunchIntent(activity, consecutivo);
        activity.startActivityForResult(intent, Constantes.CODIGO_CITA);
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
        Intent intent = new Intent(context, GestionCitasActivity.class);
        intent.putExtra(Constantes.EXTRA_CODIGO_CITA, consecutivo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_citas);

        setToolbar(); //añadimos el toolbar

        // Retener instancia
        if(getIntent().getStringExtra(Constantes.EXTRA_CODIGO_CITA) != null){
            codigoCita = getIntent().getStringExtra(Constantes.EXTRA_CODIGO_CITA);
        }

        init();//inicio las instancias

        peticionHTTP();
    }

    private void init(){
        txtDiaActual = (TextView) findViewById(R.id.txtDiaActual);
        txtHoraActual = (TextView) findViewById(R.id.txtHoraActual);
        txtDiaSemana = (TextView) findViewById(R.id.txtDiaSemana);

        txtEstadoCita = (TextView) findViewById(R.id.txtEstadoCita);
        rb_en_espera = (RadioButton) findViewById(R.id.rb_en_espera);
        rb_no_atendida = (RadioButton) findViewById(R.id.rb_no_atendida);
        rb_atendida = (RadioButton) findViewById(R.id.rb_atendida);
        txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        txtTema = (TextView) findViewById(R.id.txtTema);
        txtFechaCitaGestion = (TextView) findViewById(R.id.txtFechaCitaGestion);
        txtHoraCita = (TextView) findViewById(R.id.txtHoraCita);
        txtDuracionTema = (TextView) findViewById(R.id.txtDuracionTema);

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

        //escuchadores
        rb_atendida.setOnClickListener(this);
        rb_en_espera.setOnClickListener(this);
        rb_no_atendida.setOnClickListener(this);
    }

    //Agrego mi toolbar
    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gestion_cita);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            // Poner ícono del drawer toggle
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_dark);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     *
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinador_gestion_citas), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    private void peticionHTTP(){

        //Añadir parametros a la URL de webservice
        String newURL = Constantes.GET_GESTION_CITA_ID + "?codcita=" + codigoCita;

        //inicio progressDialog
        loading = ProgressDialog.show(this,"Autenticando...","Espere por favor...",false,false);
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
                                        loading.dismiss();
                                        procesarRespuesta(response);
                                        Log.i(TAG, "processanddo respuesta..." + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //descartar el diálogo de progreso
                                        loading.dismiss();
                                        showSnackBar("Error Volley: " + error.toString());
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
                        JSONObject datos = response.getJSONObject("tblFuncionarios");
                        //Parsear objeto
                        GestionCitas is = gson.fromJson(datos.toString(),GestionCitas.class);
                        //iniciamos la sesion
                        switch (is.getEstado()){
                            case "0"://No Atendida
                                txtEstadoCita.setTextColor(getResources().getColor(R.color.colorPrimary));
                                txtEstadoCita.setText("No Atendida");
                                rb_no_atendida.setChecked(true);
                                break;
                            case "1"://EN ESPERA
                                txtEstadoCita.setTextColor(getResources().getColor(R.color.colorAccent));
                                txtEstadoCita.setText("En espera");
                                rb_en_espera.setChecked(true);
                                break;
                            case "2"://Atendida
                                txtEstadoCita.setTextColor(getResources().getColor(R.color.color6));
                                txtEstadoCita.setText("Atendida");
                                rb_atendida.setChecked(true);
                                break;
                        }

                        txtUsuario.setText(is.getNombres() + " " + is.getApellidos());
                        txtTema.setText(is.getTema());
                        txtFechaCitaGestion.setText(is.getFechareal());
                        txtHoraCita.setText(is.getHorareali() + " - " + is.getHorarealf());
                        txtDuracionTema.setText(is.getDuracion() + " min. Aproximadamente");

                    }catch (JSONException e){
                        Log.i(TAG,"Error al llenar Adaptador " +e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    loading.dismiss();//finalizo el dialogo
                    Utilidades.showToast(this, mensaje2);
                    break;
                case "3":
                    String mensaje3 = response.getString("mensaje");
                    loading.dismiss();//finalizo el dialogo
                    Utilidades.showToast(this, mensaje3);
                    break;
            }
        }catch (JSONException je){
            Log.d(TAG, je.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_en_espera:
                if (rb_en_espera.isChecked()){
                    actualizarEstadoCita("1");//en espera
                }
                break;
            case R.id.rb_atendida:
                if (rb_atendida.isChecked()){
                    actualizarEstadoCita("2");//atendida
                }
                break;
            case R.id.rb_no_atendida:
                if (rb_no_atendida.isChecked()){
                    actualizarEstadoCita("0");
                }
                break;
        }
    }

    private void actualizarEstadoCita(String estado){
        HashMap<String, String> map = new HashMap<>();// MAPEO
        map.put("estado", estado);// Identificador
        map.put("codcita", codigoCita);

        JSONObject jobject = new JSONObject(map);// Objeto Json

        // Eliminar datos en el servidor
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATE_ESTADO_GESTION_CITA,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta
                                procesarRespuestaActualizar(response);

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
    private void procesarRespuestaActualizar(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    showSnackBar(mensaje);
                    peticionHTTP();
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
