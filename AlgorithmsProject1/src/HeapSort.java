

public class HeapSort extends IntSorter
{
    @Override
    public int[] sort(int[] array)
    {
        heapify(array);        
        int end = array.length - 1;
        while (end > 0)
        {
            swap(array, end, 0); 
            end--;
            siftDown(array, 0, end);
        }
        
        return array;
    }
    
    public void heapify(int[] array)
    {
        int start = ((array.length - 2) / 2);
        
        while (start >= 0)
        {
            siftDown(array, start, array.length - 1);
            start -= 1;
        }
    }
    
    public void siftDown(int[] array, int start, int end)
    {
        int root = start;
        
        while (root * 2 + 1 <= end)
        {
            int child = root * 2 + 1;
            int swap = root;
            
            if (array[swap] < array[child])
                swap = child;
            if (child + 1 <= end && array[swap] < array[child + 1])
                swap = child + 1;
            if (swap == root)
                return;
            else
            {
                swap(array, root, swap);
                root = swap;
            }
        }
    }

    @Override
    public String getName()
    {
        return "Heap Sort";
    }
    
    
}
