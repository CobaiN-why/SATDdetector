package main.java.controller;

import main.java.service.ProjectScanner;
import main.java.util.FileUtil;
import main.java.util.ProjectUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wanghaoye on 2019/9/17.
 */
public class PrjController {

    public List<String> dowmloadController(String file){
        FileUtil fileUtil = new FileUtil();
        List<String> gitList = fileUtil.readFile(file);
        ProjectUtil projectUtil = new ProjectUtil();

        List<String> fileList = new ArrayList<>();
        for (String git : gitList){
            String prjName = git.substring(git.lastIndexOf("github.com/") + 11, git.lastIndexOf('.'));
            fileList.add(prjName);
        }
        List<String> deleted = fileUtil.createDir("projects", fileList);

        //下载需要分析所缺少的项目
        for (String gitUri : gitList) {
            if (deleted.contains(gitUri.substring(gitUri.lastIndexOf("github.com/") + 11, gitUri.lastIndexOf('.'))))
                continue;
            System.out.println("Downloading the project: " + gitUri);
            projectUtil.downloadProject(gitUri);
        }
        System.out.println("All the projects have been downloaded!");

        return gitList;

    }

    public void scanController(List<String> gitList){
        FileUtil fileUtil = new FileUtil();
        //对每个项目进行分析
        for (String gitUri : gitList) {
            String projectName = gitUri.substring(gitUri.lastIndexOf("github.com/") + 11, gitUri.lastIndexOf('.'));
            projectName = projectName.replaceAll("/", "-");
            String projectPath = "./projects/" + projectName;

            System.out.println("Analyzing the " + projectName);

            //AST构建，提取SATD comment
            ProjectScanner projectScanner = new ProjectScanner();
            List<String> comments = projectScanner.getSatd(projectPath);
            //将所有分析结果保存 判断结果是否为空
            if (comments == null || comments.size() == 0){
                System.out.println("No SATD.");
                return;
            }
            fileUtil.saveFile(comments, projectPath);
        }
    }

    public void versionController(int versionNum, List<String> gitList){
        for (String gitUri : gitList){
            String prjName = gitUri.substring(gitUri.lastIndexOf("github.com/") + 11, gitUri.lastIndexOf('.'));
            String filePath = "./projects/" + prjName.replaceAll("/", "-");

            try {
                Git git = Git.open(new File(filePath));
                Repository repository = git.getRepository();

                Map<Integer, String> commits = new TreeMap<Integer, String>();
                Iterable<RevCommit> logHistory = git.log().call();
                for (RevCommit revCommit : logHistory){
                    String commitId = revCommit.getName();//获取commit id
                    System.out.println(commitId);
                    int commitTime = revCommit.getCommitTime();   //Fri Oct 6 10:30:26 2017 会按照秒转化为INT，从最早的开始找
                    commits.put(commitTime, commitId);
                }

                List<Ref> call = git.tagList().call();
                for (Ref ref : call) {
                    System.out.println("Tag: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
                }

                //todo: 根据排序后的commit list按时间分析
                git.checkout().setName("master").call();//将所有的commitId记录后，直接用checkout命令切换版本
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
