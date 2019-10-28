package co.com.learn.code.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import co.com.learn.code.Models.HorariosDisponibles;
import co.com.learn.code.R;
import co.com.learn.code.fragment.DialogDatosCitas;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class AdaptadorHorarioDisponibleUpdate extends RecyclerView.Adapter<AdaptadorHorarioDisponibleUpdate.ExpenseViewHolder> implements ItemClickListener{

    //Lista de objetos {@link Consultas} que representan la fuente de datos de inflado
    private List<HorariosDisponibles> items;

    //contexto donde actuá el Recicle View
    private Context context;

    private String fecha;

    //identificador de la cita
    private String codigoCita;

    //tiempo de notificacion
    private int notificacion;

    //constructor de la clase Recicle View
    public AdaptadorHorarioDisponibleUpdate(List<HorariosDisponibles> items, Context context, String codigoCita, int notificacion, String fecha) {
        this.items = items;
        this.context = context;
        this.codigoCita = codigoCita;
        this.notificacion = notificacion;
        this.fecha = fecha;
    }

    @Override
    public int getItemCount() {
        if(items != null){
            return items.size();
        }

        return 0;
    }

    @Override
    public AdaptadorHorarioDisponibleUpdate.ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View cardView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section_horas_disponibles_update, viewGroup ,false);
        return new AdaptadorHorarioDisponibleUpdate.ExpenseViewHolder(cardView,this);
    }

    @Override
    public void onBindViewHolder(final AdaptadorHorarioDisponibleUpdate.ExpenseViewHolder viewHolder, int i) {
        viewHolder.txtHoraInicia.setText((items.get(i).getHorai()));
        viewHolder.txtHoraFinal.setText((items.get(i).getHoraf()));
    }


    @Override
    public void onItemClick(View view, final int position) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder((Activity) context);
        dialogo.setTitle("Aplazar Cita");
        dialogo.setMessage("¿Esta seguro que quieres aplazar la cita");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if(notificacion != 0){
                    final ProgressDialog loading = ProgressDialog.show(context,"Actualizando.","Espere por favor...",false,false);
                    // Obtener valores actuales de los controles
                    String horarealinicial = items.get(position).getHorai();
                    String horarealfinal = items.get(position).getHoraf();
                    String tiempo_notificacion = String.valueOf(notificacion);

                    HashMap<String, String> map = new HashMap<>();// Mapeo previo

                    map.put("fechareal", fecha);
                    map.put("horarealinicial", horarealinicial);
                    map.put("horarealfinal", horarealfinal);
                    map.put("notificacion", tiempo_notificacion);
                    map.put("codcita", codigoCita);

                    // Crear nuevo objeto Json basado en el mapa
                    JSONObject jobject = new JSONObject(map);

                    // Depurando objeto Json...
                    Log.d("TAG", jobject.toString());

                    // Actualizar datos en el servidor
                    VolleySingleton.getInstance(context).addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    Constantes.UPDATE_CITA_ID,
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
                                                        // Mostrar mensaje
                                                        loading.dismiss();
                                                        dialog.dismiss();
                                                        Utilidades.showToast((Activity) context, mensaje);
                                                        ((Activity) context).finish();
                                                        ((Activity) context).overridePendingTransition(0,0);
                                                        context.startActivity(((Activity) context).getIntent());
                                                        ((Activity) context).overridePendingTransition(0,0 );
                                                        //((Activity) context).recreate();//vuelve a cargar la actividad
                                                        break;

                                                    case "2":
                                                        // Mostrar mensaje
                                                        loading.dismiss();
                                                        dialog.dismiss();
                                                        Utilidades.showToast((Activity) context, mensaje);
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
                                            Log.d("TAG", "Error Volley: " + error.getMessage());
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
                }else{
                    Utilidades.showToast((Activity) context, "Debes seleccionar un tiempo de notificacion primero");
                    dialog.dismiss();
                }
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//cerrar el dialogo
            }
        });

        dialogo.show();//mostrar el dialogo
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //campos respectivos del items
        public TextView txtHoraInicia;
        public TextView txtHoraFinal;
        public RadioButton radioButton;
        public RelativeLayout container_relative;
        public ItemClickListener listener;

        public ExpenseViewHolder(View view, ItemClickListener listener){
            super(view);

            container_relative = (RelativeLayout) view.findViewById(R.id.container_relative);
            txtHoraInicia = (TextView) view.findViewById(R.id.txtHoraInicial);
            txtHoraFinal = (TextView) view.findViewById(R.id.txtHoraFinal);
            this.listener = listener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
