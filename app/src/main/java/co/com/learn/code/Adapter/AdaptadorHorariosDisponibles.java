package co.com.learn.code.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.com.learn.code.Models.Dependencias;
import co.com.learn.code.Models.HorariosDisponibles;
import co.com.learn.code.R;
import co.com.learn.code.fragment.DialogDatosCitas;
import co.com.learn.code.ui.AgendarCitaActivity;

public class AdaptadorHorariosDisponibles extends RecyclerView.Adapter<AdaptadorHorariosDisponibles.ExpenseViewHolder> implements ItemClickListener{

    //datos que van a poblar el dialog cuando realizamos el evento onclick del adaptador
    private String fecha;
    private int codigousuario;
    private int codigodependencia;
    private int codigotema;
    private String nombredependencia;
    private String nombretema;

    //Lista de objetos {@link Consultas} que representan la fuente de datos de inflado
    private List<HorariosDisponibles> items;

    //contexto donde actuá el Recicle View
    private Context context;

    //constructor de la clase Recicle View
    public AdaptadorHorariosDisponibles(List<HorariosDisponibles> items, Context context, String fecha, int codigousuario,
                                        int codigodependencia, int codigotema, String nombredependencia, String nombretema) {
        this.items = items;
        this.context = context;
        this.fecha = fecha;
        this.codigousuario = codigousuario;
        this.codigodependencia = codigodependencia;
        this.codigotema = codigotema;
        this.nombredependencia = nombredependencia;
        this.nombretema = nombretema;
    }

    @Override
    public int getItemCount() {
        if(items != null){
            return items.size();
        }

        return 0;
    }

    @Override
    public AdaptadorHorariosDisponibles.ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View cardView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section_horas_diponibles, viewGroup ,false);
        return new AdaptadorHorariosDisponibles.ExpenseViewHolder(cardView,this);
    }

    @Override
    public void onBindViewHolder(final AdaptadorHorariosDisponibles.ExpenseViewHolder viewHolder, int i) {
        viewHolder.txtHoraInicia.setText((items.get(i).getHorai()));
        viewHolder.txtHoraFinal.setText((items.get(i).getHoraf()));
    }


    @Override
    public void onItemClick(View view, int position) {
        DialogDatosCitas.showDialogCitas((Activity) context, fecha, items.get(position).getHorai(), items.get(position).getHoraf(), codigousuario, codigodependencia, codigotema, nombredependencia, nombretema);
        //AgendarCitaActivity.launch((Activity) context, items.get(position).getIddependencia(), items.get(position).getNombre());
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //campos respectivos del items
        public TextView txtHoraInicia;
        public TextView txtHoraFinal;
        public ItemClickListener listener;

        public ExpenseViewHolder(View view, ItemClickListener listener){
            super(view);

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

