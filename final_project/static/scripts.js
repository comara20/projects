var mainBoard = setUpBoard();
var canvas = document.getElementById("myCanvas");
var context2D = canvas.getContext("2d");
var click1 = [-1, -1];
var click2 = [-1, -1];
var humanPlayer = 2;
var depthGlobal = 10;

// draws canvas
document.addEventListener('click', executeTurn);
document.addEventListener('DOMContentLoaded', domloaded, false);

function domloaded(){
    drawBoard();
    drawPieces(mainBoard);
}

/*
This function draws the guts of the 8x8 board (2d array).
It populates the canvas with 64 cells.
*/

function reset()
{
    // resets board, render it again, clears all clicks
    mainBoard = setUpBoard();
    click1 = [-1, -1];
    click2 = [-1, -1];
    drawBoard();
    drawPieces(mainBoard);
}

function drawBoard()
{
    // iterate over each row index
    for (var i = 0; i < mainBoard.length; i++)
    {
        // iterate over each column index
        for (var x = 0; x < mainBoard[0].length; x++)
        {
            // if "even cell"
            if ((i + (x % 2)) % 2 == 0)
            {
                // fill with brown
                context2D.fillStyle = "Brown";
                context2D.fillRect(i * canvas.width / 8, x * canvas.height / 8, canvas.width / 8, canvas.height / 8);
            }
            // if "odd" cell
            else
            {
                // fill with beige
                context2D.fillStyle = "Beige";
                context2D.fillRect(i * canvas.width / 8, x * canvas.height / 8, canvas.width / 8, canvas.height / 8);
            }
        }
    }
}

/*
This function determines whether the two boards passed in are equal.
The function is called within the possibleContinues function.
It returns true if the two boards are equal.
It returns false if the two board are not equal.
*/

function isEqual(board1, board2)
{
    // iterate over each row index
    for (var i = 0; i < board1.length; i++)
    {
        // iterate over each column index
        for (var x = 0; x < board1[0].length; x++)
        {
            // check if corresponding cells are not equal
            if (board1[i][x] != board2[i][x])
            {
                // not equal
                return false;
            }
        }
    }
    // equal
    return true;
}

/*
This function executes a turn for the player.
It takes as an input the click event. 
It renders alert messages for the winner. 
*/
function executeTurn(event){
    // initiate variable for whether player has moved
    var hasMoved = false;
    var weDone = false;
    // store coordinates of click event as array
    var cord = [Math.floor(event.offsetX/(canvas.width/8)), Math.floor(event.offsetY/(canvas.height/8))];
    // change color of cell clicked
    changeColor(cord);
    drawPieces(mainBoard);
    // if click1 is not logged
    if (click1[0] == -1)
    {
        // log to location of first click
        click1[0] = cord[1];
        click1[1] = cord[0];
    }
    // if click click2 is not logged
    else if (click2[0] == -1)
    {
        // log to location of second click
        click2[0] = cord[1];
        click2[1] = cord[0];
        // get array of possible moves
        var possibleMoves = jump_possible(mainBoard, humanPlayer);
        // if no possible moves left
        if (possibleMoves.length == 0)
        {
            alert("Game Over! Sorry, you lost.");
            reset();
            weDone = true;
        }
        // iterate over array of possible moves
        if (!weDone)
        {
            for (var i = 0; i < possibleMoves.length; i++)
            {
                // if click1 is among list of possible moves
                if (click1[0] == possibleMoves[i][0][0] && click1[1] == possibleMoves[i][0][1])
                {
                    // if click2 is among list of possible moves
                    if (click2[0] == possibleMoves[i][1][0] && click2[1] == possibleMoves[i][1][1])
                    {
                        // move is possible, so player will move
                        hasMoved = true;
                        // render new main board given the 2 clicks
                        mainBoard = makeMove(mainBoard, humanPlayer, click1, click2);
                        // if 2 clicks are not on adjacent rows (if hop)
                        if (Math.abs(click1[0] - click2[0]) != 1)
                        {
                            // get array of all continuations after hop
                            var isDoubled = possibleContinues(mainBoard, click2[0], click2[1]);
                            // check whether no possible continuations (now AI's move)
                            if (isDoubled.length == 1 && isEqual(isDoubled[0], mainBoard))
                            {
                                // run recursive function for AI's best move
                                mainBoard = chooseMove(mainBoard, depthGlobal, 1);
                                // if no possible moves
                                if (mainBoard == false)
                                {
                                    alert("Game Over! You won!")
                                    break;
                                }
                                // reset clicks
                                click1 = [-1, -1];
                                click2 = [-1, -1];
                                // draw board again with pieces
                                drawBoard();
                                drawPieces(mainBoard);
                                break;
                            }
                            // if another move is possible
                            else
                            {
                                click1 = [-1, -1];
                                click2 = [-1, -1];
                                drawBoard();
                                drawPieces(mainBoard);
                                break;
                            }
                        }
                        // if simple move (not hop)
                        else
                        {
                            // run recursive function for AI's best move
                            mainBoard = chooseMove(mainBoard, depthGlobal, 1);
                            // if AI has no possible moves
                            if (mainBoard == false)
                            {
                                alert("Game Over! You won");
                                reset();
                                break;
                            }
                            // reset clicks
                            click1 = [-1, -1];
                            click2 = [-1, -1];
                            drawBoard();
                            drawPieces(mainBoard);
                            break;
                        }
                    }
                }
            }
            // if player player has moved
            if (!hasMoved)
            {
                // redraw board and reset clicks
                drawBoard();
                drawPieces(mainBoard);
                click1 = [-1, -1];
                click2 = [-1, -1];
            }
            // if player 1 has won
            if (hasWon(mainBoard) == 1)
            {
                alert("Game Over! Sorry, you lost.");
                reset();
            }
            // if other player has won
            if (hasWon(mainBoard) == 2)
            {
                alert("Game Over! Congrats, you won!")
                reset();
            }
        }
    }
}

