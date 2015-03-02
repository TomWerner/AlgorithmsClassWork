
public class Problem1
{
    public static void main(String[] args)
    {
        for (int i = 1; i <= 8; i++)
            System.out.println("Steps at " + i + " : " + f(i));
    }
    
    /**
     * Returns the number of different sequences of results of rolling 
     * a standard 6 sided die if a player completes n steps.
     * @param n
     */
    public static int f(int n)
    {
        if (n < 0)
            return 0;
        else if (n == 0)
            return 1;
        else 
        {
            int sum = 0;
            for (int i = 1; i <= 6; i++)
                sum += f(n - i);
            return sum;
        }
    }
}
