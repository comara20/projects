Scrabble: Implementation of Scrabble AI in OCaml. Run with "./boarddraw.byte" in terminal. Takes a second for graphics to start up (~5). Make sure ospd.txt is in the same directory. Body of AI is in board.ml.

ghost: Run with "java ghost" in terminal. Make sure usa.txt is in the same directory. AI implementation of game where two individuals take turns adding letters to a sequence such that the sequence is a prefix for some English word. One player wins when the other cannot add another letter. Example: P1: c, P2: a, P1: t, P2: s, P1 loses because they can't think of any letter to tack onto "cats" such that it prefixes.

matrixMult: Implemention of Strassen algorithm for matrix multiplication which is O(n^(log7)) = O(n^2.8). Performs standard matrix multiplication on matrices small enough such that Strassen's is actually slower. Run with "java strassen 1". Squares 2000x2000 random matrix, and output time to do so with strassen and with standard O(n^3) algorithm.

sudoku: Implementation of a recursive algorithm to solve sudoku puzzles. Run with "java sudoku_Charlie" with sudokufile2.txt in the same directory.

final_project: final project is a checkers AI written in javascript and python. It is configured exclusively to run within the CS50 IDE, but the meat of the AI, which I wrote, is with in statics/scripts.js, in which the board is treated as an 8x8 array. The optimal AI move was determined by minMaxing over all possible moves to a depth of 10. AI was functionally unbeatable (though I'm not a very good checkers player) but it suffered from horizon effects in the end game. 
