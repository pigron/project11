package com.example.back_project.project;

import com.example.back_project.base.BaseWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping(value="/project")
public class ProjectController {

    public WebClient baseUrl() {
        return new BaseWeb().baseWeb();
    }
    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectService projectService, ProjectRepository projectRepository){
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/list") // projects 목록 조회
    public List<Project> getRequest(){

        List res = baseUrl().get()
                .header("Authorization","bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        for(int i = 0; i < res.size(); i++) { // 하나씩 가져와서 DB와 매핑시키기
            System.out.println(res.get(i)); // LinkedHashMap 으로 받아옴
            projectService.getProject((Map<String, Object>) res.get(i)); // Map<String, Object> 형태로 보내기
        }
        return projectRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/read/{id}")
    public Map fileread(@PathVariable Long id){
        return projectService.read(id);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/create") // project 생성
    public Map create(@RequestBody Map<String, Object> data) {

        data.put("name", "babo");

        System.out.println("org data = " + data);
        System.out.println("=================================");
        System.out.println("data type = " + data.getClass().getName());


        return data;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/update/{id}") // project 수정
    public Project update(@RequestBody Map<String, Object> data, @PathVariable Long id) {

        Map res = baseUrl().put()
                    .uri("/" + id)
                    .header("Authorization", "Bearer glpat-UnsExNbKsAqG6JwqxCfy")
                    .body(BodyInserters.fromObject(data))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

        return projectService.projectUpdate(res);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{id}") // project 삭제
    public String delete(@PathVariable Long id){
        return baseUrl().delete()
                .uri("/" + id)
                .header("Authorization", "Bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/members/{id}")
    public List projectMembers(@PathVariable Long id) {
        return projectService.projectMembers(id);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/members/add/{id}")
    public Map projectMemberAdd(@PathVariable Long id, @RequestBody Map member) {
        return projectService.projectMemberAdd(id, member);
    }

    @RequestMapping(method = RequestMethod.DELETE, path="/members/del/{id}/{user_id}")
    public Boolean projectMemberDel(@PathVariable Long id, @PathVariable Long user_id){
        return projectService.projectMemberDel(id, user_id);
    }

}