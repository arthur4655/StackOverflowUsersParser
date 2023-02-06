package api.parser.app.util;

import api.parser.app.dto.ResponseTagApiDto;
import api.parser.app.dto.ResponseUserApiDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRequestConfig {
    String USER_URL_VALUE = "2.3/users?pagesize=100&order=desc&min=223&sort=reputation&"
            + "site=stackoverflow&filter=!)z6chCQ64b6b*aiLI72PaGp9s6*FEn";
    String TAG_URL_VALUE = "https://api.stackexchange.com/2.3/users/{usersIds}/tags?"
            + "pagesize=100&order=desc&sort=popular&site=stackoverflow&filter=!6UovEwKhwUAtQ";

    @GET(USER_URL_VALUE)
    Call<ResponseUserApiDto> requestToUserApi(@Query("page") Long page);

    @GET(TAG_URL_VALUE)
    Call<ResponseTagApiDto> requestToTagApi(@Path("usersIds") String usersIds,
                                            @Query("page") Long page);
}
