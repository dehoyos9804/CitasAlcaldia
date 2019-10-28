package co.com.learn.code.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.com.learn.code.Models.HorariosDisponibles;
import co.com.learn.code.Models.Temas;
import co.com.learn.code.Models.TemasFuncionario;
import co.com.learn.code.R;
import co.com.learn.code.fragment.TemasFragment;
import co.com.learn.code.ui.InitialFuncionarioActivity;
import co.com.learn.code.ui.InitialUsuarioActivity;
import co.com.learn.code.utils.Constantes;
import co.com.learn.code.utils.Preferences;
import co.com.learn.code.utils.Utilidades;
import co.com.learn.code.web.VolleySingleton;

public class AdaptadorTemas extends RecyclerView.Adapter<AdaptadorTemas.ExpenseViewHolder> implements ItemClickListener{

    //Lista de objetos {@link Consultas} que representan la fuente de datos de inflado
    private List<TemasFuncionario> items;

    //contexto donde actuá el Recicle View
    private Context context;

    //constructor de la clase Recicle View
    public AdaptadorTemas(List<TemasFuncionario> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        if(items != null){
            return items.size();
        }

        return 0;
    }

    @Override
    public AdaptadorTemas.ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View cardView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lista_temas, viewGroup ,false);
        return new AdaptadorTemas.ExpenseViewHolder(cardView,this);
    }

    @Override
    public void onBindViewHolder(final AdaptadorTemas.ExpenseViewHolder viewHolder, int i) {
        viewHolder.txtTema.setText((items.get(i).getTema()));
        viewHolder.txtDuracion.setText((items.get(i).getDuracion() + " Minutos"));

        //colocar el estado del switch
        boolean estado = (items.get(i).getEstado().equalsIgnoreCase("Activo")) ? true : false;
        if(estado){
            viewHolder.switch_estado.setChecked(estado);
            viewHolder.switch_estado.setText(items.get(i).getEstado());
            viewHolder.switch_estado.setTextColor(context.getResources().getColor(R.color.colorAccent));
            viewHolder.linearColor.setBackground(context.getDrawable(R.color.colorAccent));
        }else{
            viewHolder.switch_estado.setChecked(estado);
            viewHolder.switch_estado.setText(items.get(i).getEstado());
            viewHolder.switch_estado.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolder.linearColor.setBackground(context.getDrawable(R.color.colorPrimary));
        }

        final int position = i;
        viewHolder.switch_estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.switch_estado.isChecked()){
                    actualizarEstadoTema(viewHolder, position, "Activo");
                }else{
                    actualizarEstadoTema(viewHolder, position, "Inactivo");
                }
            }
        });

        viewHolder.switch_estado.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(viewHolder.switch_estado.isChecked()){
                    actualizarEstadoTema(viewHolder, position, "Activo");
                }else{
                    actualizarEstadoTema(viewHolder, position, "Inactivo");
                }
                return true;
            }
        });
    }

    /**
     * Guarda los cambios de una meta editada.
     * <p>
     * Si está en modo inserción, entonces crea una nueva
     * meta en la base de datos
     */
    public void actualizarEstadoTema(final AdaptadorTemas.ExpenseViewHolder viewHolder, final int position, String estado_tema) {

        String estado = estado_tema;
        String idtema = items.get(position).getIdtema();

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();// Mapeo previo
        map.put("estado",estado);
        map.put("idtema", idtema);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        // Depurando objeto Json...
        Log.i("TAG", "map.." + map.toString());
        Log.d("TAG", "json tema..."+jobject);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(context).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATE_ESTADO_TEMA,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("TAG", "Ver Response-->"+response);
                                // Procesar la respuesta del servidor
                                procesarRespuesta(response, viewHolder, position);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //descartar el diálogo de progreso
                                Log.e("TAG", "Error Volley: " + error.getMessage());
                                //Utilidades.showToast(context, "Error Volley: " + error.getMessage());
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
    private void procesarRespuesta(JSONObject response, AdaptadorTemas.ExpenseViewHolder viewHolder, int position) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    //descartar el diálogo de progreso
                    //colocar el estado del switch
                    /*boolean e = (items.get(position).getEstado().equalsIgnoreCase("Activo")) ? true : false;
                    if(e){
                        viewHolder.switch_estado.setChecked(e);
                        viewHolder.switch_estado.setText(items.get(position).getEstado());
                        viewHolder.switch_estado.setTextColor(context.getResources().getColor(R.color.colorAccent));
                        viewHolder.linearColor.setBackground(context.getDrawable(R.color.colorAccent));
                    }else{
                        viewHolder.switch_estado.setChecked(e);
                        viewHolder.switch_estado.setText(items.get(position).getEstado());
                        viewHolder.switch_estado.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        viewHolder.linearColor.setBackground(context.getDrawable(R.color.colorPrimary));
                    }*/
                    break;

                case "2":
                    //descartar el diálogo de progreso
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(View view, final int position) {

    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //campos respectivos del items
        public LinearLayout linearColor;
        public TextView txtTema;
        public TextView txtDuracion;
        public SwitchCompat switch_estado;
        public ItemClickListener listener;

        public ExpenseViewHolder(View view, ItemClickListener listener){
            super(view);

            linearColor = (LinearLayout) view.findViewById(R.id.linearColor);
            txtTema = (TextView) view.findViewById(R.id.txtTema);
            txtDuracion = (TextView) view.findViewById(R.id.txtDuracion);
            switch_estado = (SwitchCompat) view.findViewById(R.id.switch_estado);
            this.listener = listener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
