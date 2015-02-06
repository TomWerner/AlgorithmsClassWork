import java.util.Arrays;


public class Quicksort {
	public static void main(String[] args)
	{
		int[] test = {9, 5, 4, 7, 1, 8, 3, 6, 2, 0};
		System.out.println("Start                 " + Arrays.toString(test));
		quicksort(test);
		System.out.println("End " + Arrays.toString(test));
	}
	
	public static void quicksort(int[] array)
	{
		quicksortHelp(array, 0, array.length - 1);
	}
	
	private static void quicksortHelp(int[] array, int low, int high)
	{
		if (low < high)
		{
			int p = partition(array, low, high);
			quicksortHelp(array, low, p - 1);
			System.out.println("After left partition  " + Arrays.toString(array));
			quicksortHelp(array, p + 1, high);
			System.out.println("After right partition " + Arrays.toString(array));
		}
	}
	
	private static int partition(int[] array, int low, int high)
	{
		int pivotIndex = low;
		int pivotValue = array[low];
		System.out.println("Partitioning with " + pivotValue + " low : " + low + " high: " + high);
		
		swap(array, pivotIndex, high);
		System.out.println("move partition        " + Arrays.toString(array));
		int storeIndex = low;
		
		for (int i = low; i <= high - 1; i++)
		{
			if (array[i] < pivotValue)
			{
				swap(array, i, storeIndex);
				System.out.println("< pivot swap          " + Arrays.toString(array));
				storeIndex++;
			}
		}
		swap(array, storeIndex, high);
		System.out.println("put pivot in place    " + Arrays.toString(array));
		return storeIndex;
	}
	
	public static void swap(int[] array, int a, int b)
	{
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
}
