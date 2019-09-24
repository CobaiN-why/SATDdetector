package main.java.controller;

import main.java.service.ProjectScanner;
import main.java.util.FileUtil;
import main.java.util.ProjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghaoye on 2019/9/17.
 */
public class PrjController {

    public void satdController(String file){
        FileUtil fileUtil = new FileUtil();
        List<String> gitList = fileUtil.readFile(file);
        ProjectUtil projectUtil = new ProjectUtil();

        List<String> fileList = new ArrayList<>();
        for (String git : gitList){
            String prjName = git.substring(git.lastIndexOf('/') + 1, git.lastIndexOf('.'));
            fileList.add(prjName);
        }
        List<String> deleted = fileUtil.createDir("projects", fileList);

        //下载需要分析所缺少的项目
        for (String gitUri : gitList) {
            if (deleted.contains(gitUri.substring(gitUri.lastIndexOf('/') + 1, gitUri.lastIndexOf('.'))))
                continue;
            System.out.println("Downloading the project: " + gitUri);
            projectUtil.downloadProject(gitUri);
        }
        System.out.println("All the projects have been downloaded!");

        //对每个项目进行分析
        for (String gitUri : gitList) {
            String projectName = gitUri.substring(gitUri.lastIndexOf('/'), gitUri.lastIndexOf('.'));
            String projectPath = "./projects" + projectName;

            System.out.println("Analyzing the " + projectName);

//
//            projectPath = "/Users/wanghaoye/workspace";
            //AST构建，提取SATD comment
            ProjectScanner projectScanner = new ProjectScanner();
            List<String> comments = projectScanner.getSatd(projectPath);
            //将所有分析结果保存 判断结果是否为空
            if (comments == null || comments.size() == 0){
                System.out.println("No SATD.");
                return;
            }
            //todo 将每个project的结果都保存至projects文件夹下，并已项目名命名
            fileUtil.saveFile(comments, projectPath);

//            for (String comment : comments){
//                System.out.println(comment);
//            }
        }

        System.out.println("Done!");
    }
}
