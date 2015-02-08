import java.util.Arrays;

public class JavaSort extends IntSorter
{

    @Override
    public int[] sort(int[] array)
    {
        Arrays.sort(array);
        return array;
    }

    @Override
    public String getName()
    {
        return "Arrays.Sort";
    }

}
