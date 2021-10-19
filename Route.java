package route;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
/**
 *
 * @author Anwar
 */
public class Route {
    
    static class List
    {
        Node head;
        int length = 0;
        
        class Node
        {
            Edge data;
            Node next;
            public Node(Edge x)
            {
                this.data = x;
                next = null;
            }
            public Node()
            {
                
            }
        }
        
        public void add(Edge x)
        {
            Node z = new Node(x);
            Node change = head;
            while(change.next != null)
            {
                change = change.next;
            }
            change.next = z;
            length++;
        }
        
        public Edge get(int x)
        {
            Node change = new Node();
            change = head.next;
            int count = 0;
            while(count < x)
            {
                change = change.next;
                count++;
            }
            return change.data;
        }
    }
    
    static Edge appendToList(Edge x, Edge y)
    {
        if(x == null)
            return y;
        x.next = appendToList(x.next,y);
        return x;
    }
    
    
    static class Vertex
    {
        String airport_code ;
        int heap_pos;
        int hash_pos;
        int dvalue;
        Vertex parent;
        Edge adj_list;
        public Vertex(String airport)
        {
            this.airport_code = airport;
            this.heap_pos = 0;
            this.hash_pos = 0;
            this.dvalue = 9999999;      
            this.parent = null;
        }
    }
    
    static class Edge
    {
        String origin;
        String dest;
        String airlines;
        int fltno;
        int deptime;
        int arrtime;
        int distance;
        Edge next;
        public Edge(String airline, int fltno, String origin, String dest, int deptime, int arrtime,int distance)
        {
            this.origin = origin;
            this.dest = dest;
            this.airlines = airlines;
            this.fltno = fltno;
            this.deptime = deptime;
            this.arrtime = arrtime;
            this.distance = distance;
            Edge next;
        }    
    }
    
    static Vertex[] heaparray = new Vertex[1000];
    static Vertex[] hasharray = new Vertex[1000];
    static int heapsize = 0;
    
    public static void main(String[] args) throws FileNotFoundException 
    {
        Scanner flights = new Scanner(new File("flights.txt"));
        Scanner airports = new Scanner(new File("airports.txt"));
        int numAirports = airports.nextInt();
        int numFlights = flights.nextInt();
        flights.nextLine();
        flights.nextLine();
        
        for(int i =0; i < numAirports;i++)
        {
            String airport = airports.next();
            Vertex v = new Vertex(airport);
            v.heap_pos = i;
            v.hash_pos = myhash(v.airport_code);
            heaparray[i] = v;
            heapsize++;
            if(hasharray[v.hash_pos] == null)
                hasharray[v.hash_pos]=v;
            else
                while(hasharray[v.hash_pos] != null)
                    v.hash_pos++;
            hasharray[v.hash_pos] = v;
        }
        for(int i = 0; i<numFlights;i++)
        {
            Edge e = new Edge(flights.next(),flights.nextInt(),flights.next(), flights.next(),flights.nextInt(),flights.nextInt(),flights.nextInt());
            Vertex v = getVertex(e.origin);
            v.adj_list = appendToList(v.adj_list,e);
        }
        
        String Start = args[0];
        String destination = args[1];
        
        getVertex(Start).dvalue = 0;
        floatUp(heaparray,getVertex(Start).heap_pos);
        while(heapsize > 0)
        {
            Vertex x = extractMin(heaparray);
            if(x.adj_list == null)
                break;
            relax(x.adj_list);
            Edge temp = x.adj_list.next;
            while(temp != null)
            {
                relax(temp);
                temp = temp.next;
            }
        }
        Vertex temp = getVertex(destination);
        int count = 0;
        while(temp != null)
        {
            temp = temp.parent;
            //System.out.println(count);
            count++;
        }
        
        String[] output = new String[count];
        count--;
        temp = getVertex(destination);
        while(temp != null)
        {
            //System.out.println("here");
            output[count] = temp.airport_code;
            temp = temp.parent;
            count--;
        }
        for(int i=0; i<output.length; i++)
        {
            System.out.print(output[i] + "-");    
        }
        System.out.println(getVertex(destination).dvalue);
    }
    
    public static Vertex getVertex(String s)
    {
        int p = myhash(s);
        if(hasharray[p].airport_code.equals(s))
            return hasharray[p];
        else
        {
            while(!hasharray[p].airport_code.equals(s))
                p++;
            return hasharray[p];
        }
    }
    
