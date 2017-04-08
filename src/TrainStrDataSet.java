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
    static int maxRowNum = -1;
    
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
        
        return MyMath.getMaxArrIdx(statisY);
    }

    @SuppressWarnings("unchecked")
    /**
     * Given selected feature and value, return matched sub data set.
     * [Hint!]: Can code another method to get every x_i's sub data set in one loop.
     * @param fromDataSet
     * @param xFeaIdx
     * @param strFeaValue
     * @return a matched sub data set.
     */
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
    
    /**
     * Construct from StrFileHelper
     * @param strFH
     */
    public TrainStrDataSet(StrFileHelper strFH) {
        //instant get some values
        rowNum = strFH.rowNum;
        if(maxRowNum == -1) {
            maxRowNum = rowNum;
        }
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

    /**
     * Construct from nothing, and be a nothing
     */
    public TrainStrDataSet() {
        rowNum = -1;
        xColuNum = -1;
        yClassNum = -1;
    }
    
    /**
     * Print input TrainStrDataSet
     * @param prtData
     */
    public static void pirntTrainData (TrainStrDataSet prtData) {
        System.out.println("====START======START=====");
        System.out.println("rowNum = " + prtData.rowNum);
        System.out.println("xColuNum = " + prtData.xColuNum);
        System.out.println("yClassNum = " + prtData.yClassNum);
        
        assert(prtData.xFeaNames.length == prtData.xColuNum);
        System.out.print("xName:{");
        for(int i = 0; i < prtData.xColuNum; i++){
            System.out.print(prtData.xFeaNames[i] + ", ");
        }
        System.out.println("}");
        
        System.out.println("Each X_i's values:");
        for(int i = 0; i < prtData.xColuNum; i++) {
            System.out.print("[" + prtData.xFeaNames[i] + ": ");
            for(String strVal : prtData.xColValuList.get(i)) {
                System.out.print(strVal + ", ");
            }
            System.out.println("]");
        }
        
//        System.out.println("Data:");
//        for(int i = 0; i < prtData.rowNum; i++) {
//            for(int j = 0; j < prtData.xColuNum; j++) {
//                System.out.print(prtData.xRowDataList.get(i)[j] + "\t");
//            }
//            System.out.println("@ " + prtData.yDataList.get(i));
//        }
        System.out.println("====END========END========END=======");
    }
    
    public static TrainStrDataSet genSameRowData(TrainStrDataSet origData) {
        TrainStrDataSet retDataSet = new TrainStrDataSet();
        
        // Column info will not change.
        retDataSet.xColuNum = origData.xColuNum;
        retDataSet.yClassNum = origData.yClassNum;
        retDataSet.xFeaNames = origData.xFeaNames;
        retDataSet.xColValuList = origData.xColValuList;
        
        //Row number also not change
        retDataSet.rowNum = origData.rowNum;
        
        //Random to get every row data.
        retDataSet.xRowDataList = new ArrayList<String[]>();
        retDataSet.yDataList    = new ArrayList<Integer>();
        int rdmIdx = 0;
        for(int rowI = 0; rowI < origData.rowNum; rowI++) {
            rdmIdx = (int) (Math.random() * origData.rowNum);
            retDataSet.xRowDataList.add( origData.xRowDataList.get(rdmIdx) );
            retDataSet.yDataList.add( origData.yDataList.get(rdmIdx) );
        }
        
        return retDataSet;
    }
}
