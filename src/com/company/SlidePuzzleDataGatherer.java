package com.company;

import java.time.Instant;
import java.util.ArrayList;

public class SlidePuzzleDataGatherer
{
    // TODO handle states generated that are the same so keep a log of originally generated states
    /**
     * Calculates average run time and nodes generated over cases_to_run using the specified Heuristic
     * @param heuristic which heuristic to use
     * @param casesToRun how many times to run
     * @param depth how many moves to solve the given puzzle
     * @param rows number of rows in the puzzle
     * @param cols number of cols in the puzzle
     */
    public static void analyzeSlidePuzzleSolving(Heuristic heuristic, int casesToRun, int depth, int rows, int cols)
    {
        int totalNodesGenerated = 0;
        long totalTimeSpentInMs = 0;
        int cases = casesToRun;

        // go through every case
        while(casesToRun > 0)
        {
            boolean validState = false;
            int[][] firstState = new int[rows][cols];

            // generate a solvable puzzle
            while(!validState)
            {
                firstState = Helpers.generateRandomPuzzle(rows, cols, depth);
                validState = Helpers.isValidState(firstState);
            }

            // create the queue
            SlidePuzzleQueue sliderPuzzleQueue = new SlidePuzzleQueue();
            //array list to hold all seen nodes
            ArrayList<Node> nodesSeen = new ArrayList<Node>();

            // solving of slide puzzle starts here
            long startTime = Instant.now().toEpochMilli();

            // create base node with initial state
            Node baseNode = new Node(firstState, heuristic, -1, null);
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

            // solving of slide puzzle ends here
            long endTime = Instant.now().toEpochMilli();

            // get the solved path of the node
            ArrayList<Node> solvedPath = Helpers.getSolvedPath(curNode);

            // if this was a puzzle with the correct depth
            if(solvedPath.size() - 1 == depth)
            {
                casesToRun -= 1;
                totalNodesGenerated += sliderPuzzleQueue.getTotalNodesAdded();
                totalTimeSpentInMs += (endTime - startTime);
            }
        }

        // get average time and average nodes generated
        float averageTime = (float)totalTimeSpentInMs / cases;
        float averageNodesGenerated = (float)totalNodesGenerated / cases;

        //print out analytics
        String heuristicString = heuristic == Heuristic.H1 ? "H1" : "H2";
        String message = "Testing " + cases + " cases at depth of: " + depth + " using Heuristic: " + heuristicString +" \n";
        message += "Total nodes generated: " + totalNodesGenerated + "\n";
        message += "Average nodes generated: " + averageNodesGenerated + "\n";
        message += "Total time to run: " + totalTimeSpentInMs + "ms\n";
        message += "Average time to run: " + averageTime + "ms\n";
        System.out.println(message);
    }
}

/* Data points needed
   Average nodes generated for H1 and H2
   Time to complete on average
   number of cases at length
 */