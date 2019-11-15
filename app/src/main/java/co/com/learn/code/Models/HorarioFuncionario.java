package co.com.learn.code.Models;

public class HorarioFuncionario {
    private String coddia;
    private String codhorario;
    private String horaentrada;
    private String horasalida;
    private String codjornada;

    //costructor
    public HorarioFuncionario(String coddia, String codhorario, String horaentrada, String horasalida, String codjornada) {
        this.coddia = coddia;
        this.codhorario = codhorario;
        this.horaentrada = horaentrada;
        this.horasalida = horasalida;
        this.codjornada = codjornada;
    }

    //getter y setter
    public String getCoddia() {
        return coddia;
    }

    public void setCoddia(String coddia) {
        this.coddia = coddia;
    }

    public String getCodhorario() {
        return codhorario;
    }

    public void setCodhorario(String codhorario) {
        this.codhorario = codhorario;
    }

    public String getHoraentrada() {
        return horaentrada;
    }

    public void setHoraentrada(String horaentrada) {
        this.horaentrada = horaentrada;
    }

    public String getHorasalida() {
        return horasalida;
    }

    public void setHorasalida(String horasalida) {
        this.horasalida = horasalida;
    }

    public String getCodjornada() {
        return codjornada;
    }

    public void setCodjornada(String codjornada) {
        this.codjornada = codjornada;
    }
}
