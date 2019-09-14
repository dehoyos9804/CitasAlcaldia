package co.com.learn.code.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import co.com.learn.code.R;
import co.com.learn.code.ui.InitialUsuarioActivity;
import co.com.learn.code.ui.ListaDependenciaActivity;

public class DialogDatosCitas {
    /**
     * Método que permite crear el dialogo de para finalizar la cita
     **/
    public static void showDialogCitas(final Activity activity, String departamento, String tema, String fecha, String Horaf, String horaf){
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

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//cierro el dialogo
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//cierro el dialogo
                Intent intent = new Intent(activity, InitialUsuarioActivity.class);
                activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP));//finaliza las actividades en pila
            }
        });

        dialog.show();//abro el dialogo
    }
}
