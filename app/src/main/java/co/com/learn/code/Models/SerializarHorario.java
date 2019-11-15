package co.com.learn.code.Models;

public class SerializarHorario {
    private String dia;
    private String jornadamaniana;
    private String horaentradamaniana;
    private String horasalidamaniana;
    private String jornadatarde;
    private String horaentradatarde;
    private String horasalidatarde;

    //constructor
    public SerializarHorario(String dia, String jornadamaniana, String horaentradamaniana, String horasalidamaniana, String jornadatarde, String horaentradatarde, String horasalidatarde) {
        this.dia = dia;
        this.jornadamaniana = jornadamaniana;
        this.horaentradamaniana = horaentradamaniana;
        this.horasalidamaniana = horasalidamaniana;
        this.jornadatarde = jornadatarde;
        this.horaentradatarde = horaentradatarde;
        this.horasalidatarde = horasalidatarde;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getJornadamaniana() {
        return jornadamaniana;
    }

    public void setJornadamaniana(String jornadamaniana) {
        this.jornadamaniana = jornadamaniana;
    }

    public String getHoraentradamaniana() {
        return horaentradamaniana;
    }

    public void setHoraentradamaniana(String horaentradamaniana) {
        this.horaentradamaniana = horaentradamaniana;
    }

    public String getHorasalidamaniana() {
        return horasalidamaniana;
    }

    public void setHorasalidamaniana(String horasalidamaniana) {
        this.horasalidamaniana = horasalidamaniana;
    }

    public String getJornadatarde() {
        return jornadatarde;
    }

    public void setJornadatarde(String jornadatarde) {
        this.jornadatarde = jornadatarde;
    }

    public String getHoraentradatarde() {
        return horaentradatarde;
    }

    public void setHoraentradatarde(String horaentradatarde) {
        this.horaentradatarde = horaentradatarde;
    }

    public String getHorasalidatarde() {
        return horasalidatarde;
    }

    public void setHorasalidatarde(String horasalidatarde) {
        this.horasalidatarde = horasalidatarde;
    }
}
