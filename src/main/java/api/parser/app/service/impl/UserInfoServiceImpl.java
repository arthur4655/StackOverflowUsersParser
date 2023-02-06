package api.parser.app.service.impl;

import api.parser.app.model.User;
import api.parser.app.service.UserInfoService;
import java.util.List;

public class UserInfoServiceImpl implements UserInfoService {
    public static final String INFO_TEMPLATE = "User name: %s; location: %s;"
            + " answer count: %d; question count: %d; tags: %s; link to profile: %s; "
            + "link to avatar: %s";
    public static final String JOINER = ", ";

    @Override
    public void printInfo(List<User> users) {
        users.forEach(user -> System.out.println(String.format(INFO_TEMPLATE, user.getName(),
                user.getLocation(), user.getAnswerCount(), user.getQuestionCount(),
                String.join(JOINER, user.getTags()), user.getLink(), user.getProfileImage())));

    }
}
