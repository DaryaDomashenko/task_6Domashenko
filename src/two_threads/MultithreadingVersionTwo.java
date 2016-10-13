package two_threads;

import java.util.Date;
import java.util.Random;


/**
 * Оба кода можно использовать как для многопоточного, так и для перемножения двумя потоками
 * */







public class MultithreadingVersionTwo
    {
        /** Заполнение матрицы случайными числами.
         *
         * @param matrix Заполняемая матрица.
         */
        private static void randomMatrix(final int[][] matrix)
        {
            final Random random = new Random();  // Генератор случайных чисел.

            for (int row = 0; row < matrix.length; ++row)           // Цикл по строкам матрицы.
                for (int col = 0; col < matrix[row].length; ++col)  // Цикл по столбцам матрицы.
                    matrix[row][col] = random.nextInt(10);         // Случайное число от 0 до 100.
        }

//    /** Однопоточное умножение матриц.
//     *
//     * @param firstMatrix  Первая матрица.
//     * @param secondMatrix Вторая матрица.
//     * @return Результирующая матрица.
//     */
//    private static int[][] multiplyMatrix(final int[][] firstMatrix,
//                                          final int[][] secondMatrix)
//    {
//        final int rowCount = firstMatrix.length;             // Число строк результирующей матрицы.
//        final int colCount = secondMatrix[0].length;         // Число столбцов результирующей матрицы.
//        final int sumLength = secondMatrix.length;           // Число членов суммы при вычислении значения ячейки.
//        final int[][] result = new int[rowCount][colCount];  // Результирующая матрица.
//
//        for (int row = 0; row < rowCount; ++row) {  // Цикл по строкам матрицы.
//            for (int col = 0; col < colCount; ++col) {  // Цикл по столбцам матрицы.
//                int sum = 0;
//                for (int i = 0; i < sumLength; ++i)
//                    sum += firstMatrix[row][i] * secondMatrix[i][col];
//                result[row][col] = sum;
//            }
//        }
//
//        return result;
//    }

    /** Многопоточное умножение матриц.
     *
     * @param firstMatrix  Первая (левая) матрица.
     * @param secondMatrix Вторая (правая) матрица.
     * @param threadCount Число потоков.
     * @return Результирующая матрица.
     */
    private static int[][] multiplyMatrixMT(final int[][] firstMatrix,
                                            final int[][] secondMatrix,
                                            int threadCount)
    {
        assert threadCount > 0;

        final int rowCount = firstMatrix.length;             // Число строк результирующей матрицы.
        final int colCount = secondMatrix[0].length;         // Число столбцов результирующей матрицы.
        final int[][] result = new int[rowCount][colCount];  // Результирующая матрица.

        final int cellsForThread = (rowCount * colCount) / threadCount;  // Число вычисляемых ячеек на поток.
        int firstIndex = 0;  // Индекс первой вычисляемой ячейки.
        final MultiplierThreadTwo[] multiplierThreads = new MultiplierThreadTwo[threadCount];  // Массив потоков.

        // Создание и запуск потоков.
        for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
            int lastIndex = firstIndex + cellsForThread;  // Индекс последней вычисляемой ячейки.
            if (threadIndex == 0) {
                /* Один из потоков должен будет вычислить не только свой блок ячеек,
                   но и остаток, если число ячеек не делится нацело на число потоков. */
                lastIndex = rowCount * colCount;
            }
            multiplierThreads[threadIndex] = new MultiplierThreadTwo(firstMatrix, secondMatrix, result, firstIndex, lastIndex);
            multiplierThreads[threadIndex].start();
            firstIndex = lastIndex;
        }

        // Ожидание завершения потоков.
        try {
            for (final MultiplierThreadTwo multiplierThread : multiplierThreads)
                multiplierThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /** Число строк первой матрицы. */
    final static int FIRST_MATRIX_ROWS  = 3;
    /** Число столбцов первой матрицы. */
    final static int FIRST_MATRIX_COLS  = 3;
    /** Число строк второй матрицы (должно совпадать с числом столбцов первой матрицы). */
    final static int SECOND_MATRIX_ROWS = FIRST_MATRIX_COLS;
    /** Число столбцов второй матрицы. */
    final static int SECOND_MATRIX_COLS = 3;

    public static void main(String[] args)
    {

        Date date1 = new Date();

        final int[][] firstMatrix  = new int[FIRST_MATRIX_ROWS][FIRST_MATRIX_COLS];    // Первая (левая) матрица.
        final int[][] secondMatrix = new int[SECOND_MATRIX_ROWS][SECOND_MATRIX_COLS];  // Вторая (правая) матрица.

        randomMatrix(firstMatrix);
        randomMatrix(secondMatrix);

        System.out.println("First matrix:");
        for (int[] ints_f : firstMatrix) {
            for (int anInt : ints_f) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }

       System.out.println("Second matrix:");
        for (int[] ints_s : secondMatrix) {
            for (int anInt : ints_s) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }

        System.out.println("Result matrix:");
        //Число потоков определяется процессором
        //final int[][] resultMatrixMT = multiplyMatrixMT(firstMatrix, secondMatrix, Runtime.getRuntime().availableProcessors());

     /**
      * Третий параметр ограничевает количество потоков двумя.
      * Верхний комментарий - .availableProcessors() выясняет количество процессоров и исходя из этого
      * выбирает число потоков*
      * */

        final int[][] resultMatrixMT = multiplyMatrixMT(firstMatrix, secondMatrix, 2);

        for (int[] ints : resultMatrixMT) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }

        Date date2 = new Date();
        long time = date2.getTime()-date1.getTime(); //Время выполнения.
        System.out.println("Время " + time);
    }
}

