package co.com.learn.code.Models;

public class ListaCitas {
    private String idcita;
    private String numerocedula;
    private String iddependencia;
    private String idtema;
    private String fechareal;
    private String horareali;
    private String nombres;
    private String apellidos;
    private String nombre;//nombre de la dependencia
    private String tema;
    private String estado;

    //constructor
    public ListaCitas(String idcita, String numerocedula, String iddependencia, String idtema, String fechareal, String horareali, String nombres, String apellidos, String nombre, String tema, String estado) {
        this.idcita = idcita;
        this.numerocedula = numerocedula;
        this.iddependencia = iddependencia;
        this.idtema = idtema;
        this.fechareal = fechareal;
        this.horareali = horareali;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.nombre = nombre;
        this.tema = tema;
        this.estado = estado;
    }

    //getter y setter
    public String getIdcita() {
        return idcita;
    }

    public void setIdcita(String idcita) {
        this.idcita = idcita;
    }

    public String getNumerocedula() {
        return numerocedula;
    }

    public void setNumerocedula(String numerocedula) {
        this.numerocedula = numerocedula;
    }

    public String getIddependencia() {
        return iddependencia;
    }

    public void setIddependencia(String iddependencia) {
        this.iddependencia = iddependencia;
    }

    public String getIdtema() {
        return idtema;
    }

    public void setIdtema(String idtema) {
        this.idtema = idtema;
    }

    public String getFechareal() {
        return fechareal;
    }

    public void setFechareal(String fechareal) {
        this.fechareal = fechareal;
    }

    public String getHorareali() {
        return horareali;
    }

    public void setHorareali(String horareali) {
        this.horareali = horareali;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
