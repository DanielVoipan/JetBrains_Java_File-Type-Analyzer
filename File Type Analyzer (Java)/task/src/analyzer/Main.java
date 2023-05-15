package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String path =  args[0];
        String filePattern = args[1];
        File dir = new File(path);
        File patternFile = new File(filePattern);
        List<List<String>> list = PatternFileContent(patternFile);
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isFile()) {
                // create a new thread
                Worker w = new Worker(f, list);
                try {
                    // start the thread
                    w.start();
                    // wait for the thread to finish
                    w.join();
                } catch (InterruptedException e) {
                    System.out.println("Searching in " + f.getName() + " intrerrupted");
                }
            }
        }
    }

    // get file pattern content in a List of lists
    static List<List<String>> PatternFileContent(File path) {
        List<List<String>> list = new ArrayList<>();
        List<String> str = new ArrayList<>();
        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                str = Arrays.stream(line.split(";")).toList();
                list.add(str);
            }
        } catch(FileNotFoundException e) {
            System.out.println("Pattern file not found.");
        }
        return list;
    }

    // find pattern in file content (file read in bytes)
    static byte[] FileGetContent(File path) {
        byte[] fileContents = new byte[0];
        try (InputStream inputStream = new FileInputStream(path)) {
            fileContents = inputStream.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContents;
    }

    // Rabin-Karp implementation
    static boolean RabinKarpImplementation(byte[] fileContents, String patternString, int q) {
        int m = patternString.length();
        int n = fileContents.length;
        int i;
        int j;
        int d = 256;
        int p = 0;
        int t = 0;
        int h = 1;
        boolean found = false;

        for (i = 0 ; i < m - 1; i++) {
            h = (h * d) % q;
        }
        // calculate hash value for pattern and text
        for (i = 0; i < m; i++) {
            char c = (char) fileContents[i];
            p = (d * p + patternString.charAt(i)) % q;
            t = (d * t + c) % q;
        }

        // Find the match
        for (i = 0; i < n - m; i++) {
            if (p == t) {
                for (j = 0; j < m; j++) {
                    if ((char) fileContents[i + j] != patternString.charAt(j)) {
                        break;
                    }
                }
                if (j == m) {
                    found = true;
                }
            }
            if (i < n - m) {
                t = (d * (t - (char) fileContents[i] * h) + (char) fileContents[i + m]) % q;
                if (t < 0) {
                    t = (t + q);
                }
            }
        }
        return found;
    }



    // KMP implementation, uses the prefix Function
    static boolean KMPImplementation(byte[] fileContents, String patternString) {
        int[] prefixFunction = prefixFunction(patternString);
        int length = fileContents.length;
        int pLength = patternString.length();
        int i = 0;
        int j = 0;
        while (i < length) {
            char c = (char) fileContents[i];
          if (c == patternString.charAt(j)) {
                j++;
                i++;
                if (j == pLength) {
                    return true;
                }
            } else {
              if (j != 0) {
                  j = prefixFunction[j - 1];
              } else {
                  i++;
              }
            }
        }
        return false;
    }

    // naive Implementation
    static boolean naiveImplementation(byte[] fileContents, String patternString) {
        int length = fileContents.length;
        int pLength = patternString.length();
        int i = 0;
        int j = 0;
        while (i < length) {
            char c = (char) fileContents[i];
            if (j == pLength - 1) {
                  return true;
            } else if (c == patternString.charAt(j)) {
                j++;
            } else {
                j = 0;
            }
            i++;
        }
        return false;
    }

    // get the prefix array
    static int[] prefixFunction(String text) {
        int currentPos = 1;
        int dimText = text.length();
        int[] prefix = new int[dimText];
        int counter = 0;
        while (currentPos <= dimText) {
            String subs = text.substring(0, currentPos);
            if (subs.length() == 1) {
                prefix[counter] = 0;
            } else {
                int c = 0;
                int i = 0;
                StringBuilder start = new StringBuilder();
                String end = "";
                int j = subs.length() - 1;
                while (i <= j) {
                    start.append(subs.charAt(i));
                    end = subs.charAt(j) + end;
                    if (start.toString().equals(end)) {
                        if (start.length() + end.length() == subs.length()) {
                            c = start.length() + end.length() - 1;
                        } else {
                            c = end.length();
                        }
                    }
                    i++;
                    j--;
                }
                if (c > 0) {
                    prefix[counter] = c;
                }
            }
            currentPos++;
            counter++;
        }
        return prefix;
    }

    // insert sort
    static void InsertSort(List<String> list, int[] array) {
        int n = list.size();
        for (int i = 0; i < n ; i++) {
            array[i] = Integer.parseInt(list.get(i));
        }
        for (int i = 1 ; i < n ; ++i) {
            int j = i - 1;
            int key = array[i];
            while (j >= 0 && array[j] < key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
