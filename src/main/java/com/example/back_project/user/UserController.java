package com.example.back_project.user;


import com.example.back_project.TokenController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping(value="/user")
public class UserController {



    User user;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @RequestMapping(method = RequestMethod.GET, path="/sync")
//    public List userSync() {
//        return userService.userSync();
//    }

    @RequestMapping(method = RequestMethod.GET, path="/list")
    public List userList() {
        return userService.userList();
    }

    @RequestMapping(method = RequestMethod.GET, path="/{id}/info")
    public Map userInfo(@PathVariable Long id){
        TokenController tokenController = new TokenController();

        tokenController.test();

        return userService.userInfo(id);
    }

//    @RequestMapping(method = RequestMethod.GET, path="/{id}/info")
//    public Map userInfo(@PathVariable Long id){
//        return userService.userInfo(id);
//    }

    @RequestMapping(method = RequestMethod.POST, path="/create")
    public Map userCreate(@RequestBody Map<String, Object> userinfo) {
        return userService.userCreate(userinfo);
    }

    @RequestMapping(method = RequestMethod.PUT, path="/{id}/update")
    public User userUpdate(@PathVariable Long id, @RequestBody Map<String, Object> userinfo) {
        return userService.userUpdate(id, userinfo);
    }

    @RequestMapping(method = RequestMethod.DELETE, path="/{id}/delete")
    public Map userDelete(@PathVariable Long id) {
        return userService.userDelete(id);
    }

    @RequestMapping(method = RequestMethod.GET, path="/{id}/project")
    public List userProject(@PathVariable Long id) {
        return userService.userProject(id);
    }

    @RequestMapping(method = RequestMethod.POST, path="/login")
    public Boolean userLogin(@RequestBody User userInfo) {
        return userService.userLogin(userInfo);
    }

    @RequestMapping(method = RequestMethod.GET, path="/chk/{name}")
    public Optional<User> userCheck(@PathVariable String name) {
        return userService.userCheck(name);
    }

    @RequestMapping(method = RequestMethod.GET, path="/{id}/groups")
    public List userGroups(@PathVariable Long id) {
        return userService.userGroups(id);
    }

    //===========================================================================
    @RequestMapping(method = RequestMethod.POST, path="/{id}/token")
    public Map userToken(@PathVariable Long id, @RequestBody Map<String,Object> tokenInfo){
        return userService.userToken(id, tokenInfo);
    }

}