package mx.gob.seguropopulartlax.bitacora_vehicular_repss.restApi.VehiculoResponse;

import java.util.ArrayList;

import mx.gob.seguropopulartlax.bitacora_vehicular_repss.entidades.Vehiculo;

public class VehiculoResponse {
    ArrayList<Vehiculo> vehiculos;

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(ArrayList<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
}
