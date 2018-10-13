package com.controller;

import com.entity.Projects;
import com.service.ProjectService;
import com.until.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Prigram: com.controller
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-21 16:29
 */

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    //提交项目
    public String uploadProject(Projects projects){
        projects.setPsType(0);
        int row = projectService.uploadProject(projects);
        if (row == 1) {
            return ResponseUtils.responseSuccess(1,"项目提交成功");
        }else {
            return ResponseUtils.responseError(2,"项目提交失败");
        }

    }
}
