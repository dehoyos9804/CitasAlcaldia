package co.com.learn.code.Models;

public class TemasFuncionario {
    private String idtema;
    private String tema;
    private String duracion;
    private String estado;

    //constructor
    public TemasFuncionario(String idtema, String tema, String duracion, String estado) {
        this.idtema = idtema;
        this.tema = tema;
        this.duracion = duracion;
        this.estado = estado;
    }

    //getter y setter
    public String getIdtema() {
        return idtema;
    }

    public void setIdtema(String idtema) {
        this.idtema = idtema;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
