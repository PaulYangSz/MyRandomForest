import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public abstract class MyMath {
    
    
    public static int getMaxArrIdx(int[] inArry) {
        
        ArrayList<Integer> maxValIdx = new ArrayList<Integer>(); //contain max Val's value list
        int temMax = 0;
        for(int i = 0; i < inArry.length; i++) {
            if(temMax < inArry[i]){ //Find a bigger
                maxValIdx.clear();
                maxValIdx.add(i);
                temMax = inArry[i];
            }
            else if(temMax == inArry[i]) { //Find a equal
                maxValIdx.add(i);
            }
        }
        
        /**
         * Get max record list's index
         */
        int rdmIdx = (int)( Math.random() * maxValIdx.size() );
        
        /**
         * return maxRecord[ramdomIdx]
         */
        return maxValIdx.get(rdmIdx);
    }

    
    
    public static int getMaxArrIdx(ArrayList<Integer> inList) {
        
        ArrayList<Integer> maxValIdx = new ArrayList<Integer>(); //contain max Val's value list
        int temMax = 0;
        for(int i = 0; i < inList.size(); i++) {
            if(temMax < inList.get(i)){ //Find a bigger
                maxValIdx.clear();
                maxValIdx.add(i);
                temMax = inList.get(i);
            }
            else if(temMax == inList.get(i)) { //Find a equal
                maxValIdx.add(i);
            }
        }

        /**
         * Get max record list's index
         */
        int rdmIdx = (int)( Math.random() * maxValIdx.size() );

        /**
         * return maxRecord[ramdomIdx]
         */
        return maxValIdx.get(rdmIdx);
    }
    
    
}
