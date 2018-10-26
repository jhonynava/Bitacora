package mx.gob.seguropopulartlax.bitacora_vehicular_repss.entidades;

public class Vehiculo {

    private String no_economico;
    private String marca;
    private String tipo;
    private int modelo;
    private int capacidad_tanque;
    private int cilindraje;
    private String tipo_combustible;
    private String no_serie;
    private String no_motor;
    private String placas;
    private String area_resguardante;
    private String resguardante;
    private boolean disponibilidad;
    private int imagen;

    public Vehiculo() {
    }

    public Vehiculo(String no_economico, String marca, String tipo, int modelo, int capacidad_tanque, int cilindraje, String tipo_combustible, String no_serie, String no_motor, String placas, String area_resguardante, String resguardante, boolean disponibilidad, int imagen) {
        this.no_economico = no_economico;
        this.marca = marca;
        this.tipo = tipo;
        this.modelo = modelo;
        this.capacidad_tanque = capacidad_tanque;
        this.cilindraje = cilindraje;
        this.tipo_combustible = tipo_combustible;
        this.no_serie = no_serie;
        this.no_motor = no_motor;
        this.placas = placas;
        this.area_resguardante = area_resguardante;
        this.resguardante = resguardante;
        this.disponibilidad = disponibilidad;
        this.imagen = imagen;
    }

    public String getNo_economico() {
        return no_economico;
    }

    public void setNo_economico(String no_economico) {
        this.no_economico = no_economico;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public int getCapacidad_tanque() {
        return capacidad_tanque;
    }

    public void setCapacidad_tanque(int capacidad_tanque) {
        this.capacidad_tanque = capacidad_tanque;
    }

    public int getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
    }

    public String getTipo_combustible() {
        return tipo_combustible;
    }

    public void setTipo_combustible(String tipo_combustible) {
        this.tipo_combustible = tipo_combustible;
    }

    public String getNo_serie() {
        return no_serie;
    }

    public void setNo_serie(String no_serie) {
        this.no_serie = no_serie;
    }

    public String getNo_motor() {
        return no_motor;
    }

    public void setNo_motor(String no_motor) {
        this.no_motor = no_motor;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public String getArea_resguardante() {
        return area_resguardante;
    }

    public void setArea_resguardante(String area_resguardante) {
        this.area_resguardante = area_resguardante;
    }

    public String getResguardante() {
        return resguardante;
    }

    public void setResguardante(String resguardante) {
        this.resguardante = resguardante;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "no_economico='" + no_economico + '\'' +
                ", marca='" + marca + '\'' +
                ", tipo='" + tipo + '\'' +
                ", modelo=" + modelo +
                ", capacidad_tanque=" + capacidad_tanque +
                ", cilindraje=" + cilindraje +
                ", tipo_combustible='" + tipo_combustible + '\'' +
                ", no_serie='" + no_serie + '\'' +
                ", no_motor='" + no_motor + '\'' +
                ", placas='" + placas + '\'' +
                ", area_resguardante='" + area_resguardante + '\'' +
                ", resguardante='" + resguardante + '\'' +
                ", disponibilidad=" + disponibilidad +
                ", imagen=" + imagen +
                '}';
    }
}
