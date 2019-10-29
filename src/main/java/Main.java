package main.java;

import main.java.controller.PrjController;

import java.util.List;

/**
 * Created by wanghaoye on 2019/9/17.
 */

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }
        String file = args[0].trim();
        int versionNum = Integer.parseInt(args[1].trim());

        //String file = "/Users/wanghaoye/workspace/SATDdetector/gitlist.txt";
        PrjController projectController = new PrjController();
        List<String> gitList = projectController.dowmloadController(file);
        // projectController.scanController(gitList);
        projectController.versionController(versionNum, gitList);

        System.out.println("Done!");
    }

    private static void printHelp() {
        System.out.println("Please specify the github uri list and the number of version need to parse.");
        System.out.println("For example:\n$ java -jar satd_detector.jar list.txt 6");
    }

}