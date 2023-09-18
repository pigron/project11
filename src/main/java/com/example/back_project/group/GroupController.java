package com.example.back_project.group;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping(value="/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/list" ,headers = "Authorization")
    public List groupList(@RequestHeader("Authorization") String token) {
        return groupService.groupList(token);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/members")
    public List groupMemberList(@PathVariable Long id) {
        return groupService.groupMemberList(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/info")
    public Optional<Group> groupInfo(@PathVariable Long id) {
        return groupService.groupInfo(id);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/add")
    public Map groupMemberAdd(@PathVariable Long id, @RequestBody Map<String, Object> member) {
        return groupService.groupMemberAdd(id, member);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}/del/{user_id}")
    public Map groupMemberDel(@PathVariable Long id, @PathVariable Long user_id) {
        return groupService.groupMemberDel(id, user_id);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/create")
    public Group groupCreate(@RequestBody Map<String, Object> groupInfo) {
        return groupService.groupCreate(groupInfo);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}/delete")
    public Map groupDelete(@PathVariable Long id) {
        return groupService.groupDelete(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/projects")
    public List groupProjects(@PathVariable Long id) {
        return groupService.groupProjects(id);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}/update")
    public Map groupUpdate(@PathVariable Long id, @RequestBody Map update) {
        return groupService.groupUpdate(id, update);
    }
}