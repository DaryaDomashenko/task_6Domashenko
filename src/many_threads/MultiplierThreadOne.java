package many_threads;

public class MultiplierThreadOne extends Thread {
    private int startRow;
    private int endRow;
    private int[][] a;
    private int[][] b;
    private int[][] result;
    private int n;

    public MultiplierThreadOne(int[][] a, int[][] b, int[][] result, int startRow, int endRow) {
        this.a = a;
        this.b = b;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
        this.n = b.length;
    }

    private int calcSingleValue(int row, int col) {
        int c = 0;
        for (int i = 0; i < n; i++) {
            c += a[row][i] * b[i][col];
        }
        return c;
    }

    @Override
    public void run() {
        System.out.println("Считаю со строки " + startRow + " до строки " + endRow + " включительно");
        for (int row = startRow; row <= endRow; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = calcSingleValue(row, col);
            }
        }
    }

    public static int[][] multiply(int[][] a, int[][] b, int threadsCount) {
        //проверки
        if (a == null || a.length == 0 || a[0] == null || a[0].length == 0) {
            throw new IllegalArgumentException("a");
        }
        if (b == null || b.length == 0 || b[0] == null || b[0].length == 0) {
            throw new IllegalArgumentException("b");
        }
        if (a[0].length != b.length) {
            throw new IllegalArgumentException("матрицы не согласованы");
        }
        //определяем размеры результирующей матрицы
        int m = a.length;
        int q = b[0].length;
        int[][] result = new int[m][q];
        //если количество потоков больше чем количество строк - уменьшим кол-во потоков
        if (threadsCount > m) {
            threadsCount = m;
        }
        //посчитаем сколько строк результирующей матрицы будет считать каждый поток
        int count = m / threadsCount;
        int additional = m % threadsCount; //если не делится на threadsCount, то добавим к первому потоку
        //создаем и запускаем потоки
        Thread[] threads = new Thread[threadsCount];
        int start = 0;
        for (int i = 0; i < threadsCount; i++) {
            int cnt = ((i == 0) ? count + additional : count);
            threads[i] = new MultiplierThreadOne(a, b, result, start, start + cnt - 1);
            start += cnt;
            threads[i].start();
        }
        //ждем завершения
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        return result;
    }
}
