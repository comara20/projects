/* sudoku_template.java                 *
 * please change to be sudoku_yourname  *
 ****************************************/


import java.io.*;
import java.util.Scanner;

public class sudoku_template{

	static int size=9;
	

	public static void main(String[] args){

        int[][] board = new int[size][size];
        readpuzzle(board);
        print(board);
		System.out.println(solve(board));
		System.out.println("");
        print(board);

	}
	public static boolean solve(int[][] board1){
		int r=10;
		int c=10;
		for (int i=0; i<board1.length; i++){
			for (int x=0; x<board1.length; x++){
				if (board1[i][x]==0){
					r=i;
					c=x;
					break;
				}
				else if (i==8 && x==8){
					return true;
				}
			}
			if (r!=10){
				break;
			}
		}
		for (int i=1; i<=board1.length; i++){
			if (isLegal(i, r, c, board1)){
				board1[r][c]=i;
				if(solve(board1)){
					return true;
				}
				board1[r][c]=0;		
			}
		}
		return false;
	}

	public static boolean isLegal(int attempt, int r, int c, int[][] board){
		for (int i=0; i<board.length; i++){
			if (attempt==board[r][i]){
				return false;
			}
			else if (attempt==board[i][c]){
				return false;
			}
		}
		for (int i=0; i<3; i++){
			for (int x=0; x<3; x++){
				if (board[i+3*(r/3)][x+3*(c/3)]==attempt){
					return false;
				}
			}
		}
		return true;
	}

    public static void readpuzzle(int[][] board){
		Scanner s = new Scanner(System.in);
        for (int i=0; i<size; i++) {
        	String line = s.nextLine();
        	for (int j=0; j<size; j++) {
            	char c = line.charAt(j);
            	if(c!=' '){
                	board[i][j]=Integer.valueOf(c+"");
                }
            }
        }
        
	}

	public static int[][] copy(int[][] input){

		int[][] output = new int[input[0].length][input[0].length];
		for(int i=0; i<input[0].length; i++){
			for(int j=0; j<input[0].length; j++){
				output[i][j]=input[i][j];
			}
		}
		return output;
	}


	public static void print(int[][] input){
		for(int i=0; i<input[0].length; i++){
			for(int j=0; j<input[0].length; j++){
				System.out.print(input[i][j]+" ");
			}
			System.out.println("");
		}
	}

}
