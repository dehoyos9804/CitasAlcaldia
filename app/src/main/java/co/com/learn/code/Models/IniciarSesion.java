package co.com.learn.code.Models;

public class IniciarSesion {
    private String numerocedula;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String tipousuario;

    //constructor
    public IniciarSesion(String numerocedula, String nombres, String apellidos, String telefono, String correo, String tipousuario) {
        this.numerocedula = numerocedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.tipousuario = tipousuario;
    }

    //getter y setter
    public String getNumerocedula() {
        return numerocedula;
    }

    public void setNumerocedula(String numerocedula) {
        this.numerocedula = numerocedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(String tipousuario) {
        this.tipousuario = tipousuario;
    }
}
