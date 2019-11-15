package co.com.learn.code.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.com.learn.code.Models.Dependencias;
import co.com.learn.code.Models.Usuarios;
import co.com.learn.code.R;
import co.com.learn.code.ui.AgendarCitaActivity;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ExpenseViewHolder> implements ItemClickListener{

    //Lista de objetos {@link Consultas} que representan la fuente de datos de inflado
    private List<Usuarios> items;
    private Dialog myDialog;

    private TextView txtClose;
    private LinearLayout customEditar;
    private LinearLayout customApartarCita;

    //contexto donde actuá el Recicle View
    private Context context;

    //constructor de la clase Recicle View
    public AdaptadorUsuarios(List<Usuarios> items, Context context) {
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
    public AdaptadorUsuarios.ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View cardView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section_usuarios, viewGroup ,false);
        return new AdaptadorUsuarios.ExpenseViewHolder(cardView,this);
    }

    @Override
    public void onBindViewHolder(final AdaptadorUsuarios.ExpenseViewHolder viewHolder, int i) {
        viewHolder.txtUsuario.setText((items.get(i).getNombres() + " " + items.get(i).getApellidos()));
    }


    @Override
    public void onItemClick(View view, int position) {
        showDialogPopup(position);
    }

    //Abrimos el nuevo dialogo
    private void showDialogPopup(int position){
        myDialog = new Dialog(context);

        myDialog.setContentView(R.layout.dialog_custompop);
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
        customEditar = (LinearLayout) myDialog.findViewById(R.id.customEditar);
        customApartarCita = (LinearLayout) myDialog.findViewById(R.id.customApartarCita);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        customEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        customApartarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //campos respectivos del items
        public TextView txtUsuario;
        public ItemClickListener listener;

        public ExpenseViewHolder(View view, ItemClickListener listener){
            super(view);

            txtUsuario = (TextView) view.findViewById(R.id.txtUsuario);
            this.listener = listener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
