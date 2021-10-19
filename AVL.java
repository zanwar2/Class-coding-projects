package avl;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/**
 *
 * @author Anwar
 */
class Node
    {
        int key, data, size, height, mindata,bf;
        Node left, right, parent;
        
        public Node(int key, int data)
        {
            this.key = key;
            this.data = data;
        }
    }

public class AVL {

    /**
     * @param args the command line arguments
     */
   
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner cin = new Scanner(new File(args[0]));
        String choice ="";
        while(cin.hasNext())
        {
            choice = cin.next();
            
            switch(choice)
            {
                case"IN":
                    Node z = new Node(cin.nextInt(), cin.nextInt());
                    
                   if(root == null)
                   {
                      
                       root = z;
                   }
                   else
                   {
                       insert(root,z);
                       
                   }
                   break;
                case "RM":
                    System.out.println(RangeMinData(root,cin.nextInt(),cin.nextInt()));
            }
                
        }
        
    }

    
    
    
    
    
    public static Node root;
    public static boolean height_inc = false;
    public static int RangeMinData(Node x, int k1, int k2)
    {
        int min;
        
        while(x.key <= k1 || x.key >= k2)
        {
            if(x.key <= k1)
                x = x.right;
            if(x.key >= k2)
                x = x.left;
        }
        min = x.data;
        Node y = x.left;
        //Searching the k1 path
        while((y != null) && (y.key >= k1))
        {
            //Searching the k1 path
            if(k1 <= y.key)
            {
                if(min > y.data)
                {
                    min = y.data;
                }
                if(y.right != null)
                {
                    if(min > y.right.mindata)
                    {
                        min = y.right.mindata;
                    }
                }
                y = y.left;
            }
            else
                y = y.right;
                
        }
        
        if((y!=null) && (y.key < k1))
        {
            while((y!=null) && (y.key < k1)&& (y.right!=null))
            {
                y=y.right;
            }
            if(min > y.data)
            {
                min = y.data;
            }
            if(y.right != null)
            {
                if(y.right.mindata < min)
                {
                    min = y.right.mindata;
                }
            }
        }
        //Searching the k2 path
        y = x.right;
        while((y!=null) && (y.key<= k2))
        {
            if(k2 >= y.key)
            {
                if(min > y.data)
                    min = y.data;
                if(y.left != null)
                {
                    if(min > y.left.mindata)
                        min = y.left.mindata;
                }
                y = y.right;
            }
            else
                y=y.left;
        }
        if((y!=null)&& (y.key > k2))
        {
            while((y != null) && (y.key> k2) && (y.left!= null))
            {
                y = y.left;
            }
            if(min > y.data)
            {
                min = y.data;
            }
            if(y.left != null)
            {
                if(min > y.left.mindata)
                {
                    min = y.left.mindata;
                }
            }
        }
        
        return min;
    }
    
    
    public static void insert(Node x, Node y)
    {
       //Case 1
        if(x.key < y.key)
        {  
            if(x.right != null)
            {    
                insert(x.right, y);
                recalc_mindata(x);
            }
                
            else
            {  
                y.parent = x;
                y.bf =0;
                recalc_mindata(y);
                x.right = y;
                recalc_mindata(x);
                height_inc = true;
            }
            if(height_inc)
            {
                //case 2.1
                if(x.bf == 0)
                {

                    x.bf = -1;
                    recalc_mindata(x);
                }
                   //case 2.2
                else if(x.bf == 1)
                {
                    x.bf = 0;
                    recalc_mindata(x);
                    height_inc = false;
                }
                //case 2.3
                else
                {
                   //Case 2.3.1
                    if(x.right.bf == -1)
                    {
                        L_rotate(x);
                        x.bf = x.parent.bf = 0;
                        recalc_mindata(x);
                        height_inc = false;
                    }
                    //Case 2.3.2
                    else if(x.right.bf == 1)
                    {
                        int b = x.right.left.bf;
                        R_rotate(x.right);
                        L_rotate(x);
                        x.parent.bf = 0;
                        recalc_mindata(x);
                        if(b == 0)
                        {
                            x.bf = x.parent.right.bf = 0;
                            recalc_mindata(x);
                        }
                        else if(b== 1)
                        {
                            x.bf =0;
                            recalc_mindata(x);
                            x.parent.right.bf = -1;
                            recalc_mindata(x);
                        }
                        else if(b == -1)
                        {
                            x.bf = 1;
                            recalc_mindata(x);
                            x.parent.right.bf = 0;
                            recalc_mindata(x);
                        }
                        height_inc = false;
                    }
                }
            }
        }
        else if(x.key>y.key)
        {
            if(x.left != null)
            {
                insert(x.left, y);
                recalc_mindata(x);
            }
            else
            {
                y.parent = x;
                y.bf = 0;
                recalc_mindata(y);
                x.left = y;
                recalc_mindata(x);
                height_inc = true;
            }
            if(height_inc)
            {
                if(x.bf == 0)
                {
                    x.bf = 1;
                    recalc_mindata(x);
                }
                    
                else if(x.bf == -1)
                {
                    x.bf = 0;
                    recalc_mindata(x);
                    height_inc = false;
                }
                 else
                {
                   //Case 2.3.1
                    if(x.left.bf == 1)
                    {
                        R_rotate(x);
                        x.bf = x.parent.bf = 0;
                        recalc_mindata(x);
                        height_inc = false;
                    }
                    //Case 2.3.2
                    else if(x.left.bf == -1)
                    {
                        int b = x.left.right.bf;
                        L_rotate(x.left);
                        R_rotate(x);
                        x.parent.bf = 0;
                        recalc_mindata(x);
                        if(b == 0)
                        {
                            x.bf = x.parent.left.bf = 0;
                            recalc_mindata(x);
                        }
                        else if(b== -1)
                        {
                            x.bf =0;
                            recalc_mindata(x);
                            x.parent.left.bf = 1;
                            recalc_mindata(x);
                        }
                        else if(b == 1)
                        {
                            x.bf = -1;
                            recalc_mindata(x);
                            x.parent.left.bf = 0;
                            recalc_mindata(x);
                        }
                        height_inc = false;
                    }
                }
            }
        }
        recalc_mindata(x);
        recalc_height(x);
        recalc_size(x);
        recalc_mindata(y);
        recalc_height(y);
        recalc_size(y);
    }
    
    static void print2DUtil(Node root, int space)
{
    // Base case  
    if (root == null)
        return;

    // Increase distance between levels  
    space += 10;

    // Process right child first  
    print2DUtil(root.right, space);

    // Print current node after space  
    // count  
    System.out.println("");
    for (int i = 10; i < space; i++)
        System.out.print(" ");
    System.out.println(root.key);

    // Process left child  
    print2DUtil(root.left, space);
}
    
    

    
    public static void balance(Node x)
    {
        if(x.left == null  && x.right == null)
             x.bf = 0;
        else if(x.left == null)
            x.bf = -1*x.right.height;
        else if(x.right == null)
            x.bf = x.left.height;
        else
            x.bf = x.left.height - x.right.height;
    }
    public static void recalc_height(Node x)
    {
        if((x.left == null) && (x.right == null))
            x.height = 1;
        else if(x.left == null)
            x.height = x.right.height+1;
        
        else if(x.right == null)
            x.height = x.left.height+1;
        else
        {
            int maxheight = x.left.height;
            if(maxheight < x.right.height)
                maxheight = x.right.height;
            x.height = 1 + maxheight;
        }
    }
    
    public static void recalc_size(Node x)
    {
        if((x.left == null) && (x.right == null))
            x.size = 1;
        else if(x.left == null)
            x.height = x.right.size+1;
        else if(x.right == null)
            x.height = x.left.size+1;
        else
            x.size = 1+ x.left.size + x.right.size;
    }
    
    public static void recalc_mindata(Node x)
    {
        if((x.right == null) && (x.left == null))
            x.mindata = x.data;
        else if(x.left == null)
            if(x.right.mindata < x.data)
                x.mindata = x.right.mindata;
            else
                x.mindata = x.data;
        else if(x.right == null)
            if(x.left.mindata < x.data)
                x.mindata = x.left.mindata;
            else
                x.mindata = x.data;
        else
        {
            int min = x.data;
            
            if(x.left.mindata < min)
            {
              
                min = x.left.mindata;
            }
                
            
            if(x.right.mindata < min)
            {
                
                min = x.right.mindata;
            }
         x.mindata = min;       
        }   
    }
    
    
    public static Node R_rotate(Node x)
    {
        Node p = x.parent;
        Node y = x.left;
        Node z = y.right;
        
        y.right = x;
        x.parent = y;
        x.left = z;
        
        if(z != null)
            z.parent = x;
        
        if(p != null)
        {
            if(p.left == x)
            {
                p.left = y;
                y.parent = p;
            }
            if(p.right == x)
            {
                p.right = y;
                y.parent = p;
            }
        }
        else
        {
            root = y;
            root.parent = null;
        }
        recalc_height(x);
        recalc_height(y);
        recalc_size(x);
        recalc_size(y);
        recalc_mindata(x);
        recalc_mindata(y);
        
        return y;
    }
    
    public static Node L_rotate(Node x)
    {
        Node p = x.parent;
        Node y = x.right;
        Node z = y.left;
        
        y.left = x;
        x.parent = y;
        x.right = z;
        
        if(z != null)
            z.parent = x;
        
        if(p != null)
        {
            if(p.left == x)
            {
                p.left = y;
                y.parent = p;
            }
            if(p.right == x)
            {
                p.right = y;
                y.parent = p;
            }
        }
        else
        {
            root = y;
            root.parent = null;
        }

        
        
        recalc_height(x);
        recalc_height(y);
        recalc_size(x);
        recalc_size(y);
        recalc_mindata(x);
        recalc_mindata(y);
        
        return y;
    }
    
}
