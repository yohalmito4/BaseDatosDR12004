package fia.ues.sv.basedatosdr12004.Controladores;

/**
 * Created by yohalmo on 05-12-16.
 */
public class Materia {

    private String codmateria;
    private String nommateria;
    private String unidadesval;

    public Materia() {
    }

    public Materia(String codmateria, String nommateria, String unidadesval) {
        this.codmateria = codmateria;
        this.nommateria = nommateria;
        this.unidadesval = unidadesval;
    }

    public String getCodmateria() {
        return codmateria;
    }

    public void setCodmateria(String codmateria) {
        this.codmateria = codmateria;
    }

    public String getNommateria() {
        return nommateria;
    }

    public void setNommateria(String nommateria) {
        this.nommateria = nommateria;
    }

    public String getUnidadesval() {
        return unidadesval;
    }

    public void setUnidadesval(String unidadesval) {
        this.unidadesval = unidadesval;
    }
}
