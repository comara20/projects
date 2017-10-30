import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class strassen
{
	public static void main(String[] args)
	{
		if (args[0].equals("0"))
		{
			ArrayList<int[][]> matrices = pullMat(Integer.parseInt(args[1]), args[2]);
			int[][] alpha = matrices.get(0);
			int[][] beta = matrices.get(1);
			int[][] result = strass2n(alpha, beta, new int[alpha.length][alpha.length], 0);
			for (int i = 0; i < result.length; i++)
			{
				System.out.println(result[i][i]);
			}
		}
		else if (args[0].equals("1"))
		{
			Random rand = new Random();
			int[][] tester = new int[Integer.parseInt(args[1])][Integer.parseInt(args[1])];
			for (int i = 0; i < Integer.parseInt(args[1]); i++)
			{
				for (int x = 0; x < Integer.parseInt(args[1]); x++)
				{
					tester[i][x] = rand.nextInt(3);
					if (tester[i][x] == 2)
					{
						tester[i][x] = -1;
					}
				}
			}
			//printMat(tester);
			int[][] result = strass2n(tester, tester, new int[tester.length][tester.length], 0);
			for (int i = 0; i < result.length; i++)
			{
				System.out.println(result[i][i]);
			}
			int[][] result1 = mult(tester, tester);
			System.out.println("BREAK");
			for (int i = 0; i < result.length; i++)
			{
				System.out.println(result1[i][i]);
			}
			for (int x = 0; x < result.length; x++)
			{
				for (int i = 0; i < result.length; i++)
				{
					if (result[i][x] != result1[i][x])
					{
						System.out.println("problem");
					}
				}
				System.out.println("check");
			}
			System.out.print("done");
			//printMat(mult(tester, tester, new int[tester.length][tester.length]));
			int[][] tester1 = new int[Integer.parseInt(args[1])][Integer.parseInt(args[1])];
			for (int i = 0; i < Integer.parseInt(args[1]); i++)
			{
				for (int x = 0; x < Integer.parseInt(args[1]); x++)
				{
					tester1[i][x] = i - x;
				}
			}
			
		}
		else if (args[0].equals("2"))
		{
			float averageE = 0;
			float eCount = 0;
			float averageO = 0;
			float oCount = 0;
			for (int alpha = 0; alpha < 10000; alpha ++)
			{
				long sTime;
				long cTime;
				long vTime;
				int i = 20;
				while (true)
				{
					int[][] tester = new int[i][i];
					for (int x = 0; x < i; x ++)
					{
						for (int j = 0; j < i; j++)
						{
							tester[x][j] = x + j;
						}
					}
					sTime = System.nanoTime();
					mult(tester, tester);
					cTime = System.nanoTime() - sTime;
					sTime = System.nanoTime();
					strass2n(tester, tester, new int[tester.length][tester.length], tester.length/2 + 2);
					vTime = System.nanoTime() - sTime;
					if (vTime < cTime)
					{
						System.out.println(i);
						if (i % 2 == 0)
						{
							averageE += (float)i;
							eCount += 1;
						}
						else{
							averageO += (float)i;
							oCount += 1;
						}
						break;
					}
					i+=2;
				}
			}
			System.out.println("ans even");
			System.out.println(averageE / eCount);
			System.out.println("ans odd");
			System.out.println(averageO / oCount);
		}
		
	}

	public static int[][] strass2n(int[][] alpha1, int[][] beta1, int[][] sto, int n0)
	{
		// ideal base case per experimental results
		if (alpha1.length == 1 || ((alpha1.length % 2 == 0 && alpha1.length <= 58) || (alpha1.length % 2 == 1 && alpha1.length <= 73)))
		{
			return mult(alpha1, beta1);
		}
		if (alpha1.length % 2 != 0)
		{
			int[][] a11 = new int[(alpha1.length + 1) / 2][(alpha1[0].length + 1) / 2];
			int[][] a12 = new int[(alpha1.length + 1) / 2][(alpha1[0].length + 1) / 2];
			int[][] b11 = new int[(beta1.length + 1) / 2][(beta1[0].length + 1) / 2];
			int[][] b12 = new int[(beta1.length + 1) / 2][(beta1[0].length + 1) / 2];
			int[][] a21 = new int[(alpha1.length + 1) / 2][(alpha1[0].length + 1) / 2];
			int[][] a22 = new int[(alpha1.length + 1) / 2][(alpha1[0].length + 1) / 2];
			int[][] b21 = new int[(beta1.length + 1) / 2][(beta1[0].length + 1) / 2];
			int[][] b22 = new int[(beta1.length + 1) / 2][(beta1[0].length + 1) / 2];
			for (int i = 0; i < (alpha1.length + 1) / 2; i++)
			{
				for (int x = 0; x < (alpha1.length + 1) / 2; x++)
				{
					int len = (alpha1.length + 1) / 2;
					a11[i][x] = alpha1[i][x];
					b11[i][x] = beta1[i][x];
					if (i == len - 1)
					{
						a21[i][x] = 0;
						b21[i][x] = 0;
						a22[i][x] = 0;
						b22[i][x] = 0;
						if (x == len - 1)
						{
							a12[i][x] = 0;
							b12[i][x] = 0;
						}
						else
						{
							a12[i][x] = alpha1[i][x + len];
							b12[i][x] = beta1[i][x + len];
						}
					}
					else
					{
						if (x == len - 1)
						{
							a12[i][x] = 0;
							b12[i][x] = 0;
							a22[i][x] = 0;
							b22[i][x] = 0;
							a21[i][x] = alpha1[i + len][x];
							b21[i][x] = beta1[i + len][x];
						}
						else
						{
							a12[i][x] = alpha1[i][x + len];
							b12[i][x] = beta1[i][x + len];
							a21[i][x] = alpha1[i + len][x];
							b21[i][x] = beta1[i + len][x];
							a22[i][x] = alpha1[i + len][x + len];
							b22[i][x] = beta1[i + len][x + len];
						}
					}
				}
			}
			int[][] aTest = addMat(a11, a22);
			int[][] bTest = addMat(b11, b22);
			int[][] m1 = strass2n(aTest, bTest, aTest, n0);
			int[][] a21Pa22 = addMat(a21, a22);
			int[][] m2 = strass2n(a21Pa22, b11, a21Pa22, n0);
			int[][] b12Mb22 = minusMat(b12, b22);
			int[][] m3 = strass2n(a11, b12Mb22, b12Mb22, n0);
			int[][] b21Mb11 = minusMat(b21, b11);
			int[][] m4 = strass2n(a22, b21Mb11, b21Mb11, n0);
			int[][] a11Pa12 = addMat(a11, a12);
			int[][] m5 = strass2n(a11Pa12, b22, a11Pa12, n0);
			int[][] m6 = strass2n(minusMatP(a21, a11, a11), addMatP(b11, b12, b11), a21, n0);
			int[][] m7 = strass2n(minusMatP(a12, a22, a22), addMatP(b21, b22, b22), a12, n0);
			for (int i = 0; i < alpha1.length; i++)
			{
				for (int x = 0; x < alpha1.length; x++)
				{
					if (i < (alpha1.length + 1) / 2)
					{
						if (x < (alpha1.length + 1) / 2)
						{
							sto[i][x] = m1[i][x] + m4[i][x] - m5[i][x] + m7[i][x];
						}
						else
						{
							sto[i][x] = m3[i][x - (alpha1.length + 1) / 2] + m5[i][x - (alpha1.length + 1) / 2];
							//result[i][x] = c12[i][x - alpha.length / 2];
						}
					}
					else
					{
						if (x < (alpha1.length + 1) / 2)
						{
							sto[i][x] = m2[i - (alpha1.length + 1) / 2][x] + m4[i - (alpha1.length + 1) / 2][x];
						}
						else
						{
							sto[i][x] = m1[i - (alpha1.length + 1) / 2][x - (alpha1.length + 1) / 2]
								- m2[i - (alpha1.length + 1) / 2][x - (alpha1.length + 1) / 2] 
								+ m3[i - (alpha1.length + 1) / 2][x - (alpha1.length + 1) / 2] 
								+ m6[i - (alpha1.length + 1) / 2][x - (alpha1.length + 1) / 2];	
						}
					}
				}
			}
			return sto;
		}
		else
		{
			int[][] a11 = new int[alpha1.length / 2][alpha1[0].length / 2];
			int[][] a12 = new int[alpha1.length / 2][alpha1[0].length / 2];
			int[][] b11 = new int[beta1.length / 2][beta1[0].length / 2];
			int[][] b12 = new int[beta1.length / 2][beta1[0].length / 2];
			int[][] a21 = new int[alpha1.length / 2][alpha1[0].length / 2];
			int[][] a22 = new int[alpha1.length / 2][alpha1[0].length / 2];
			int[][] b21 = new int[beta1.length / 2][beta1[0].length / 2];
			int[][] b22 = new int[beta1.length / 2][beta1[0].length / 2];
			for (int i = 0; i < alpha1.length / 2; i++)
			{
				for (int x = 0; x < alpha1.length / 2; x++)
				{
					a11[i][x] = alpha1[i][x];
					b11[i][x] = beta1[i][x];
					a12[i][x] = alpha1[i][x + alpha1.length / 2];
					b12[i][x] = beta1[i][x + alpha1.length / 2];
					a21[i][x] = alpha1[i + alpha1.length / 2][x];
					b21[i][x] = beta1[i + alpha1.length / 2][x];
					a22[i][x] = alpha1[i + alpha1.length / 2][x + alpha1.length / 2];
					b22[i][x] = beta1[i + alpha1.length / 2][x + alpha1.length / 2];
				}
			}
			int[][] aTest = addMat(a11, a22);
			int[][] bTest = addMat(b11, b22);
			int[][] m1 = strass2n(aTest, bTest, aTest, n0);
			int[][] a21Pa22 = addMat(a21, a22);
			int[][] m2 = strass2n(a21Pa22, b11, a21Pa22, n0);
			int[][] b12Mb22 = minusMat(b12, b22);
			int[][] m3 = strass2n(a11, b12Mb22, b12Mb22, n0);
			int[][] b21Mb11 = minusMat(b21, b11);
			int[][] m4 = strass2n(a22, b21Mb11, b21Mb11, n0);
			int[][] a11Pa12 = addMat(a11, a12);
			int[][] m5 = strass2n(a11Pa12, b22, a11Pa12, n0);
			int[][] m6 = strass2n(minusMatP(a21, a11, a11), addMatP(b11, b12, b11), a21, n0);
			int[][] m7 = strass2n(minusMatP(a12, a22, a22), addMatP(b21, b22, b22), a12, n0);
			for (int i = 0; i < alpha1.length; i++)
			{
				for (int x = 0; x < alpha1.length; x++)
				{
					if (i < alpha1.length / 2)
					{
						if (x < alpha1.length / 2)
						{
							sto[i][x] = m1[i][x] + m4[i][x] - m5[i][x] + m7[i][x];
						}
						else
						{
							sto[i][x] = m3[i][x - alpha1.length / 2] + m5[i][x - alpha1.length / 2];
						}
					}
					else
					{
						if (x < alpha1.length / 2)
						{
							sto[i][x] = m2[i - alpha1.length / 2][x] + m4[i - alpha1.length / 2][x];
						}
						else
						{
							sto[i][x] = m1[i - alpha1.length / 2][x - alpha1.length / 2]
								- m2[i - alpha1.length / 2][x - alpha1.length / 2] 
								+ m3[i - alpha1.length / 2][x - alpha1.length / 2] 
								+ m6[i - alpha1.length / 2][x - alpha1.length / 2];
						}
					}
				}
			}
			return sto;
		}
	}

	public static int[][] addMat(int[][] a, int[][] b)
	{
		int[][] ans = new int[a.length][a.length];
		for (int i = 0; i < a.length; i++)
		{
			for (int x = 0; x < a.length; x++)
			{
				ans[i][x] = a[i][x] + b[i][x];
			}
		}
		return ans;
	}
	public static int[][] addMatP(int[][] a, int[][] b, int[][] sto)
	{
		for (int i = 0; i < a.length; i++)
		{
			for (int x = 0; x < a.length; x++)
			{
				sto[i][x] = a[i][x] + b[i][x];
			}
		}
		return sto;
	}

	public static int[][] minusMat(int[][] a, int[][] b)
	{
		int[][] ans = new int[a.length][a.length];
		for (int i = 0; i < a.length; i++)
		{
			for (int x = 0; x < a.length; x++)
			{
				ans[i][x] = a[i][x] - b[i][x];
			}
		}
		return ans;
	}
	public static int[][] minusMatP(int[][] a, int[][] b, int[][] sto)
	{
		for (int i = 0; i < a.length; i++)
		{
			for (int x = 0; x < a.length; x++)
			{
				sto[i][x] = a[i][x] - b[i][x];
			}
		}
		return sto;
	}


	public static ArrayList<int[][]> pullMat(int dim, String fileName)
	{
		ArrayList<int[][]> result = new ArrayList<int[][]>();
		File file = new File(fileName);
		try
		{
			Scanner scanner = new Scanner(file);
			int[][] a = new int[dim][dim];
			for (int i = 0; i < dim; i++)
			{
				for (int x = 0; x < dim; x++)
				{
					a[i][x] = scanner.nextInt();
				}
			}
			result.add(a);
			int[][] b = new int[dim][dim];
			for (int i = 0; i < dim; i++)
			{
				for (int x = 0; x < dim; x++)
				{
					b[i][x] = scanner.nextInt();
				}
			}
			result.add(b);
		}
		catch (FileNotFoundException x)
		{
			System.err.println(x);
		}
		return result;
	}

	public static int[][] mult(int[][] a, int[][] b)
	{
		int[] tempCol = new int[b[0].length];
		int[][] result = new int[a.length][a.length];
		for (int i = 0; i < b[0].length; i++)
		{
			for (int alpha = 0; alpha < b[0].length; alpha++)
			{
				tempCol[alpha] = b[alpha][i];
			}
			for (int x = 0; x < a.length; x++)
			{
				int temp = a[x][0] * tempCol[0];
				for (int z = 1; z < a[0].length; z++)
				{
					temp += a[x][z] * tempCol[z];
				}
				result[x][i] = temp;
			}
		}
		return result;
	}

	public static void printMat(int[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++)
		{
			for (int x = 0; x < matrix[0].length; x++)
			{
				System.out.print(matrix[i][x]);
				System.out.print(" ");
			}
			System.out.println("");
		}
	}
}