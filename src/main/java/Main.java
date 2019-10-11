package main.java;

import main.java.controller.PrjController;

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
//        String file = "/Users/wanghaoye/workspace/SATDdetector/test.txt";
        PrjController projectController = new PrjController();
        projectController.satdController(file);
    }

    private static void printHelp() {
        System.out.println("Please specify the github uri list.");
        System.out.println("For example:\n$ java -jar satd_detector.jar list.txt");
    }

}