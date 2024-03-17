package ru.sergeich;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> arr = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

        double result = arr.stream()
                .filter(num -> num % 2 == 0)
                .mapToInt(num -> num)
                .average()
                .orElse(0.0);

        System.out.printf("Среднее значение всех четных чисел в массиве: %s", result);
    }
}