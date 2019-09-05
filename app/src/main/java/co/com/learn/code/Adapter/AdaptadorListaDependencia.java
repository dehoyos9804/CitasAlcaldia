package co.com.learn.code.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import co.com.learn.code.Models.Dependencias;
import co.com.learn.code.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorListaDependencia extends RecyclerView.Adapter<AdaptadorListaDependencia.ExpenseViewHolder> implements ItemClickListener{

    //Lista de objetos {@link Consultas} que representan la fuente de datos de inflado
    private List<Dependencias> items;

    //contexto donde actuá el Recicle View
    private Context context;

    //constructor de la clase Recicle View
    public AdaptadorListaDependencia(List<Dependencias> items, Context context) {
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
    public AdaptadorListaDependencia.ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View cardView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section_dependencia, viewGroup ,false);
        return new AdaptadorListaDependencia.ExpenseViewHolder(cardView,this);
    }

    @Override
    public void onBindViewHolder(final AdaptadorListaDependencia.ExpenseViewHolder viewHolder, int i) {
        viewHolder.txtNombreDependencia.setText((items.get(i).getNombre()));
    }


    @Override
    public void onItemClick(View view, int position) {
        //DetailAsesoresActivity.launch((Activity) context, items.get(position).getNumerocedula());
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //campos respectivos del items
        public TextView txtNombreDependencia;
        public ItemClickListener listener;

        public ExpenseViewHolder(View view, ItemClickListener listener){
            super(view);

            txtNombreDependencia = (TextView) view.findViewById(R.id.txtNombreDependencia);
            this.listener = listener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
