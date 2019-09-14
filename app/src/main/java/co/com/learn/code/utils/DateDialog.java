package co.com.learn.code.utils;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Fragmento con un diálogo de selección de fechas
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DateDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){

        super.onCreateDialog(savedInstanceState);
        //Obtener fecha actual del sistema
        final Calendar c= Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);

        //retorna una instancia del dialogo selector de fecha
        return new DatePickerDialog(getContext(),(DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);

    }
}
