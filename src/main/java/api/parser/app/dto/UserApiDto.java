package api.parser.app.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserApiDto {
    @SerializedName("display_name")
    private String name;
    @SerializedName("user_id")
    private Long userId;
    private String location;
    private String link;
    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("answer_count")
    private Long answerCount;
    @SerializedName("question_count")
    private Long questionCount;
}
