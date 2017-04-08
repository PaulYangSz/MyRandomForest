import java.util.ArrayList;
import java.util.List;

/**
 * Random Forest implements codes.
 */

/**
 * @author Paul Yang
 *
 */
public class RandomForestModel {
    int                kRandomFea    = 0;    //select k random number of features to construct tree.
    ArrayList<Integer> fullFeaList   = null; //total features as alternative list.
    TrainStrDataSet    trainSet      = null; //training data set.
    DevisionMethod     feaSelcMethod = null; //ID3 or C4.5 and so on...
    int                treeModelNum  = 0;    //How many tree model need be trained. (Odd is preferred)
    ArrayList<DecisionTreeModel> trainedForest = null;
    
    public RandomForestModel(TrainStrDataSet trainDataSet, String xFeaStr, DevisionMethod giveDivMed) {
        trainSet = trainDataSet;

        fullFeaList = new ArrayList<Integer>();
        if(xFeaStr.equals("") == true) {
            for(int i=0; i < trainDataSet.xColuNum; i++) {
                fullFeaList.add(i);
            }
        }
        else{
            String[] strIdx = xFeaStr.split(",");
            for(int i=0; i < strIdx.length; i++) {
                fullFeaList.add( Integer.parseInt(strIdx[i]) );
            }
        }
        kRandomFea = (int) Math.round( Math.log(fullFeaList.size()) / Math.log(2) );
        
        feaSelcMethod = giveDivMed;
    }

    public RandomForestModel(TrainStrDataSet trainDataSet, String xFeaStr, DevisionMethod giveDivMed, int kFeaNum) {
        trainSet = trainDataSet;

        fullFeaList = new ArrayList<Integer>();
        if(xFeaStr.equals("") == true) {
            for(int i=0; i < trainDataSet.xColuNum; i++) {
                fullFeaList.add(i);
            }
        }
        else{
            String[] strIdx = xFeaStr.split(",");
            for(int i=0; i < strIdx.length; i++) {
                fullFeaList.add( Integer.parseInt(strIdx[i]) );
            }
        }
        kRandomFea = kFeaNum;
        
        feaSelcMethod = giveDivMed;
    }
    
    
    public void genRandomForest(int oddTreeNum) {
        trainedForest = new ArrayList<DecisionTreeModel>();
        /**
         * Generate every tree of forest.
         */
        for(int i = 0; i < oddTreeNum; i++) {
            /**
             * Random to select the training data set.
             */
            TrainStrDataSet rdmDataSet = TrainStrDataSet.genSameRowData(trainSet);
            
            /**
             * Random to select the k-feature list.
             */
            @SuppressWarnings("unchecked")
            ArrayList<Integer> rdmFeaList = (ArrayList<Integer>) fullFeaList.clone();
            int rdmRmIdx = -1;
            for(int j = 0; j < fullFeaList.size() - kRandomFea; j++) {
                rdmRmIdx = (int) (Math.random() * rdmFeaList.size());
                rdmFeaList.remove(rdmRmIdx);
            }
            assert(rdmFeaList.size() == kRandomFea);
            StringBuilder rdmFeaStr = new StringBuilder();
            for(int strI = 0; strI < kRandomFea; strI++) {
                rdmFeaStr.append(rdmFeaList.get(strI));
                if(strI != kRandomFea - 1) {
                    rdmFeaStr.append(",");
                }
            }
            System.out.println("Random select feature are: " + rdmFeaStr.toString());

            /**
             * Use random data set and selected feature list to train a decision tree.
             * Add add to the forest.
             */
            DecisionTreeModel deciTreeModel = new DecisionTreeModel(rdmDataSet, rdmFeaStr.toString(), feaSelcMethod);
            deciTreeModel.startTraining();
            trainedForest.add(deciTreeModel);
        }
    }
    
    public int doPredict(String[] inputX) {
        int[] yValCnt = new int[trainSet.yClassNum];
        
        int tmpPreY = -1;
        for(int i = 0; i < trainedForest.size(); i++) {
            tmpPreY = trainedForest.get(i).doPredict(inputX);
            yValCnt[tmpPreY]++;
        }
        
        return MyMath.getMaxArrIdx(yValCnt);
    }
}
