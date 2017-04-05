import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Record all String type training data.
 */

/**
 * @author Paul Yang
 *
 */
public class TrainStrDataSet {
    int      rowNum    = 0;
    int      xColuNum  = 0;
    int      yClassNum = 0; //Range of  Y's value
    String[] xFeaNames = null; //1st line, X_i's names
    ArrayList<Integer>   yDataList    = null; //Y_i
    ArrayList<String[]>  xRowDataList = null; // X_i
    TreeSet<String>[]    xColValuList = null; // X_i's value range
    
    public boolean belongTo1Class() {
        boolean ret = true;
        int first = yDataList.get(0);
        for(int i = 1; i < rowNum; i++) {
            if(first != yDataList.get(i).intValue()) {
                ret = false;
                break;
            }
        }
        return ret;
    }
    
    public int getMaxYClass() {
        assert(rowNum > 0);
        
        int[] statisY = new int[yClassNum]; //count every Y's value's number.
        for(int i = 0; i < rowNum; i++) { //for every yDataList[]
            assert(yDataList.get(i).intValue() < yClassNum);
            statisY[yDataList.get(i).intValue()]++;
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

    @SuppressWarnings("unchecked")
    public static TrainStrDataSet getSubDataSet(TrainStrDataSet fromDataSet, int xFeaIdx, String strFeaValue) {
        TrainStrDataSet subData = new TrainStrDataSet();
        
        // Column info will not change.
        subData.xColuNum = fromDataSet.xColuNum;
        subData.yClassNum = fromDataSet.yClassNum;
        subData.xFeaNames = fromDataSet.xFeaNames;
        subData.xColValuList = fromDataSet.xColValuList;
        
        // Row info will change.
        subData.xRowDataList = (ArrayList<String[]>) fromDataSet.xRowDataList.clone();
        subData.yDataList = (ArrayList<Integer>) fromDataSet.yDataList.clone();
        for (Iterator<String[]> itX = subData.xRowDataList.iterator(); itX.hasNext(); ) {
            Iterator<Integer> itY = subData.yDataList.iterator();
            itY.next();
            if(itX.next()[xFeaIdx].equals(strFeaValue) == false) {
                itX.remove();
                itY.remove();
            }
        }
        subData.rowNum = subData.yDataList.size();
        
        return subData;
    }
}
