package co.com.learn.code.utils;

public class Constantes {
    public static final String STRING_PREFERENCES = "co.com.learn.code.citasalcaldia.citasalcaldia.utils.Preferences";
    //Puerto Utilizado en la conexion
    private static final String PUERTO_HOST = ":80";
    //Dirección IP
    public static final String IP = "192.168.1.19";

    public static final int DEPENDENCIA = 100;

    public static final String EXTRA_CODIGO_DEPENDENCIA = "coddependencia";
    public static final String EXTRA_NOMBRE_DEPENDENCIA = "nombredependencia";

    //rutas web service ~ usuario
    public static final String INSERTAR_USUARIO = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Usuario/insertar_usuario.php";
    public static final String GET_DEPENDENCIAS = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Dependencia/obtener_dependencias.php";
    public static final String GET_CODIGO_FUNCIONARIO_DEPENDENCIA = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Dependencia/obtener_funcionario_id_dependencia.php";
    public static final String GET_ALL_TEMAS = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Tema/obtener_temas_for_dependencia.php";
    public static final String GET_HORAS_DISPONIBLES = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Usuario/obtener_horario_disponible.php";

}