/*
This function changes the color of clicked cells.
It is called upon in the executeTurn function.
*/

function changeColor(cord)
{
    // set fillStyle to Blue
    context2D.fillStyle = "Blue";
    // fill clicked cell 
    context2D.fillRect(cord[0] * canvas.width / 8, cord[1] * canvas.height / 8, canvas.width / 8, canvas.height / 8);
}

/*
This function draws the checkers pieces on the board.
It takes as an input the present board.
*/

function drawPieces(board) {
    // iterate over each row index
    for (var i = 0; i < board.length; i++)
    {
        // store size of tile
        var tile_size = canvas.width/8
        // iterate over each column index
        for (var x = 0; x < board[0].length; x++)
        {
            // if player 1's regular piece
            if (board[i][x] == 1)
            {
                // place white regular piece on tile
                context2D.fillStyle = "White";
                context2D.fillRect(x* tile_size+tile_size/4, i*tile_size+tile_size/4, tile_size/2, tile_size/2 );
            }
            // if player 1's king piece
            else if (board[i][x] == 2)
            { 
                // place yellow king piece on tile
                context2D.fillStyle = "Yellow";
                context2D.fillRect(x* tile_size+tile_size/4, i*tile_size+tile_size/4, tile_size/2, tile_size/2 );
            }
            // if other player's regular piece
            if (board[i][x] == 3)
            {
                // place black regular piece on tile
                context2D.fillStyle = "Black";
                context2D.fillRect(x* tile_size+tile_size/4, i*tile_size+tile_size/4, tile_size/2, tile_size/2 );
            }
            // if other player's king piece
            if (board[i][x] == 4)
            {
                // place purple king piece on tile
                context2D.fillStyle = "Purple";
                context2D.fillRect(x* tile_size+tile_size/4, i*tile_size+tile_size/4, tile_size/2, tile_size/2 );
            }
        }
    }
}



/*This function checks the entire board for all possible moves.
It takes as inputs the current board and the player (1 or 3) for which 
we are checking.
Returns an array of all possible moves, expressed by a starting and 
landing index for each move.*/


