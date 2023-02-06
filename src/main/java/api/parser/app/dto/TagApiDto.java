package api.parser.app.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagApiDto {
    @SerializedName("user_id")
    private Long userId;
    private String name;
}
