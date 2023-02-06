package api.parser.app.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTagApiDto {
    private TagApiDto[] items;
    @SerializedName("has_more")
    private boolean hasMore;
    private Long backoff;
}
