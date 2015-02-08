public class InsertionSort extends IntSorter
{

    @Override
    public int[] sort(int[] array)
    {
        insertionSort(array, 0, array.length - 1);
        return array;
    }

    public void insertionSort(int[] array, int low, int high)
    {
        for (int i = low + 1; i <= high; i++)
        {
            int j = i;
            while (j > low && array[j - 1] > array[j])
            {
                swap(array, j, j - 1);
                j--;
            }
        }
    }

    @Override
    public String getName()
    {
        return "Insertion Sort";
    }
    
    
}
