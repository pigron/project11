package com.example.back_project.project;

import com.example.back_project.base.BaseWeb;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@Setter
public class ProjectService {

    private final ProjectRepository projectRepository;

    private Project project;

    public WebClient baseUrl() {
        return new BaseWeb().baseWeb();
    }

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void getProject(Map<String, Object> pt) { // 프로젝트 목록 조회
        Project getPt = new Project();

        Long id = new Long ((Integer)pt.get("id"));

        getPt.setId(id);
        getPt.setName((String) pt.get("name"));
        getPt.setName_with_namespace((String) pt.get("name_with_namespace"));
        getPt.setDescription((String) pt.get("description"));
        projectRepository.save(getPt);
    }

    public Project projectUpdate(Map<String, Object> client) { //프로젝트 생성 및 업데이트

        Project getPt = new Project();

        Long id = new Long ((Integer)client.get("id"));

        getPt.setId(id);
        getPt.setName((String) client.get("name"));
        getPt.setName_with_namespace((String) client.get("name_with_namespace"));
        getPt.setDescription((String) client.get("description"));
        return projectRepository.save(getPt);
    }
    public Map read(Long id) {

       Map data = baseUrl().get()
               .uri("/" + id )
               .header("Authorization", "Bearer glpat-UnsExNbKsAqG6JwqxCfy")
               .retrieve()
               .bodyToMono(Map.class)
               .block();

       Map result = new HashMap<>();

       result.put("name", data.get("name"));
       result.put("id", data.get("id"));
       result.put("last_commit_time", data.get("last_activity_at"));


        return result;
    }

    public List projectMembers(Long id) {

        List result = new ArrayList<>();

        List<Map<String, Object>> data = baseUrl().get()
                .uri("/" + id + "/members")
                .header("Authorization", "Bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        for(Map<String, Object> data1 : data){
            Map<String, Object> proc = new HashMap<>();

            proc.put("id", data1.get("id"));
            proc.put("name", data1.get("name"));

            result.add(proc);

        }


        return result;
    }

    public Map projectMemberAdd(Long id, Map member) {

        Map data = baseUrl().post()
                .uri("/" + id + "/members")
                .header("Authorization", "Bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .body(BodyInserters.fromObject(member))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return data;
    }

    public Boolean projectMemberDel(Long id, Long userId) {

        return baseUrl().delete()
                .uri("/" + id + "/members/" + userId)
                .header("Authorization", "Bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

}