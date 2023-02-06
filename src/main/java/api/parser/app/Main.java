package api.parser.app;

import api.parser.app.mapper.UserDtoMapper;
import api.parser.app.model.User;
import api.parser.app.service.UserInfoService;
import api.parser.app.service.UserParseService;
import api.parser.app.service.impl.UserInfoServiceImpl;
import api.parser.app.service.impl.UserParseServiceImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDtoMapper userDtoMapper = new UserDtoMapper();
        UserParseService userParseService = new UserParseServiceImpl(userDtoMapper);

        List<User> users = userParseService.syncFromApi();

        UserInfoService userInfoService = new UserInfoServiceImpl();
        userInfoService.printInfo(users);
    }
}