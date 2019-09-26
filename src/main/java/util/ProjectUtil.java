package main.java.util;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.io.File;

/**
 * Created by wanghaoye on 2019/9/17.
 */
public class ProjectUtil {

    public void downloadProject(String gitUri){
        try {
            String prjName = gitUri.substring(gitUri.lastIndexOf('/'), gitUri.lastIndexOf('.'));
            File file = new File("./projects/" + prjName);
            if (!file.exists())
                Git.cloneRepository().setURI(gitUri).setDirectory(file).setProgressMonitor(new SimpleProgressMonitor()).call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class SimpleProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
            System.out.println("Starting work on " + totalTasks + " tasks");
        }

        @Override
        public void beginTask(String title, int totalWork) {
            System.out.println("Start " + title + ": " + totalWork);
        }

        @Override
        public void update(int completed) {
            //System.out.print(completed);
            //System.out.print("\b");
        }

        @Override
        public void endTask() {
            System.out.println("Done.");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}
