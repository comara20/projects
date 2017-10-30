/* 
Charles O'Mara
Programming assignment 3 cs124 
04/09/2017
*/

import java.util.*;
import java.math.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;


public class kk 
{

	public static void main(String[] args)
	{
		mergeTest();
		if (args.length == 1)
		{
			long[] nums = pullNums(args[0]);
			System.out.println(karmKarp(nums));
		}
		else
		{
			testMethods();
		}
	}

	public static void testMethods()
	{
		try
		{
			File currentDir = new File("");
			File result = new File(currentDir.getAbsolutePath()+"/"+"results2"+".csv");
			BufferedWriter writer = new BufferedWriter(new FileWriter(result));
			String firstLine = "Rep,karmKarp,karmKarpTime,rand1,rand1Time,hill1,hill1Time,anneal1,anneal1Time,rand2,rand2Time,hill2,hill2Time,anneal2,anneal2Time";
			writer.write(firstLine, 0, firstLine.length());
			writer.newLine();
			String line1 = "";
			for (int i = 0; i < 100; i++)
			{
				long time;
				System.out.println(i);
				line1 = "";
				int iter = 50000;
				long[] test = genList(100);
				line1 += Integer.toString(i)+",";
				time =System.currentTimeMillis();
				line1 += Long.toString(karmKarp(test)) + ",";
				line1 += Long.toString(System.currentTimeMillis() - time) + ",";
				time = System.currentTimeMillis();
				line1 += Long.toString(residue(repRand1(test, iter), test)) + ",";
				line1 += Long.toString(System.currentTimeMillis() - time) + ",";
				time = System.currentTimeMillis();
				line1 += Long.toString(residue(hillClimb1(test, iter), test)) + ",";
				line1 += Long.toString(System.currentTimeMillis() - time) + ",";
				time = System.currentTimeMillis();
				line1 += Long.toString(residue(anneal1(test, iter), test)) + ",";
				line1 += Long.toString(System.currentTimeMillis() - time) + ",";
				time = System.currentTimeMillis();
				line1 += Long.toString(prePartRes(test, repRand2(test, iter))) + ",";
				line1 += Long.toString(System.currentTimeMillis() - time) + ",";
				time = System.currentTimeMillis();
				line1 += Long.toString(prePartRes(test, hillClimb2(test, iter))) + ",";
				line1 += Long.toString(System.currentTimeMillis() - time) + ",";
				time = System.currentTimeMillis();
				line1 += Long.toString(prePartRes(test, anneal2(test, iter))) + ",";
				line1 += Long.toString(System.currentTimeMillis() - time);
				writer.write(line1, 0, line1.length());
				writer.newLine();
			}
			writer.flush();
			writer.close();
		}
		catch(IOException x){System.err.println(x); return ;}
	}

	public static long karmKarp(long[] input)
	{
		Heap storage = new Heap(input.length);
		for (int i = 0; i < input.length; i++)
		{
			storage.insert(input[i]);
		}
		while (storage.length > 1)
		{
			long temp = storage.deleteMax();
			storage.insert(temp - storage.deleteMax());
		}
		return storage.deleteMax();
	}
	public static long[] place(long[] input, long val)
	{
		long[] result = new long[input.length - 1];
		boolean set = false;
		int rPos = 0;
		int iPos = 0;
		while (rPos < result.length)
		{
			if (rPos == result.length - 1 && !set)
			{
				result[rPos] = val;
				rPos ++;
			}
			else if (!set && val <= input[iPos])
			{
				result[rPos] = val;
				rPos++;
				set = true;
			}
			else
			{
				result[rPos] = input[iPos];
				rPos ++;
				iPos ++;
			}
		}
		return result;
	}


	public static void testRand()
	{	
		Random rand = new Random();
		long max = -1000;
		long min = 1000000000;
		for (int i = 0; i < 2000000000; i++)
		{
		long rand1 = (long)rand.nextInt(1000000);
		long rand2 = (long)rand.nextInt(1000000);
		long mil = 1000000;
		long result = mil * rand1 + rand2 + 1;
			if (result > max)
			{
				max = result;
			}
			if (result < min)
			{
				min = result;
			}
		}
		System.out.println(max);
		System.out.println(min);
	}

	public static long[] anneal1(long[] input, int iters)
	{
		long[] split = new long[input.length];
		long[] split1 = new long[input.length];
		long[] split11 = new long[input.length];
		Random rand = new Random();
		for (int i = 0; i < split.length; i++)
		{
			if (rand.nextBoolean())
			{
				split[i] = 1;
				split1[i] = 1;
				split11[i] = 1;
			}
			else
			{
				split[i] = -1;
				split1[i] = -1;
				split11[i] = -1;
			}
		}
		for (int i = 0; i < iters; i++)
		{
			int vert = rand.nextInt(input.length);
			split1[vert] = -split[vert];
			if (rand.nextInt(2) == 0)
			{
				int vert1 = rand.nextInt(input.length);
				while (vert1 == vert)
				{
					vert1 = rand.nextInt(input.length);
				}
				split1[vert1] = -split[vert1];
			}
			if (residue(split1, input) < residue(split, input))
			{
				split = setVals(split, split1);
			}
			else
			{
				if (Math.exp(-(residue(split1, input)-residue(split, input))/cool(iters)) >= rand.nextDouble())
				{
					split = setVals(split, split1);
				}
				else
				{
					split1 = setVals(split1, split);
				}
			}
			if (residue(split, input) < residue(split11, input))
			{
				split11 = setVals(split11, split);
			}
		}
		return split11;
	}

