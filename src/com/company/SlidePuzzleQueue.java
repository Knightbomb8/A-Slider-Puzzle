package com.company;

import java.util.ArrayList;

public class SlidePuzzleQueue
{
    ArrayList<Node> nodeList = new ArrayList<Node>();
    int nodesAdded = 0;

    /**
     * adds a node to the array queue
     * @param newNode node to add
     */
    public void addNode(Node newNode)
    {
        // iterate through all known nodes
        int insertIndex = nodeList.size();
        for(int i = 0; i < nodeList.size(); i++)
        {
            // if the newNode has a higher cost continue
            if(nodeList.get(i).estimatedTotalCost > newNode.estimatedTotalCost)
            {
                insertIndex = i;
                break;
            }
        }

        this.nodesAdded += 1;

        // insert the new node at the selected position.
        nodeList.add(insertIndex, newNode);
    }

    /**
     * pops the node at the head of the list and shifts the list accordingly
     * @return the first Node in the list
     */
    public Node popNode()
    {
        if(nodeList.size() > 0)
            return nodeList.remove(0);

        return null;
    }

    /**
     * gets total nodes ever added to this queue
     * @return int denoting total nodes added
     */
    public int getTotalNodesAdded()
    {
        return this.nodesAdded;
    }
}
