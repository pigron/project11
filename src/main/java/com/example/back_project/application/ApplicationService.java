package com.example.back_project.application;

import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.back_project.base.BaseWeb;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {

    public WebClient baseUrl() {
        return new BaseWeb().argoWeb();
    }

    public List appList() {
        Map app = baseUrl().get()
                .uri("/applications")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List result = new ArrayList<>();

        List app1 = (List) app.get("items");

        Map mf = new HashMap<>();

        for (int i = 0 ; i < app1.size(); i++) {
            Map data = (Map) app1.get(i);
            System.out.println(data);

            Map metadata = (Map) data.get("metadata");

            Map spec = (Map) data.get("spec");
            Map source = (Map) spec.get("source");

            Map status = (Map) data.get("status");
            Map sync = (Map) status.get("sync");
            Map health = (Map) status.get("health");

            mf.put("name", metadata.get("name"));
//            mf.put("namespace", metadata.get("namespace"));
//            mf.put("creationTimestamp", metadata.get("creationTimestamp"));
//            mf.put("repoURL", source.get("repoURL"));
            mf.put("sync_status", sync.get("status"));
            mf.put("health_status", health.get("status"));

            result.add(mf);

            mf = new HashMap<>(); // mf 초기화
        }

        return result;
    }

    public Map appInfo(String name) {

        Map app = baseUrl().get()
                .uri("/applications/" + name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map result = new HashMap<>();

        Map metadata = (Map) app.get("metadata");

        Map spec = (Map) app.get("spec");
        Map source = (Map) spec.get("source");

        Map status = (Map) app.get("status");
        Map sync = (Map) status.get("sync");
        Map health = (Map) status.get("health");

        result.put("name", metadata.get("name"));
        result.put("namespace", metadata.get("namespace"));
        result.put("creationTimestamp", metadata.get("creationTimestamp"));
        result.put("repoURL", source.get("repoURL"));
        result.put("branch", source.get("targetRevision"));
        result.put("path", source.get("path"));
        result.put("sync_status", sync.get("status"));
        result.put("health_status", health.get("status"));

        return result;
    }

    public Map appCreate(Map appDesc) {

        Map app = baseUrl().post()
                .uri("/applications")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(appDesc))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return app;
    }

    public Map appDelete(String name) {

        Map app = baseUrl().delete()
                .uri("/applications/" + name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return app;
    }

// ==============================================================================================

    public Map repoCreate(Map repoInfo) {

        Map app = baseUrl().post()
                .uri("/repositories")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(repoInfo))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return app;
    }

    public Map repoDelete(String url) {

        Map app = baseUrl().delete()
                .uri(uriBuilder -> uriBuilder.path("/repositories/{id}").build(url) )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return app;
    }

    public List repoAPP(String request) {

        List result = new ArrayList<>();

        Map app =  baseUrl().get()
                .uri(uriBuilder -> uriBuilder.path("/applications").queryParam("repo", request).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List data = (List) app.get("items");

        for(int i = 0; i < data.size() ; i ++) {
            Map data1 = (Map) data.get(i);
            Map need = new HashMap();

            Map metadata = (Map) data1.get("metadata");

            Map spec = (Map) data1.get("spec");
            Map source = (Map) spec.get("source");

            Map status = (Map) data1.get("status");
            Map sync = (Map) status.get("sync");
            Map health = (Map) status.get("health");

            need.put("name", metadata.get("name"));
            need.put("namespace", metadata.get("namespace"));
            need.put("creationTimestamp", metadata.get("creationTimestamp"));
            need.put("repoURL", source.get("repoURL"));
            need.put("branch", source.get("targetRevision"));
            need.put("path", source.get("path"));
            need.put("sync_status", sync.get("status"));
            need.put("health_status", health.get("status"));

            result.add(need);

            need = new HashMap<>();
        }

        return result;
    }

    public Map syncApp(String name) {

        return baseUrl().post()
                .uri("/applications/" + name + "/sync")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

    }

    public List appTree(String name) {

        Map data = baseUrl().get()
                .uri("/applications/" + name + "/resource-tree")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List data1 = (List) data.get("nodes");

        List<Map<String, Object>> result = new ArrayList();

        for (int i = 0 ; i < data1.size() ; i ++) {
            Map proc = (Map) data1.get(i);

            Map proc1 = new HashMap<>();

            List parent = (List) proc.get("parentRefs");

            if(parent != null ) {
                Map parent1 = (Map) parent.get(0);
                proc1.put("parentRefs_uid", parent1.get("uid"));
            }

            proc1.put("uid", proc.get("uid"));
            proc1.put("kind", proc.get("kind"));
            proc1.put("name", proc.get("name"));
            proc1.put("createdAt", proc.get("createdAt"));

            result.add(proc1);

            proc1 = new HashMap<>();
            parent = new ArrayList<>();
        }


        List<Map<String, Object>> rootNodes = new ArrayList<>(); // ChatGPT의 힘

        for (Map<String, Object> node : result) {   // parents가 없는 애들 모으기
            if (!node.containsKey("parentRefs_uid")) {
                rootNodes.add(node);
            }
        }

        Map<String, Map<String, Object>> dataMap = new HashMap<>(); // uid만 모으기
        for (Map<String, Object> node : result) {
            dataMap.put((String) node.get("uid"), node);
        }

        for (Map<String, Object> node : result) {
            if (node.containsKey("parentRefs_uid")) {  // parent가 존재하는 애들
                String parentUid = (String) node.get("parentRefs_uid");
                Map<String, Object> parentNode = dataMap.get(parentUid);
                if (parentNode != null) { //
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parentNode.get("children");
                    if (children == null) {
                        children = new ArrayList<>();
                        parentNode.put("children", children);
                    }
                    children.add(node);
                }
            }
        }

        //========================================================== 삽질 했던 것
//
//        List result1 = new ArrayList<>();
//
//        for (int i = 0; i < result.size() ; i ++) { // 리스트를 불러온다
//
//            Map parent1 = (Map) result.get(i);
//            List children1 = new ArrayList<>();
//
//            for(int j = 0; j < result.size(); j ++) { // 리스트 객체 하나를 불러와 리스트에 있는 것과 비교한다.
//
//                Map children = (Map) result.get(j);
//
//                if (parent1.get("uid").equals(children.get("parentRefs_uid"))) {
//                    children1.add(result.get(j));
//                    parent1.remove("parentRefs_uid");
//                }
//            }
//            parent1.put("children", children1);
//
//            result1.add(i, parent1);
//
//            children1 = null;
//        }
//
//        for (int i = 0; i<result1.size();i++) {
//            Map data2 = (Map) result1.get(i);
//
//            if(data2.containsKey("parentRefs_uid")) {
//                System.out.println(result1.get(i));
//                result1.remove(i);
//            }
//
//        }

        return rootNodes;
    }


}