function jump_possible(board, player)
{
    // initiate array to contain all possible moves
    var Moves = [];
    // ensure game is not over
    if (hasWon(board) != 0)
    {
        return Moves;
    }
    // check if player 2
    if (player == 2)
    {
        // treated player incorrectly throughout function, if player = 2, then maps
        // to a tile value of 3
        player = 3;
    }
    if (player == 1)
    {
        // iterate over each row index for board
        for (var i = 0; i < 8; i++)
        {
            // iterate over each column index for board
            for (var x = 0; x < 8; x++)
            {
                // dealing with player 1 regular piece
                if (board[i][x] == player)
                {
                    // ensure not on bottom boundary
                    if (i + 1 <= board.length - 1)
                    {
                        // ensure not on right boundary
                        if (x + 1 <= board.length - 1)
                        {
                            // check if bottom right diagonal is empty
                            if (board[i + 1][x + 1] == 0)
                            {
                                // make 2d array with starting, landing indexes
                                var temp = [[i, x], [i + 1,x + 1]];
                                // add to Moves array
                                Moves.push(temp);
                            }
                        }
                        // ensure not on left boundary
                        if (x - 1 >= 0)
                        {
                            // check if bottom left cell is empty
                            if (board[i + 1][x - 1] == 0)
                            {
                                // make 2d array with starting, landing indexes
                                var temp = [[i, x], [i + 1, x - 1]];
                                // add to Moves array
                                Moves.push(temp);
                            }
                        }
                    }
                    // checking for captures now 
                    // ensure not hopping beyond bottom boundary
                    if (i + 2 <= board.length - 1)
                    {
                        // ensure not hopping beyond right boundary
                        if (x + 2 <= board[0].length - 1)
                        {
                            // if right diagonal cell is opponent and next diagonal is empty
                            if ((board[i + 1][x + 1] == 3 || board[i + 1][x + 1] == 4) && board[i + 2][x + 2] == 0)
                            {
                                // make  2d array with starting, landing indexes
                                var temp = [[i, x], [i + 2, x + 2]];
                                // add to Moves array
                                Moves.push(temp);
                            }
                        }
                        // ensure not hopping beyond left boundary
                        if (x - 2 >= 0)
                        {
                            // if left diagonal is opponent and next diagonal is empty
                            if ((board[i + 1][x - 1] == 3 || board[i + 1][x - 1] == 4) && board[i + 2][x - 2] == 0)
                            {
                                // make  2d array with starting, landing indexes
                                var temp =[[i, x], [i + 2, x - 2]];
                                // add to Moves array
                                Moves.push(temp);
                            }
                        }
                    }
                }
                // now checking king for player 1, need to check more directions
                // if cell contains a king for player 1
                else if (board[i][x] == 2)
                {
                    // if not on right boundary
                    if (i + 1 <= board.length - 1)
                    {
                        // if not on bottom boundary
                        if (x + 1 <= board.length - 1)
                        {
                            // if bottom right diagonal is empty
                            if (board[i + 1][x + 1] == 0)
                            {
                                // add bottom right move
                                var temp = [[i, x], [i + 1, x + 1]];
                                Moves.push(temp);
                            }
                        }
                        // if not on left boundary
                        if (x - 1 >= 0)
                        {
                            // if bottom left diagonal is empty
                            if (board[i + 1][x - 1] == 0)
                            {
                                // add bottom left move
                                var temp = [[i, x], [i + 1, x - 1]];
                                Moves.push(temp);
                            }
                        }
                    }
                    // if not on top boundary
                    if (i - 1 >= 0)
                    {
                        // if not on right boundary
                        if (x + 1 <= board.length - 1)
                        {
                            // if top right diagonal is empty
                            if (board[i - 1][x + 1] == 0)
                            {
                                // add top right move
                                var temp = [[i, x], [i - 1, x + 1]];
                                Moves.push(temp);
                            }
                        }
                        // if not on left boundary
                        if (x - 1 >= 0)
                        {
                            // if top left diagonal is empty
                            if (board[i - 1][x - 1] == 0)
                            {
                                // add top left move
                                var temp = [[i, x], [i - 1, x - 1]];
                                Moves.push(temp);
                            }
                        }
                    }
                    // checking for captures now 
                    // if not hopping beyond bottom boundary
                    if (i + 2 <= board.length - 1)
                    {
                        // if not hopping beyond right boundary
                        if (x + 2 <= board[0].length - 1)
                        {
                            // if bottom right diagonal is opponent and next diagonal is empty
                            if ((board[i + 1][x + 1] == 3 || board[i + 1][x + 1] == 4) && board[i + 2][x + 2] == 0)
                            {
                                // add bottom right hop
                                var temp = [[i, x], [i + 2, x + 2]];
                                Moves.push(temp);
                            }
                        }
                        // if not hopping beyond left boundary
                        if (x - 2 >= 0)
                        {
                            // if bottom left diagonal is opponent and next diagonal is empty
                            if ((board[i + 1][x - 1] == 3 || board[i + 1][x - 1] == 4) && board[i + 2][x - 2] == 0)
                            {
                                // add bottom left hop
                                var temp =[[i, x], [i + 2, x - 2]];
                                Moves.push(temp);
                            }
                        }
                    }
                    // if not hopping beyond top boundary
                    if (i - 2 >= 0)
                    {
                        // if not hopping beyond right boundary
                        if (x + 2 <= board[0].length - 1)
                        {
                            // if top right diagonal is opponent and next diagonal is empty
                            if ((board[i - 1][x + 1] == player + 2 || board[i - 1][x + 1] == player + 3) && board[i - 2][x + 2] == 0)
                            {
                                // add top right hop
                                var temp = [[i, x], [i - 2, x + 2]];
                                Moves.push(temp);
                            }
                        }
                        // if not hopping beyond left boundary
                        if (x - 2 >= 0)
                        {
                            // if top left diagonal is opponent and next diagonal is empty
                            if ((board[i - 1][x - 1] == player + 2 || board[i - 1][x - 1] == player + 3) && board[i - 2][x - 2] == 0)
                            {
                                // add top left hop
                                var temp =[[i, x], [i - 2, x - 2]];
                                Moves.push(temp);
                            }
                        }
                    }
                }
            }
        }
    }
    // so we know it's the other player 
    else
    {
        // iterate over each row index
        for (var i = 0; i < board.length; i++)
        {
            // iterate over each column index
            for (var x = 0; x < board[0].length; x++)
            {
                // dealing with other player's regular piece
                if (board[i][x] == player)
                {
                    // if not on top boundary
                    if (i - 1 >= 0)
                    {
                        // if not on right boundary
                        if (x + 1 <= board.length - 1)
                        {
                            // if top right diagonal is empty
                            if (board[i - 1][x + 1] == 0)
                            {
                                // add top right move
                                var temp = [[i, x], [i - 1, x + 1]];
                                Moves.push(temp);
                            }
                        }
                        // if not on left boundary
                        if (x - 1 >= 0)
                        {
                            // if top left diagonal is empty
                            if (board[i - 1][x - 1] == 0)
                            {
                                // add top left move
                                var temp = [[i, x], [i - 1, x - 1]];
                                Moves.push(temp);
                            }
                        }
                    }
                    // checking for captures now 
                    // if not hopping beyond top boundary
                    if (i - 2 >= 0)
                    {
                        // if not hopping beyond right boundary
                        if (x + 2 <= board[0].length - 1)
                        {
                            // if top right diagonal is opponent and next diagonal is empty 
                            if ((board[i - 1][x + 1] == player - 1 || board[i - 1][x + 1] == player - 2) && board[i - 2][x + 2] == 0)
                            {
                                // add top right hop
                                var temp = [[i, x], [i - 2, x + 2]];
                                Moves.push(temp);
                            }
                        }
                        // if not hopping beyond left boundary
                        if (x - 2 >= 0)
                        {
                            // if top left diagonal is opponent and next diagonal is empty
                            if ((board[i - 1][x - 1] == player - 1 || board[i - 1][x - 1] == player - 2) && board[i - 2][x - 2] == 0)
                            {
                                // add top left hop
                                var temp =[[i, x], [i - 2, x - 2]];
                                Moves.push(temp);
                            }
                        }
                    }
                }
                // now checking king for other player, need to check more directions
                // if cell contains other player's king
                else if (board[i][x] == player + 1)
                {
                    // if not on bottom boundary
                    if (i + 1 <= board.length - 1)
                    {
                        // if not on right boundary
                        if (x + 1 <= board.length - 1)
                        {
                            // if bottom right diagonal is empty
                            if (board[i + 1][x + 1] == 0)
                            {
                                // add bottom right move
                                var temp = [[i, x], [i + 1, x + 1]];
                                Moves.push(temp);
                            }
                        }
                        // if not on left boundary
                        if (x - 1 >= 0)
                        {
                            // bottom left is empty
                            if (board[i + 1][x - 1] == 0)
                            {
                                // add bottom left move
                                var temp = [[i, x], [i + 1, x - 1]];
                                Moves.push(temp);
                            }
                        }
                    }
                    // if not on top boundary
                    if (i - 1 >= 0)
                    {
                        // if not on right boundary
                        if (x + 1 <= board.length - 1)
                        {
                            // if top right diagonal is empty
                            if (board[i - 1][x + 1] == 0)
                            {
                                // add top right move
                                var temp = [[i, x], [i - 1, x + 1]];
                                Moves.push(temp);
                            }
                        }
                        // if not on left boundary
                        if (x - 1 >= 0)
                        {
                            // if top left diagonal is empty
                            if (board[i - 1][x - 1] == 0)
                            {
                                
                                // add top left move
                                var temp = [[i, x], [i - 1, x - 1]];
                                Moves.push(temp);
                            }
                        }
                    }
                    // checking for captures now 
                    // if not hopping beyond bottom boundary
                    if (i + 2 <= board.length - 1)
                    {
                        // if not hopping beyond right boundary
                        if (x + 2 <= board[0].length - 1)
                        {
                            // if bottom right diagonal is opponent and next diagonal is empty
                            if ((board[i + 1][x + 1] == player - 1 || board[i + 1][x + 1] == player - 2) && board[i + 2][x + 2] == 0)
                            {
                                // add bottom right hop
                                var temp = [[i, x], [i + 2, x + 2]];
                                Moves.push(temp);
                            }
                        }
                        // if not hopping beyond left boundary
                        if (x - 2 >= 0)
                        {
                            // if bottom left diagonal is opponent and next diagonal is empty
                            if ((board[i + 1][x - 1] == player - 1 || board[i + 1][x - 1] == player - 2) && board[i + 2][x - 2] == 0)
                            {
                                // add bottom left hop
                                var temp =[[i, x], [i + 2, x - 2]];
                                Moves.push(temp);
                            }
                        }
                    }
                    // if not hopping beyond top boundary
                    if (i - 2 >= 0)
                    {
                        // if not hopping beyond right boundary
                        if (x + 2 <= board[0].length - 1)
                        {
                            // if top right diagonal is opponent and next diagonal is empty
                            if ((board[i - 1][x + 1] == player - 1 || board[i - 1][x + 1] == player - 2) && board[i - 2][x + 2] == 0)
                            {
                                // add top right hop
                                var temp = [[i, x], [i - 2, x + 2]];
                                Moves.push(temp);
                            }
                        }
                        // if not hopping beyond let boundary
                        if (x - 2 >= 0)
                        {
                            // if top left diagonal is opponent and next diagonal is empty
                            if ((board[i - 1][x - 1] == player - 1 || board[i - 1][x - 1] == player - 2) && board[i - 2][x - 2] == 0)
                            {
                                // add top left hop
                                var temp =[[i, x], [i - 2, x - 2]];
                                Moves.push(temp);
                            }
                        }
                    }
                }
            }
        }
    }
    // to store all possible jumps available
    var jumpMoves = [];
    // iterate over Moves
    for (var i = 0; i < Moves.length; i++)
    {
        // if a capture exists
        if (Math.abs(Moves[i][0][0] - Moves[i][1][0]) != 1)
        {
            // add to jumpMoves
            jumpMoves.push(Moves[i]);
        }
    }
    // if jumps exist
    if (jumpMoves.length > 0)
    {
        // return array of jumps
        return jumpMoves;
    }
    // return array of possible moves
    return Moves;
}


