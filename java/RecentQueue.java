/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author WBakewell
 */
public class RecentQueue {
    private String[] queue;
    private int length = 5;
    
    public RecentQueue()
    {
        queue = new String[length];
    }
    
    public int getLength()
    {
        return length;
    }
    
    public String getAtIndex(int index)
    {
        return queue[index];
    }
    
    public void add(String toAdd)
    {
        for(int i = 0; i < length; i++)
        {
            if(queue[i] == null)                // If the queue has an empty spot, add and abort
            {
                queue[i] = toAdd;
                return;
            }
            if(queue[i].equals(toAdd)) return;  // If the id is in the queue already, abort
        }
        // If we get here, we know the queue is full and we need to pop
        for(int i = 0; i < length - 1; i++)
            queue[i] = queue[i + 1];
        queue[length - 1] = toAdd;
    }
}
