package api.parser.app.service.impl;

import api.parser.app.dto.ResponseTagApiDto;
import api.parser.app.dto.ResponseUserApiDto;
import api.parser.app.dto.TagApiDto;
import api.parser.app.dto.UserApiDto;
import api.parser.app.mapper.UserDtoMapper;
import api.parser.app.model.User;
import api.parser.app.service.UserParseService;
import api.parser.app.util.ApiRequestConfig;
import api.parser.app.util.ApiRequestUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import retrofit2.Response;

public class UserParseServiceImpl implements UserParseService {
    public static final String JOINER = ";";
    public static final String JAVA_LANG = "java";
    public static final String CSHARP_LANG = "c#";
    public static final String DOCKER = "docker";
    public static final String DOTNET_LANG = ".net";
    public static final String COUNTRY_MOLDOVA = "Moldova";
    public static final String COUNTRY_ROMANIA = "Romania";
    public static final int REQUEST_QUOTA_MAX = 10;
    public static final int DELAY_TIME = 100;
    public static final int MAX_PARAM_LENGTH = 20;
    public static final int MIN_ANSWERED_QUESTIONS = 1;
    private final UserDtoMapper userDtoMapper;

    public UserParseServiceImpl(UserDtoMapper userDtoMapper) {
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public List<User> syncFromApi() {
        List<User> users = null;
        Map<Long, List<String>> usersTags;
        long pageNumber = 1;
        ApiRequestConfig requestApi = ApiRequestUtil.createRequest();
        try {
            ResponseUserApiDto responseUserApiDto = requestApi.requestToUserApi(pageNumber++)
                            .execute().body();
            if (responseUserApiDto != null) {
                users = new ArrayList<>(filterByLocationNdAnswers(responseUserApiDto.getItems()));
            }
            while (responseUserApiDto != null && responseUserApiDto.isHasMore()
                    && pageNumber < REQUEST_QUOTA_MAX) {
                if (responseUserApiDto.getBackoff() != null) {
                    long delaySec = responseUserApiDto.getBackoff() * 1000;
                    Thread.sleep(delaySec);
                } else {
                    Thread.sleep(DELAY_TIME);
                }
                if (pageNumber > 2) {
                    users.addAll(filterByLocationNdAnswers(responseUserApiDto.getItems()));
                }
                responseUserApiDto = requestApi.requestToUserApi(pageNumber++).execute().body();
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while processing response from user API", e);
        }
        if (users != null) {
            usersTags = syncTags(requestApi, users);
            users = filterUsersByTags(users, usersTags);
        }
        return users;
    }

    private List<User> filterByLocationNdAnswers(UserApiDto[] items) {
        Predicate<UserApiDto> userFilter = item -> {
            if (item.getLocation() == null || item.getAnswerCount() < MIN_ANSWERED_QUESTIONS) {
                return false;
            }
            return item.getLocation().contains(COUNTRY_MOLDOVA)
                        || item.getLocation().contains(COUNTRY_ROMANIA);
        };

        return Arrays.stream(items)
                .filter(userFilter)
                .map(userDtoMapper::toModel)
                .collect(Collectors.toList());
    }

    private Map<Long, List<String>> syncTags(ApiRequestConfig requestConf, List<User> users) {
        List<String> userIdsList = getUserIds(users);
        Response<ResponseTagApiDto> tags;
        Map<Long, List<String>> usersTags = null;
        for (String usersIds : userIdsList) {
            long pageNumber = 1;
            try {
                tags = requestConf.requestToTagApi(usersIds, pageNumber++).execute();
                if (tags.body() != null) {
                    usersTags = new HashMap<>(collectTags(tags.body()));
                }
                while (tags.body() != null && tags.body().isHasMore()
                            && pageNumber < REQUEST_QUOTA_MAX) {
                    if (tags.body().getBackoff() != null) {
                        long delaySec = tags.body().getBackoff() * 1000;
                        Thread.sleep(delaySec);
                    } else {
                        Thread.sleep(DELAY_TIME);
                    }
                    if (pageNumber > 2) {
                        usersTags.putAll(collectTags(tags.body()));
                    }
                    tags = requestConf.requestToTagApi(usersIds, pageNumber++).execute();
                }

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error while processing response from tag API", e);
            }
        }
        return usersTags;
    }

    private List<String> getUserIds(List<User> users) {
        List<String> usersIds = new ArrayList<>();
        int size = users.size();
        int startSize = 0;
        int maxSize = 0;
        int count = (int) Math.ceil(size / (double) MAX_PARAM_LENGTH);
        for (;count > 0;count--) {
            maxSize += Math.min(size, MAX_PARAM_LENGTH);
            usersIds.add(IntStream.range(startSize, maxSize)
                    .mapToObj(users::get)
                    .map(User::getUserId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(JOINER)));
            startSize += MAX_PARAM_LENGTH;
            size -= MAX_PARAM_LENGTH;
        }
        return usersIds;
    }

    private Map<Long, List<String>> collectTags(ResponseTagApiDto tagApiDto) {
        Predicate<TagApiDto> tagFilter = item -> {
            String name = item.getName();
            return name.equalsIgnoreCase(JAVA_LANG)
                    || name.equalsIgnoreCase(CSHARP_LANG)
                    || name.equalsIgnoreCase(DOCKER)
                    || name.equalsIgnoreCase(DOTNET_LANG);
        };
        return Arrays.stream(tagApiDto.getItems())
                .filter(tagFilter)
                .collect(Collectors.groupingBy(TagApiDto::getUserId,
                        Collectors.mapping(TagApiDto::getName, Collectors.toList())));
    }

    private List<User> filterUsersByTags(List<User> users, Map<Long, List<String>> usersTags) {
        return users.stream()
                .filter(u -> usersTags.containsKey(u.getUserId()))
                .peek(u -> u.setTags(usersTags.get(u.getUserId()).toArray(String[]::new)))
                .collect(Collectors.toList());
    }
}
