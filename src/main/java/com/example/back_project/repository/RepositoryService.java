package com.example.back_project.repository;

import com.example.back_project.base.BaseWeb;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class RepositoryService {

    public WebClient baseUrl() {
        return new BaseWeb().baseWeb();
    }

    public List bottomFileList(Long id) {

        List orgList = baseUrl().get()
                .uri(uriBuilder -> uriBuilder.path("/" + id + "/repository/tree").build())
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        return orgList;

    }

    public List newFileList(Long id, String path) {

        System.out.println("Service");
        
        List orgList = baseUrl().get()
                .uri(uriBuilder -> uriBuilder.path("/" + id + "/repository/tree").queryParam("path", path).build())
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        return orgList;
    }
    public Map<String, Object> Commit(Map<String, Object> commits){
        Map<String, Object> process = new HashMap<>();

        process.put("id", commits.get("id"));
        process.put("committed_date", commits.get("committed_date"));
        process.put("committer_name", commits.get("committer_name"));
        process.put("title", commits.get("title"));

        return process;
    }

    public Map CommitInfo(Long id, String name){

        Map commitInfo = baseUrl().get()
                .uri("/" + id + "/repository/commits/" + name)
                .header("Authorization" , "bearer glpat-UnsExNbKsAqG6JwqxCfy")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map result = new HashMap<>();

        result.put("committed_date", commitInfo.get("committed_date"));
        result.put("message", commitInfo.get("message"));

        return result;
    }

//    public Map FileList(Long id, Map<String, Object> objPath){
//
////        List name = baseUrl().get()// name과 type 가져오기
////                .uri("/" + id + "/repository/tree")
////                .accept(MediaType.APPLICATION_JSON)
////                .header("Authorization", "bearer glpat-UnsExNbKsAqG6JwqxCfy")
////                .retrieve()
////                .bodyToMono(List.class)
////                .block();
//
//        Map<String, Object> objAnswer = new HashMap<>(); // result에 넣을 내용 정리
//
////        List test = new ArrayList<>(); // List 리턴 하기
//
////        for (int x = 0; x < name.size(); x++){ // 파일 리스트
////            Map<String, Object> objName = (Map<String, Object>) name.get(x); // x에 해당하는 파일 정보
//            String path = (String) objPath.get("path");
//
//            Map<String, Object> objId = baseUrl().get() // Last commit id 가져오기
//                    .uri("/" + id + "/repository/files/" + path + "?ref=main")
//                    .header("Authorization", "bearer glpat-UnsExNbKsAqG6JwqxCfy")
//                    .retrieve()
//                    .bodyToMono(Map.class)
//                    .block();
//
//            String last_commit_id = (String) objId.get("last_commit_id");
//
//            Map<String, Object> objDate = baseUrl().get()
//                    .uri("/" + id + "/repository/commits/" + last_commit_id)
//                    .header("Authorization", "bearer glpat-UnsExNbKsAqG6JwqxCfy")
//                    .retrieve()
//                    .bodyToMono(Map.class)
//                    .block();
//
//            objAnswer.put("last_committed_date", objDate.get("committed_date"));
//            objAnswer.put("last_committer_name", objDate.get("committer_name"));
//            objAnswer.put("last_message", objDate.get("message"));
//            objAnswer.put("id", objPath.get("id"));
//            objAnswer.put("name", objPath.get("name"));
//            objAnswer.put("path", objPath.get("path"));
//            objAnswer.put("type", objPath.get("type"));
//            objAnswer.put("mode", objPath.get("mode"));
//
////            test.add(objAnswer);
////            System.out.println(objAnswer);
////        }
//        return objAnswer;
//
//    }
}