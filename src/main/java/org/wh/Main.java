package org.wh;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {
        Random rand = new Random();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int arraySize = 100_000_000;
        int[] array = new int[arraySize];
        int[] arrayClone;
        for (int i = 0; i < arraySize; i++) {
            array[i] = rand.nextInt(arraySize * 10);
        }
        LocalDateTime startTime;

        arrayClone = array.clone();
        startTime = LocalDateTime.now();
        new ForkJoinPool(availableProcessors).invoke(new ForkJoinQuickSort(arrayClone, 0, arraySize - 1));
        System.out.printf("MultiThread ForkJoinPool(%d threads) Sorting takes %d millis%n%n", availableProcessors,
            Duration.between(startTime, LocalDateTime.now()).toMillis());

        arrayClone = array.clone();
        startTime = LocalDateTime.now();
        new ForkJoinPool(availableProcessors / 2).invoke(new ForkJoinQuickSort(arrayClone, 0, arraySize - 1));
        System.out.printf("MultiThread ForkJoinPool(%d threads) Sorting takes %d millis%n%n", availableProcessors / 2,
            Duration.between(startTime, LocalDateTime.now()).toMillis());

        arrayClone = array.clone();
        startTime = LocalDateTime.now();
        Arrays.parallelSort(arrayClone);
        System.out.printf("Arrays.parallelSort() takes %d millis%n%n",
            Duration.between(startTime, LocalDateTime.now()).toMillis());

        arrayClone = array.clone();
        startTime = LocalDateTime.now();
        ExecutorServiceMergeSort.sort(arrayClone);
        System.out.printf("ExecutorServiceMergeSort.sort() takes %d millis%n%n",
            Duration.between(startTime, LocalDateTime.now()).toMillis());

        arrayClone = array.clone();
        startTime = LocalDateTime.now();
        QuickSort.sort(arrayClone, 0, arraySize - 1);
        System.out.printf("SingleThread Sorting takes %d millis%n%n",
            Duration.between(startTime, LocalDateTime.now()).toMillis());
    }

}
