package co.com.learn.code.Models;

public class HorariosDisponibles {
    private String horai;
    private String horaf;

    //constructor
    public HorariosDisponibles(String horai, String horaf) {
        this.horai = horai;
        this.horaf = horaf;
    }

    //getter y setter
    public String getHorai() {
        return horai;
    }

    public void setHorai(String horai) {
        this.horai = horai;
    }

    public String getHoraf() {
        return horaf;
    }

    public void setHoraf(String horaf) {
        this.horaf = horaf;
    }
}
