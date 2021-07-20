package com.company;

import java.util.ArrayList;

// holds node information for the specific state of hte 8 puzzle with a given heuristic
public class Node
{
    // holds current state and heuristic type
    int[][] state;
    Heuristic heuristic;
    // holds heuristic values
    int costSoFar;
    public int estimatedTotalCost;

    Node parentNode;

    public boolean isSolved = false;

    /**
     * The Node class holds information regarding a node for the slide puzzle
     *
     * @param state holds the state of the puzzle at this node
     * @param heuristic the type of heuristic for this puzzle to follow
     * @param cost_of_parent how many steps the parent node has taken
     * @param parentNode the node that generated this node
     * */
    public Node(int[][] state, Heuristic heuristic, int cost_of_parent, Node parentNode)
    {
        this.state = state;
        this.heuristic = heuristic;
        this.costSoFar = cost_of_parent + 1;
        this.parentNode = parentNode;
        this.calculateEstimateTotalCost();
    }

    /**
     * Takes the current node then generates a list of possible next states
     * @return a list of possible child Nodes
     */
    public ArrayList<Node> generateChildNodes()
    {
        ArrayList<Node> childNodes = new ArrayList<Node>();

        // need to find where the 0 is
        int zeroindex = 0;
        int rows = this.state.length;
        int cols = this.state[0].length;

        // loops through array
        for(int i = 0; i < rows * cols; i++)
        {
            // if outer loop value is 0 we store index
            if (state[i / cols][i % cols] == 0)
            {
                zeroindex = i;
                break;
            }
        }

        // get zeroIndex row and col
        int zeroIndexRow = zeroindex / cols;
        int zeroIndexCol = zeroindex % cols;

        // switch with left number
        if(zeroIndexCol >= 1)
        {
            // copy the child array
            int[][] childState = this.copyNodeState();

            // set current zero pos to number to left then set the number to left to 0
            childState[zeroIndexRow][zeroIndexCol] = childState[zeroIndexRow][zeroIndexCol - 1];
            childState[zeroIndexRow][zeroIndexCol - 1] = 0;

            // create child node and add to list
            Node newNode = new Node(childState, this.heuristic, this.costSoFar, this);
            childNodes.add(newNode);
        }

        // switch with right number
        if(zeroIndexCol < cols - 1)
        {
            // copy the child array
            int[][] childState = this.copyNodeState();

            // set current zero pos to number to right then set the number to right to 0
            childState[zeroIndexRow][zeroIndexCol] = childState[zeroIndexRow][zeroIndexCol + 1];
            childState[zeroIndexRow][zeroIndexCol + 1] = 0;

            // create child node and add to list
            Node newNode = new Node(childState, this.heuristic, this.costSoFar, this);
            childNodes.add(newNode);
        }

        // switch with above number
        if(zeroIndexRow >= 1)
        {
            // copy the child array
            int[][] childState = this.copyNodeState();

            // set current zero pos to number above then set the number above to 0
            childState[zeroIndexRow][zeroIndexCol] = childState[zeroIndexRow - 1][zeroIndexCol];
            childState[zeroIndexRow - 1][zeroIndexCol] = 0;

            // create child node and add to list
            Node newNode = new Node(childState, this.heuristic, this.costSoFar, this);

            childNodes.add(newNode);
        }

        // switch with below number
        if(zeroIndexRow < rows - 1)
        {
            // copy the child array
            int[][] childState = this.copyNodeState();

            // set current zero pos to number below then set the number below to 0
            childState[zeroIndexRow][zeroIndexCol] = childState[zeroIndexRow + 1][zeroIndexCol];
            childState[zeroIndexRow + 1][zeroIndexCol] = 0;

            // create child node and add to list
            Node newNode = new Node(childState, this.heuristic, this.costSoFar, this);
            childNodes.add(newNode);
        }

        return childNodes;
    }

    /**
     * Copies the nodes state
     * @return a 2d array containing the parent nodes state
     */
    int[][] copyNodeState()
    {
        // copy the current state
        int[][] state = new int[this.state.length][this.state[0].length];
        for(int i = 0; i < this.state.length; i++)
            for(int j = 0; j < this.state[0].length; j++)
                state[i][j] = this.state[i][j];

        return state;
    }

    /**
     * Estimates the total cost of a given state based on the heuristic
     * @return int denoting total estimate cost
     */
    void calculateEstimateTotalCost()
    {
        int estimated_cost = 0;

        // heuristic H1 checks for misplaced tiles
        if(this.heuristic == Heuristic.H1)
        {
            int rows = this.state.length;
            int cols = this.state[0].length;

            // loops through array to determine if a tile is misplaced
            for(int i = 0; i < rows * cols; i++)
            {
                // if outer loop value is 0 we don't care, skip it
                if(state[i/cols][i%cols] == 0)
                    continue;

                // if the tile at index i does not equal i then it is misplaced
                if(state[i/cols][i%cols] != i)
                    estimated_cost += 1;
            }
        }

        // heuristic H2 checks how far all collective tiles are from their destination
        else if(this.heuristic == Heuristic.H2)
        {
            int rows = this.state.length;
            int cols = this.state[0].length;

            // loops through array and checks for distance from where it should be
            for(int i = 0; i < rows * cols; i++)
            {
                int indexValue = state[i / cols][i % cols];
                // if outer loop value is 0 we don't care, skip it
                if (indexValue == 0)
                    continue;

                // determine how many columns it is offset
                estimated_cost += Math.abs((i % cols) - (indexValue % cols));

                // determine how many rows it is offset by
                estimated_cost += Math.abs((i / cols) - (indexValue / cols));
            }
        }

        // check if the puzzle is solved by seeing if it is not a zero heuristic
        if (estimated_cost == 0)
            this.isSolved = true;

        this.estimatedTotalCost = estimated_cost + this.costSoFar;
    }

    /**
     * Checks if two boards are the same
     * @param node board to check equivalency against
     * @return true if same board and false if not
     */
    public boolean isEqualTo(Node node)
    {
        for(int i = 0; i < this.state.length; i++)
            for(int j = 0; j < this.state[0].length; j++)
            {
                // if any part of the board is different return false
                if(this.state[i][j] != node.state[i][j])
                {
                    return false;
                }
            }
        // return true
        return true;
    }

    /**
     * returns the state in string form
     * */
    public String toString()
    {
        String result = "";
        // turn the equivalent array to a string
        for(int i = 0; i < this.state.length; i++)
        {
            for(int j = 0; j < this.state[0].length; j++)
            {
                result += this.state[i][j] + " ";
            }
            result += "\n";
        }

        // return the result
        return result;
    }
}
