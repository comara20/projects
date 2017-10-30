import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class ghost
{
	public static void main(String[] args)
	{
		String str = "";
		String word = "";
		ArrayList<String> dict;
		Scanner scanner = new Scanner(System.in);
		ArrayList<String> words = new ArrayList<String>();
		String[] alphabet = {"e","t","a","o","i","n","s","h","r","d","l","c","u","m","w","f","g",
		"y","p","b","v","k","j","x","q","z"};
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("usa.txt"));
			while ((str = in.readLine()) != null)
			{
				str = str.replaceAll("'", "");
				words.add(str);
			}
			dict = words;
			while (true)
			{
				if (word.length() > 0)
				{
					System.out.println(word);
				}
				System.out.print("Enter your letter: ");
				String let = scanner.next();
				if (let.equals("help"))
				{
					provideHelp(word, dict, alphabet);
					System.out.print("Enter your letter: ");
					let = scanner.next();
				}
				word += let;
				if (!isWordPart(word, dict))
				{
					System.out.println("illegal letter, cpu wins!");
					break;
				}
				word += chooseNext(word, shuffle(alphabet), dict);
			}
		}
		catch (IOException x)
		{
			System.out.println(x);
		}
	}

	public static void provideHelp(String word, ArrayList<String> dict, String[] alphabet)
	{
		boolean foundWord = false;
		for (int i = 0; i < alphabet.length; i++)
		{
			if (isWordPart(word + alphabet[i], dict))
			{
				System.out.println(word + alphabet[i]);
				foundWord = true;
			}
		} 
		if (!foundWord)
		{
			System.out.println("you're screwed");
		}
	}

	public static boolean isWordPart(String word, ArrayList<String> dict)
	{
		for (int i = 0; i < dict.size(); i++)
		{
			String temp = dict.get(i);
			if (temp.length() >= word.length())
			{
				if (word.equals(temp.substring(0, word.length())))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static String chooseNext(String word, String[] alphabet, ArrayList<String> dict)
	{
		for (int i = alphabet.length - 1; i >= 0; i--)
		{
			if (helper(word + alphabet[i], dict, alphabet))
			{
				return alphabet[i];
			}
		}

		return "you win!";
	}
	public static boolean helper(String word, ArrayList<String> dict, String[] alphabet)
	{
		ArrayList<String> thisDict = new ArrayList<String>();
		for (int i = 0; i < dict.size(); i++)
		{
			thisDict.add(dict.get(i));
		}
		if(!isWordPart(word, thisDict))
		{
			return false;
		}

		for (int i = thisDict.size() - 1; i >= 0; i--)
		{
			if (thisDict.get(i).length() <= word.length())
			{
				thisDict.remove(i);
			}
			else
			{
				if (!thisDict.get(i).substring(0, word.length()).equals(word))
				{
					thisDict.remove(i);
				}
			}
		}
		if (thisDict.size() == 0)
		{
			return true;
		}
		else
		{
			boolean next;
			for (int i = alphabet.length - 1; i >= 0; i--)
			{
				if (helper(word + alphabet[i], thisDict, alphabet))
				{
					return false;
				}
			}
		}
		return true;
	}

	public static String[] shuffle(String[] input)
	{
		Random ran = new Random();
		String[] result = new String[input.length];
		for (int i = 0; i < input.length; i++)
		{
			int temp = ran.nextInt(input.length - i);
			int index = 0;
			while (true)
			{
				if (result[index] == null)
				{
					if (temp == 0)
					{
						result[index] = input[i];
						break;
					}
					else
					{
						temp += -1;
					}
					index ++;
				}
				else
				{
					index ++;
				}
			}
		}
		return result;
	}
}


