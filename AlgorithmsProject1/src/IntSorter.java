
public abstract class IntSorter
{
    protected void swap(int[] array, int a, int b)
    {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
    
    protected boolean isSorted(int[] array, int low, int high) 
    {
        for (int i = low + 1; i <= high; i++)
            if (array[i] < array[i - 1])
                return false;
        return true;
    }
    
    public abstract int[] sort(int[] array);
    public abstract String getName();
}
