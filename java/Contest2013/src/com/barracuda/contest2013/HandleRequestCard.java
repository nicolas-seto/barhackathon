

package com.barracuda.contest2013;
import java.util.Arrays;

/**
 *
 * @author charleslong
 */
public class HandleRequestCard {
    
    public static int[]  sortedArray;
    public static int high, medium, low;
    
    public HandleRequestCard(int[] normalArray){
        this.sortedArray = normalArray;
        Arrays.sort(this.sortedArray);
        this.high = this.medium = this.low = 0;
        run();
    }
    
    public static void run(){
        placeTiers();
    }
    
    public static void placeTiers(){
        
        for(int i = 0; i < sortedArray.length; i++){
            if(sortedArray[i] >= 9){
                high++;
            }
            else if(sortedArray[i] >= 5){
                medium++;
            }
            else{
                low++;
            }
        }
        
    }

    public static boolean bestHand(){
        //if hand has  more highs 
    	if(high > (medium+low) && high == arraySize-1){
            return true;
    	}
        else{
            return false;
        }
    }
    
    public static boolean goodHand(){
        //if hand has  more highs 
    	if(high > (medium+low)){
            return true;
    	}
        else{
            return false;
        }
    }
    
    public static boolean mediumHand(){
        //if hand has  more highs 
    	if((high + medium) > low){
            return true;
    	}
        else{
            return false;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        HandleRequestCard hrc = new HandleRequestCard(new int[]{1,4,2,5,1});
        
        for(int i = 0; i < hrc.sortedArray.length;i++){
            System.out.print(hrc.sortedArray[i]);
        }
        System.out.println();
        System.out.println(hrc.high);
        System.out.println(hrc.medium);
        System.out.println(hrc.low);
    }
    
}