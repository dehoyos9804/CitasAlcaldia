package co.com.learn.code.utils;

public class Constantes {
    public static final String STRING_PREFERENCES = "co.com.learn.code.citasalcaldia.citasalcaldia.utils.Preferences";
    //Puerto Utilizado en la conexion
    private static final String PUERTO_HOST = ":80";
    //Dirección IP
    public static final String IP = "192.168.1.19";

    //code para las extra
    public static final int DEPENDENCIA = 100;

    //extras
    public static final String EXTRA_CODIGO_DEPENDENCIA = "coddependencia";
    public static final String EXTRA_NOMBRE_DEPENDENCIA = "nombredependencia";

    //estado para la preferencia
    public static final boolean ESTADO_PREFERENCIA_TRUE = true;
    public static final boolean ESTADO_PREFERENCIA_FALSE = false;

    //clave para las preferencias
    public static final String PREFERENCIA_IDENTIFICACION_CLAVE = "identificacion";
    public static final String PREFERENCIA_NOMBRES_CLAVE = "nombres";
    public static final String PREFERENCIA_APELLIDOS_CLAVE = "apellidos";
    public static final String PREFERENCIA_TELEFONOS_CLAVE = "telefono";
    public static final String PREFERENCIA_CORREO_CLAVE = "correo";
    public static final String PREFERENCIA_TIPO_USUARIO_CLAVE = "tipousuario";
    public static final String PREFERENCIA_SESION_CLAVE = "mantenersesion";

    //rutas web service ~ usuario
    public static final String INSERTAR_USUARIO = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Usuario/insertar_usuario.php";
    public static final String GET_DEPENDENCIAS = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Dependencia/obtener_dependencias.php";
    public static final String GET_CODIGO_FUNCIONARIO_DEPENDENCIA = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Dependencia/obtener_funcionario_id_dependencia.php";
    public static final String GET_ALL_TEMAS = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Tema/obtener_temas_for_dependencia.php";
    public static final String GET_HORAS_DISPONIBLES = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Usuario/obtener_horario_disponible.php";
    public static final String INSERTAR_CITA = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Cita/insertar_cita.php";
    public static final String GET_LISTA_CITAS_USUARIO = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Cita/get_lista_cita_for_usuario.php";
    public static final String GET_INICIAR_SESION = "http://" + IP + PUERTO_HOST + "/proyecto_citas_alcaldia/web/Usuario/iniciar_sesion.php";

}
