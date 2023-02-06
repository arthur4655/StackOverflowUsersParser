package api.parser.app.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUserApiDto {
    @SerializedName("has_more")
    private boolean hasMore;
    @SerializedName("items")
    private UserApiDto[] items;
}
