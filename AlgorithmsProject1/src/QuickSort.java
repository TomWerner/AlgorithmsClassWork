
public class QuickSort extends IntSorter
{
    @Override
    public int[] sort(int[] array)
    {
        quickSort(array, 0, array.length - 1);
        return array;
    }

    protected void quickSort(int[] array, int low, int high)
    {
        if (low < high)
        {
            int p = partition(array, low, high);
            quickSort(array, low, p - 1);
            quickSort(array, p + 1, high);
        }
    }

    protected int partition(int[] array, int low, int high)
    {
        int pivotIndex = getPivotIndex(array, low, high);
        int pivotValue = array[pivotIndex];

        swap(array, pivotIndex, high);
        int storeIndex = low;

        for (int i = low; i <= high - 1; i++)
        {
            if (array[i] < pivotValue)
            {
                swap(array, i, storeIndex);
                storeIndex++;
            }
        }
        swap(array, storeIndex, high);
        return storeIndex;
    }

    protected int getPivotIndex(int[] array, int low, int high)
    {
        return (high + low) / 2;
    }

    @Override
    public String getName()
    {
        return "Quicksort";
    }
    
    
}
