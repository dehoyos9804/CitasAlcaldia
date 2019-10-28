package co.com.learn.code.Models;

public class CitasFuncionario {
    private String codcita;
    private String fechareal;
    private String horareali;
    private String horarealf;

    //constructor
    public CitasFuncionario(String codcita, String fechareal, String horareali, String horarealf) {
        this.codcita = codcita;
        this.fechareal = fechareal;
        this.horareali = horareali;
        this.horarealf = horarealf;
    }

    //getter y setter
    public String getCodcita() {
        return codcita;
    }

    public void setCodcita(String codcita) {
        this.codcita = codcita;
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
}
