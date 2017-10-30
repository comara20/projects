import java.util.*;

public class recurrenceTest
{
	public static void main(String[] args)
	{
		System.out.println(combos(16));
	}

	public static double combos(double n)
	{
		if (n == 1)
		{
			return 1;
		}
		double naive = 2*n*n*n-n*n;
		if (n % 2 == 1)
		{
			n += 1;
		}
		double temp = 7*combos(n * 0.5) + 4.5*n*n;
		if (n == 14)
		{
			System.out.println(temp);
		}
		if (n == 16)
		{
			System.out.println(naive);
		}
		if (naive < temp)
		{
			return naive;
		}
		return temp;

	}
}