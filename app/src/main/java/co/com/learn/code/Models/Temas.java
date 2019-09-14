package co.com.learn.code.Models;

public class Temas {
    private int idtema;
    private String tema;
    private int duracion;

    public Temas(int idtema, String tema, int duracion) {
        this.idtema = idtema;
        this.tema = tema;
        this.duracion = duracion;
    }

    public int getIdtema() {
        return idtema;
    }

    public void setIdtema(int idtema) {
        this.idtema = idtema;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return tema;
    }
}
