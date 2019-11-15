package co.com.learn.code.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import co.com.learn.code.Models.SerializarHorario;
import co.com.learn.code.R;
import co.com.learn.code.fragment.UsuariosFragment;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class AgregarHorarioActivity extends AppCompatActivity implements View.OnClickListener {

    //etiqueta para la depuracion
    private static final String TAG = UsuariosFragment.class.getSimpleName();
    private Gson gson = new Gson();
    private ArrayList<SerializarHorario> array;
    private JSONObject obj;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //instancia del progress dialog
    private static ProgressDialog loading = null;

    private SwitchCompat[] switch_open = new SwitchCompat[7];
    private LinearLayout[] lyt_jornadas = new LinearLayout[7];
    private SwitchCompat[] switch_jornada_manana = new SwitchCompat[7];
    private SwitchCompat[] switch_jornada_tarde = new SwitchCompat[7];
    private LinearLayout[] lyt_hours_manana = new LinearLayout[7];
    private LinearLayout[] lyt_hours_tarde = new LinearLayout[7];
    private AppCompatButton btn_apply;
    private AppCompatSpinner[] spin_bh_from_manana = new AppCompatSpinner[7];
    private AppCompatSpinner[] spin_bh_from_manana_a = new AppCompatSpinner[7];
    private AppCompatSpinner[] spin_bh_from_tarde = new AppCompatSpinner[7];
    private AppCompatSpinner[] spin_bh_from_tarde_a = new AppCompatSpinner[7];

    private int isCantidad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horario);

        init();//iniciar componentes
    }

    private void init(){
        switch_open[0] = (SwitchCompat) findViewById(R.id.switch_open_1);
        switch_open[1] = (SwitchCompat) findViewById(R.id.switch_open_2);
        switch_open[2] = (SwitchCompat) findViewById(R.id.switch_open_3);
        switch_open[3] = (SwitchCompat) findViewById(R.id.switch_open_4);
        switch_open[4] = (SwitchCompat) findViewById(R.id.switch_open_5);
        switch_open[5] = (SwitchCompat) findViewById(R.id.switch_open_6);
        switch_open[6] = (SwitchCompat) findViewById(R.id.switch_open_7);

        lyt_jornadas[0] = (LinearLayout) findViewById(R.id.lyt_jornadas_lunes);
        lyt_jornadas[1] = (LinearLayout) findViewById(R.id.lyt_jornadas_marte);
        lyt_jornadas[2] = (LinearLayout) findViewById(R.id.lyt_jornadas_miercole);
        lyt_jornadas[3] = (LinearLayout) findViewById(R.id.lyt_jornadas_jueves);
        lyt_jornadas[4] = (LinearLayout) findViewById(R.id.lyt_jornadas_viernes);
        lyt_jornadas[5] = (LinearLayout) findViewById(R.id.lyt_jornadas_sabado);
        lyt_jornadas[6] = (LinearLayout) findViewById(R.id.lyt_jornadas_domingo);

        switch_jornada_manana[0] = (SwitchCompat) findViewById(R.id.switch_jornada_manana_1);
        switch_jornada_manana[1] = (SwitchCompat) findViewById(R.id.switch_jornada_manana_2);
        switch_jornada_manana[2] = (SwitchCompat) findViewById(R.id.switch_jornada_manana_3);
        switch_jornada_manana[3] = (SwitchCompat) findViewById(R.id.switch_jornada_manana_4);
        switch_jornada_manana[4] = (SwitchCompat) findViewById(R.id.switch_jornada_manana_5);
        switch_jornada_manana[5] = (SwitchCompat) findViewById(R.id.switch_jornada_manana_6);
        switch_jornada_manana[6] = (SwitchCompat) findViewById(R.id.switch_jornada_manana_7);

        switch_jornada_tarde[0] = (SwitchCompat) findViewById(R.id.switch_jornada_tarde_1);
        switch_jornada_tarde[1] = (SwitchCompat) findViewById(R.id.switch_jornada_tarde_2);
        switch_jornada_tarde[2] = (SwitchCompat) findViewById(R.id.switch_jornada_tarde_3);
        switch_jornada_tarde[3] = (SwitchCompat) findViewById(R.id.switch_jornada_tarde_4);
        switch_jornada_tarde[4] = (SwitchCompat) findViewById(R.id.switch_jornada_tarde_5);
        switch_jornada_tarde[5] = (SwitchCompat) findViewById(R.id.switch_jornada_tarde_6);
        switch_jornada_tarde[6] = (SwitchCompat) findViewById(R.id.switch_jornada_tarde_7);

        lyt_hours_manana[0] = (LinearLayout) findViewById(R.id.lyt_hours_manana_1);
        lyt_hours_manana[1] = (LinearLayout) findViewById(R.id.lyt_hours_manana_2);
        lyt_hours_manana[2] = (LinearLayout) findViewById(R.id.lyt_hours_manana_3);
        lyt_hours_manana[3] = (LinearLayout) findViewById(R.id.lyt_hours_manana_4);
        lyt_hours_manana[4] = (LinearLayout) findViewById(R.id.lyt_hours_manana_5);
        lyt_hours_manana[5] = (LinearLayout) findViewById(R.id.lyt_hours_manana_6);
        lyt_hours_manana[6] = (LinearLayout) findViewById(R.id.lyt_hours_manana_7);

        lyt_hours_tarde[0] = (LinearLayout) findViewById(R.id.lyt_hours_tarde_1);
        lyt_hours_tarde[1] = (LinearLayout) findViewById(R.id.lyt_hours_tarde_2);
        lyt_hours_tarde[2] = (LinearLayout) findViewById(R.id.lyt_hours_tarde_3);
        lyt_hours_tarde[3] = (LinearLayout) findViewById(R.id.lyt_hours_tarde_4);
        lyt_hours_tarde[4] = (LinearLayout) findViewById(R.id.lyt_hours_tarde_5);
        lyt_hours_tarde[5] = (LinearLayout) findViewById(R.id.lyt_hours_tarde_6);
        lyt_hours_tarde[6] = (LinearLayout) findViewById(R.id.lyt_hours_tarde_7);

        btn_apply = (AppCompatButton) findViewById(R.id.btn_apply);

        spin_bh_from_manana[0] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_1);
        spin_bh_from_manana[1] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_2);
        spin_bh_from_manana[2] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_3);
        spin_bh_from_manana[3] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_4);
        spin_bh_from_manana[4] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_5);
        spin_bh_from_manana[5] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_6);
        spin_bh_from_manana[6] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_7);

        spin_bh_from_manana_a[0] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_a_1);
        spin_bh_from_manana_a[1] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_a_2);
        spin_bh_from_manana_a[2] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_a_3);
        spin_bh_from_manana_a[3] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_a_4);
        spin_bh_from_manana_a[4] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_a_5);
        spin_bh_from_manana_a[5] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_a_6);
        spin_bh_from_manana_a[6] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_manana_a_7);

        spin_bh_from_tarde[0] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_1);
        spin_bh_from_tarde[1] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_2);
        spin_bh_from_tarde[2] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_3);
        spin_bh_from_tarde[3] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_4);
        spin_bh_from_tarde[4] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_5);
        spin_bh_from_tarde[5] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_6);
        spin_bh_from_tarde[6] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_7);

        spin_bh_from_tarde_a[0] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_a_1);
        spin_bh_from_tarde_a[1] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_a_2);
        spin_bh_from_tarde_a[2] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_a_3);
        spin_bh_from_tarde_a[3] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_a_4);
        spin_bh_from_tarde_a[4] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_a_5);
        spin_bh_from_tarde_a[5] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_a_6);
        spin_bh_from_tarde_a[6] = (AppCompatSpinner) findViewById(R.id.spin_bh_from_tarde_a_7);

        ocultarLinearLayout();
        setVisibleLayout();
        btn_apply.setOnClickListener(this);
    }

    private void ocultarLinearLayout(){
        //ocultar los linear layout correspondientes
        for (int i = 0; i < 7; i++){
            lyt_jornadas[i].setVisibility(View.GONE);
            lyt_hours_manana[i].setVisibility(View.GONE);
            lyt_hours_tarde[i].setVisibility(View.GONE);
        }
    }

    private void setVisibleLayout(){
        for (int i = 0; i < 7; i ++){
            final int m = i;
            switch_open[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switch_open[m].isChecked()){
                        lyt_jornadas[m].setVisibility(View.VISIBLE);
                    }else{
                        lyt_jornadas[m].setVisibility(View.GONE);
                    }
                }
            });

            switch_jornada_manana[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(switch_jornada_manana[m].isChecked()){
                        lyt_hours_manana[m].setVisibility(View.VISIBLE);
                    }else{
                        lyt_hours_manana[m].setVisibility(View.GONE);
                    }
                }
            });

            switch_jornada_tarde[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(switch_jornada_tarde[m].isChecked()){
                        lyt_hours_tarde[m].setVisibility(View.VISIBLE);
                    }else{
                        lyt_hours_tarde[m].setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_apply:
                try {
                    recolectarDatosGUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private boolean validarDatosGUI(){
        boolean estado = false;
        for (int i = 0; i < switch_open.length; i++){
            if (switch_open[i].isChecked()){
                if(switch_jornada_manana[i].isChecked()){
                    if (spin_bh_from_manana[i].getSelectedItemPosition() == 0){
                        showSnackBar("debe seleccionar una hora");
                        spin_bh_from_manana[i].performClick();
                    }else{
                        if(spin_bh_from_manana_a[i].getSelectedItemPosition() == 0){
                            showSnackBar("debe seleccionar una hora");
                            spin_bh_from_manana_a[i].performClick();
                        }
                    }
                }

                if(switch_jornada_tarde[i].isChecked()){
                    if (spin_bh_from_tarde[i].getSelectedItemPosition() == 0){
                        showSnackBar("debe seleccionar una hora");
                        spin_bh_from_tarde[i].performClick();
                    }else{
                        if(spin_bh_from_tarde_a[i].getSelectedItemPosition() == 0){
                            showSnackBar("debe seleccionar una hora");
                            spin_bh_from_tarde_a[i].performClick();
                        }
                    }
                }

            }

        }
        return estado;
    }

    private void recolectarDatosGUI() throws JSONException {
        //mostrar el diálogo de progreso
        loading = ProgressDialog.show(this,"guardando...","Espere por favor...",false,false);

        array = new ArrayList<>();

        for (int i = 0; i < switch_open.length; i++){
            obj = new JSONObject();
            if (switch_open[i].isChecked()){
                obj.put("dia", i+1);

                if (switch_jornada_manana[i].isChecked()){
                    obj.put("jornadamaniana", 1);
                    obj.put("horaentradamaniana", spin_bh_from_manana[i].getSelectedItem().toString());
                    obj.put("horasalidamaniana", spin_bh_from_manana_a[i].getSelectedItem().toString());
                }

                if(switch_jornada_tarde[i].isChecked()){
                    obj.put("jornadatarde", 2);
                    obj.put("horaentradatarde", spin_bh_from_tarde[i].getSelectedItem().toString());
                    obj.put("horasalidatarde", spin_bh_from_tarde_a[i].getSelectedItem().toString());
                }
            }

            SerializarHorario is = gson.fromJson(obj.toString(), SerializarHorario.class);
            array.add(is);
        }


        Log.i("TAG", "ARRAY==>" +array.toString());

        String codfuncionario = Preferences.getPreferenceString(this, Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();// Mapeo previo
        map.put("codfuncionario", codfuncionario);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        // Depurando objeto Json...
        Log.i(TAG, "map.." + map.toString());
        Log.d(TAG, "json tema..."+jobject);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERTAR_HORARIO_FUNCIONARIO,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Ver Response-->"+response);
                                // Procesar la respuesta del servidor
                                procesarRespuestaInsertar(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //descartar el diálogo de progreso
                                loading.dismiss();
                                Log.e(TAG, "Error Volley: " + error.getMessage());
                                Utilidades.showToast(AgregarHorarioActivity.this, "Error Volley: " + error.getMessage());
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
     * Procesa la respuesta obtenida desde el sevidor
     *
     * @param response Objeto Json
     */
    private void procesarRespuestaInsertar(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    for (int i = 0; i < array.size(); i++){
                        if (array.get(i).getJornadamaniana() != null){
                            insertarDelleHorario(array.get(i).getDia(), mensaje, array.get(i).getHoraentradamaniana(), array.get(i).getHorasalidamaniana(), array.get(i).getJornadamaniana());
                        }

                        if (array.get(i).getJornadatarde() != null){
                            insertarDelleHorario(array.get(i).getDia(), mensaje, array.get(i).getHoraentradatarde(), array.get(i).getHorasalidatarde(), array.get(i).getJornadatarde());
                        }

                        isCantidad = i;
                    }
                    break;

                case "2":
                    //descartar el diálogo de progreso
                    loading.dismiss();
                    // Mostrar mensaje
                    showSnackBar(mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void insertarDelleHorario(String coddia, String codhorario, String horaentrada,String horasalida, String codjornada){

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();// Mapeo previo
        map.put("coddia", coddia);
        map.put("codhorario", codhorario);
        map.put("horaentrada", horaentrada);
        map.put("horasalida", horasalida);
        map.put("codjornada", codjornada);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        // Depurando objeto Json...
        Log.i(TAG, "map.." + map.toString());
        Log.d(TAG, "json tema..."+jobject);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERTAR_DETALLE_HORARIO_FUNCIONARIO,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Ver Response-->"+response);
                                // Procesar la respuesta del servidor
                                procesarRespuestaInsertarDetalle(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //descartar el diálogo de progreso
                                Log.e(TAG, "Error Volley: " + error.getMessage());
                                showSnackBar("Error Volley: " + error.getMessage());
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
     * Procesa la respuesta obtenida desde el sevidor
     *
     * @param response Objeto Json
     */
    private void procesarRespuestaInsertarDetalle(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    if(isCantidad == (array.size() - 1)){
                        loading.dismiss();
                        for (int j = 0; j < switch_open.length; j++){
                            switch_open[j].setChecked(false);
                            switch_jornada_tarde[j].setChecked(false);
                            switch_jornada_manana[j].setChecked(false);
                        }
                        ocultarLinearLayout();

                        showSnackBar(mensaje);
                    }
                    break;

                case "2":
                    //descartar el diálogo de progreso
                    loading.dismiss();
                    // Mostrar mensaje
                    showSnackBar(mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator_horario), msg, Snackbar.LENGTH_LONG)
                .show();
    }
}
