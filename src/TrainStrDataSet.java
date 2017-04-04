import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Record all String type training data.
 */

/**
 * @author Administrator
 *
 */
public class TrainStrDataSet {
    int   rowNum    = 0;
    int   xColuNum  = 0;
    int   yClassNum = 0; //Range of  Y's value
    int[] yData     = null; //Y_i
    ArrayList<String[]>  xRowDataList = null; // X_i
    TreeSet<String[]>    xColValuList = null; // X_i's value range
    
    public int getMaxYClass() {
        int[] statisY = new int[yClassNum];
        
        for(int i = 0; i < rowNum; i++) {
            assert(yData[i] < yClassNum);
            statisY[ yData[i] ]++;
        }
        
        for()
        return 
    }
}
