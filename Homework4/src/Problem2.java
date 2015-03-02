public class Problem2
{
    public static int count = 0;

    public static void main(String[] args)
    {
        String input = "5944728";
        generateSequence("", input);
    }

    public static void generateSequence(String helper, String number)
    {
        if (number.length() == 0)
        {
            System.out.println(helper);
            count++;
        }
        else
        {
            int firstDigit = (number.charAt(0) - '0');
            for (Character c : getLetters(firstDigit))
                generateSequence(helper + c, number.substring(1));
        }
    }

    public static char[] getLetters(int number)
    {
        if (number == 2)
            return "ABC".toCharArray();
        else if (number == 3)
            return "DEF".toCharArray();
        else if (number == 4)
            return "GHI".toCharArray();
        else if (number == 5)
            return "JKL".toCharArray();
        else if (number == 6)
            return "MNO".toCharArray();
        else if (number == 7)
            return "PQRS".toCharArray();
        else if (number == 8)
            return "TUV".toCharArray();
        else if (number == 9)
            return "WXYZ".toCharArray();
        return null;
    }
}
