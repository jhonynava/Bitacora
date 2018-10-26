package mx.gob.seguropopulartlax.bitacora_vehicular_repss.restApi;

import mx.gob.seguropopulartlax.bitacora_vehicular_repss.restApi.VehiculoResponse.VehiculoResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EndpointsApi  {

    @GET(ConstantesRestApi.URL_VEHICULOS_GETALL)
    Call<VehiculoResponse> getAllVehiculos();
}
