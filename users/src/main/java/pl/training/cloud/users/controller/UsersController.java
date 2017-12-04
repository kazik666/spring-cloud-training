package pl.training.cloud.users.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.training.cloud.users.dto.PageDto;
import pl.training.cloud.users.dto.UserDto;
import pl.training.cloud.users.model.Mapper;
import pl.training.cloud.users.model.ResultPage;
import pl.training.cloud.users.model.User;
import pl.training.cloud.users.service.UsersService;

import java.net.URI;
import java.util.List;

@Api(description = "Users resource")
@RequestMapping(value = "users")
@RestController
public class UsersController {

    private UsersService usersService;
    private Mapper mapper;
    private UriBuilder uriBuilder = new UriBuilder();

    @Autowired
    public UsersController(UsersService usersService, Mapper mapper) {
        this.usersService = usersService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Create new user")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@ApiParam(name = "user") @RequestBody UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        usersService.addUser(user);
        URI uri = uriBuilder.requestUriWithId(user.getId());
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Get users", response = PageDto.class)
    @RequestMapping(method = RequestMethod.GET)
    public PageDto<UserDto> getUsers(
            @RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
            @RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
        ResultPage<User> resultPage = usersService.getUsers(pageNumber, pageSize);
        List<UserDto> usersDtos = mapper.map(resultPage.getContent(), UserDto.class);
        return new PageDto<>(usersDtos, resultPage.getPageNumber(), resultPage.getTotalPages());
    }

}
