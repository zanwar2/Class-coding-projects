
package heap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
public class Heap  
{

    //Main method that has the reader to read the InputFile provided by 
    //by gradescope
    public static void main(String[] args) throws FileNotFoundException
    {
        MinHeap minHeap = new MinHeap(0);
        File inputFile = new File("inputFile.txt");
        Scanner cin = new Scanner(inputFile);
        int numberOfInstruction = 0;
        int min = 0;
        if(cin.hasNextLine())
            numberOfInstruction= Integer.parseInt(cin.nextLine());
        while(numberOfInstruction >= 1 && cin.hasNextLine())
        {
            String instruction = cin.nextLine();
            String[] operands = instruction.split(" ");
            if(operands[0].equals("IN"))
            {
                minHeap.insert(Integer.parseInt(operands[1]));
            }
            else if(operands[0].equals("DK"))
            {
                minHeap.decreaseKey(Integer.parseInt(operands[1]),Integer.parseInt(operands[2]));
            }
            else if(operands[0].equals("EM"))
            {
                min = minHeap.extractMin();
               
            } 
            //System.out.println(Arrays.toString(minHeap.getArray()));
        }
         System.out.print(min+"\n");
        
    }
    
        
}

class MinHeap 
{
    //Intilization of parameters for the constructor
    private int array[];
    private int arraySize;
    private int heapSize;
    
    /**
     * The constructor for the minHeap object
     * @param arraySize 
     * the parameter provides values for heapSize and arraySize, and also
     * provides the array length of the array of the minHeap object.
     */
    public MinHeap(int arraySize)
    {
        this.arraySize = arraySize;
        heapSize = arraySize;
        this.array = new int[heapSize];  
        
    }
    
    /**
     * Method to sort an array in descending order that essentially extracts the
     * minimum until in descending order.
     * @param array
     * The array that is to be sorted.
     * @param nodeCount 
     * The amount of nodes found in the heap form of the array that is to be 
     * sorted
     */
    public void heapSort(int[] array, int nodeCount)
      {
            System.out.printf("intial list is %d %d %d %d %d %d %d %d\n", array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7]);

            heapify(array,nodeCount);

            System.out.printf("heapified list is %d %d %d %d %d %d %d %d\n", array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7]);
            for(int end = nodeCount-1; end > 0; end--)
            {
                int value = array[end];
                array[end] = array[0];
                array[0] = value;
                sinkDown(array, 0, end -1);
            }
            System.out.printf("sorted list is %d %d %d %d %d %d %d %d\n", array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7]);
      }
    
    /**
     * Method to set the array of the minHeap object
     * @param array 
     * The parameter is the array that is set as the array of the minHeap
     * object.
     */
    public void addValues(int[] array)
    {
        this.array = array;
    }
    
    /**
     * This method turns a given array into a minimum heap based off how many
     * nodes are present
     * @param array The array that is to be heapified
     * @param nodeCount The amount of nodes in the heap
     */
    public  void heapify(int[] array, int nodeCount)
      {
          for(int i=nodeCount/2; i >=0;i--)
          {
              sinkDown(array, i, nodeCount-1);
          }    
      }
    public void sinkDown(int[] numbers, int root, int bottom)
      {
          int temp, minChild, otherChild, thirdChild;
          minChild = root*3+1;
          if(minChild > bottom)
              return;
          else if(minChild < bottom)
          {
              otherChild = minChild +1;
              thirdChild = minChild+1;
              //System.out.printf("%d, %d%n",numbers[otherChild],numbers[minChild]);
              if(numbers[otherChild] < numbers[minChild])
              {
                  minChild = otherChild;
              }
              if(numbers[thirdChild] < numbers[minChild])
              {
                  minChild = thirdChild;
              }
          }
          if(numbers[root] <= numbers[minChild])
          {
              return;
          }
          temp = numbers[root];
          numbers[root] = numbers[minChild];
          numbers[minChild]=temp;
          //System.out.println(Arrays.toString(array));
          sinkDown(numbers,minChild,bottom);
      }
    /**
     * Getter method for the array of a minHeap object
     * @return the array of a minHeap object
     */
    public int[] getArray()
    {
        return this.array;
    }
    
    /**
     * A recursive method to put a value in its proper node location to keep heap 
     * property in a heapified array.
     * @param array The array that is being rearranged
     * @param pos the position of the node that needs to swapped
     * @param nodeCount The amount of nodes in the heap tree
     */
    public void floatUp(int[] array, int pos, int nodeCount)
      {
          if (pos == 0)
          {
              return;
          }
          int parent = (pos-1)/3;
          int temp;
          if(array[pos]>array[parent])
          {
              return;
          }
          else
          {
              temp = array[parent];
              array[parent] = array[pos];
              array[pos] = temp;
          }
          floatUp(array,parent,nodeCount);
      }
    
    /**
     * A method to insert an element at the end of a heapified array and then
     * move it to its proper index to keep the heap property.
     * @param element the value of the new element added
     */
    public void insert(int element)
    {
        this.heapSize+=1;
        int temp[] = new int[this.heapSize];
        for(int i = 0; i<this.heapSize-1;i++)
        {
            temp[i]=this.array[i];
        }
        temp[this.heapSize-1]=element;
        this.array=temp;
        floatUp(this.array,heapSize-1,heapSize);
    }
    
    /**
     * A method to decrease the value of a certain node that then moves the node
     * into the proper index in order to keep heap property.
     * @param index the node that is being changed
     * @param newElement the new value of the node that is changed
     */
    public void decreaseKey(int index, int newElement)
    {
        array[index] = newElement;
        floatUp(this.array,index,this.heapSize-1);
        sinkDown(this.array,index,this.heapSize-1);
    }
    
    /**
     * Method to find the min of a heap and then swap places with the last node
     * then it is removed. The Heap is then rearranged to keep heap properties.
     * @return returns the minimum
     */
    public int extractMin()
    {
        int min = array[0];
        int nodeCount = 0;
        array[0]=array[heapSize-1];
        array[heapSize-1] = min;
        //System.out.println(Arrays.toString(array));
        sinkDown(array,0,heapSize-2);
        this.heapSize--;
        int temp[]=new int[heapSize];
        for(int i = 0;i<heapSize;i++)
        {
            temp[i] = array[i];
        }
        this.array = temp;
        return min;
    }
}
