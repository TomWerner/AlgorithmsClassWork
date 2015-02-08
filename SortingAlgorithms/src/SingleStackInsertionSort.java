import java.util.Stack;


public class SingleStackInsertionSort 
{
	public static void main(String[] args)
	{
		int[] values = new int[10];
		for (int i = 0; i < values.length; i++)
			values[i] = (int) (Math.random() * 100);
		Stack<Integer> stack = new Stack<Integer>();
		for (int i : values)
			stack.push(i);
		System.out.println("Start - " + stack);
		System.out.println("Double stack - " + stackInsertionSort1(stack));
		stack = new Stack<Integer>();
		for (int i : values)
			stack.push(i);
		System.out.println("Single stack - " + stackInsertionSort(stack));
	}
	
	public static Stack<Integer> stackInsertionSort(Stack<Integer> items)
	{
		Stack<Integer> left = new Stack<Integer>();
		Stack<Integer> right = new Stack<Integer>();
		
		while (!items.isEmpty())
		{
			int value = items.pop();
			if (left.isEmpty() || value > left.peek())
			{
				if (right.isEmpty() || value < right.peek())
					left.push(value);
				else
				{
					while (value > right.peek())
						left.push(right.pop());
					left.push(value);
				}
			}
			else
			{
				while (value < left.peek())
					right.push(left.pop());
				
				if (right.isEmpty() || value < right.peek())
					left.push(value);
				else
				{
					while (value > right.peek())
						left.push(right.pop());
					left.push(value);
				}
			}
		}
		
		while (!left.isEmpty())
			right.push(left.pop());
		items = right;
		
		return items;
	}
	
	public static Stack<Integer> stackInsertionSort1(Stack<Integer> items)
	{
		Stack<Integer> temp1 = new Stack<Integer>();

		int popCount = 0;
		int numItems = items.size();
		for (int i = 0; i < numItems; i++)
		{
			int next = items.pop(); popCount++;
			//System.out.println("Item to place " + next);
			for (int k = i + 1; k < numItems; k++)
			{
				popCount++;
				int value = items.pop();
				//System.out.println("popping off " + value + " to push on temp");
				temp1.push(value);
			}
			
			while (!items.isEmpty() && items.peek() < next)
			{
				popCount++;
				int value = items.pop();
				//System.out.println("Popping off " + value + " to push on temp2 since its less than " + next);
				temp1.push(value);
			}
			items.push(next);
			//System.out.println("Pushing choice " + next + " onto result");
			
			while (!temp1.isEmpty())
			{
				popCount++;
				int value = temp1.pop();
				//System.out.println("Pushing " + value + " onto result");
				items.push(value);
			}
		}
		System.out.println(popCount);
		return items;
	}
	
}
