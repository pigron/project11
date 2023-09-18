package com.example.back_project.user;

import com.example.back_project.base.BaseWeb;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Setter
@Service
public class UserService {

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public WebClient baseUrl() {
        return new BaseWeb().userWeb();
    }

    private final UserRepository userRepository;

    private User user;

    public List userList() {

        List result = new ArrayList<>();

        for(User data : userRepository.findAll()){
            Map<String, Object> proc = new HashMap<>();

            proc.put("id", data.getId());
            proc.put("name", data.getName());
            proc.put("state", data.getState());
            proc.put("email", data.getEmail());
            proc.put("username", data.getUsername());

            result.add(proc);
        }

        return result;

    }

    public Map userCreate(Map<String, Object> userinfo) {

        Map userCreate = baseUrl().post()
                .uri("/users?skip_confirmation=true")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(userinfo))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        JSONObject tokenInfo = new JSONObject();

        tokenInfo.put("name", "signupApi");
        tokenInfo.put("scopes", "api");

        Long id = new Long ((Integer)userCreate.get("id"));

        Map userToken = baseUrl().post()
                .uri("/users/" + id + "/impersonation_tokens")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(tokenInfo))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        User userdata = new User();

        userdata.setPassword((String) userinfo.get("password"));
        userdata.setName((String) userCreate.get("name"));
        userdata.setUsername((String) userCreate.get("username"));
        userdata.setEmail((String) userCreate.get("email"));
        userdata.setId(id);
        userdata.setState((String) userCreate.get("state"));
        userdata.setToken((String) userToken.get("token"));

        userRepository.save(userdata);

        return userCreate;
    }

    public Map<String, Object> userInfo(Long id) {

        User data = userRepository.findById(id).get();

        Map<String, Object> proc = new HashMap<>();

        proc.put("id", data.getId());
        proc.put("name", data.getName());
        proc.put("username", data.getUsername());
        proc.put("email", data.getEmail());
        proc.put("state", data.getState());

        return proc;
    }

    public User userUpdate(Long id, Map<String, Object> userinfo) {

        baseUrl().put()
                .uri("/users/" + id)
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(userinfo))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        User data = userRepository.findById(id).get();

        if(userinfo.containsKey("password")) {
            data.setPassword((String) userinfo.get("password"));
        }

        if(userinfo.containsKey("name")) {
            data.setName((String) userinfo.get("name"));
        }

        if(userinfo.containsKey("email")) {
            data.setEmail((String) userinfo.get("email"));
        }

        userRepository.save(data);

        return data;
    }

    public Map userDelete(Long id) {

        Map userDelete = baseUrl().delete()
                .uri("/users/" + id + "?hard_delete=true")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        userRepository.deleteById(id);

        return userDelete;
    }

    public List userProject(Long id) {

        List result = new ArrayList<>();

        List<Map<String, Object>> data = baseUrl().get()
                .uri("/users/" + id + "/projects")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        for(Map<String, Object> data1 : data ) {
            Map<String, Object> proc = new HashMap<>();

            proc.put("id", data1.get("id"));
            proc.put("name", data1.get("name"));
            proc.put("path", data1.get("path"));
            proc.put("last_activity_at", data1.get("last_activity_at"));
            proc.put("description", data1.get("description"));

            result.add(proc);
        }

        return result;
    }

    public Boolean userLogin(User userId) {

        if ( userRepository.existsByUsername(userId.getUsername()) & userRepository.existsByPassword(userId.getPassword()) ){
            return true;
        }
        return false;
    }


    //=============================================================================
    public Map userToken(Long id, Map<String, Object> tokenInfo) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", "signupApi");
        jsonObject.put("scopes", "api");

        Map userToken = baseUrl().post()
                .uri("/users/" + id + "/impersonation_tokens")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(jsonObject))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        User data = userRepository.getOne(id);

        data.setToken((String) userToken.get("token"));

        userRepository.save(data);

        return userToken;
    }

    public Optional<User> userCheck(String username) {
        return userRepository.findByUsername(username);
    }

    public List userGroups(Long id) {

        List<Map<String, Object>> data = baseUrl().get()
                .uri("/users/" + id + "/memberships")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List result = new ArrayList<>();

        for(Map<String, Object> data1 : data) {
            Map proc = new HashMap<>();

            proc.put("id", data1.get("source_id"));
            proc.put("name", data1.get("source_"));
        }

        return result;
    }
}