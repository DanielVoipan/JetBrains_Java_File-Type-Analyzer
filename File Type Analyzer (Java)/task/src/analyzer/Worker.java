package analyzer;

import java.io.File;
import java.util.*;

import static analyzer.Main.*;

class Worker extends Thread {
    File f;
    List<List<String>> list;

    public Worker(File f, List<List<String>> list) {
        this.f = f;
        this.list = list;
    }

    @Override
    public void run() {
        try {
            byte[] content = FileGetContent(f);
            Map<Integer, String> priorities = new HashMap<>();
            Map<Integer, String[]> details = new HashMap<>();
            int counter = 0;
            boolean found = false;
            for (List<String> l : list) {
                String pattern = l.get(1);
                String result = l.get(2);
                String priority = l.get(0);
                // remove the " "
                String patternString = pattern.substring(1, pattern.length() - 1);
                String resultString = result.substring(1, result.length() - 1);

                //boolean find = KMPImplementation(content, patternString);
                boolean find = RabinKarpImplementation(content, patternString, 13);
                // if pattern found
                if (find) {
                    found = true;
                    priorities.put(counter, priority);
                    details.put(counter, new String[]{patternString, resultString});
                }
                counter++;
            }

            // if pattern not found, it's an unknown file
            if (!found) {
                details.put(counter, new String[]{"", "Unknown file type"});
                priorities.put(counter, "0");
            }
            // show file types based on priority

            int[] array = new int[priorities.values().size()];
            InsertSort(priorities.values().stream().toList(), array);
            int maxValue = array[0];

            for (var v : priorities.entrySet()) {
                if (Integer.parseInt(v.getValue()) == maxValue) {
                    String[] text = details.get(v.getKey());
                    System.out.println(f.getName() + ": " + text[1]);
                }
            }
        } catch (Exception ignored) {

        }
    }
}
