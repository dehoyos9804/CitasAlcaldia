package co.com.learn.code.fragment;

import android.app.ActivityOptions;
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

import co.com.learn.code.Adapter.AdaptadorTemas;
import co.com.learn.code.Adapter.AdaptadorUsuarios;
import co.com.learn.code.Models.TemasFuncionario;
import co.com.learn.code.Models.Usuarios;
import co.com.learn.code.R;
import co.com.learn.code.ui.RegistrarUsuarioActivity;
import co.com.learn.code.ui.RegistrarUsuarioFuncionarioActivity;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class UsuariosFragment extends Fragment implements View.OnClickListener {

    //etiqueta para la depuracion
    private static final String TAG = UsuariosFragment.class.getSimpleName();
    private Gson gson = new Gson();
    //adaptador del RecicleView
    private AdaptadorUsuarios adapter;
    //instancia global de RecicleView
    private RecyclerView recyclerView;
    //instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    //instancia del progress dialog
    private static ProgressDialog loading = null;

    private View view;

    private TextView data_empty;
    private FloatingActionButton fab_usuario;

    //costructor del fragmento
    public UsuariosFragment() {

    }

    /**
     * Crea una instancia prefabricada de {@link UsuariosFragment}
     *
     * @return Instancia dle fragmento
     */
    public static UsuariosFragment newInstance() {
        UsuariosFragment fragment = new UsuariosFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_section_usuarios, container, false);

        init();//iniciar componentes

        return view;
    }

    private void init(){
        data_empty = (TextView) view.findViewById(R.id.data_empty);
        fab_usuario = (FloatingActionButton) view.findViewById(R.id.fab_usuario);

        recyclerView = (RecyclerView) view.findViewById(R.id.reciclador_usuario);
        recyclerView.setHasFixedSize(true);

        //Usar el Administrador para LinearLayout
        lManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lManager);

        llenarDatos();

        //agrego los escuchadores
        fab_usuario.setOnClickListener(this);
    }

    public void llenarDatos(){
        loading = ProgressDialog.show(getContext(),"Cargando.","Espere por favor...",false,false);

        //petición GET
        VolleySingleton.
                getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                Constantes.GET_ALL_USUARIOS_FUNCIONARIOS,
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
                        Usuarios[] usuarios = gson.fromJson(mensaje.toString(), Usuarios[].class);
                        // Inicializar adaptador
                        adapter = new AdaptadorUsuarios(Arrays.asList(usuarios), getContext());
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
                .make(view.findViewById(R.id.coordinator_usuario), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_usuario:
                Intent i = new Intent(getActivity(), RegistrarUsuarioFuncionarioActivity.class);
                if(Utilidades.materialDesign()){
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }else{
                    startActivity(i);
                }
                break;
        }
    }
}