/*This function checks whether anyone has won the game.
Returns 0 when no one has yet one.
Returns 1 when player 1 wins.
Returns 3 when other player wins
*/

function hasWon(board){
    var p1piece = false;
    var p2piece = false;
    // iterate over each row index
    for (var i = 0; i < board.length; i++)
    {
        // iterate over each column index
        for (var x = 0; x < board[0].length; x++)
        {
            // if player 1's piece
            if (board[i][x] == 1 || board[i][x] == 2)
            {
                // at least one player 1 piece still on board
                p1piece = true;
                // if at least one other player piece on board
                if (p2piece)
                {
                    // both player 1 and player 2 pieces on board
                    // game not over
                    return 0;
                }
            }
            // if other player's piece
            else if (board[i][x] == 3 || board[i][x] == 4)
            {
                // at least one other player piece still on board
                p2piece = true;
                // if at least on player 1 piece on board
                if (p1piece)
                {
                    // both player 1 and player 2 pieces on board
                    // game not over
                    return 0;
                }
            }
        }
    }
    // no player 1 pieces
    if (!p1piece)
    {
        // other player wins
        return 2;
    }
    // no player 2 pieces, player 1 wins
    return 1;
}

/* This function generates a list of all possible boards as a result of 
available moves */

function getPossibleBoards(board, player)
{
    // what we'll ultimately return
    var allBoards = [];
    // retrieving move list
    var Moves = jump_possible(board, player);
    for (var i = 0; i < Moves.length; i++)
    {
        // don't want to modify originally passed in board
        var board1=copyBoard(board);
        // shortening my var name
        var m = Moves[i];
        // temp value
        var preVal = board1[m[0][0]][m[0][1]];
        // clearing the square we just moved off of
        board1[m[0][0]][m[0][1]] = 0;
        
        // setting the current square to val of piece moved to it
        board1[m[1][0]][m[1][1]] = preVal;
        if (preVal == 1 && m[1][0] == 7)
        {
            board1[m[1][0]][m[1][1]] = 2;
        }
        else if (preVal == 3 && m[1][0] == 0)
        {
            board1[m[1][0]][m[1][1]] = 4;
        }
        // checking to see if the move was a capture
        if (Math.abs(m[0][0]-m[1][0]) != 1)
        {
            // setting the captured square to 0;
            board1[(m[0][0]+m[1][0])/2][(m[0][1]+m[1][1]) / 2] = 0;
            // checking for all possible double jump permutations
            var moreBoards = possibleContinues(board1, m[1][0], m[1][1]);
            for (var x = 0; x < moreBoards.length; x++)
            {
                allBoards.push(moreBoards[x]);
            }
        }
        // if it wasn't a capture, then we're done. Simple, elegant
        else
        {
            allBoards.push(board1);
        }
    }
    return allBoards;
}

