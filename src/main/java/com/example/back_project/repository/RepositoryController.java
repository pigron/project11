package com.example.back_project.repository;

import com.example.back_project.base.BaseWeb;

import com.sun.jndi.toolkit.url.Uri;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping(value="/repo")
public class RepositoryController { // 읽기만 하는 Controller

    RepositoryService repositoryService = new RepositoryService();

    public WebClient baseUrl() {
        return new BaseWeb().baseWeb();
    }

    public WebClient normalUrl() {
        return new BaseWeb().normalWeb();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/file/list/{id}")
    public List bottomfileList(@PathVariable Long id) {
        System.out.println("controller");
        return repositoryService.bottomFileList(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/file/list/{id}/**")
    public List fileList(@PathVariable Long id, HttpServletRequest request) {

        String path
                = request.getRequestURI().split(request.getContextPath()+"/file/list/" + id + "/")[1];

        System.out.println("controller");
        return repositoryService.newFileList(id, path);
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/file/list/{id}") // 파일 목록 조회
//    public List filelist(@PathVariable Long id){
//        return baseUrl().get()
//                .uri("/" + id + "/repository/tree")
//                .accept(MediaType.APPLICATION_JSON)
//                .header("Authorization" , "bearer glpat-YuYqJs1TRKGwxrw8oJCY")
//                .retrieve()
//                .bodyToMono(List.class)
//                .block();
//    }

    @RequestMapping(method = RequestMethod.GET, path = "/test/{path:.+}")
    public String test(@PathVariable URI path) {

        System.out.println(path);

        String result = "http://192.168.100.75:30780/api/v4/projects/14/repository/files/" + path + "/blame?ref=main";

        return baseUrl().get()
                .uri(URI.create(result))
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(String.class)
                .block();

//        return path;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/file/read/{id}/**") // 파일 내용 불러오기
    public Map fileread(@PathVariable Long id, HttpServletRequest request) {

        String path
                = request.getRequestURI().split(request.getContextPath()+"/file/read/" + id + "/")[1];

        String org = path.replace("/", "%2F");

        String uri1 = "http://192.168.100.75:30780/api/v4/projects/" + id + "/repository/files/" + org + "?ref=main";

        String uri2 = "http://192.168.100.75:30780/api/v4/projects/" + id + "/repository/files/" + org + "/blame?ref=main";

        String uri3 = "http://192.168.100.75:30780/api/v4/projects/" + id + "/repository/files/" + org + "/raw";

//        return normalUrl().get()
//                .uri("http://192.168.100.75:30780/api/v4/projects/" + id + "/repository/files/" + encoded + "/blame?ref=main")
//                .header("Authorization" , "bearer glpat-YuYqJs1TRKGwxrw8oJCY")
//                .retrieve()
//                .bodyToMono(List.class)
//                .block();

        Map filename = baseUrl().get()
                .uri(URI.create(uri1))
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List timedata = baseUrl().get()
                .uri(URI.create(uri2))
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        String content = baseUrl().get()
                .uri(URI.create(uri3))
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> out = new HashMap<>();
        Map<String, Object> remaster = (Map<String, Object>) timedata.get(0);
        Map<String, Object> remaster2 = (Map<String, Object>) remaster.get("commit");

        out.put("name", filename.get("file_name"));
        out.put("path", filename.get("file_path"));
        out.put("lines", content);
        out.put("authored_date", remaster2.get("authored_date"));
        out.put("committed_date", remaster2.get("committed_date"));
        out.put("message", remaster2.get("message")); // 파일을 생성할 때 등록하는 메세지

        return out;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/file/create/{id}/**", headers = "Authorization") // 파일 생성
    public Map filecreate(@RequestBody Map<String, Object> file, @PathVariable Long id, HttpServletRequest request, @RequestHeader("Authorization") String token){

        String path
                = request.getRequestURI().split(request.getContextPath()+"/file/create/" + id + "/")[1];

        String name = path.replace("/", "%2F");

        String uri1 = "http://192.168.100.75:30780/api/v4/projects/" + id + "/repository/files/" + name;

        return baseUrl().post()
                .uri(URI.create(uri1))
                .body(BodyInserters.fromObject(file))
                .header("Authorization" , "bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/file/update/{id}/**", headers = "Authorization") // 파일 수정
    public Map fileupdate(@RequestBody Map<String, Object> file, @PathVariable Long id,  HttpServletRequest request, @RequestHeader("Authorization") String token) {

        String path
                = request.getRequestURI().split(request.getContextPath()+"/file/update/" + id + "/")[1];

        String name = path.replace("/", "%2F");

        String uri1 = "http://192.168.100.75:30780/api/v4/projects/" + id + "/repository/files/" + name;

        return baseUrl().put()
                .uri(URI.create(uri1))
                .body(BodyInserters.fromObject(file))
                .header("Authorization" , "bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/file/delete/{id}/**", headers = "Authorization") // 파일 삭제
    public Boolean filedelete(@RequestBody Map<String, Object> file, @PathVariable Long id, HttpServletRequest request, @RequestHeader("Authorization") String token) {

        String path
                = request.getRequestURI().split(request.getContextPath()+"/file/delete/" + id + "/")[1];

//        String proc = path.replace(".", "%2E");

        String name = path.replace("/", "%2F");

        String uri1 = "http://192.168.100.75:30780/api/v4/projects/" + id + "/repository/files/" + name;

        return baseUrl().method(HttpMethod.DELETE)
                .uri(URI.create(uri1))
                .body(BodyInserters.fromObject(file))
                .header("Authorization" , "bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/commits/list/{id}") // commit 목록 가공 후 return 하기
    public List<Object> commits(@PathVariable Long id){

        List res = baseUrl().get()
                .uri("/" + id + "/repository/commits")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List<Object> process = new ArrayList<>();

        for(int i = 0; i < res.size(); i ++){
            process.add(i, repositoryService.Commit((Map<String, Object>) res.get(i)));
//            process.set(i, repositoryService.Commit((Map<String, Object>) res.get(i)));
        }
        return process;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/commits/diff/{id}/{commitId}")
    public Map<String, Object> commitDiff(@PathVariable Long id, @PathVariable String commitId){

        List diff1 = baseUrl().get()
                .uri("/" + id + "/repository/commits/" + commitId + "/diff")
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        Map<String, Object> diff2 = (Map<String, Object>) diff1.get(0);

        Map<String, Object> res = new HashMap<>();

        res.put("diff", diff2.get("diff"));
        res.put("new_path", diff2.get("new_path"));
        res.put("old_path", diff2.get("old_path"));
        res.put("new_file", diff2.get("new_file"));
        res.put("renamed_file", diff2.get("renamed_file"));
        res.put("deleted_file", diff2.get("deleted_file"));

        return res;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/commits/info/{id}/{commitId}")
    public Map commitInfo(@PathVariable Long id, @PathVariable String commitId){

        return repositoryService.CommitInfo(id, commitId);
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/file/list/{id}")
//    public List list(@PathVariable Long id) {
//
//        List result = new ArrayList<>();
//
//        Map<String, Object> path = new HashMap<>();
//
//        List name = baseUrl().get()// name과 type 가져오기
//                .uri("/" + id + "/repository/tree")
//                .accept(MediaType.APPLICATION_JSON)
//                .header("Authorization", "bearer glpat-UnsExNbKsAqG6JwqxCfy")
//                .retrieve()
//                .bodyToMono(List.class)
//                .block();
//
//        for(int i = 0 ; i < name.size(); i++){
//            path = (Map<String, Object>) name.get(i);
//            result.add(repositoryService.FileList(id, path));
//        }
//
//        return result;
//    }
}