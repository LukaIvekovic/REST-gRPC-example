package hr.fer.rassus.lab.prvi.client.rest;

import hr.fer.rassus.lab.prvi.client.rest.dto.SaveReadingRequest;
import hr.fer.rassus.lab.prvi.client.rest.dto.NeighborResponse;
import hr.fer.rassus.lab.prvi.client.rest.dto.RegisterSensorRequest;
import hr.fer.rassus.lab.prvi.client.rest.dto.IdResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SensorRestClient {
    @POST("sensors")
    Call<IdResponse> registerSensor(@Body RegisterSensorRequest request);

    @GET("sensors/{id}/neighbors/closest")
    Call<NeighborResponse> getClosestNeighbor(@Path("id") Integer id);

    @POST("sensors/{id}/readings")
    Call<IdResponse> saveReading(@Path("id") Integer id, @Body SaveReadingRequest request);
}