/*
This function sets up a new board of size 8x8.
Fills first three rows with player 1 pieces alternating diagonally.
Fills last three rows with other player's pieces alternating diagonally.
Leaves two rows blank.
Returns the new board array.
*/

function setUpBoard(){
    var board = [];
    
    // iterate over each row index
    for (var i = 0; i < 8; i++)
    {
        // create array for each new row
        var newRow = [];
        // iterate over each column index
        for (var x = 0; x < 8; x++)
        {
            // if first three rows
            if (i < 3)
            {
                // every other cell, alternating diagonally
                if ((x + i % 2) % 2 == 0)
                {
                    // add value 1 to new row
                    newRow.push(1);
                }
                // should be empty
                else
                {
                    // add value 0 to new row
                    newRow.push(0);
                }
            }
            // if after first five rows (skip 2 rows)
            else if (i > 4)
            {
                // every other cell, alternating diagonally
                if ((x + i % 2) % 2 == 0)
                {
                    // add value 3 to new row
                    newRow.push(3);
                }
                // should be empty
                else
                {
                    // add value 0 to new row
                    newRow.push(0);
                }
            }
            // 2 empty rows in middle
            else
            {
                // add value 0 to new row
                newRow.push(0);
            }
        }
        // add new row to the board
        board.push(newRow);
    }
    // return the board
    return board;
}

