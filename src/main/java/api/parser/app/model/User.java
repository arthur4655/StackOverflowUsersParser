package api.parser.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private String name;
    private Long userId;
    private String location;
    private String profileImage;
    private Long answerCount;
    private Long questionCount;
    private String link;
    private String[] tags;
}
