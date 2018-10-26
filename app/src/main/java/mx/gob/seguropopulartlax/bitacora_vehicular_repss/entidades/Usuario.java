package mx.gob.seguropopulartlax.bitacora_vehicular_repss.entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String curp;
    private String password;
    private String nombre;
    private String apaterno;
    private String amaterno;

    public Usuario() {
    }

    public Usuario(String curp, String password, String nombre, String apaterno, String amaterno) {
        this.curp = curp;
        this.password = password;
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApaterno() {
        return apaterno;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }
}
