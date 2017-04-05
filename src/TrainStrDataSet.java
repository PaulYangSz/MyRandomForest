import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Record all String type training data.
 */

/**
 * @author Paul Yang
 *
 */
public class TrainStrDataSet {
    int   rowNum    = 0;
    int   xColuNum  = 0;
    int   yClassNum = 0; //Range of  Y's value
    ArrayList<Integer>   yDataList    = null; //Y_i
    ArrayList<String[]>  xRowDataList = null; // X_i
    TreeSet<String[]>    xColValuList = null; // X_i's value range
    
    public int getMaxYClass() {
        assert(rowNum > 0);
        
        int[] statisY = new int[yClassNum]; //count every Y's value's number.
        for(int i = 0; i < rowNum; i++) { //for every yDataList[]
            int tmpY = yDataList.get(i);
            assert(tmpY < yClassNum);
            statisY[tmpY]++;
        }
        
        ArrayList<Integer> maxYIdx = new ArrayList<Integer>(); //contain max Y's value list
        int temMax = 0;
        for(int i = 0; i < yClassNum; i++) {
            if(temMax < statisY[i]){ //Find a bigger
                maxYIdx.clear();
                maxYIdx.add(i);
                temMax = statisY[i];
            }
            else if(temMax == statisY[i]) { //Find a equal
                maxYIdx.add(i);
            }
        }
        
        int rdmIdx = (int)( Math.random() * maxYIdx.size() );
        
        return maxYIdx.get(rdmIdx);
    }
}
