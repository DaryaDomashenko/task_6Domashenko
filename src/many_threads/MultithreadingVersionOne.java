package many_threads;

import java.util.Date;
import java.util.Random;

import static many_threads.MultiplierThreadOne.multiply;

public class MultithreadingVersionOne {

    /**
     Заполнение матрицы случайными числами.
     */
    private static void randomMatrix(final int[][] matrix)
    {
        final Random random = new Random();  // Генератор случайных чисел.

        for (int row = 0; row < matrix.length; ++row)           // Цикл по строкам матрицы.
            for (int col = 0; col < matrix[row].length; ++col)  // Цикл по столбцам матрицы.
                matrix[row][col] = random.nextInt(5);         // Случайное число от 0 до 5.
    }

    public static void main(String[] args) {

        Date date1 = new Date(); // Для  подсчета времени выполнения.

        int d=50; // Так как матрица в задании квадратная, одной переменной задаем размерность.
        /**
        Первая матрица, ее заполнение и вывод.
        */
        final int[][] a  = new int[d][d];
        randomMatrix(a);
        System.out.println("First matrix:");
        for (int[] ints_a : a) {
            for (int anInt : ints_a) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }

        /**
        Вторая матрица, ее заполнение и вывод.
        */
        final int[][] b = new int[d][d];
        randomMatrix(b);
        System.out.println("Second matrix:");
        for (int[] ints_b : b) {
            for (int anInt : ints_b) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }

        /**
        * Результирующая матрица.
        **/

        // третий параметр как количество потоков (по строкам матрицы).
        // поставив вместо d - 2 получим количество потоков ограниченное двумя

        int[][] c = multiply(a, b, d);
        for (int[] ints : c) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }

        Date date2 = new Date();
        long time = date2.getTime()-date1.getTime(); //Время выполнения.
        System.out.println("Время " + time); //Выводим время выполнения.
    }
}
