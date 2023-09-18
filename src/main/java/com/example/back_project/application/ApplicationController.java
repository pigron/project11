package com.example.back_project.application;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping(value="/app")
public class ApplicationController {
    ApplicationService applicationService = new ApplicationService();

    @RequestMapping(method = RequestMethod.GET, value="/list")
    public List appList() {
        return applicationService.appList();
    }

    @RequestMapping(method = RequestMethod.GET, value="/info/{name}")
    public Map appInfo(@PathVariable String name) {
        return applicationService.appInfo(name);
    }

    @RequestMapping(method = RequestMethod.GET, value="/tree/{name}")
    public List appTree(@PathVariable String name) {
        return applicationService.appTree(name);
    }

    @RequestMapping(method = RequestMethod.POST, value="/create")
    public Map appCreate(@RequestBody Map appDesc ) {
        return applicationService.appCreate(appDesc);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/delete/{name}")
    public Map appDelete(@PathVariable String name) {
        return applicationService.appDelete(name);
    }

    @RequestMapping(method = RequestMethod.POST, value="/repo/create")
    public Map repoCreate(@RequestBody Map repoInfo) {
        return applicationService.repoCreate(repoInfo);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/repo/delete/**")
    public Map repoDelete(HttpServletRequest request) {
        String path
                = request.getRequestURI().split(request.getContextPath()+"/repo/delete/")[1];
        return applicationService.repoDelete(path);
    }

    @RequestMapping(method = RequestMethod.GET, value="/repo/select/**")
    public List repoApp(HttpServletRequest request) {
        String path
                = request.getRequestURI().split(request.getContextPath()+"/repo/select/")[1];
        return applicationService.repoAPP(path);
    }

    @RequestMapping(method = RequestMethod.POST, value="/sync/{name}")
    public Map syncApp(@PathVariable String name) {
        return applicationService.syncApp(name);
    }

    @PostMapping("/test/map")
    public Map<String, Object> test(@RequestBody Map<String, Object> body) {
        return body;
    }

    @PostMapping("/test/string")
    public String test1(@RequestBody String body) {
        return body;
    }

}