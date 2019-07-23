package tool;


import java.util.*;

public class MyTranslator {

    public static List<String> translate(String input) {

        Map<Integer, List<String>> helperMap = new HashMap<>();
        String str = removeRedundantBrackets(input).trim();

        List<String> segments = parseByBrackets(str);
        for (int i = 0; i < segments.size(); i++) {
            List<String> parseResult = handleSegment(segments.get(i).trim());
            helperMap.put(i, parseResult);
        }

        return getAllCombinations(helperMap);
    }

    public static List<String> handleSegment(String input) {
        List<String> segmentResult = new ArrayList<>();
        if(!containsBrackets(input)) {
            segmentResult.addAll(parseNoBracketStatement(input));
        }else {
            List<String> orSegments = parseByDelimeter(input,'|');
            orSegments.forEach(seg-> segmentResult.addAll(translate(seg)));
        }
        return segmentResult;

    }

    private static String removeRedundantBrackets(String input) {
        String s = input;
        if(isACompletedBracketStatement(input)) {
            s = removeLeftAndRightBrackets(input);
        }

        if(!input.contains("|")) {
            s = removeAllBrackets(input);
        }
        return s;
    }

    private static List<String> parseNoBracketStatement(String input) {
        Map<Integer, List<String>> helperMap = new HashMap<>();

        String[] splits = input.split("\\s+");
        for(int i = 0 ;i< splits.length;i++){
            String[] tokens = splits[i].split("\\|");
            helperMap.put(i,Arrays.asList(tokens));
        }
        return getAllCombinations(helperMap);
    }

    private static boolean isACompletedBracketStatement(String str) {
        if(str.length()<2){return false;}
        if(!(str.startsWith("{")&&str.endsWith("}"))){return false;}
        int count = 0;
        for(int i = 0 ; i < str.length(); i++){
            char c = str.charAt(i);
            if(c == '{'){
                ++count;
            }
            if(c == '}'){
                --count;
            }
            if(count == 0){
                return i == str.length() - 1;
            }
        }
        return false;
    }

    private static boolean containsBrackets(String input){
        return input.contains("{") && input.contains("}");
    }

    public static List<String> parseByBrackets(String input) {
        Stack stack = new Stack();
        List<String> result = new ArrayList();

        StringBuffer temp = new StringBuffer("");

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '{': {
                    if (stack.isEmpty()) {
                        if (i > 0 && input.charAt(i - 1) != '|') {
                            String aSegment = temp.toString().trim();
                            if (!aSegment.isEmpty()) {
                                result.add(aSegment);
                                temp.delete(0, temp.length());
                            }
                        }
                    }
                    temp.append(c);
                    stack.push("{");
                    break;
                }
                case '}': {
                    temp.append(c);
                    stack.pop();
                    if (i < input.length() - 1 && input.charAt(i + 1) == '|') {
                        break;
                    } else if (stack.isEmpty()) {
                        result.add(temp.toString().trim());
                        temp.delete(0, temp.length());
                    }
                    break;
                }
                default: {
                    temp.append(c);
                }
            }
        }

        if (!temp.toString().isEmpty()) {
            result.add(temp.toString().trim());
        }
        print("parse by brackets", result);

        return result;
    }

    private static List<String> parseByDelimeter(String input, char delemiter){
        Stack stack = new Stack();
        List<String> result = new ArrayList<>();
        StringBuffer temp = new StringBuffer("");
        for(int i = 0; i < input.length();i++){
            char c = input.charAt(i);
            if (c =='{'){
                temp.append(c);
                stack.push("{");
            }else if (c=='}'){
                temp.append(c);
                stack.pop();
            }else if(c == delemiter){
                if(stack.isEmpty()){
                    result.add(temp.toString().trim());
                    temp.delete(0,temp.length());
                }else{
                    temp.append(c);
                }
            }else{
                temp.append(c);
            }
        }
        if (!temp.toString().isEmpty()) {
            result.add(temp.toString().trim());
        }
        return result;
    }

    private static String removeLeftAndRightBrackets(String str) {
        return str.startsWith("{") && str.endsWith("}") ? str.replaceAll("^.|.$","") : str;
    }

    private static String removeAllBrackets(String str) {
        return str.replaceAll("\\{","").replaceAll("\\}","");
    }

    private static List<String> getAllCombinations(Map<Integer,List<String>> aMap){
        List<String> combined = new ArrayList<>();

        if(aMap.get(0)==null){
            return combined;
        }

        combined.addAll(aMap.get(0));
        return fanOut(aMap, combined,1);
    }

    private static List<String> fanOut(Map<Integer,List<String>> aMap, List<String> aCollection, Integer idx){
        List<String> currentSeg = aMap.get(idx);
        if(currentSeg==null){
            return aCollection;
        }
        List<String> fanOutResult = new ArrayList<>();
        aCollection.forEach(e-> currentSeg.forEach(seg-> {
            String delimiter = isChinese(e) && isChinese(seg) ? "": " ";
            fanOutResult.add(e + delimiter + seg);
        }));
        return fanOut(aMap,fanOutResult,++idx);
    }

    private static void print(String prefix, List<String> content){
        content.forEach(e-> System.out.println("[" + prefix + "] " + e));

    }

    private static boolean isChinese(String s){
        return s.codePoints().anyMatch(
                codepoint->Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

}

