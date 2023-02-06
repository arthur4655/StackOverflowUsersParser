package api.parser.app.service;

import api.parser.app.model.User;
import java.util.List;

public interface UserParseService {
    List<User> syncFromApi();
}