	public static double cool(int iters)
	{
		return Math.pow(10, 10) * Math.pow(0.8, iters/300);
	}

	public static long[] hillClimb1(long[] input, int iters)
	{
		long[] split = new long[input.length];
		long[] newSplit = new long[input.length];
		Random rand = new Random();
		for (int i = 0; i < split.length; i++)
		{
			if (rand.nextBoolean())
			{
				split[i] = 1;
				newSplit[i] = 1;
			}
			else
			{
				split[i] = -1;
				newSplit[i] = -1;
			}
		}
		for (int i = 0; i < iters; i++)
		{
			newSplit = setVals(newSplit, split);
			int vert = rand.nextInt(input.length);
			newSplit[vert] = -split[vert];
			if (rand.nextBoolean())
			{
				int vert1 = rand.nextInt(input.length);
				while (vert1 == vert)
				{
					vert1 = rand.nextInt(input.length);
				}
				newSplit[vert1] = -split[vert1];
			}
			if (residue(newSplit, input) < residue(split, input))
			{
				split = setVals(split, newSplit);
			}
		}
		return split;
	}

	public static long prePartRes(long[] input, long[] part)
	{
		long[] result = new long[input.length];
		for (int i = 0; i < part.length; i++)
		{
			result[(int)part[i]] += input[i];
		}
		return karmKarp(result);
	}

	public static long[] hillClimb2(long[] input, int iters)
	{
		long[] split = new long[input.length];
		long[] newSplit = new long[input.length];
		Random rand = new Random();
		for (int i = 0; i < split.length; i++)
		{
			split[i] = rand.nextInt(input.length);
			newSplit[i] = split[i];
		}
		for (int i = 0; i < iters; i++)
		{
			newSplit = setVals(newSplit, split);
			int index = rand.nextInt(input.length);
			int val = rand.nextInt(input.length);
			while (newSplit[index] == val)
			{
				val = rand.nextInt(input.length);
			}
			newSplit[index] = val;
			if (prePartRes(input, newSplit) < prePartRes(input, split))
			{
				split = setVals(split, newSplit);
			}
		}
		return split;
	}

	public static long[] anneal2(long[] input, int iters)
	{
		long[] split = new long[input.length];
		Random rand = new Random();
		long[] split11 = new long[input.length];
		for (int i = 0; i < input.length; i++)
		{
			split[i] = rand.nextInt(input.length);
			split11[i] = split[i];
		}
		
		for (int i = 0; i < iters; i++)
		{
			long[] split1 = new long[input.length];
			for (int z = 0; z < input.length; z++)
			{
				split1[z] = split[z];
			}
			int index = rand.nextInt(input.length);
			int val = rand.nextInt(input.length);
			while (split1[index] == val)
			{
				val = rand.nextInt(input.length);
			}
			split1[index] = val;
			if (prePartRes(input, split1) < prePartRes(input, split))
			{
				split = setVals(split, split1);
			}
			else
			{
				if (Math.exp(-(residue(split1, input)-residue(split, input))/cool(iters)) >= rand.nextDouble())
				{
					split = setVals(split, split1);
				}
			}
			if (prePartRes(input, split) < prePartRes(input, split11))
			{
				split11 = setVals(split11, split);
			}
		}
		return split11;
	}

	public static long[] setVals(long[] targ, long[] val)
	{
		for (int i = 0; i < targ.length; i++)
		{
			targ[i] = val[i];
		}
		return targ;
	}

	public static long[] repRand2(long[] input, int iters)
	{
		long[] split = new long[input.length];
		long[] splitNew = new long[input.length];
		Random rand = new Random();
		for (int i = 0; i < split.length; i++)
		{
			split[i] = rand.nextInt(input.length);
		}
		for (int i = 0; i < iters; i++)
		{
			for (int x = 0; x < input.length; x++)
			{
				splitNew[x] = rand.nextInt(input.length);
			}
			if (prePartRes(input, splitNew) < prePartRes(input, split))
			{
				split = setVals(split, splitNew);
			}
		}
		return split;
	}

