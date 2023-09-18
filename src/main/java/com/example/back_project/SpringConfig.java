package com.example.back_project;


import com.example.back_project.project.ProjectRepository;
import com.example.back_project.project.ProjectService;
import com.example.back_project.user.UserRepository;
import com.example.back_project.user.UserService;
import org.springframework.context.annotation.Bean;


public class SpringConfig {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    public SpringConfig(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

//    @Bean
//    public UserService userService() {
//        return new UserService(userRepository);
//    }

    @Bean
    public ProjectService projectService() {
        return new ProjectService(projectRepository);
    }

}