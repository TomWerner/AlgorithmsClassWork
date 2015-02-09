public class Quicksort2 extends QuickSort
{

    @Override
    protected void quickSort(int[] array, int low, int high)
    {
        if (low < high)
        {
            int p = partition(array, low, high);

            if (!isSorted(array, low, p - 1))
            {
                if (Math.abs((p - 1) - low) < 50)
                    new InsertionSort().insertionSort(array, low, p - 1);
                else
                    quickSort(array, low, p - 1);
            }

            if (!isSorted(array, p + 1, high))
            {
                if (Math.abs(high - (p + 1)) < 50)
                    new InsertionSort().insertionSort(array, p + 1, high);
                else
                    quickSort(array, p + 1, high);
            }
        }
    }

    @Override
    protected int getPivotIndex(int[] array, int low, int high)
    {
        int mid = (high + low) / 2;
        low = returnMedianIndex(array, low, low + 1, low + 2);
        high = returnMedianIndex(array, high - 2, high - 1, high);
        mid = returnMedianIndex(array, mid - 1, mid, mid + 1);
        return returnMedianIndex(array, low, mid, high);
    }
    
    private int returnMedianIndex(int[] array, int a, int b, int c)
    {
        if (array[c] < array[a])
            swap(array, a, c);
        if (array[b] < array[a])
            swap(array, b, a);
        if (array[c] < array[b])
            swap(array, c, b);
        return b;
    }
    

    @Override
    public String getName()
    {
        return "Quicksort 2 (a) (c) (d)";
    }
    
    
}
