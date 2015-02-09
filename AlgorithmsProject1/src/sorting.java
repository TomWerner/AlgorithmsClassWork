import java.util.Random;
import java.util.Scanner;

public class sorting
{
    public static void main(String[] args)
    {
        IntSorter[] sorters = { new Quicksort1(), new Quicksort2(), new Quicksort3(), new Quicksort4(),
                new QuickSort(), new MergeSort(), new HeapSort(), new InsertionSort(), new JavaSort() };

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the number of runs: ");
        int runs = scan.nextInt();
        System.out.print("Enter the number of elements: ");
        int arraySize = scan.nextInt();
        
        long[][] tenThouRangeTimes = new long[runs][sorters.length];
        long[][] oneMilRangeTimes = new long[runs][sorters.length];
        long[][] hundredMilRangeTimes = new long[runs][sorters.length];
        

        for (int run = 0; run < runs; run++)
            tenThouRangeTimes[run] = getTimes(sorters, arraySize, 10000);
        for (int run = 0; run < runs; run++)
            oneMilRangeTimes[run] = getTimes(sorters, arraySize, 1000000);
        for (int run = 0; run < runs; run++)
            hundredMilRangeTimes[run] = getTimes(sorters, arraySize, 100000000);

        printSummary(new long[][][] { tenThouRangeTimes, oneMilRangeTimes, hundredMilRangeTimes }, new String[] {
                "10.000 Range", "1.000.000 Range", "100.000.000 Range" }, sorters);
        
        scan.close();
    }

    private static void printSummary(long[][][] times, String[] runTypes, IntSorter[] sorters)
    {
        System.out.println("Copy this data into a .csv file for best viewing experience");

        // Print main header
        System.out.print(",");
        for (String string : runTypes)
            System.out.print(string + ",,,");
        System.out.println();

        // Print secondary header
        System.out.print(",");
        for (int i = 0; i < runTypes.length; i++)
            System.out.print("Average,Best,Worst,");
        System.out.println();

        // Print data
        for (int i = 0; i < sorters.length; i++)
        {
            String name = sorters[i].getName();
            System.out.print(name + ",");
            for (int k = 0; k < runTypes.length; k++)
                System.out.print(getAverage(times, k, i) + " , " + getBest(times, k, i) + ", " + getWorst(times, k, i)
                        + ", ");
            System.out.println();
        }
    }

    private static long getAverage(long[][][] times, int runType, int sorter)
    {
        long[][] runs = times[runType]; // Get all the trials from this runtype
        long sum = 0;
        for (int i = 0; i < 10; i++)
            // For all 10 trials
            sum += runs[i][sorter];
        return sum / 10;
    }

    private static long getWorst(long[][][] times, int runType, int sorter)
    {
        long[][] runs = times[runType]; // Get all the trials from this runtype
        long low = Long.MIN_VALUE;
        for (int i = 0; i < 10; i++)
            // For all 10 trials
            low = Math.max(low, runs[i][sorter]);
        return low;
    }

    private static long getBest(long[][][] times, int runType, int sorter)
    {
        long[][] runs = times[runType]; // Get all the trials from this runtype
        long low = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++)
            // For all 10 trials
            low = Math.min(low, runs[i][sorter]);
        return low;
    }

    private static long[] getTimes(IntSorter[] sorters, int arraySize, int randomRange)
    {
        long[] times = new long[sorters.length];
        Random randomGenerator = new Random();

        int[] baseArray = new int[arraySize];
        for (int i = 0; i < arraySize; i++)
            baseArray[i] = randomGenerator.nextInt(randomRange);

        for (int i = 0; i < sorters.length; i++)
        {
            IntSorter sorter = sorters[i];
            int[] array = new int[baseArray.length];
            System.arraycopy(baseArray, 0, array, 0, array.length);

            long start = System.currentTimeMillis();
            sorter.sort(array);
            times[i] = System.currentTimeMillis() - start;
        }

        return times;
    }
}