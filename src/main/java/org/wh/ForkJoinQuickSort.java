package org.wh;

import java.util.concurrent.RecursiveAction;

public class ForkJoinQuickSort extends RecursiveAction {

    private static final int MIN_PARALLEL_SORT_SIZE = 4096; // given from DualPivotQuicksort
    private final int[] array;
    private final int left;
    private final int right;

    public ForkJoinQuickSort(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (left < right) {
            int pivotIndex = QuickSort.partition(array, left, right);
            if ((right - left) > MIN_PARALLEL_SORT_SIZE) {
                invokeAll(
                    new ForkJoinQuickSort(array, left, pivotIndex - 1),
                    new ForkJoinQuickSort(array, pivotIndex + 1, right));
            } else {
                QuickSort.sort(array, left, pivotIndex - 1);
                QuickSort.sort(array, pivotIndex + 1, right);
            }
        }
    }

}
