package co.com.learn.code.utils;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

public class Utilidades {
    /*
    * Método que permite colocar un mensajes en pantalla
    */
    public static void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }
}
