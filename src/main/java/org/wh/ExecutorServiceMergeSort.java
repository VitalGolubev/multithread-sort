package org.wh;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceMergeSort {

    private ExecutorServiceMergeSort() {
    }

    public static int[] sort(int[] arr) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int[][] partitions = partition(arr, executorService);
        mergeSort(partitions, executorService);
        int[] result = merge(partitions);
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return result;
    }

    private static int[][] partition(int[] arr, ExecutorService executorService) {
        int numPartitions = Runtime.getRuntime().availableProcessors();
        int[][] partitions = new int[numPartitions][];
        int partitionSize = arr.length / numPartitions;
        for (int i = 0; i < numPartitions; i++) {
            int startIndex = i * partitionSize;
            int endIndex = i == numPartitions - 1 ? arr.length : (i + 1) * partitionSize;
            int[] partition = Arrays.copyOfRange(arr, startIndex, endIndex);
            executorService.submit(() -> Arrays.sort(partition));
            partitions[i] = partition;
        }
        return partitions;
    }

    private static void mergeSort(int[][] partitions, ExecutorService executorService) {
        while (partitions.length > 1) {
            int[][] mergedPartitions = new int[(partitions.length + 1) / 2][];
            for (int i = 0; i < partitions.length; i += 2) {
                if (i + 1 == partitions.length) {
                    mergedPartitions[i / 2] = partitions[i];
                } else {
                    int[] mergedPartition = merge(partitions[i], partitions[i + 1]);
                    executorService.submit(() -> Arrays.sort(mergedPartition));
                    mergedPartitions[i / 2] = mergedPartition;
                }
            }
            partitions = mergedPartitions;
        }
    }

    private static int[] merge(int[] arr1, int[] arr2) {
        int[] mergedArr = new int[arr1.length + arr2.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] < arr2[j]) {
                mergedArr[k++] = arr1[i++];
            } else {
                mergedArr[k++] = arr2[j++];
            }
        }
        while (i < arr1.length) {
            mergedArr[k++] = arr1[i++];
        }
        while (j < arr2.length) {
            mergedArr[k++] = arr2[j++];
        }
        return mergedArr;
    }

    private static int[] merge(int[][] partitions) {
        int[] mergedArr = partitions[0];
        for (int i = 1; i < partitions.length; i++) {
            mergedArr = merge(mergedArr, partitions[i]);
        }
        return mergedArr;
    }

}
