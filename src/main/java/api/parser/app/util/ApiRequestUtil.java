package api.parser.app.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestUtil {
    public static final String URL_BASE = "https://api.stackexchange.com/";

    public static ApiRequestConfig createRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiRequestConfig.class);
    }
}