/*
From a board state of a recent capture, permutates over all possible double, triple ...
jump possiblities to return a complete board list
*/

function possibleContinues(board, row, col)
{
    var continues = [];
    var hasContinues = false;
    // value of pieces you can "capture", opposite of player val
    var opp = 0;
    var curVal = board[row][col];
    if (curVal == 1 || curVal == 2)
    {
        opp = 3;
    }
    else
    {
        opp = 1;
    }
    // in this case we're messing with a king
    if (curVal % 2 == 0)
    {
        // making sure what I'm doing won't throw errors
        // king, so it can attack in either direction
        if (row - 2 >= 0)
        {
            if (col - 2 >= 0)
            {
                if ((board[row - 1][col - 1] == opp || board[row - 1][col - 1] == opp + 1) && board[row - 2][col - 2] == 0)
                {
                    // found a possible capture
                    hasContinues = true;
                    var tempCopy = copyBoard(board);
                    // change board to reflect move
                    tempCopy[row][col] = 0;
                    tempCopy[row - 1][col - 1] = 0;
                    tempCopy[row - 2][col - 2] = curVal;
                    // check for any more jumping possibilities from new position
                    var temps = possibleContinues(tempCopy, row - 2, col - 2);
                    // add all permutations
                    for (var i = 0; i < temps.length; i++)
                    {
                        continues.push(temps[i]);
                    }
                }
            }
            // same idea as above, except we're checking the diagonal in the opposite direction
            if (col + 2 <= 7)
            {
                if ((board[row - 1][col + 1] == opp || board[row - 1][col + 1] == opp + 1) && board[row - 2][col + 2] == 0)
                {
                    hasContinues = true;
                    var tempCopy = copyBoard(board);
                    tempCopy[row][col] = 0;
                    tempCopy[row - 1][col + 1] = 0;
                    tempCopy[row - 2][col + 2] = curVal;
                    var temps = possibleContinues(tempCopy, row - 2, col + 2);
                    for (var i = 0; i < temps.length; i++)
                    {
                        continues.push(temps[i]);
                    }
                }
                
            }
        }
        // same as above, just checking in different direction
        if (row + 2 <= 7)
        {
            if (col - 2 >= 0)
            {
                if ((board[row + 1][col - 1] == opp || board[row + 1][col - 1] == opp + 1) && board[row + 2][col - 2] == 0)
                {
                    hasContinues = true;
                    var tempCopy = copyBoard(board);
                    tempCopy[row][col] = 0;
                    tempCopy[row + 1][col - 1] = 0;
                    tempCopy[row + 2][col - 2] = curVal;
                    var temps = possibleContinues(tempCopy, row + 2, col - 2);
                    for (var i = 0; i < temps.length; i++)
                    {
                        continues.push(temps[i]);
                    }
                }
            }
            if (col + 2 <= 7)
            {
                if ((board[row + 1][col + 1] == opp || board[row + 1][col + 1] == opp + 1) && board[row + 2][col + 2] == 0)
                {
                    hasContinues = true;
                    var tempCopy = copyBoard(board);
                    tempCopy[row][col] = 0;
                    tempCopy[row + 1][col + 1] = 0;
                    tempCopy[row + 2][col + 2] = curVal;
                    var temps = possibleContinues(tempCopy, row + 2, col + 2);
                    for (var i = 0; i < temps.length; i++)
                    {
                        continues.push(temps[i]);
                    }
                }
                
            }
        }
    }
    // so we're not dealing with a king 
    else
    {
        if (curVal == 1)
        {
            // its a regular player 1 piece, so it can only attack "downboard"
            // ie to a point with a higher row value
            if (row + 2 <= 7)
            {
                if (col - 2 >= 0)
                {
                    if ((board[row + 1][col - 1] == opp || board[row + 1][col - 1] == opp + 1) && board[row + 2][col - 2] == 0)
                    {
                        hasContinues = true;
                        var tempCopy = copyBoard(board);
                        tempCopy[row][col] = 0;
                        tempCopy[row + 1][col - 1] = 0;
                        tempCopy[row + 2][col - 2] = curVal;
                        // making a king, if necessary
                        if (row + 2 == 7)
                        {
                            tempCopy[row + 2][col - 2] = 2;
                        }
                        var temps = possibleContinues(tempCopy, row + 2, col - 2);
                        for (var i = 0; i < temps.length; i++)
                        {
                            continues.push(temps[i]);
                        }
                    }
                }
                if (col + 2 <= 7)
                {
                    if ((board[row + 1][col + 1] == opp || board[row + 1][col + 1] == opp + 1) && board[row + 2][col + 2] == 0)
                    {
                        hasContinues = true;
                        var tempCopy = copyBoard(board);
                        tempCopy[row][col] = 0;
                        tempCopy[row + 1][col + 1] = 0;
                        tempCopy[row + 2][col + 2] = curVal;
                        // making a king, if necessary
                        if (row + 2 == 7)
                        {
                            tempCopy[row + 2][col + 2] = 2;
                        }
                        var temps = possibleContinues(tempCopy, row + 2, col + 2);
                        for (var i = 0; i < temps.length; i++)
                        {
                            continues.push(temps[i]);
                        }
                    }
                
                }
            }
        }
        else
        {
            // player 2 regular piece, so it can only attack upboard, ie to lower row value
            if (row - 2 >= 0)
            {
                if (col - 2 >= 0)
                {
                    if ((board[row - 1][col - 1] == opp || board[row - 1][col - 1] == opp + 1) && board[row - 2][col - 2] == 0)
                    {
                        hasContinues = true;
                        var tempCopy = copyBoard(board);
                        tempCopy[row][col] = 0;
                        tempCopy[row - 1][col - 1] = 0;
                        tempCopy[row - 2][col - 2] = curVal;
                        // making a king, if necessary
                        if (row - 2 == 0)
                        {
                            tempCopy[row - 2][col - 2] = 4;
                        }
                        var temps = possibleContinues(tempCopy, row - 2, col - 2);
                        for (var i = 0; i < temps.length; i++)
                        {
                            continues.push(temps[i]);
                        }
                    }
                }
                if (col + 2 <= 7)
                {
                    if ((board[row - 1][col + 1] == opp || board[row - 1][col + 1] == opp + 1) && board[row - 2][col + 2] == 0)
                    {
                        hasContinues = true;
                        var tempCopy = copyBoard(board);
                        tempCopy[row][col] = 0;
                        tempCopy[row - 1][col + 1] = 0;
                        tempCopy[row - 2][col + 2] = curVal;
                        // making a king, if necessary
                        if (row - 2 == 0)
                        {
                            tempCopy[row - 2][col + 2] = 4;
                        }
                        var temps = possibleContinues(tempCopy, row - 2, col + 2);
                        for (var i = 0; i < temps.length; i++)
                        {
                            continues.push(temps[i]);
                        }
                    }
                }
            }
        }
    }
    // base case, static scenario
    if (hasContinues == false)
    {
        continues.push(board);
    }
    return continues;
}

