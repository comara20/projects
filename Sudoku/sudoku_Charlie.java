/*           sudoku_template.java           *
 * please change to be sudoku_Charlie.java  *
 *******************************************/
//Charles O'Mara
//12/8/15


import java.io.*;
import java.util.Scanner;

public class sudoku_Charlie{
	//size of board, makes it easier to do samurai later
	static int size=9;

	public static void main(String[] args){
		//declares board
        int[][] board = new int[size][size];
        readpuzzle(board, "sudokufile2.txt");
        print(board);
		solve(board);
		System.out.println("");
        print(board);

	}
	public static boolean solve(int[][] board1){
		//r and c and index of first 0 position found on board looping from top left corner to bottom right corner
		int r=board1.length;
		int c=board1.length;
		for (int i=0; i<board1.length; i++){
			for (int x=0; x<board1.length; x++){
				if (board1[i][x]==0){
					r=i;
					c=x;
					break;
				}
				//base case where there are no zeros left in the board. We're done!!!!
				else if (i==board1.length-1 && x==board1.length-1){
					return true;
				}
			}
			//already found a zero pos in the board so we need to STOP looping
			if (r!=board1.length){
				break;
			}
		}
		for (int i=1; i<=9; i++){
			//checks all possible numbers that could work at a given position in the immediate future ie one level of complexity
			if (isLegal(i, r, c, board1)){
				//so it works, therefore we change position at point r, c determined above to whatever i value works there
				board1[r][c]=i;
				//sees if solving the board with that i value eventually yields a true base case, if so, we done!!!!
				if(solve(board1)){
					return true;
				}	
			}
		}
		//resets board position if the final loop value for which (isLegal) does not end up working
		board1[r][c]=0;
		//other base case if nothing ends up being legal	
		return false;
	}

	public static boolean isLegal(int attempt, int r, int c, int[][] board){
		for (int i=0; i<board.length; i++){
			//checks row
			if (attempt==board[r][i]){
				return false;
			}
			//checks column
			else if (attempt==board[i][c]){
				return false;
			}
		}
		//checks box
		for (int i=0; i<3; i++){
			for (int x=0; x<3; x++){
				if (board[i+3*(r/3)][x+3*(c/3)]==attempt){
					return false;
				}
			}
		}
		return true;
	}
	//Darby code that she didn't comment because she is a delinquent
    public static void readpuzzle(int[][] board, String name){
		//Scanner s = new Scanner(System.in);
		//Scanner s = new Scanner(name);
		try{
		FileReader s1 = 
                new FileReader(name);
        BufferedReader s = new BufferedReader(s1);
        for (int i=0; i<size; i++) {
        	String line = s.readLine();
        	for (int j=0; j<size; j++) {
            	char c = line.charAt(j);
            	if(c!=' '){
                	board[i][j]=Integer.valueOf(c+"");
                }
            } 
        }
        }
            catch(IOException ex) {}
        
	}
	//Darby code that she didn't comment because she is a delinquent
	public static int[][] copy(int[][] input){

		int[][] output = new int[input[0].length][input[0].length];
		for(int i=0; i<input[0].length; i++){
			for(int j=0; j<input[0].length; j++){
				output[i][j]=input[i][j];
			}
		}
		return output;
	}

	//Darby code that she didn't comment because she is a delinquent
	public static void print(int[][] input){
		for(int i=0; i<input[0].length; i++){
			for(int j=0; j<input[0].length; j++){
				System.out.print(input[i][j]+" ");
			}
			System.out.println("");
		}
	}

}
