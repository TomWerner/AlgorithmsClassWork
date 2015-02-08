public class MergeSort extends IntSorter
{

    @Override
    public int[] sort(int[] array)
    {
        int[] buffer = new int[array.length];
        mergesort(array, buffer, 0, array.length - 1);
        return array;
    }

    private void mergesort(int[] array, int[] buffer, int low, int high)
    {
        // Check if low is smaller then high, if not then the array is sorted
        if (low < high)
        {
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            // Sort the left side of the array
            mergesort(array, buffer, low, middle);
            // Sort the right side of the array
            mergesort(array, buffer, middle + 1, high);
            // Combine them both
            merge(array, buffer, low, middle, high);
        }
    }

    private void merge(int[] array, int[] buffer, int low, int middle, int high)
    {
        // Copy both parts into the buffer array
        for (int i = low; i <= high; i++)
        {
            buffer[i] = array[i];
        }

        int i = low;
        int j = middle + 1;
        int k = low;
        // Copy the smallest values from either the left or the right side back
        // to the original array
        while (i <= middle && j <= high)
        {
            if (buffer[i] <= buffer[j])
            {
                array[k] = buffer[i];
                i++;
            }
            else
            {
                array[k] = buffer[j];
                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle)
        {
            array[k] = buffer[i];
            k++;
            i++;
        }

    }

    @Override
    public String getName()
    {
        return "Mergesort";
    }
    
    
}