/*
AI choosing best move (returned as board state) based on minMax
*/

function chooseMove(board, depth, player)
{
    // retrieve all boardstates after one move
    var boardStates = getPossibleBoards(board, player);
    // no more moves
    if (boardStates.length == 0)
    {
        return false;
    }
    // there's only one move to choose, so we choose it
    if (boardStates.length == 1)
    {
        return boardStates[0];
    }
    var bestBoardIndex = 0;
    var bestBoardVal;
    if (player == 1)
    {
        bestBoardVal = -10000;
    }
    else
    {
        bestBoardVal = 10000;
    }
    for (var i = 0; i < boardStates.length; i++)
    {
        if (player == 1)
        {
            var temp = minMax(boardStates[i], depth, 2, -10000, 10000);
            if (temp > bestBoardVal){
                bestBoardIndex = i;
                bestBoardVal = temp;
            }
        }
        else
        {
            var temp = minMax(boardStates[i], depth, 1, -10000, 10000);
            if (temp < bestBoardVal)
            {
                bestBoardIndex = i;
                bestBoardVal = temp;
            }
        }
    }
    return boardStates[bestBoardIndex];
}

/*
This function returns a player's value based on the number
of pieces on the board, with kings weighted more heavily.
*/

function getValue(board)
{
    // set total value to 0 to strt
    var totalValue = 0;
    // if other player has one, return huge negative number 
    if (hasWon(board) == 2)
    {
        return -800;
    }
    // if player 1 has won, return huge positive number
    else if (hasWon(board) == 1)
    {
        return 800;
    }
    // loop over each cell, adding each piece found to total
    // iterate over each row index
    for (var i = 0; i < board.length; i++)
    {
        // iterate over each column index
        for (var x = 0; x < board.length; x++)
        {
            // if player 1 regular piece
            if (board[i][x] == 1)
            {
                totalValue += 5;
            }
            // if player 1 king
            else if (board[i][x] == 2)
            {
                totalValue += 5;
            }
            // if other player regular piece
            else if (board[i][x] == 3)
            {
                totalValue = totalValue - 5;
            }
            // if other player king
            else if (board[i][x] == 4)
            {
                totalValue = totalValue - 5;
            }
        }
    }
    // return total
    return totalValue;
}



