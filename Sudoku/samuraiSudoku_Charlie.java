/* sudoku_template.java                 *
 * please change to be sudoku_yourname  *
 ****************************************/


import java.io.*;
import java.util.Scanner;

public class samuraiSudoku_Charlie{

	static int size=21;
	static int[][] board = new int[size][size];
	static boolean stop=false;

	public static void main(String[] args){

        readpuzzle(board);
        print(board);
		solve(board);
		System.out.println("");
        print(board);

	}
	public static void solve(int[][] board1){
		int r=board1.length;
		int c=board1.length;
		boolean done=false;
		for (int i=0; i<board1.length; i++){
			for (int x=0; x<board1.length; x++){
				
				if (board1[i][x]==0){
					r=i;
					c=x;
					break;
				}
				else if (i==board1.length-1 && x==board1.length-1){
					board=board1;
					System.out.println("fuck");
					stop=true;
					/*for (int a=0; a<board1.length; a++){
            			/*if (x<6 || x>14){
            				board1[a][9]=0;
            				board1[a][10]=0;
            				board1[a][11]=0;
            				board1[9][a]=0;
            				board1[10][a]=0;
            				board1[11][a]=0;
            			}*/
            		
				}
			}
			if (r!=board1.length){
				break;
			}
		}
		if (r!=board1.length){
		for (int i=1; i<=9; i++){
			if (isLegal(i, r, c, board1)){
				board1[r][c]=i;
				int[][] board2=copy(board1);
				if (!stop){
					solve(board2);
				}
				
				done=true;	
			}
		}
		}

		if (!done){
			//print(board1);
		}
	}
	/*public static int[][] subBoard(int [][] board, int r, int c){
		int[][] result = new int[9][9];
		if (r<0 || c<0){
			return result;
		}
		for (int i=0; i<result.length; i++){
			for (int x=0; x<result.length; x++){
				result[i][x]=board[r+i][c+x];
			}
		}
		return result;
	}*/
	public static boolean isLegal(int attempt, int r, int c, int[][] board){
		for (int i=0; i<3; i++){
			for (int x=0; x<3; x++){
				if (board[i+3*(r/3)][x+3*(c/3)]==attempt){
					return false;
				}
			}
		}
		if (r>5 && r<15 && c>5 && c<15){
			for (int i=6; i<15; i++){
				if (attempt==board[r][i]){
					return false;
				}
				else if (attempt==board[i][c]){
					return false;
				}
			}
		}
		if (r<9 && c<9){
			for (int i=0; i<9; i++){
				if (attempt==board[r][i]){
					return false;
				}
				else if (attempt==board[i][c]){
					return false;
				}
			}
		}
		else if (r<9 && c>11){
			for (int i=12; i<21; i++){
				if (attempt==board[r][i]){
					return false;
				}
			}
			for (int i=0; i<9; i++){
				if (attempt==board[i][c]){
					return false;
				}
			}
		}
		else if (r>11 && c<9){
			for (int i=12; i<21; i++){
				if (attempt==board[i][c]){
					return false;
				}
			}
			for (int i=0; i<9; i++){
				if (attempt==board[r][i]){
					return false;
				}
			}
		}
		else if (r>11 && c>11){
			for (int i=12; i<21; i++){
				if (attempt==board[r][i]){
					return false;
				}
				else if (attempt==board[i][c]){
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
            for (int x=0; x<board.length; x++){
            	if (x<6 || x>14){
            		board[x][9]=1;
            		board[x][10]=1;
            		board[x][11]=1;
            		board[9][x]=1;
            		board[10][x]=1;
            		board[11][x]=1;
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
