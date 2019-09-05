package co.com.learn.code.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static co.com.learn.code.utils.Constantes.STRING_PREFERENCES;

/*clase que permite guardar y obtener datos con sharePreferences*/
public class Preferences {
    /*guardar preferencias tipo @boolean*/
    public static void savePreferenceBoolean(Context context, boolean valor, String clave){
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES,context.MODE_PRIVATE);
        preferences.edit().putBoolean(clave, valor).apply();
    }

    /*guardar preferencia tipo @String*/
    public static void savePreferenceString(Context context, String valor, String clave){
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES,context.MODE_PRIVATE);
        preferences.edit().putString(clave, valor).apply();
    }

    /*guardar preferencia tipo @Integer*/
    public static void savePreferenceInteger(Context context, int valor, String clave){
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES,context.MODE_PRIVATE);
        preferences.edit().putInt(clave, valor).apply();
    }

    /*obtener preferencia tipo @boolean*/
    public static boolean getPreferenceBoolean(Context context,String clave){
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, context.MODE_PRIVATE);
        return preferences.getBoolean(clave, false);
    }

    /*obtener preferencia tipo @String*/
    public static String getPreferenceString(Context context,String clave){
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, context.MODE_PRIVATE);
        return preferences.getString(clave, "");
    }

    /*obtener preferencia tipo @Integer*/
    public static int getPreferenceInt(Context context,String clave){
        SharedPreferences preferences = context.getSharedPreferences(STRING_PREFERENCES, context.MODE_PRIVATE);
        return preferences.getInt(clave, 0);
    }

}
