package co.com.learn.code.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import co.com.learn.code.R;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class RegistrarUsuarioFuncionarioActivity extends AppCompatActivity implements View.OnClickListener {
    //etiqueta para la depuracion
    private static final String TAG = RegistrarUsuarioFuncionarioActivity.class.getSimpleName();

    private EditText txtIdentificacion;
    private EditText txtNombres;
    private EditText txtApellidos;
    private EditText txtTelefonos;
    private EditText txtDireccion;
    private EditText txtCorreo;
    private EditText txtClave;
    private CheckBox checkTerminos;
    private TextView txtTerminosCondiciones;
    private Button btnCancelar;
    private Button btnGuardar;

    //progress dialog
    private ProgressDialog loading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        //iniciar componente
        init();
    }

    private void init(){
        txtIdentificacion = (EditText) findViewById(R.id.txtIdentificacion);
        txtNombres = (EditText) findViewById(R.id.txtNombres);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtTelefonos = (EditText) findViewById(R.id.txtTelefonos);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtClave = (EditText) findViewById(R.id.txtClave);
        checkTerminos = (CheckBox) findViewById(R.id.checkTerminos);
        txtTerminosCondiciones = (TextView) findViewById(R.id.txtTerminosCondiciones);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        //se agrega la contraseña por defecto para cada funcionario
        txtClave.setText("password");

        //agregar escuchador
        txtTerminosCondiciones.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txtTerminosCondiciones:
                Utilidades.showToast(this, "Leer terminos y condiciones");
                break;
            case R.id.btnCancelar:
                finish();
                break;
            case R.id.btnGuardar:
                if(validarFormulario()){
                    guardarProductor();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Método que permite validar si algún campo esta sin llenar
     * */
    private boolean validarFormulario(){
        boolean estado = false;
        if(txtIdentificacion.getText().toString().trim().length() == 0){
            Utilidades.showToast(this, "Campo identificacion Vacio");
            txtIdentificacion.requestFocus();
        }else{
            if(txtNombres.getText().toString().trim().length() == 0){
                Utilidades.showToast(this, "Campo Nombre Vacio");
                txtNombres.requestFocus();
            }else{
                if(txtApellidos.getText().toString().trim().length() == 0){
                    Utilidades.showToast(this, "Campo Apellido Vacio");
                    txtApellidos.requestFocus();
                }else{
                    if (txtTelefonos.getText().toString().trim().length() == 0){
                        Utilidades.showToast(this, "Campo Telefono vacio");
                        txtTelefonos.requestFocus();
                    }else{
                        if(txtDireccion.getText().toString().trim().length() == 0){
                            Utilidades.showToast(this, "Campo Direccion Vacio");
                            txtDireccion.requestFocus();
                        }else{
                            if(txtCorreo.getText().toString().trim().length() == 0){
                                Utilidades.showToast(this, "Campo Correo Vacio");
                                txtCorreo.requestFocus();
                            }else{
                                if(txtClave.getText().toString().trim().length() == 0){
                                    Utilidades.showToast(this, "Campo Contraseña Vacio");
                                    txtClave.requestFocus();
                                }else{
                                    if (!checkTerminos.isChecked()){
                                        Utilidades.showToast(this, "Acepte los Terminos Y Condiciones");
                                    }else{
                                        estado = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return estado;
    }

    /*
     * método que permite limpiar el formulario
     * */
    private void limpiar(){
        txtIdentificacion.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefonos.setText("");
        txtDireccion.setText("");
        txtCorreo.setText("");
        txtClave.setText("");
        checkTerminos.setChecked(false);
    }

    /**
     * Guarda los cambios de una meta editada.
     * <p>
     * Si está en modo inserción, entonces crea una nueva
     * meta en la base de datos
     */
    public void guardarProductor() {
        //mostrar el diálogo de progreso
        loading = ProgressDialog.show(this,"guardando...","Espere por favor...",false,false);

        String idusua = txtIdentificacion.getText().toString().trim();
        String nomb = txtNombres.getText().toString().trim();
        String ape = txtApellidos.getText().toString().trim();
        String telefo = txtTelefonos.getText().toString().trim();
        String direcc = txtDireccion.getText().toString().trim();
        String email = txtCorreo.getText().toString().trim();
        String cla = txtClave.getText().toString().trim();
        String codfuncionario = Preferences.getPreferenceString(this, Constantes.PREFERENCIA_IDENTIFICACION_CLAVE);

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();// Mapeo previo
        map.put("idusuario",idusua);
        map.put("nombres", nomb);
        map.put("apellidos", ape);
        map.put("telefonos", telefo);
        map.put("direccion", direcc);
        map.put("correo", email);
        map.put("clave", cla);
        map.put("codfuncionario", codfuncionario);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        // Depurando objeto Json...
        Log.i(TAG, "map.." + map.toString());
        Log.d(TAG, "json productor..."+jobject);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERTAR_USUARIO,
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
                                Utilidades.showToast(RegistrarUsuarioFuncionarioActivity.this, "Error Volley: " + error.getMessage());
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
                    loading.dismiss();
                    // Mostrar mensaje
                    Utilidades.showToast(this, mensaje);
                    // Enviar código de éxito
                    setResult(Activity.RESULT_OK);
                    limpiar();
                    // Terminar actividad
                    finish();
                    break;

                case "2":
                    //descartar el diálogo de progreso
                    loading.dismiss();
                    // Mostrar mensaje
                    Utilidades.showToast(this, mensaje);
                    // Enviar código de falla
                    setResult(Activity.RESULT_CANCELED);
                    limpiar();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
