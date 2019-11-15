package co.com.learn.code.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import co.com.learn.code.Adapter.AdaptadorCitasFuncionario;
import co.com.learn.code.Adapter.AdaptadorUsuarios;
import co.com.learn.code.Models.CitasFuncionario;
import co.com.learn.code.Models.Usuarios;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class CitasFragment  extends Fragment implements View.OnClickListener {

    //etiqueta para la depuracion
    private static final String TAG = CitasFragment.class.getSimpleName();
    private Gson gson = new Gson();
    //adaptador del RecicleView
    private AdaptadorCitasFuncionario adapter;
    //instancia global de RecicleView
    private RecyclerView recyclerView;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //instancia del progress dialog
    private static ProgressDialog loading = null;

    private View view;

    private TextView data_empty;
    private CalendarView mi_calendario;
    private String fechaReal = "";

    //costructor del fragmento
    public CitasFragment() {

    }

    /**
     * Crea una instancia prefabricada de {@link CitasFragment}
     *
     * @return Instancia dle fragmento
     */
    public static CitasFragment newInstance() {
        CitasFragment fragment = new CitasFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_section_citas, container, false);

        init();//iniciar componentes

        return view;
    }

    private void init(){
        mi_calendario = (CalendarView) view.findViewById(R.id.mi_calendario);
        data_empty = (TextView) view.findViewById(R.id.data_empty);

        recyclerView = (RecyclerView) view.findViewById(R.id.reciclador_citas);
        recyclerView.setHasFixedSize(true);
        //Usar el Administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lManager);

        //colocar la fecha y hora actual
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        //txtDiaActual.setText((currentYear + "-" + currentMonth + "-" + currentDay));
        SimpleDateFormat fechaactual = new SimpleDateFormat("yyyy-MM-dd");
        String formatoFecha = fechaactual.format(localCalendar.getTime());

        llenarDatos(formatoFecha);

        //eventos
        mi_calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
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
                llenarDatos(outDate);
            }
        });
    }

    public void llenarDatos(String fecha){
        loading = ProgressDialog.show(getContext(),"Cargando.","Espere por favor...",false,false);
        String newURL = Constantes.GET_ALL_CITAS_FUNCIONARIOS + "?codfuncionario=" + Preferences.getPreferenceString(getActivity(), Constantes.PREFERENCIA_IDENTIFICACION_CLAVE) + "&fecha=" + fecha;
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
                        JSONArray mensaje = response.getJSONArray("tblcitas");
                        // Parsear con Gson
                        CitasFuncionario[] citas = gson.fromJson(mensaje.toString(), CitasFuncionario[].class);
                        // Inicializar adaptador
                        adapter = new AdaptadorCitasFuncionario(Arrays.asList(citas), getContext());
                        // Setear adaptador a la lista
                        recyclerView.setAdapter(adapter);
                        data_empty.setText("");
                        loading.dismiss();

                    } catch (JSONException e) {
                        Log.i(TAG, "Error al llenar Adaptador " + e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    //data_empty.setText("");
                    showSnackBar(mensaje2);
                    adapter = new AdaptadorCitasFuncionario(null, getContext());;
                    recyclerView.setAdapter(adapter);
                    data_empty.setText("No hay citas programadas para esta fecha");
                    loading.dismiss();
                    break;
            }
        }catch (JSONException je){
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
                .make(view.findViewById(R.id.coordinator_citas), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onClick(View v) {

    }
}
