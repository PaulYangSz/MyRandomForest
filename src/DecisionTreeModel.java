import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Decision Tree learning basic algorithm
 */

/**
 * @author Paul Yang
 *
 */
public class DecisionTreeModel {
    TrainStrDataSet    dataSet     = null; //Training data set.
    ArrayList<Integer> xFeaList    = null; //Selected features used to generate a decision tree.
    DevisionMethod     divMethod   = null; //Which method used to division the tree's node.
    DecisionTree       trainedTree = null; //Result decision tree.

    public static class DecisionTree {
        boolean leafFlag  = true; //Whether is a leaf
        int     leafValue = 0xFFFFFFFF; //If is a leaf, this is a classification result.
        int     xFeaIdx   = 0xFFFFFFFF; //If not a leaf, which x_i choose to do the next decision.
        TreeSet<String>         xValues = null; //Ordered x_i's values.
        ArrayList<DecisionTree> branch  = null; //This node's branches, same order as xValues.
        
        public static void printTree(DecisionTree prtTree) {
            System.out.println("Gen a tree: {{{" + prtTree);
            if(prtTree.leafFlag) {
                System.out.println("\tA leaf node, with Y = " + prtTree.leafValue);
            }
            else {
                System.out.println("\tA branch node, with xFeaIdx: [" + prtTree.xFeaIdx);
                int i = 0;
                for(String banchName : prtTree.xValues) {
                    System.out.println("\t\t\"" + banchName + "\" \t@" + prtTree.branch.get(i));
                    i++;
                }
                System.out.println("\t]");
            }
            System.out.println("}}}");
        }
    }
    
    /**
     * Construct a DecisionTreeModel
     * @param trainDataSet: training data set.
     * @param xFeaStr: "i, ... , j" means selected as features x_i.
     * @param giveDivMed : devision method, like ID3 or C4.5 and so on.
     */
    public DecisionTreeModel (TrainStrDataSet trainDataSet, String xFeaStr, DevisionMethod giveDivMed) {
        dataSet = trainDataSet;
        
        xFeaList = new ArrayList<Integer>();
        if(xFeaStr.equals("") == true) {
            for(int i=0; i < trainDataSet.xColuNum; i++) {
                xFeaList.add(i);
            }
        }
        else{
            String[] strIdx = xFeaStr.split(",");
            for(int i=0; i < strIdx.length; i++) {
                xFeaList.add( Integer.parseInt(strIdx[i]) );
            }
        }
        
        divMethod = giveDivMed;
        
        trainedTree = null; //Call start train to generate.
    }
    
    /**
     * Generate a decision tree, need given a devision method.
     * @param currData
     * @param currFeaList
     * @return a DecisionTree
     */
    public DecisionTree treeGenerate(TrainStrDataSet currData, final ArrayList<Integer> currFeaList) {
        //Recursion process.
        assert(currData.rowNum > 0);
        DecisionTree retTree = new DecisionTree();
        
        if(currData.belongTo1Class()) { //Only one Y value in dataSet
            retTree.leafFlag = true;
            retTree.leafValue = currData.yDataList.get(0);
        }
        else if(currFeaList.isEmpty()) { //alternative feature list is null
            retTree.leafFlag = true;
            retTree.leafValue = currData.getMaxYClass();
        }
        else {
            int selcFeaIdx = divMethod.selcFeaDevi(currData, currFeaList); //Call ID3 or C4.5, return currList's index.
            if(selcFeaIdx == -1) {
                retTree.leafFlag = true;
                retTree.leafValue = currData.getMaxYClass();
            }
            else {
                retTree.leafFlag = false;
                retTree.xFeaIdx = currFeaList.get(selcFeaIdx); //X_i's i
                retTree.xValues = currData.xColValuList.get(retTree.xFeaIdx);
                //retTree.xValues = currData.xColValuList[retTree.xFeaIdx];
                
                //generate the sub-tree
                retTree.branch = new ArrayList<DecisionTree>();
                @SuppressWarnings("unchecked")
                ArrayList<Integer> nextFeaList = (ArrayList<Integer>) currFeaList.clone();
                nextFeaList.remove(selcFeaIdx);
                for(String strFeaValue : retTree.xValues) {
                    TrainStrDataSet nextData = TrainStrDataSet.getSubDataSet(currData, retTree.xFeaIdx, strFeaValue);
                    DecisionTree subTree = null;
                    if(nextData.rowNum == 0) {
                        subTree = new DecisionTree();
                        subTree.leafFlag = true;
                        subTree.leafValue = currData.getMaxYClass();
                    }
                    else {
                        subTree = treeGenerate(nextData, nextFeaList);
                    }
                    
                    if(DebugConfig.TRACE_TREE_GEN) {
                        DecisionTree.printTree(subTree);
                    }
                    
                    retTree.branch.add(subTree);
                }
            }
        }
        
        if(DebugConfig.TRACE_TREE_GEN && currData.rowNum == TrainStrDataSet.maxRowNum) {
            System.out.println(TrainStrDataSet.maxRowNum + " rows data's Root-Tree###");
            DecisionTree.printTree(retTree);
        }
        
        return retTree;
    }
    
    /**
     * Use the training data and selected feature list to generate a decision tree.
     */
    public void startTraining() {
        trainedTree = treeGenerate(dataSet, xFeaList);
        if(StatisInfo.STATIS_G_FLAG) {
            StatisInfo.meanGain = StatisInfo.sumGain / StatisInfo.numGain;
            StatisInfo.meanGratio = StatisInfo.sumGratio / StatisInfo.numGratio;
            System.out.println("Gain: MAX="+StatisInfo.maxGain + ", MIN="+StatisInfo.minGain + ", NUM="+StatisInfo.numGain + ", SUM="+StatisInfo.sumGain + ", MEAN="+StatisInfo.meanGain);
            System.out.println("G-Ratio: MAX="+StatisInfo.maxGratio + ", MIN="+StatisInfo.minGratio + ", NUM="+StatisInfo.numGratio + ", SUM="+StatisInfo.sumGratio + ", MEAN="+StatisInfo.meanGratio);
        }
    }
    
    /**
     * Use the trained decision tree to predict the input x_i[]'s y_i
     */
    public int doPredict(String[] inputX) {
        //recursion the trained tree.
        DecisionTree  recuTree = trainedTree;
        while(recuTree.leafFlag != true) {
            String selcFeaVal = inputX[recuTree.xFeaIdx];
            int banchIdx = 0;
            for(String xVal : recuTree.xValues) {
                if(xVal.equals(selcFeaVal)) {
                    recuTree = recuTree.branch.get(banchIdx);
                }
                else {
                    banchIdx++;
                }
            }
        }
        
        return recuTree.leafValue;
    }
}
