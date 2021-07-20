package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("CS 4200 Project 1");

        boolean keepRunning = true;

        /*// gather info here if wanted
        Heuristic heuristic = Heuristic.H2;
        int casesToRun = 100;
        int depth = 4;
        int rows = 3;
        int cols = 3;
        SlidePuzzleDataGatherer.analyzeSlidePuzzleSolving(heuristic, casesToRun, depth, rows, cols);
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
                    RunSingleTestPuzzle();
                    break;
                case 3:
                    keepRunning = false;
                    System.out.println("Thank you for using my A* slider puzzle.");
                    break;
            }
        }
    }

    /**
     * Runs a single test puzzle for a slider puzzle
     */
    public static void RunSingleTestPuzzle()
    {
        int[][] initialState = getFirstState();
        // print initial state

        System.out.println(Helpers.arrayToString(initialState));

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

        printSolvedPath(solvedPath);
        System.out.println("Search Cost: " + sliderPuzzleQueue.getTotalNodesAdded());
    }

    /**
     * gets the first node for the program
     * @return the first state of a slide puzzle
     */
    public static int[][] getFirstState()
    {
        // get the user selection for how to generate first state
        String message = "Select input Method\n";
        message += "[1] Random\n";
        message += "[2] File";
        int selection = Helpers.handleIntegerInput(message, 1, 2);

        // TODO: make the cols and rows dynamic from getFirstState params
        int[][] firstState = new int[3][3];

        // either get init state from user or generate a random one
        boolean validState = false;
        while(!validState)
        {
            switch(selection)
            {
                // generate a random state
                case 1:
                    // TODO: make depth a user entry
                    firstState = Helpers.generateRandomPuzzle(3, 3, 4);
                    validState = Helpers.isValidState(firstState);
                    break;
                // ask user for a initial state
                case 2:
                    System.out.println("Please enter the initial state:");
                    firstState = Helpers.getInitialStateFromUser(3, 3);
                    validState = Helpers.isValidState(firstState);
                    if(!validState)
                    {
                        System.out.println("Please enter a solvable slide puzzle.");
                    }
                    break;
            }
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
        String message = "Select H function\n";
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


