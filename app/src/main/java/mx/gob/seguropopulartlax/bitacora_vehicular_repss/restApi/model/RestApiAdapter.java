package mx.gob.seguropopulartlax.bitacora_vehicular_repss.restApi.model;

import mx.gob.seguropopulartlax.bitacora_vehicular_repss.restApi.ConstantesRestApi;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.restApi.EndpointsApi;
import mx.gob.seguropopulartlax.bitacora_vehicular_repss.restApi.VehiculoResponse.VehiculoResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiAdapter {

    public EndpointsApi establecerConexionRestApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(EndpointsApi.class);
    }
}
