package api.parser.app.mapper;

import api.parser.app.dto.UserApiDto;
import api.parser.app.model.User;

public class UserDtoMapper {
    public User toModel(UserApiDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLocation(dto.getLocation());
        user.setAnswerCount(dto.getAnswerCount());
        user.setQuestionCount(dto.getQuestionCount());
        user.setLink(dto.getLink());
        user.setProfileImage(dto.getProfileImage());
        user.setUserId(dto.getUserId());
        return user;
    }
}
