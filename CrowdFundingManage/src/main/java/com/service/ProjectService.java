package com.service;

import com.dao.ProjectsMapper;
import com.entity.Projects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Prigram: com.service
 * @Description: TODO
 * @Author: DongFang
 * @CreaeteTime: 2018-09-21 16:09
 */
@Service
public class ProjectService {
    @Autowired
    ProjectsMapper projectsMapper;

    public int uploadProject(Projects projects) {
        return projectsMapper.insert(projects);
    }

    //查询筹款到期项目
    public List<Projects> findExpireProject(int page) {

            return projectsMapper.selectByPsType(page);
    }

    public int markFailure(int psType, Integer psId) {
        //通过项目的id获取到项目，设置项目众筹经过的审核的，更新数据库
        Projects projects = projectsMapper.selectByPrimaryKey(psId);
        projects.setPsType(psType);
        return projectsMapper.updateByPrimaryKey(projects);
    }
}
