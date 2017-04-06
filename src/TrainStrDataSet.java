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
    ArrayList< TreeSet<String> >    xColValuList = null; // X_i's value range
    
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
    
    public TrainStrDataSet(StrFileHelper strFH) {
        //instant get some values
        rowNum = strFH.rowNum;
        xColuNum = strFH.columNum - 1;
        
        //Cut out the X_i's names
        xFeaNames = new String[xColuNum];
        System.arraycopy(strFH.firstNames, 0, xFeaNames, 0, xColuNum);
        
        //begin read and copy out other values
        xRowDataList = new ArrayList<String[]>();
        yDataList    = new ArrayList<Integer>();
        xColValuList = new ArrayList< TreeSet<String> >();
        for(int i = 0; i < xColuNum; i++) {
            TreeSet<String> xColValuTree = new TreeSet<String>();
            xColValuList.add(xColValuTree);
        }
        
        TreeSet<Integer> yValues = new TreeSet<Integer>();
        for(int iLine = 0; iLine < rowNum; iLine++) {
            String[] x_i = new String[xColuNum];
            int y_i = 0;
            
            //Add x_i
            System.arraycopy(strFH.data.get(iLine), 0, x_i, 0, xColuNum);
            xRowDataList.add(x_i);
            for(int i = 0; i < xColuNum; i++) {
                xColValuList.get(i).add(x_i[i]);
            }
            
            //Add y_i
            y_i = Integer.parseInt(strFH.data.get(iLine)[xColuNum]);
            yDataList.add(y_i);
            yValues.add(y_i);
        }
        yClassNum = yValues.size();
    }

    public TrainStrDataSet() {
        rowNum = -1;
        xColuNum = -1;
        yClassNum = -1;
    }
    
    public static void pirntTrainData (TrainStrDataSet prtData) {
        System.out.println("rowNum = " + prtData.rowNum);
        System.out.println("xColuNum = " + prtData.xColuNum);
        System.out.println("yClassNum = " + prtData.yClassNum);
        
        assert(prtData.xFeaNames.length == prtData.xColuNum);
        System.out.print("xName:{");
        for(int i = 0; i < prtData.xColuNum; i++){
            System.out.print(prtData.xFeaNames[i] + ", ");
        }
        System.out.println("}");
    }
}
