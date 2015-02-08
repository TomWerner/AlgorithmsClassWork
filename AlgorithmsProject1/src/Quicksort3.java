public class Quicksort3 extends QuickSort
{

    @Override
    protected void quickSort(int[] array, int low, int high)
    {
        if (low < high)
        {
            int p = partition(array, low, high);

            if (!isSorted(array, low, p - 1))
                quickSort(array, low, p - 1);

            if (!isSorted(array, p + 1, high))
                quickSort(array, p + 1, high);
        }
    }

    @Override
    protected int getPivotIndex(int[] array, int low, int high)
    {
        int mid = (high + low) / 2;
        if (array[high] < array[low])
            swap(array, low, high);
        if (array[mid] < array[low])
            swap(array, mid, low);
        if (array[high] < array[mid])
            swap(array, high, mid);
        return mid;
    }

    @Override
    public String getName()
    {
        return "Quicksort 3 (b) (d)";
    }

}
