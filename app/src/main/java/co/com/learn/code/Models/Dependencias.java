package co.com.learn.code.Models;

public class Dependencias {
    private String iddependencia;
    private String nombre;
    private String observacion;

    //constructor
    public Dependencias(String iddependencia, String nombre, String observacion) {
        this.iddependencia = iddependencia;
        this.nombre = nombre;
        this.observacion = observacion;
    }

    //getter y setter
    public String getIddependencia() {
        return iddependencia;
    }

    public void setIddependencia(String iddependencia) {
        this.iddependencia = iddependencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