/*
This function completes a move on the board.
It takes as inputs the current board, the player moving,
the start index, and the finish index.
Returns updated board.
*/

function makeMove(board, player, start, finish)
{
    // make copy of the present board
    var boardTemp = copyBoard(board);
    // check that move is legal
    // moving checker piece from prev square to new square
    // set temp to original value at start position
    var temp = boardTemp[start[0]][start[1]];
    // set boardTemp at start position to empty space (0)
    boardTemp[start[0]][start[1]] = 0;
    // set boardTemp at finish position to new value
    boardTemp[finish[0]][finish[1]] = temp;
    
    // if its a capture, must set intervening square to 0
    // if a hop (more than one diagonal move)
    if (Math.abs(start[0]-finish[0]) != 1)
    {
        // set intervening cell to 0 since capture
        boardTemp[(start[0]+finish[0])/2][(start[1]+finish[1])/2] = 0;
    }
    
    // checking to see if move made a king for either team
    // temp is value of piece we started the move with (recall)
    // for player 1
    if (finish[0] == boardTemp.length - 1 && temp == 1)
    {
        // make king for player 1
        boardTemp[finish[0]][finish[1]] = 2;
    }
    // for other player
    else if (finish[0] == 0 && temp == 3)
    {
        // make king for other player
        boardTemp[finish[0]][finish[1]] = 4;
    }
    // and we're done
    
    // sets one second timeout
    setTimeout(function(){  }, 1000);
    
    return boardTemp;
}

/*
Assigns an integer value to each board state by assuming best play by oppenent
The more negative the better for player 2, the more positive the better player 1
alpha is the greatest lower bound on results that are still "viable", whereas
beta is the lowest upper bound on results that we're interested in, so if that interval
becomes empty then we're done. see https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
*/

function minMax(board, depth, player, alpha, beta)
{
    // base case, use static evaluation function
    if (depth == 0)
    {
        return getValue(board);
    }
    // retrieve all board states to recurse over
    var boardStates = getPossibleBoards(board, player);
    if (player == 1)
    {
        for (var i = 0; i < boardStates.length; i++)
        {
            // so I don't make the call twice
            var temp = minMax(boardStates[i], depth - 1, 2, alpha, beta);
            // alpha is the greatest lower bound for solutions that we're interested in 
            // track the greatest value we've found so far
            if (temp > alpha)
            {
                alpha = temp;
            }
            // there's no longer an interesection an interesection between lub and glb, so we
            // can stop searching this "limb"
            if (alpha >= beta)
            {
                break;
            }
        }
        return alpha;
    }
    else
    {
        for (var i = 0; i < boardStates.length; i++)
        {
            var temp = minMax(boardStates[i], depth - 1, 1, alpha, beta);
            // beta is least upper bound of solutions we're interest in
            // tracks least solution found so far
            if (temp < beta)
            {
                beta = temp;
            }
            // there's no longer an interesection an interesection between lub and glb, so we
            // can stop searching this "limb"
            if (beta <= alpha)
            {
                break;
            }
        }
        return beta;
    }
    
}

/*
This function makes a copy of the board row by row.
It returns the copy of the board.
*/ 

function copyBoard(board){
    // declare empty array to copy into
    var copy = [];
    // iterate over each row index
    for (var i = 0; i < board.length; i++)
    {
        // declare empty array for each row
        var tempRow = [];
        // iterate over each column index
        for (var x = 0; x < board[i].length; x++)
        {
            // populate temp rwo with the board's values
            tempRow.push(board[i][x]);
        }
        // add row to copy array
        copy.push(tempRow);
    }
    // and we're done
    return copy;
}


// starting here, sign in code

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });
  }

document.getElementById('username').value = onSignIn(googleUser);
function onSignIn(googleUser) {
  var profile = googleUser.getBasicProfile();
  return(profile.getName());
}


function onSuccess(googleUser) {
      console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
}
function onFailure(error) {
      console.log(error);
}

function renderButton() {
    gapi.signin2.render('my-signin2',
    {
        'scope': 'profile email',
        'width': 240,
        'height': 50,
        'longtitle': true,
        'theme': 'dark',
        'onsuccess': onSuccess,
        'onfailure': onFailure
    });
}
