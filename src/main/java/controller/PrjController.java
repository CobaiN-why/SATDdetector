package main.java.controller;

import main.java.service.CommentComparator;
import main.java.service.ProjectScanner;
import main.java.util.ExcelUtil;
import main.java.util.FileUtil;
import main.java.util.ProjectUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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

    public List<String> scanOnePrj(String filePath, String tagName){
        ExcelUtil excelUtil = new ExcelUtil();
        //AST构建，提取SATD comment
        ProjectScanner projectScanner = new ProjectScanner();
        List<String> comments = projectScanner.getSatd(filePath);
        //将所有分析结果保存 判断结果是否为空
        if (comments == null || comments.size() == 0){
            System.out.println("No SATD.");
            return comments;
        }
        excelUtil.saveExcel(comments, filePath + tagName);
        return comments;
    }

    public void versionController(int versionNum, List<String> gitList){
        for (String gitUri : gitList){
            String prjName = gitUri.substring(gitUri.lastIndexOf("github.com/") + 11, gitUri.lastIndexOf('.'));
            System.out.println("Analyzing the " + prjName);
            String filePath = "./projects/" + prjName.replaceAll("/", "-");
            List<List<String>> satdVersions = new ArrayList<>();
            List<String> tagNames = new ArrayList<>();

            try {
                Git git = Git.open(new File(filePath));
                Repository repository = git.getRepository();

                List<Ref> call = git.tagList().call();
                int lenCall = call.size();
                for (int i = lenCall - 1 ; i >= lenCall-versionNum ; i--) {
                    List<String> tmp = new ArrayList<>();
                    Ref ref = call.get(i);
                    String tagName = ref.getName().substring(10);
                    String commitName = ref.getObjectId().getName();
                    System.out.println("Switch to the version: " + tagName);
                    git.checkout().setName(commitName).call();//将所有的commitId记录后，直接用checkout命令切换版本
                    tmp = scanOnePrj(filePath, tagName);
                    tagNames.add(tagName);
                    satdVersions.add(tmp);
                    //重新至恢复master分支
                    git.checkout().setName("master").call();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //todo: 对satdVersions（按照最新，往前N个版本）进行分析，
            int lastCount = 0;
            List<String> lastComments = satdVersions.get(versionNum-1);
            CommentComparator commentComparator = new CommentComparator();
            for (int i = versionNum-2; i >= 0; i--){ //这样就是从最早的一个version开始分析
                List<String> nowComments = satdVersions.get(i);
                List<List<String>> compare = commentComparator.commentCmp(lastComments, nowComments);
                // todo 包括每个版本包含的satd数量，增加和减少的satd是哪些？
            }
        }
    }
}
