import java.util.HashSet;


public class Problem3
{
    public static void main(String[] args)
    {
        int[] array = { 1, 1, 2, 2, 3, 1, };
        genPermHelper("", array);
    }
    public static HashSet<String> printed = new HashSet<String>();
    
    public static void genPermHelper(String sofar, int[] array)
    {
        boolean done = true;
        for (int i = 0; i < array.length; i++)
        {
            int[] temp = new int[array.length];
            System.arraycopy(array, 0, temp, 0, array.length);
            
            if (array[i] != Integer.MAX_VALUE)
            {
                temp[i] = Integer.MAX_VALUE;
                genPermHelper(array[i] + sofar, temp);
                done = false;
            }
        }
        if (done && !printed.contains(sofar))
        {
            printed.add(sofar);
            System.out.println(sofar);
        }

    }
}
