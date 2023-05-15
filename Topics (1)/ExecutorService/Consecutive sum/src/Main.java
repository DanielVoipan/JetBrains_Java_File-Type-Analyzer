import java.util.Scanner;
import java.util.concurrent.*;

class Numbers {

    public static Callable<Integer> consecutiveSum(int start, int end) {
        // write your code here
        return null;
    }

    // Don't change the code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int start = scanner.nextInt();
        int end = scanner.nextInt();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(consecutiveSum(start, end));

        int result = 0;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        System.out.println(result);
    }
}