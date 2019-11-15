package co.com.learn.code.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import co.com.learn.code.Models.CitasFuncionario;
import co.com.learn.code.Models.Usuarios;
import co.com.learn.code.R;
import co.com.learn.code.ui.AgendarCitaActivity;
import co.com.learn.code.ui.DetalleCitaActivity;
import co.com.learn.code.ui.GestionCitasActivity;
import co.com.learn.code.utils.Utilidades;

public class AdaptadorCitasFuncionario extends RecyclerView.Adapter<AdaptadorCitasFuncionario.ExpenseViewHolder> implements ItemClickListener{

    //Lista de objetos {@link Consultas} que representan la fuente de datos de inflado
    private List<CitasFuncionario> items;

    //contexto donde actuá el Recicle View
    private Context context;

    //constructor de la clase Recicle View
    public AdaptadorCitasFuncionario(List<CitasFuncionario> items, Context context) {
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
    public AdaptadorCitasFuncionario.ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View cardView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section_citas, viewGroup ,false);
        return new AdaptadorCitasFuncionario.ExpenseViewHolder(cardView,this);
    }

    @Override
    public void onBindViewHolder(final AdaptadorCitasFuncionario.ExpenseViewHolder viewHolder, int i) {

        //conversiones para la fecha y la hora
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat mes = new SimpleDateFormat("MM");
        SimpleDateFormat diaSemana = new SimpleDateFormat("EEEE");

        String fechareal = items.get(i).getFechareal();
        try {
            Date d = new Date(fecha.parse(fechareal).getTime());
            String dayofTheWeek = diaSemana.format(d);
            String diasemana = dayofTheWeek.substring(0, 3);//reduzco la cadena de la semana en 3 caracteres


            String meses = fechareal.substring(5, 7);//2019-00-10 obtengo 00 perteneciente al mes
            int mes = Integer.parseInt(meses);

            String[] meses_anio = {"Ene", "Feb", "Mar", "Abr", "May","Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

            String dia = fechareal.substring(8,10);//2019-00-10 obtengo el 10 perteneciente al dia

            viewHolder.itemFlightDateText.setText((diasemana + "\n" + meses_anio[mes-1] + " " + dia));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int[] colors = {R.color.color1, R.color.color2,R.color.color3,
                R.color.color4,R.color.color5, R.color.colorAccent, R.color.colorPrimary};

        viewHolder.itemFlightDateText.setBackground(context.getResources().getDrawable(colors[new Random().nextInt(colors.length)]));
        viewHolder.itemDepartureAirportCityText.setText((items.get(i).getHorareali()));
        viewHolder.itemDestinationAirportCityText.setText((items.get(i).getHorarealf()));
    }


    @Override
    public void onItemClick(View view, int position) {
        GestionCitasActivity.launch((Activity) context, items.get(position).getCodcita());
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //campos respectivos del items
        public TextView itemFlightDateText;//informacion del dia y fecha
        public TextView itemDepartureAirportCityText;//hora inicial
        public TextView itemDestinationAirportCityText;//hora final
        public ItemClickListener listener;

        public ExpenseViewHolder(View view, ItemClickListener listener){
            super(view);

            itemFlightDateText = (TextView) view.findViewById(R.id.itemFlightDateText);
            itemDepartureAirportCityText = (TextView) view.findViewById(R.id.itemDepartureAirportCityText);
            itemDestinationAirportCityText = (TextView) view.findViewById(R.id.itemDestinationAirportCityText);

            this.listener = listener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
