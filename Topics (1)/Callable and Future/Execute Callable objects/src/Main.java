import java.util.List;
import java.util.concurrent.*;


class FutureUtils {

    public static int executeCallableObjects(List<Future<Callable<Integer>>> items) {
        int sum = 0;
        int c = 0;
        int llength = items.size();
        for (int i = llength - 1; i >= 0; i--) {
            try {
                var v = items.get(i);
                c = v.get().call();
                sum = c + sum;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sum;
    }
}