    public static void relax(Edge e)
    {
        Vertex v = getVertex(e.origin);
        if(v.dvalue >= e.deptime)
            return;
        v = getVertex(e.dest);
        if(v.dvalue > e.arrtime)
        {
            v.dvalue = e.arrtime;
            floatUp(heaparray,v.heap_pos);
            v.parent = getVertex(e.origin);
        }
    }
    
    public static int myhash(String s)
        {
            int p0 = (int) s.charAt(0) - 'A'+1;
            int p1 = (int) s.charAt(1) - 'A'+1;
            int p2 = (int) s.charAt(2) - 'A'+1;
            int p3 = p0*467*467+p1*467+p2;
            int p4 = p3%7193;
            return p4%1000;
        }
    
    public static void floatUp(Vertex[] array, int pos)
      {
          if (pos == 0)
          {
              return;
          }
          int parent = (pos-1)/3;
          Vertex temp;
          int temp_pos = 0;
          if(array[pos].dvalue>array[parent].dvalue)
          {
              return;
          }
          else
          {
              temp = array[parent];
              temp_pos = array[parent].heap_pos;
              array[parent] = array[pos];
              array[parent].heap_pos = array[pos].heap_pos;
              array[pos] = temp; 
              array[pos].heap_pos = temp_pos;
          }
          floatUp(array,parent);
      }
    
    public static void decreaseKey(Vertex[] array, Vertex v)
    {
        int index = v.heap_pos;
        //array[index].dvalue ;
        floatUp(array,index);
    }
    
    public static void sinkDown(Vertex[] heap, int root, int bottom)
      {
          Vertex temp;
          int minChild, otherChild, tempPos;
          minChild = root*2+1;
          if(minChild > bottom)
              return;
          else if(minChild < bottom)
          {
              otherChild = minChild +1;
              //System.out.printf("%d, %d%n",numbers[otherChild],numbers[minChild]);
              if(heap[otherChild].dvalue < heap[minChild].dvalue)
              {
                  minChild = otherChild;
              }
          }
          if(heap[root].dvalue <= heap[minChild].dvalue)
          {
              return;
          }
          temp = heap[root];
          tempPos = heap[root].heap_pos;
          heap[root] = heap[minChild];
          heap[root].heap_pos = heap[minChild].heap_pos;
          heap[minChild]=temp;
          heap[minChild].heap_pos= temp.heap_pos;
          //System.out.println(Arrays.toString(array));
          sinkDown(heap,minChild,bottom);
      }
    
