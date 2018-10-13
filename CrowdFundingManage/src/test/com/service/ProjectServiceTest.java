package com.service;

import com.entity.Projects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//向JUnit4声明spring.xml配置文件的地址
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:spring-mvc.xml"})
public class ProjectServiceTest {

    @Autowired
    ProjectService projectService;
    @Test
    public void uploadProject() {
        Projects projects = new Projects();
        projects.setPsMoney(new BigDecimal(8000));
        projects.setPsGetmoney(new BigDecimal(10000));
        projects.setPsName("大忽悠");
        projectService.uploadProject(projects);
    }

    @Test
    public void findExpireProject() {
        List<Projects> expireProject = projectService.findExpireProject(0);
        assertTrue(expireProject.size()!=0);
    }

    @Test
    public void markFailure() {
        int row = projectService.markFailure(4, 1);
        assertTrue(row==1);
    }
}