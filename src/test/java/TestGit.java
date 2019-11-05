package test.java;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestGit {
    @Test
    public void testGitCommit(){
        String filePath = "./projects/google-gulava";
        try {
            Git git = Git.open(new File(filePath));
            Repository repository = git.getRepository();

            Map<Integer, String> commits = new TreeMap<Integer, String>();
            Iterable<RevCommit> logHistory = git.log().call();
            for (RevCommit revCommit : logHistory){
                String commitId = revCommit.getName();//获取commit id
                int commitTime = revCommit.getCommitTime();   //Fri Oct 6 10:30:26 2017 会按照秒转化为INT，从最早的开始找
                commits.put(commitTime, commitId);
            }
            //todo: 根据排序后的commit list按时间分析
            git.checkout().setName("master").call();//将所有的commitId记录后，直接用checkout命令切换版本
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testGitTag(){
        String filePath = "./projects/Bilibili-boxing";
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
