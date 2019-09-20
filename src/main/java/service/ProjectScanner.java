package main.java.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import satd_detector.core.utils.SATDDetector;
import main.java.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghaoye on 2019/9/17.
 */
public class ProjectScanner {
    public List<String> getSatd(String projectPath) {
        List<String> satds = new ArrayList<String>();
        SATDDetector detector = new SATDDetector();

        //该项目下所有java文件
        List<String> javaFiles = new ArrayList<>();
        FileUtil fileUtil = new FileUtil();
        javaFiles = fileUtil.findJavas(projectPath);
        if (javaFiles.size() == 0)
            return satds;
        for (String file : javaFiles){
            try {
                CompilationUnit cu = StaticJavaParser.parse(new File(file));
                List<Comment> comments = cu.getAllContainedComments();
                for (Comment comment : comments) {
                    String content = comment.getContent();
                    if (detector.isSATD(content)){
                        satds.add(content);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        return satds;
    }
}
