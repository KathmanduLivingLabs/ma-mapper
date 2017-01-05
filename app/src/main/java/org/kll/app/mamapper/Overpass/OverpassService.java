package org.kll.app.mamapper.Overpass;

/**
 * Created by nirab on 12/29/16.
 */

import org.kll.app.mamapper.Model.OverpassQueryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OverpassService {
    @GET("/api/interpreter")
    Call<OverpassQueryResult> interpreter(@Query("data") String data);
}

