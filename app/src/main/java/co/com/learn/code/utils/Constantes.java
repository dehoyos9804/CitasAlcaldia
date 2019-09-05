package co.com.learn.code.utils;

public class Constantes {
    public static final String STRING_PREFERENCES = "co.com.learn.code.citasalcaldia.citasalcaldia.utils.Preferences";
    //Puerto Utilizado en la conexion
    private static final String PUERTO_HOST = ":80";
    //Dirección IP
    public static final String IP = "192.168.1.18";

    //rutas web service ~ usuario
    public static final String INSERTAR_USUARIO = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Usuario/insertar_usuario.php";
    public static final String GET_DEPENDENCIAS = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Dependencia/obtener_dependencias.php";
}
