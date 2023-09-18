package com.example.back_project.group;


import com.example.back_project.base.BaseWeb;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Setter
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public WebClient baseUrl() {
        return new BaseWeb().userWeb();
    }

    public List groupList(String token) {

        List<Group> result = new ArrayList<>();

        List<Map<String, Object>> data =  baseUrl().get()
                .uri("/groups")
                .header("Authorization" , "bearer " + token)
                .retrieve()
                .bodyToMono(List.class)
                .block();
        for(Map<String, Object> data1 : data){
            Group proc = new Group();

            Long id = new Long ((Integer)data1.get("id"));

            proc.setId(id);
            proc.setName((String) data1.get("name"));
            proc.setDescription((String) data1.get("description"));

            result.add(proc);

            groupRepository.save(proc);
        }
        return result;
    }

    public Optional<Group> groupInfo(Long id) {
        return groupRepository.findById(id);
    }

    public Map groupMemberAdd(long id, Map<String, Object> member) {

        Map result = baseUrl().post()
                .uri("/groups/" + id + "/members")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(member))
                .retrieve()
                .bodyToMono(Map.class)
                .block();


        return result;
    }

    public Group groupCreate(Map<String, Object> groupInfo) { // 프로젝트 2개 생성

        Map<String, Object> data = baseUrl().post()
                .uri("/groups")
                .header("Authorization" ,"bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(groupInfo))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Group result = new Group();

        Long id = new Long ((Integer)data.get("id"));

        result.setId(id);
        result.setName((String) data.get("name"));
        result.setDescription((String) data.get("description"));

        JSONObject src = new JSONObject();

        src.put("name", id + "_src");
        src.put("path", id + "_src");
        src.put("description", id + "_src");
        src.put("namespace_id", data.get("id"));
        src.put("initialize_with_readme", "true");

        JSONObject ops = new JSONObject();

        ops.put("name", id + "_ops");
        ops.put("path", id + "_ops");
        ops.put("description", id + "_ops");
        ops.put("namespace_id", data.get("id"));
        ops.put("initialize_with_readme", "true");

        baseUrl().post() // src 생성
                .uri("/projects")
                .header("Authorization" ,"bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(src))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        baseUrl().post() // ops 생성
                .uri("/projects")
                .header("Authorization" ,"bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(ops))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        groupRepository.save(result);

        return result;
    }

    public Map groupDelete(Long id) { // 프로젝트 2개 삭제

        List<Map<String, Object>> data = baseUrl().get()
                        .uri("/groups/" + id + "/projects")
                        .header("Authorization" ,"bearer glpat-UnsExNbKsAqG6JwqxCfy")
                        .retrieve()
                        .bodyToMono(List.class)
                        .block();

        for(Map<String, Object> data1 : data) {
            Map proc = baseUrl().delete()
                    .uri("/projects/" + data1.get("id"))
                    .header("Authorization", "Bearer glpat-UnsExNbKsAqG6JwqxCfy")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            System.out.println(proc);
        }

        Map result = baseUrl().delete()
                .uri("/groups/" + id)
                .header("Authorization" ,"bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        groupRepository.deleteById(id);

        return result;
    }

    public List groupProjects(Long id) {

        List<Map<String, Object>> data = baseUrl().get()
                .uri("/groups/" + id + "/projects")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List result = new ArrayList<>();

        for(Map<String, Object> data1 : data) {
            Map<String, Object> proc = new HashMap<>();

            proc.put("id", data1.get("id"));
            proc.put("name", data1.get("name"));

            result.add(proc);
        }

        return result;
    }

    public Map groupMemberDel(Long id, Long userId) {

        Map result = baseUrl().delete()
                .uri("/groups/" + id + "/members/" + userId)
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return result;
    }

    public List groupMemberList(Long id) {

        List<Map<String, Object>> data = baseUrl().get()
                .uri("/groups/" + id + "/members")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List result = new ArrayList<>();

        for(Map<String, Object> data1 : data){
            Map<String, Object> proc = new HashMap<>();

            proc.put("id", data1.get("id"));
            proc.put("name", data1.get("name"));

            result.add(proc);
        }
        return result;
    }

    public Map groupUpdate(Long id, Map update) {

        Group data = groupRepository.findById(id).get();

        data.setName((String) update.get("name"));
        data.setDescription((String) update.get("description"));


        groupRepository.save(data);

        return baseUrl().put()
                .uri("/groups/" + id)
                .body(BodyInserters.fromObject(update))
                .header("Authorization" ,"bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

    }
}
