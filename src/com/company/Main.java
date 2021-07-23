package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("CS 4200 Project 1");

        boolean keepRunning = true;
        int rows = 3;
        int cols = 3;

        /*// gather info here if wanted
        Heuristic heuristic = Heuristic.H2;
        int casesToRun = 1000;
        int depth = 16;
        SlidePuzzleDataGatherer.analyzeSlidePuzzleSolving(heuristic, casesToRun, depth, rows, cols);
        // turn off keep running as we just want to gather data nothing else
        keepRunning = false;
        /**/

        while(keepRunning)
        {
            // get selection
            String message = "Select:\n";
            message += "[1] Single Test Puzzle\n";
            message += "[2] Multi-Test Puzzle:\n";
            message += "[3] Exit:";
            int selection = Helpers.handleIntegerInput(message, 1, 3);

            // handle initial selection
            switch(selection)
            {
                case 1:
                    // run single test puzzle here
                    RunSingleTestPuzzle(rows, cols);
                    break;
                case 3:
                    keepRunning = false;
                    System.out.println("Thank you for using my A* slider puzzle.");
                    break;
            }
        }
    }

    /**
     * @param rows number of rows in the puzzle to create
     * @param cols number of cols in puzzle to create
     * Runs a single test puzzle for a slider puzzle
     */
    public static void RunSingleTestPuzzle(int rows, int cols)
    {
        // works for any rows * cols except 1 x 1 because the random generation will infinitely loop
        int[][] initialState = getFirstState(rows, cols);
        // print initial state

        System.out.println("Puzzle:\n" + Helpers.arrayToString(initialState));

        // create the queue
        SlidePuzzleQueue sliderPuzzleQueue = new SlidePuzzleQueue();
        //array list to hold all seen nodes
        ArrayList<Node> nodesSeen = new ArrayList<Node>();

        // create base node with initial state
        Node baseNode = new Node(initialState, getHeuristicStyle(), -1, null);
        Node curNode = null;
        sliderPuzzleQueue.addNode(baseNode);
        nodesSeen.add(baseNode);

        boolean solutionFound = false;

        // keep running until a solution is found
        while(!solutionFound)
        {
            curNode = sliderPuzzleQueue.popNode();

            // check if we recieved null as first node meaning no nodes left to check
            if(curNode == null)
            {
                System.out.println("Solution not found");
                return;
            }

            // if we pop a solved node then we solved our puzzle
            if(curNode.isSolved)
            {
                solutionFound = true;
                continue;
            }

            // get the child nodes
            ArrayList<Node> childNodes = curNode.generateChildNodes();
            for(int i = 0; i < childNodes.size(); i++)
            {
                boolean alreadyExists = false;
                // check if we have seen the node before
                for (int j = 0; j < nodesSeen.size(); j++)
                {
                    if(nodesSeen.get(j).isEqualTo(childNodes.get(i)))
                    {
                        alreadyExists = true;
                        break;
                    }
                }

                // add all new nodes to the queue
                if(!alreadyExists)
                {
                    nodesSeen.add(childNodes.get(i));
                    sliderPuzzleQueue.addNode(childNodes.get(i));
                }
            }
        }

        // get the solved path of the node
        ArrayList<Node> solvedPath = Helpers.getSolvedPath(curNode);

        System.out.println("Solution Found");
        printSolvedPath(solvedPath);
        System.out.println("Search Cost: " + sliderPuzzleQueue.getTotalNodesAdded());
    }

    /**
     * gets the first node for the program
     * @param rows number of rows for first state
     * @param cols number of cols for first state
     * @return the first state of a slide puzzle
     */
    public static int[][] getFirstState(int rows, int cols)
    {
        // get the user selection for how to generate first state
        String message = "Select input Method\n";
        message += "[1] Random\n";
        message += "[2] File";
        int selection = Helpers.handleIntegerInput(message, 1, 2);

        int[][] firstState = new int[rows][cols];

        // either get init state from user or generate a random one
        boolean validState = false;
        switch(selection)
        {
            // generate a random state
            case 1:
                // get depth from user
                message = "Enter Solution Depth (2-20):";
                int depth = Helpers.handleIntegerInput(message, 2, 20);

                while(!validState)
                {
                    firstState = Helpers.generateRandomPuzzle(rows, cols, depth);
                    validState = Helpers.isValidState(firstState);
                }
                break;
            // ask user for a initial state
            case 2:
                while(!validState)
                {
                    System.out.println("Please enter the initial state:");
                    firstState = Helpers.getInitialStateFromUser(rows, cols);
                    validState = Helpers.isValidState(firstState);
                    if(!validState)
                    {
                        System.out.println("Please enter a solvable slide puzzle.");
                    }
                }
                // print an empty line
                System.out.println();
                break;
        }

        // return initial state
        return firstState;
    }

    /**
     * gets the heuristic style the user wants
     * @return Heuristic enum
     */
    public static Heuristic getHeuristicStyle()
    {
        // read the heuristic wanted by user
        String message = "Select H function:\n";
        message += "[1] H1\n";
        message += "[2] H2";
        int selection = Helpers.handleIntegerInput(message, 1, 2);

        switch(selection)
        {
            case 1:
                return Heuristic.H1;
            case 2:
                return Heuristic.H2;
        }

        return Heuristic.H1;
    }

    /**
     * prints the complete solved route for the puzzle
     * @param solvedPath the list of nodes that took us from start to finish
     */
    public static void printSolvedPath(ArrayList<Node> solvedPath)
    {
        // skip initial state so start at 1
        for(int i = 1; i < solvedPath.size(); i++)
        {
            System.out.println("Step: " + i);
            System.out.println(solvedPath.get(i).toString());
        }
    }
}

// heuristic for determining type of sort
enum Heuristic
{
    H1,
    H2
}


