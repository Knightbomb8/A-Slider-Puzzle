package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Helpers
{
    /**
     * Given the solution node return an ordered array list of steps to get there
     * @param solutionNode the node with the solved state
     * @return ArrayList<Node> with all nodes that it took to reach solutionNode
     */
    public static ArrayList<Node> getSolvedPath(Node solutionNode)
    {
        ArrayList<Node> solvedPath = new ArrayList<Node>();
        // go all the way back up through the base node
        while(solutionNode != null)
        {
            solvedPath.add(0, solutionNode);
            solutionNode = solutionNode.parentNode;
        }
        return solvedPath;
    }

    // TODO: this doesn't work as well as I expected, rework
    /**
     * Generates a random slide puzzle not guaranteed to be solvable
     * @param rows number of rows for the generated puzzle
     * @param cols number of cols for the generated puzzle
     * @return int[][] puzzle
     */
    public static int[][] generateRandomPuzzle(int rows, int cols, int depth)
    {
        Random rand = new Random();

        // generate a random puzzle with certain depth
        int[][] depthPuzzle = new int[rows][cols];
        //set default values
        for(int i = 0; i < cols * rows; i++)
        {
            depthPuzzle[i / cols][i % cols] = i;
        }

        int zeroIndexRow = 0;
        int zeroIndexCol = 0;
        int previousMovement = -1;
        // move the puzzle while depth has not reached zero
        while(depth > 0)
        {
            //generate number 0->3
            int direction = rand.nextInt(4);
            switch (direction)
            {
                // left
                case 0:
                    // if we just moved right or not a possible movement
                    if(previousMovement == 1 || zeroIndexCol < 1)
                        continue;

                    // switch the numbers
                    depthPuzzle[zeroIndexRow][zeroIndexCol] = depthPuzzle[zeroIndexRow][zeroIndexCol - 1];
                    depthPuzzle[zeroIndexRow][zeroIndexCol - 1] = 0;

                    // update where the zer0(empty tile is)
                    zeroIndexCol -= 1;

                    previousMovement = direction;
                    break;

                // right
                case 1:
                    // if we just moved left or not a possible movement
                    if(previousMovement == 0 || zeroIndexCol >= cols - 1)
                        continue;

                    depthPuzzle[zeroIndexRow][zeroIndexCol] = depthPuzzle[zeroIndexRow][zeroIndexCol + 1];
                    depthPuzzle[zeroIndexRow][zeroIndexCol + 1] = 0;

                    // update where the zer0(empty tile is)
                    zeroIndexCol += 1;

                    previousMovement = direction;
                    break;

                // up
                case 2:
                    // if we just moved down or not possible movement
                    if(previousMovement == 3 || zeroIndexRow < 1)
                        continue;

                    depthPuzzle[zeroIndexRow][zeroIndexCol] = depthPuzzle[zeroIndexRow - 1][zeroIndexCol];
                    depthPuzzle[zeroIndexRow - 1][zeroIndexCol] = 0;

                    // update where the zer0(empty tile is)
                    zeroIndexRow -= 1;

                    previousMovement = direction;
                    break;

                // down
                case 3:
                    // if we just moved up or if the movement is not possible
                    if(previousMovement == 2 || zeroIndexRow >= rows - 1)
                        continue;

                    depthPuzzle[zeroIndexRow][zeroIndexCol] = depthPuzzle[zeroIndexRow + 1][zeroIndexCol];
                    depthPuzzle[zeroIndexRow + 1][zeroIndexCol] = 0;

                    // update where the zer0(empty tile is)
                    zeroIndexRow += 1;

                    previousMovement = direction;
                    break;
            }

            depth -= 1;
        }

        return depthPuzzle;
    }

    /**
     * Handles the input of an integer with error checking
     * @param message the message to display before asking for input
     * @param min minimum int allowed
     * @param max maximum int allowed
     * @return int between min and max inclusive
     */
    public static int handleIntegerInput(String message, int min, int max)
    {
        // store initial value, scanner, and whether or not the int has been found
        int answer = -1;
        Scanner scanner = new Scanner(System.in);
        boolean intFound = false;

        while(!intFound)
        {
            // print the message requesting input
            System.out.println(message);
            // try to get the input
            try
            {
                int temp_answer = scanner.nextInt();
                // check if the input is in the allowed range
                if(temp_answer >= min && temp_answer <= max)
                {
                    intFound = true;
                    answer = temp_answer;
                }
                else
                {
                    System.out.println("Number entered not an acceptable input. Try again");
                }
            }
            // catch and do nothing
            catch (Exception e)
            {
                System.out.println("Invalid Input. Try again");
                scanner.nextLine();
            }
        }

        return answer;
    }

    /**
     * Determines validity of state
     * @return boolean denoting if state is valid(true) or invalid(false)
     */
    public static boolean isValidState(int[][] state)
    {
        // counter for number of inverted tiles
        int invertedTiles = 0;

        int rows = state.length;
        int cols = state[0].length;

        // loop through the whole array to determine how many larger numbers appear before smaller ones
        // turn the equivalent array to a string
        for(int i = 0; i < rows * cols; i++)
        {
            // if outer loop value is 0 we don't care, skip it
            if(state[i/cols][i%cols] == 0)
                continue;

            for(int j = i + 1; j < rows * cols; j++)
            {
                // check if inner loop value isn't 0
                // check if inner loops value is less than outer loop meaning it is misplaced
                if(state[j/cols][j%cols] != 0 && state[j/cols][j%cols] < state[i/cols][i%cols])
                    invertedTiles += 1;
            }
        }

        // if even return true, otherwise false
        return invertedTiles % 2 == 0 ? true : false;
    }

    /**
     * prints out a 2d array
     * @param arrayToPrint the array to print
     * @return the string version of array
     */
    public static String arrayToString(int[][] arrayToPrint)
    {
        String result = "";
        for(int i = 0; i < arrayToPrint.length; i++)
        {
            for(int j = 0; j < arrayToPrint[0].length; j++)
            {
                result += arrayToPrint[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }

    /**
     * gets initial state from the user
     * @param rows number of rows expected from user entry
     * @param cols number of cols expected from user entry
     * @return the initial state as a 2d int array
     */
    public static int[][] getInitialStateFromUser(int rows, int cols)
    {
        int[][] initialState = new int[rows][cols];
        Scanner scanner = new Scanner(System.in);

        // bool to determine when input has been properly received
        boolean inputReceived = false;

        // get string from user of initial state
        while(!inputReceived)
        {
            // try and get the input
            try
            {
                // get input for each row
                for(int i = 0; i < rows; i++)
                {
                    for(int j = 0; j < cols; j++)
                    {
                        // grab each number
                        int input = scanner.nextInt();

                        // place each number in the valid spot
                        initialState[i][j] = input;
                    }
                }

                inputReceived = true;

                // see if any numbers repeat or are past the allowable amount
                for(int i = 0; i < rows * cols; i++)
                {
                    // if the number is out of bounds of allowed numbers
                    int indexValue = initialState[i/cols][i%cols];
                    if(indexValue < 0 || indexValue >= rows * cols)
                    {
                        System.out.println("Slide tile inputted with invalid value: " + indexValue + ". Please enter a valid puzzle");
                        inputReceived = false;
                        break;
                    }
                    for (int j = i + 1; j < rows * cols; j++)
                    {
                        // if there is a duplicate value continue as well
                        if(indexValue == initialState[j/cols][j%cols])
                        {
                            System.out.println("Duplicate slide value of: " + indexValue + " entered. Please enter a valid puzzle");
                            inputReceived = false;
                            break;
                        }
                    }
                }
            }
            // otherwise notify user and try again
            catch (Exception exception)
            {
                System.out.println("Invalid Input. Try again");
                scanner.nextLine();
            }
        }

        // return initial state
        return initialState;
    }
}
