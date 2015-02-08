import java.util.Arrays;


public class InPlaceHeapSort 
{
	public static void main(String[] args)
	{
		int[] test = {5, 4, 7, 1, 8, 3, 6, 2};
		heapSort(test);
	}
	
	public static void heapSort(int[] array)
	{
		System.out.println("Start             " + Arrays.toString(array));
		heapify(array);
		System.out.println("Done with heapify " + Arrays.toString(array));
		
		int end = array.length - 1;
		while (end > 0)
		{
			swap(array, end, 0); 
			System.out.println("Taking top        " + Arrays.toString(array));
			end--;
			siftDown(array, 0, end);
			System.out.println("Restoring heap    " + Arrays.toString(array));
		}
		System.out.println("Done with sort    " + Arrays.toString(array));

	}
	
	public static void heapify(int[] array)
	{
		int start = ((array.length - 2) / 2);
		
		while (start >= 0)
		{
			siftDown(array, start, array.length - 1);

			System.out.println("Sifted            " + Arrays.toString(array));
			start -= 1;
		}
	}
	
	public static void siftDown(int[] array, int start, int end)
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
	
	public static void swap(int[] array, int a, int b)
	{
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
}
