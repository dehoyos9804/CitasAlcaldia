package co.com.learn.code.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.com.learn.code.Adapter.AdaptadorCitasFuncionario;
import co.com.learn.code.Models.CitasFuncionario;
import co.com.learn.code.Models.HorarioFuncionario;
import co.com.learn.code.Models.SerializarHorario;
import co.com.learn.code.R;
import co.com.learn.code.ui.AgregarHorarioActivity;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class HorarioFragment extends Fragment implements View.OnClickListener {

    //etiqueta para la depuracion
    private static final String TAG = UsuariosFragment.class.getSimpleName();
    private Gson gson = new Gson();
    private TextView data_empty_horario;
    private CardView[] liner_dias = new CardView[7];
    private FloatingActionButton fab_horario;
    private TextView[] txtHorarioInicialManana = new TextView[7];
    private TextView[] txtHorarioFinalManana = new TextView[7];
    private TextView[] txtHorarioInicialTarde = new TextView[7];
    private TextView[] txtHorarioFinalTarde = new TextView[7];
    private ProgressDialog loading = null;

    private View view;
    private boolean isHorario = false;

    //costructor del fragmento
    public HorarioFragment() {

    }

    /**
     * Crea una instancia prefabricada de {@link UsuariosFragment}
     *
     * @return Instancia dle fragmento
     */
    public static HorarioFragment newInstance() {
        HorarioFragment fragment = new HorarioFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_section_horario, container, false);

        init();//iniciar componentes

        return view;
    }

    private void init(){
        data_empty_horario = (TextView) view.findViewById(R.id.data_empty_horario);

        liner_dias[0] = (CardView) view.findViewById(R.id.liner_lunes);
        liner_dias[1] = (CardView) view.findViewById(R.id.liner_martes);
        liner_dias[2] = (CardView) view.findViewById(R.id.liner_miercoles);
        liner_dias[3] = (CardView) view.findViewById(R.id.liner_jueves);
        liner_dias[4] = (CardView) view.findViewById(R.id.liner_viernes);
        liner_dias[5] = (CardView) view.findViewById(R.id.liner_sabado);
        liner_dias[6] = (CardView) view.findViewById(R.id.liner_domingo);

        fab_horario = (FloatingActionButton) view.findViewById(R.id.fab_horario);

        txtHorarioInicialManana[0] = (TextView) view.findViewById(R.id.txtHorarioInicialManana_1);
        txtHorarioInicialManana[1] = (TextView) view.findViewById(R.id.txtHorarioInicialManana_2);
        txtHorarioInicialManana[2] = (TextView) view.findViewById(R.id.txtHorarioInicialManana_3);
        txtHorarioInicialManana[3] = (TextView) view.findViewById(R.id.txtHorarioInicialManana_4);
        txtHorarioInicialManana[4] = (TextView) view.findViewById(R.id.txtHorarioInicialManana_5);
        txtHorarioInicialManana[5] = (TextView) view.findViewById(R.id.txtHorarioInicialManana_6);
        txtHorarioInicialManana[6] = (TextView) view.findViewById(R.id.txtHorarioInicialManana_7);

        txtHorarioFinalManana[0] = (TextView) view.findViewById(R.id.txtHorarioFinalManana_1);
        txtHorarioFinalManana[1] = (TextView) view.findViewById(R.id.txtHorarioFinalManana_2);
        txtHorarioFinalManana[2] = (TextView) view.findViewById(R.id.txtHorarioFinalManana_3);
        txtHorarioFinalManana[3] = (TextView) view.findViewById(R.id.txtHorarioFinalManana_4);
        txtHorarioFinalManana[4] = (TextView) view.findViewById(R.id.txtHorarioFinalManana_5);
        txtHorarioFinalManana[5] = (TextView) view.findViewById(R.id.txtHorarioFinalManana_6);
        txtHorarioFinalManana[6] = (TextView) view.findViewById(R.id.txtHorarioFinalManana_7);

        txtHorarioInicialTarde[0] = (TextView) view.findViewById(R.id.txtHorarioInicialTarde_1);
        txtHorarioInicialTarde[1] = (TextView) view.findViewById(R.id.txtHorarioInicialTarde_2);
        txtHorarioInicialTarde[2] = (TextView) view.findViewById(R.id.txtHorarioInicialTarde_3);
        txtHorarioInicialTarde[3] = (TextView) view.findViewById(R.id.txtHorarioInicialTarde_4);
        txtHorarioInicialTarde[4] = (TextView) view.findViewById(R.id.txtHorarioInicialTarde_5);
        txtHorarioInicialTarde[5] = (TextView) view.findViewById(R.id.txtHorarioInicialTarde_6);
        txtHorarioInicialTarde[6] = (TextView) view.findViewById(R.id.txtHorarioInicialTarde_7);

        txtHorarioFinalTarde[0] = (TextView) view.findViewById(R.id.txtHorarioFinalTarde_1);
        txtHorarioFinalTarde[1] = (TextView) view.findViewById(R.id.txtHorarioFinalTarde_2);
        txtHorarioFinalTarde[2] = (TextView) view.findViewById(R.id.txtHorarioFinalTarde_3);
        txtHorarioFinalTarde[3] = (TextView) view.findViewById(R.id.txtHorarioFinalTarde_4);
        txtHorarioFinalTarde[4] = (TextView) view.findViewById(R.id.txtHorarioFinalTarde_5);
        txtHorarioFinalTarde[5] = (TextView) view.findViewById(R.id.txtHorarioFinalTarde_6);
        txtHorarioFinalTarde[6] = (TextView) view.findViewById(R.id.txtHorarioFinalTarde_7);

        ocultarLayer();
        llenarDatos();

        fab_horario.setOnClickListener(this);
    }

    private void ocultarLayer(){
        for (int i = 0; i < liner_dias.length; i++){
            liner_dias[i].setVisibility(View.GONE);//oculto todas las vista
        }
    }

    /**
     * Proyecta una {@link Snackbar} con el string usado
     * @param msg Mensaje
     */
    private void showSnackBar(String msg) {
        Snackbar
                .make(view.findViewById(R.id.coordinator_add_horario), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    public void llenarDatos(){
        loading = ProgressDialog.show(getContext(),"Cargando.","Espere por favor...",false,false);
        String newURL = Constantes.GET_HORARIO_FUNCIONARIO + "?codfuncionario=" + Preferences.getPreferenceString(getActivity(), Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);
        //petición GET
        VolleySingleton.
                getInstance(getContext()).
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
                                        loading.dismiss();
                                        showSnackBar("Error de red: " + error.getLocalizedMessage());
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
                        JSONArray mensaje = response.getJSONArray("tblfuncionarios");
                        // Parsear con Gson
                        HorarioFuncionario[] horario = gson.fromJson(mensaje.toString(), HorarioFuncionario[].class);
                        // Inicializar adaptador
                        agregarHorario(Arrays.asList(horario));
                        loading.dismiss();
                        data_empty_horario.setText("");
                        isHorario = true;

                    } catch (JSONException e) {
                        Log.i(TAG, "Error al llenar Adaptador " + e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    data_empty_horario.setText(mensaje2);
                    //data_empty.setText("");
                    showSnackBar(mensaje2);
                    loading.dismiss();
                    break;
            }
        }catch (JSONException je){
            Log.d(TAG, je.getMessage());
        }
    }

    private void agregarHorario(List<HorarioFuncionario> h){
        if(h.size() != 0) {
            for (int m = 0; m < h.size(); m++) {
                for (int j = 0; j < liner_dias.length; j++) {
                    if (h.get(m).getCoddia().equalsIgnoreCase(String.valueOf(j + 1))) {
                        liner_dias[j].setVisibility(View.VISIBLE);
                        if (h.get(m).getCodjornada().equalsIgnoreCase("1")) {
                            txtHorarioInicialManana[j].setText("DE " + h.get(m).getHoraentrada() + " AM");
                            txtHorarioFinalManana[j].setText("HASTA " + h.get(m).getHorasalida() + " PM");
                        }

                        if (h.get(m).getCodjornada().equalsIgnoreCase("2")) {
                            txtHorarioInicialTarde[j].setText("DE " + h.get(m).getHoraentrada() + " PM");
                            txtHorarioFinalTarde[j].setText("HASTA " + h.get(m).getHorasalida() + " PM");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_horario:
                if(isHorario){
                    showSnackBar("Ya Registro Su Horario");
                }else{
                    Intent i = new Intent(getActivity(), AgregarHorarioActivity.class);
                    startActivity(i);
                }
                break;
        }
    }
}
