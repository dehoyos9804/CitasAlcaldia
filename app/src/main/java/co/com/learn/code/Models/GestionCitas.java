package co.com.learn.code.Models;

public class GestionCitas {
    private String codcita;
    private String estado;
    private String nombres;
    private String apellidos;
    private String tema;
    private String fechareal;
    private String horareali;
    private String horarealf;
    private String duracion;

    //constructor
    public GestionCitas(String codcita, String estado, String nombres, String apellidos, String tema, String fechareal, String horareali, String horarealf, String duracion) {
        this.codcita = codcita;
        this.estado = estado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tema = tema;
        this.fechareal = fechareal;
        this.horareali = horareali;
        this.horarealf = horarealf;
        this.duracion = duracion;
    }

    //getter y setter
    public String getCodcita() {
        return codcita;
    }

    public void setCodcita(String codcita) {
        this.codcita = codcita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
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

    public String getHorarealf() {
        return horarealf;
    }

    public void setHorarealf(String horarealf) {
        this.horarealf = horarealf;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
