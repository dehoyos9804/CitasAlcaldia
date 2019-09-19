package co.com.learn.code.ui;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import co.com.learn.code.Models.IniciarSesion;
import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Etiqueta para la depuracion
    private static final String TAG = MainActivity.class.getSimpleName();

    //atributos y variables
    private Gson gson = new Gson();
    private EditText txtUsuario;
    private EditText txtClave;
    private Button btnIniciarSesion;
    private TextView txtOlvidasteContraseña;
    private TextView txtCrearCuenta;

    private ProgressDialog loading = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicio las preferencias guardadas
        if(Preferences.getPreferenceBoolean(this, Constantes.PREFERENCIA_SESION_CLAVE)){
            Intent intent = new Intent(this, InitialUsuarioActivity.class);
            startActivity(intent);
            finish();//cierro la actividad de iniciar sesion
        }

        //cambiar el titulo del toolbar
        getSupportActionBar().setTitle(R.string.app_name_main);
        //iniciar atributos
        init();
    }

    //método que permite iniciar los componentes
    private void init(){
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtClave = (EditText) findViewById(R.id.txtClave);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        txtOlvidasteContraseña = (TextView) findViewById(R.id.txtOlvidasteContraseña);
        txtCrearCuenta = (TextView) findViewById(R.id.txtCrearCuenta);

        //implementar escuchadores
        btnIniciarSesion.setOnClickListener(this);
        txtOlvidasteContraseña.setOnClickListener(this);
        txtCrearCuenta.setOnClickListener(this);
    }

    //evento onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIniciarSesion:
                if(validarDatos()){
                    peticionHTTP(txtUsuario.getText().toString().trim(),txtClave.getText().toString().trim());
                }
                //Intent intent = new Intent(this, InitialUsuarioActivity.class);
                //startActivity(intent);
                break;
            case R.id.txtOlvidasteContraseña:
                Utilidades.showToast(this, "Recuperando Contraseña...");
                break;
            case R.id.txtCrearCuenta:
                Intent i = new Intent(this, RegistrarUsuarioActivity.class);
                if(Utilidades.materialDesign()){
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                }else{
                    startActivity(i);
                }
                break;
        }
    }

    private void limpiar(){
        txtUsuario.setText("");
        txtClave.setText("");
    }

    //método que permite validar los campos
    private boolean validarDatos(){
        boolean  estado = false;

        if (txtUsuario.getText().toString().trim().isEmpty()){
            Utilidades.showToast(this, "Campo Usuario Vacio");
            txtUsuario.requestFocus();
            estado = false;
        }else{
            if (txtClave.getText().toString().trim().isEmpty()){
                Utilidades.showToast(this, "Campo Contraseña Vacio");
                txtClave.requestFocus();
                estado = false;
            }else{
                estado = true;
            }
        }

        return estado;
    }

    private void peticionHTTP(String usuario, String clave){

        //Añadir parametros a la URL de webservice
        String newURL = Constantes.GET_INICIAR_SESION + "?usuario=" + usuario + "&clave=" + clave;

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
                                        procesarRespuesta(response);
                                        Log.i(TAG, "processanddo respuesta..." + response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //descartar el diálogo de progreso
                                        loading.dismiss();
                                        Utilidades.showToast(MainActivity.this, "Error de red: " + error.getLocalizedMessage());
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
                        JSONObject datos = response.getJSONObject("tbliniciosesion");
                        //Parsear objeto
                        IniciarSesion is = gson.fromJson(datos.toString(),IniciarSesion.class);
                        Log.i("TAG","tag-->"+is.getNumerocedula());
                        loading.dismiss();//finalizo el dialogo
                        //iniciamos la sesion
                        AutenticacionValida(is.getNumerocedula(),is.getNombres(),is.getApellidos(),is.getTelefono(),is.getCorreo(),is.getTipousuario());

                    }catch (JSONException e){
                        Log.i(TAG,"Error al llenar Adaptador " +e.getLocalizedMessage());
                    }
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    loading.dismiss();//finalizo el dialogo
                    limpiar();
                    Utilidades.showToast(this, mensaje2);
                    break;
                case "3":
                    String mensaje3 = response.getString("mensaje");
                    loading.dismiss();//finalizo el dialogo
                    Utilidades.showToast(this, mensaje3);
                    break;
                case "4":
                    String mensaje4 = response.getString("mensaje");
                    loading.dismiss();//finalizo el dialogo
                    Utilidades.showToast(this, mensaje4);
                    break;
            }
        }catch (JSONException je){
            Log.d(TAG, je.getMessage());
        }
    }

    private void AutenticacionValida(String numerocedula, String nombres, String apellidos, String telefono, String correo, String tipousuario){
        switch (tipousuario){
            case "USUARIO":
                Intent usuario = new Intent(this, InitialUsuarioActivity.class);

                Preferences.savePreferenceString(this, numerocedula, Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);
                Preferences.savePreferenceString(this, nombres, Constantes.PREFERENCIA_NOMBRES_CLAVE);
                Preferences.savePreferenceString(this, apellidos, Constantes.PREFERENCIA_APELLIDOS_CLAVE);
                Preferences.savePreferenceString(this, telefono, Constantes.PREFERENCIA_TELEFONOS_CLAVE);
                Preferences.savePreferenceString(this, correo, Constantes.PREFERENCIA_CORREO_CLAVE);
                Preferences.savePreferenceString(this, tipousuario, Constantes.PREFERENCIA_TIPO_USUARIO_CLAVE);
                Preferences.savePreferenceBoolean(this, Constantes.ESTADO_PREFERENCIA_TRUE, Constantes.PREFERENCIA_SESION_CLAVE);

                startActivity(usuario);
                finish();//finalizar actividad
                break;
            case "FUNCIONARIO":
                Utilidades.showToast(this, "Actividad del funcionario");
                break;
        }
    }
}
