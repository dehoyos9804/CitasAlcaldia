package co.com.learn.code.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import co.com.learn.code.Adapter.AdaptadorTemas;
import co.com.learn.code.Models.TemasFuncionario;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class TemasFragment extends Fragment implements View.OnClickListener {
    //etiqueta para la depuracion
    private static final String TAG = TemasFragment.class.getSimpleName();
    private Gson gson = new Gson();
    //adaptador del RecicleView
    private AdaptadorTemas adapter;
    //instancia global de RecicleView
    private RecyclerView recyclerView;
    //private TextView emptyView;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //instancia del progress dialog
    private static ProgressDialog loading = null;
    private static ProgressDialog progress = null;

    private TextView data_empty;
    private FloatingActionButton fab;
    private EditText txtTema;
    private Spinner spinner_duracion;
    private Button btnCancelar;
    private Button btnEnviar;

    private String duracion = "";

    private View view;

    private Dialog dialog = null;


    //costructor del fragmento
    public TemasFragment() {

    }

    /**
     * Crea una instancia prefabricada de {@link TemasFragment}
     *
     * @return Instancia dle fragmento
     */
    public static TemasFragment newInstance() {
        TemasFragment fragment = new TemasFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_section_temas, container, false);

        init();//iniciar componentes

        return view;
    }

    private void init(){
        data_empty = (TextView) view.findViewById(R.id.data_empty);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        recyclerView = (RecyclerView) view.findViewById(R.id.reciclador_temas);
        recyclerView.setHasFixedSize(true);

        //Usar el Administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lManager);

        //llenarDatos();

        //agrego los escuchadores
        fab.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        llenarDatos();
    }

    public void llenarDatos(){
        loading = ProgressDialog.show(getContext(),"Cargando.","Espere por favor...",false,false);
        //Añadir parametros a la URL de webservice
        String newURL = Constantes.GET_ALL_TEMAS_FUNCIONARIO + "?codfuncionario=" + Preferences.getPreferenceString(getContext(), Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);

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
                        JSONArray mensaje = response.getJSONArray("tbltemas");
                        // Parsear con Gson
                        TemasFuncionario[] temas = gson.fromJson(mensaje.toString(), TemasFuncionario[].class);
                        // Inicializar adaptador
                        adapter = new AdaptadorTemas(Arrays.asList(temas), getContext());
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
                    data_empty.setText("");
                    showSnackBar(mensaje2);
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
                .make(view.findViewById(R.id.coordinator_tema), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                showDialogRegistrarTema();
                break;
            case R.id.btnCancelar:
                dialog.dismiss();//elimino el dialogo
                break;
            case R.id.btnEnviar:
                if (validarDatos()){
                    guardarTema();
                }
                break;
        }
    }

    private boolean validarDatos(){
        boolean estado = false;
            if(txtTema.getText().toString().trim().isEmpty()){
                Utilidades.showToast(getActivity(),"Debe Agregar un Tema");
                txtTema.requestFocus();
            }else{
                if(spinner_duracion.getSelectedItemPosition() == 0){
                    Utilidades.showToast(getActivity(),"Debe seleccionar una duracion aproximada del tema");
                    spinner_duracion.performClick();
                }else{
                    estado = true;
                }
            }
        return estado;
    }

    /**permite abrir un dialogo para poder registrar los temas*/
    private void showDialogRegistrarTema() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_temas);

        //array para llenar la duracion de los temas
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.duracion_tema, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        txtTema = (EditText) dialog.findViewById(R.id.txtTema);
        spinner_duracion = (Spinner) dialog.findViewById(R.id.spinner_duracion);
        btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
        btnEnviar = (Button) dialog.findViewById(R.id.btnEnviar);

        //lleno el spinner
        spinner_duracion.setAdapter(arrayAdapter);

        spinner_duracion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        duracion = "5";
                        break;
                    case 2:
                        duracion = "10";
                        break;
                    case 3:
                        duracion = "15";
                        break;
                    case 4:
                        duracion = "20";
                        break;
                    case 5:
                        duracion = "25";
                        break;
                    case 6:
                        duracion = "30";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //agregar los escuchadores
        btnCancelar.setOnClickListener(this);
        btnEnviar.setOnClickListener(this);

        dialog.show();//abro el dialogo
    }

    /**
     * Guarda los cambios de una meta editada.
     * <p>
     * Si está en modo inserción, entonces crea una nueva
     * meta en la base de datos
     */
    public void guardarTema() {
        //mostrar el diálogo de progreso
        progress = ProgressDialog.show(getActivity(),"guardando...","Espere por favor...",false,false);

        String coddependencia = Preferences.getPreferenceString(getActivity(), Constantes.PREFERENCIA_COD_DEPENDENCIA);
        String tema = txtTema.getText().toString().trim();
        String codfuncionario = Preferences.getPreferenceString(getActivity(), Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();// Mapeo previo
        map.put("coddependencia",coddependencia);
        map.put("tema", tema);
        map.put("duracion", duracion);
        map.put("codfuncionario", codfuncionario);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        // Depurando objeto Json...
        Log.i(TAG, "map.." + map.toString());
        Log.d(TAG, "json tema..."+jobject);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERTAR_TEMAS,
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
                                progress.dismiss();
                                Log.e(TAG, "Error Volley: " + error.getMessage());
                                Utilidades.showToast(getActivity(), "Error Volley: " + error.getMessage());
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
                    //descartar el diálogo de progreso
                    progress.dismiss();
                    //descarto el dialogo
                    dialog.dismiss();
                    llenarDatos();
                    // Mostrar mensaje
                    showSnackBar(mensaje);
                    break;

                case "2":
                    //descartar el diálogo de progreso
                    progress.dismiss();
                    //descartar el diálogo
                    dialog.dismiss();
                    // Mostrar mensaje
                    showSnackBar(mensaje);
                    break;
                case "3":
                    //descartar el diálogo de progreso
                    progress.dismiss();
                    //descartar el diálogo
                    dialog.dismiss();
                    // Mostrar mensaje
                    showSnackBar(mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
