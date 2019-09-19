package co.com.learn.code.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
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
import co.com.learn.code.ui.InitialUsuarioActivity;
import co.com.learn.code.ui.ListaDependenciaActivity;
import co.com.learn.code.ui.RegistrarUsuarioActivity;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class DialogDatosCitas {
    /**
     * Método que permite crear el dialogo de para finalizar la cita
     **/
    public static void showDialogCitas(final Activity activity, final String fecha, final String horainicial, final String horafinal, final int codigousuario,
                                       final int coddependencia, final int codtema, String nombredependencia, String nombreTema){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_generar_new_citas);

        TextView txtDepartamento = (TextView) dialog.findViewById(R.id.txtDepartamento);
        TextView txtTema = (TextView) dialog.findViewById(R.id.txtTema);
        TextView txtFecha = (TextView) dialog.findViewById(R.id.txtFecha);
        TextView txtHora = (TextView) dialog.findViewById(R.id.txtHora);
        Spinner spinnerNotificacion = (Spinner) dialog.findViewById(R.id.spinnerNotificacion);
        Button btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
        Button btnEnviar = (Button) dialog.findViewById(R.id.btnEnviar);

        txtDepartamento.setText(nombredependencia);
        txtTema.setText(nombreTema);
        txtFecha.setText(fecha);
        txtHora.setText((horainicial + " - " + horafinal));

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//cierro el dialogo
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostrar el diálogo de progreso
                final ProgressDialog progressDialog = ProgressDialog.show(activity,"guardando...","Espere por favor...",false,false);

                String codusuario = String.valueOf(codigousuario);
                String codigodependencia = String.valueOf(coddependencia);
                String codigotema = String.valueOf(codtema);

                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();// Mapeo previo
                map.put("fecha",fecha);
                map.put("horainicial", horainicial);
                map.put("horafinal", horafinal);
                map.put("codigousuario", codusuario);
                map.put("notificacion", "30");
                map.put("coddependencia", codigodependencia);
                map.put("codtema", codigotema);

                // Crear nuevo objeto Json basado en el mapa
                JSONObject jobject = new JSONObject(map);
                // Depurando objeto Json...
                //Log.i(TAG, "map.." + map.toString());
                //Log.d(TAG, "json productor..."+jobject);

                // Actualizar datos en el servidor
                VolleySingleton.getInstance(activity).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_CITA,
                                jobject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            // Obtener estado
                                            String estado = response.getString("estado");
                                            // Obtener mensaje
                                            String mensaje = response.getString("mensaje");

                                            switch (estado) {
                                                case "1":
                                                    //descartar el diálogo de progreso
                                                    progressDialog.dismiss();
                                                    // Mostrar mensaje
                                                    Utilidades.showToast(activity, mensaje);
                                                    // Enviar código de éxito
                                                    //setResult(Activity.RESULT_OK);
                                                    //limpiar();
                                                    // Terminar actividad
                                                    dialog.dismiss();//cierro el dialogo
                                                    Intent intent = new Intent(activity, InitialUsuarioActivity.class);
                                                    activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP));//finaliza las actividades en pila
                                                    break;

                                                case "2":
                                                    //descartar el diálogo de progreso
                                                    progressDialog.dismiss();
                                                    // Mostrar mensaje
                                                    Utilidades.showToast(activity, mensaje);
                                                    // Enviar código de falla
                                                    //setResult(Activity.RESULT_CANCELED);
                                                    //limpiar();
                                                    break;
                                                case "3":
                                                    //descartar el diálogo de progreso
                                                    progressDialog.dismiss();
                                                    // Mostrar mensaje
                                                    Utilidades.showToast(activity, mensaje);
                                                    // Enviar código de falla
                                                    //setResult(Activity.RESULT_CANCELED);
                                                    //limpiar();
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //descartar el diálogo de progreso
                                        progressDialog.dismiss();
                                        //Log.e(TAG, "Error Volley: " + error.getMessage());
                                        //Utilidades.showToast(RegistrarUsuarioActivity.this, "Error Volley: " + error.getMessage());
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
        });

        dialog.show();//abro el dialogo
    }
}
