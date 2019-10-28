package co.com.learn.code.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.com.learn.code.Models.Dependencias;
import co.com.learn.code.Models.ListaCitas;
import co.com.learn.code.R;
import co.com.learn.code.ui.DetalleCitaActivity;

public class AdaptadorListaCitas extends RecyclerView.Adapter<AdaptadorListaCitas.ExpenseViewHolder> implements ItemClickListener{

    //Lista de objetos {@link Consultas} que representan la fuente de datos de inflado
    private List<ListaCitas> items;

    //contexto donde actuá el Recicle View
    private Context context;

    //constructor de la clase Recicle View
    public AdaptadorListaCitas(List<ListaCitas> items, Context context) {
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
    public AdaptadorListaCitas.ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View cardView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section_lista_citas, viewGroup ,false);
        return new AdaptadorListaCitas.ExpenseViewHolder(cardView,this);
    }

    @Override
    public void onBindViewHolder(final AdaptadorListaCitas.ExpenseViewHolder viewHolder, int i) {
        viewHolder.txtFechaCita.setText(items.get(i).getFechareal());
        viewHolder.txtHoraInicial.setText(items.get(i).getHorareali());
        viewHolder.txtTemaCita.setText(items.get(i).getTema());
        viewHolder.txtDependenciaCita.setText(items.get(i).getNombre());
        viewHolder.txtFuncionarioCita.setText((items.get(i).getNombres() + " " + items.get(i).getApellidos()));
        viewHolder.txtEstado.setText(items.get(i).getEstado());
    }


    @Override
    public void onItemClick(View view, int position) {
        DetalleCitaActivity.launch((Activity) context, items.get(position).getIdcita());
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //campos respectivos del items
        public LinearLayout linearColor;
        public TextView txtFechaCita;
        public TextView txtHoraInicial;
        public TextView txtTemaCita;
        public TextView txtDependenciaCita;
        public TextView txtFuncionarioCita;
        public TextView txtEstado;
        public ItemClickListener listener;

        public ExpenseViewHolder(View view, ItemClickListener listener){
            super(view);
            linearColor = (LinearLayout) view.findViewById(R.id.linearColor);
            txtFechaCita = (TextView) view.findViewById(R.id.txtFechaCita);
            txtHoraInicial = (TextView) view.findViewById(R.id.txtHoraInicial);
            txtTemaCita = (TextView) view.findViewById(R.id.txtTemaCita);
            txtDependenciaCita = (TextView) view.findViewById(R.id.txtDependenciaCita);
            txtFuncionarioCita = (TextView) view.findViewById(R.id.txtFuncionarioCita);
            txtEstado = (TextView) view.findViewById(R.id.txtEstado);
            this.listener = listener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}