        public static Vertex extractMin(Vertex[] heap)
    {
        Vertex min = heap[0];
        int nodeCount = 0;
        heap[0]=heap[heapsize-1];
        heap[heapsize-1] = min;
        //System.out.println(Arrays.toString(array));
        sinkDown(heap,0,heapsize-2);
        heapsize--;
        Vertex temp[]=new Vertex[heapsize];
        for(int i = 0;i<heapsize;i++)
        {
            temp[i] = heap[i];
        }
        heap = temp;
        return min;
    }
    
//    class MinHeap 
//{
//    //Intilization of parameters for the constructor
//    private int array[];
//    private int arraySize;
//    private int heapSize;
//    
//    /**
//     * The constructor for the minHeap object
//     * @param arraySize 
//     * the parameter provides values for heapSize and arraySize, and also
//     * provides the array length of the array of the minHeap object.
//     */
//    public MinHeap(int arraySize)
//    {
//        this.arraySize = arraySize;
//        heapSize = arraySize;
//        this.array = new int[heapSize];  
//        
//    }
//    
//    /**
//     * Method to sort an array in descending order that essentially extracts the
//     * minimum until in descending order.
//     * @param array
//     * The array that is to be sorted.
//     * @param nodeCount 
//     * The amount of nodes found in the heap form of the array that is to be 
//     * sorted
//     */
//    public void heapSort(int[] array, int nodeCount)
//      {
//            System.out.printf("intial list is %d %d %d %d %d %d %d %d\n", array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7]);
//
//            heapify(array,nodeCount);
//
//            System.out.printf("heapified list is %d %d %d %d %d %d %d %d\n", array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7]);
//            for(int end = nodeCount-1; end > 0; end--)
//            {
//                int value = array[end];
//                array[end] = array[0];
//                array[0] = value;
//                sinkDown(array, 0, end -1);
//            }
//            System.out.printf("sorted list is %d %d %d %d %d %d %d %d\n", array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7]);
//      }
//    
//    /**
//     * Method to set the array of the minHeap object
//     * @param array 
//     * The parameter is the array that is set as the array of the minHeap
//     * object.
//     */
//    public void addValues(int[] array)
//    {
//        this.array = array;
//    }
//    
//    /**
//     * This method turns a given array into a minimum heap based off how many
//     * nodes are present
//     * @param array The array that is to be heapified
//     * @param nodeCount The amount of nodes in the heap
//     */
//    public  void heapify(int[] array, int nodeCount)
//      {
//          for(int i=nodeCount/2; i >=0;i--)
//          {
//              sinkDown(array, i, nodeCount-1);
//          }    
//      }
//    public void sinkDown(int[] numbers, int root, int bottom)
//      {
//          int temp, minChild, otherChild, thirdChild;
//          minChild = root*3+1;
//          if(minChild > bottom)
//              return;
//          else if(minChild < bottom)
//          {
//              otherChild = minChild +1;
//              thirdChild = minChild+1;
//              //System.out.printf("%d, %d%n",numbers[otherChild],numbers[minChild]);
//              if(numbers[otherChild] < numbers[minChild])
//              {
//                  minChild = otherChild;
//              }
//              if(numbers[thirdChild] < numbers[minChild])
//              {
//                  minChild = thirdChild;
//              }
//          }
//          if(numbers[root] <= numbers[minChild])
//          {
//              return;
//          }
//          temp = numbers[root];
//          numbers[root] = numbers[minChild];
//          numbers[minChild]=temp;
//          //System.out.println(Arrays.toString(array));
//          sinkDown(numbers,minChild,bottom);
//      }
//    /**
//     * Getter method for the array of a minHeap object
//     * @return the array of a minHeap object
//     */
//    public int[] getArray()
//    {
//        return this.array;
//    }
//    
//    /**
//     * A recursive method to put a value in its proper node location to keep heap 
//     * property in a heapified array.
//     * @param array The array that is being rearranged
//     * @param pos the position of the node that needs to swapped
//     * @param nodeCount The amount of nodes in the heap tree
//     */
//    public void floatUp(int[] array, int pos, int nodeCount)
//      {
//          if (pos == 0)
//          {
//              return;
//          }
//          int parent = (pos-1)/3;
//          int temp;
//          if(array[pos]>array[parent])
//          {
//              return;
//          }
//          else
//          {
//              temp = array[parent];
//              array[parent] = array[pos];
//              array[pos] = temp;
//          }
//          floatUp(array,parent,nodeCount);
//      }
//    
//    /**
//     * A method to insert an element at the end of a heapified array and then
//     * move it to its proper index to keep the heap property.
//     * @param element the value of the new element added
//     */
//    public void insert(int element)
//    {
//        this.heapSize+=1;
//        int temp[] = new int[this.heapSize];
//        for(int i = 0; i<this.heapSize-1;i++)
//        {
//            temp[i]=this.array[i];
//        }
//        temp[this.heapSize-1]=element;
//        this.array=temp;
//        floatUp(this.array,heapSize-1,heapSize);
//    }
//    
//    /**
//     * A method to decrease the value of a certain node that then moves the node
//     * into the proper index in order to keep heap property.
//     * @param index the node that is being changed
//     * @param newElement the new value of the node that is changed
//     */
//    public void decreaseKey(int index, int newElement)
//    {
//        array[index] = newElement;
//        floatUp(this.array,index,this.heapSize-1);
//        sinkDown(this.array,index,this.heapSize-1);
//    }
//    
//    /**
//     * Method to find the min of a heap and then swap places with the last node
//     * then it is removed. The Heap is then rearranged to keep heap properties.
//     * @return returns the minimum
//     */
//    public int extractMin()
//    {
//        int min = array[0];
//        int nodeCount = 0;
//        array[0]=array[heapSize-1];
//        array[heapSize-1] = min;
//        //System.out.println(Arrays.toString(array));
//        sinkDown(array,0,heapSize-2);
//        this.heapSize--;
//        int temp[]=new int[heapSize];
//        for(int i = 0;i<heapSize;i++)
//        {
//            temp[i] = array[i];
//        }
//        this.array = temp;
//        return min;
//    }
//    }
//    
    
}
    
    
    
    
    
    
   
    

