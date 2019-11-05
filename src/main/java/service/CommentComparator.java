package main.java.service;

import java.util.ArrayList;
import java.util.List;

public class CommentComparator {
    public List<List<String>> commentCmp(List<String> last, List<String> now){
        List<List<String>> result = new ArrayList<>();
        List<String> diffA = new ArrayList<>(last); //在第二个版本时去除的
        List<String> diffB = new ArrayList<>(now); //在第二个版本时新加的
        diffA.removeAll(now);
        diffB.removeAll(last);
        result.add(diffA);
        result.add(diffB);
        return result;
    }
}