	public static long[] repRand1(long[] input, int iters)
	{
		long[] split = new long[input.length];
		long[] splitNew = new long[input.length];
		Random rand = new Random();
		for (int i = 0; i < split.length; i++)
		{
			if (rand.nextBoolean())
			{
				split[i] = 1;
			}
			else
			{
				split[i] = -1;
			}
		}
		for (int i = 0; i < iters; i++)
		{
			for (int x = 0; x < splitNew.length; x++)
			{
				if (rand.nextBoolean())
				{
					splitNew[x] = 1;
				}
				else
				{
					splitNew[x] = -1;
				}
			}
			if (residue(splitNew, input) < residue(split, input))
			{
				split = setVals(split, splitNew);
			}
		}
		return split;
	}
	public static long residue(long[] split, long[] input)
	{
		long result = 0;
		for (int i = 0; i < input.length; i++)
		{
			result += split[i] * input[i];
		}
		return Math.abs(result);
	}

	public static long[] genList(int n)
	{
		long[] result = new long[n];
		long rand1;
		long rand2;
		long mil = 1000000;
		Random rand = new Random();
		for (int i =0 ; i < n; i++)
		{
			rand1 = (long)rand.nextInt(1000000);
			rand2 = (long)rand.nextInt(1000000);
			result[i] = mil * rand1 + rand2 + 1;
		}
		return result;
	} 

	public static long[] pullNums(String fileName)
	{

		File file = new File(fileName);
		try
		{
			Scanner scanner = new Scanner(file);
			long[] result = new long[100];
			for (int i = 0; i < result.length; i++)
			{
				result[i] = scanner.nextLong();
			}
			return result;
		}
		catch (FileNotFoundException x)
		{
			System.err.println(x);
		}
		System.err.println("bad list");
		return new long[0];
	}

	public static void printArray(long[] input)
	{
		for (int i = 0; i < input.length; i++)
		{
			System.out.print(input[i] + " ");
		}

	}

	public static long[] mergeSort(long[] input)
	{
		if (input.length == 1)
		{
			return input;
		}
		long[] half1 = new long[input.length / 2];
		long[] half2 = new long[input.length - input.length / 2];
		for (int i = 0; i < input.length; i++)
		{
			if (i < input.length / 2)
			{
				half1[i] = input[i];
			}
			else
			{
				half2[i - input.length / 2] = input[i]; 
			}
		}
		half1 = mergeSort(half1);
		half2 = mergeSort(half2);
		return merge(half1, half2);
	}

	public static long[] merge(long[] a, long[] b)
	{
		int aI = 0;
		int bI = 0;
		int rI = 0;
		long[] result = new long[a.length + b.length];
		while (aI < a.length && bI < b.length)
		{
			if (a[aI] <= b[bI])
			{
				result[rI] = a[aI];
				aI ++;
				rI ++;
			}
			else
			{
				result[rI] = b[bI];
				bI ++;
				rI ++;
			}
		}
		while (bI < b.length)
		{
			result[rI] = b[bI];
			rI ++;
			bI ++;
		}
		while (aI < a.length)
		{
			result[rI] = a[aI];
			rI ++;
			aI ++;
		}
		return result;
	}
	public static boolean mergeTest()
	{
		long[] test1 = new long[1000];
		long[] test2 = new long[1000];
		Random rand = new Random();
		for (int i = 0; i < 1000; i++)
		{
			test1[i] = (long)rand.nextInt();
			test2[i] = test1[i];
		}
		test1 = mergeSort(test1);
		Arrays.sort(test2);
		for (int i = 0; i < 1000; i++)
		{
			if (test1[i] != test2[i])
			{
				System.out.println("mess up");
				return false;
			}
		}
		return true;
	}

}

class Heap {
	protected int length = 0;
	private long[] heap;
	Heap(int size)
	{
		heap = new long[size];
	}
	private int par(int child)
	{
		return (child - 1) / 2;
	}
	private void swap(int a, int b)
	{
		long temp = heap[a];
		heap[a] = heap[b];
		heap[b] = temp;
	}
	public void insert(long val)
	{
		length++;
		heap[length - 1] = val;
		int n = length - 1;
		while (n != 0 && heap[n] > heap[par(n)])
		{
			swap(n, par(n));
			n = par(n);
		}
	}
	public long deleteMax()
	{
		long result = heap[0];
		heap[0] = heap[length - 1];
		length--;
		maxHeapify(0);
		return result;
	}
	private int child1(int n)
	{
		if (2 * n + 1 < length)
		{
			return 2 * n + 1;
		}
		return -1;
	}
	private int child2(int n)
	{
		if (2 * n + 2 < length)
		{
			return 2 * n + 2;
		}
		return -1;
	}
	private void maxHeapify(int start)
	{
		int largest = child1(start);
		int second = child2(start);
		if (largest == -1)
		{
			if (second != - 1)
			{
				largest = second;
			}
			else {return ;}
		}
		else {
			if (second != -1)
			{
				if (heap[second] > heap[largest])
				{
					largest = second;
				}
			}
		}
		if (heap[largest] > heap[start])
		{
			swap(largest, start);
			maxHeapify(largest);
		}
	}
}


