import java.util.ArrayList;

/**
 * Decision Tree learning basic algorithm
 */

/**
 * @author Paul Yang
 *
 */
public class DecisionTreeModel {
    TrainStrDataSet    dataSet   = null; //Training data set.
    ArrayList<Integer> xFeaList  = null; //Selected features used to generate a decision tree.
    DevisionMethod     divMethod = null; //Which method used to division the tree's node.
    ArrayList<DecisionTree> trainedTree = null; //Result decision tree.

    public class DecisionTree {
        boolean leafFlag  = true; //Whether is a leaf
        int     leafValue = 0xFFFFFFFF; //If is a leaf, this is a classification result.
        int     xFeaIdx   = 0xFFFFFFFF; //If not a leaf, which x_i choose to do the next decision.
        ArrayList<String> xValues = null; //Ordered x_i's values.
        ArrayList<DecisionTree> branch = null; //This node's branches, same order as xValues.
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
        
        trainedTree = new ArrayList<DecisionTree>();
    }
    
    /**
     * Use dataSet, xFeaList and divMethod(with epcilon) to generate a tree.
     * @return
     */
    public DecisionTree treeGenerate(TrainStrDataSet currData, ArrayList<Integer> currFeaList) {
        //Recursion process.
        assert(currData.rowNum > 0);
        DecisionTree retTree = new DecisionTree();
        
        if() {
            
        }
        else if(currFeaList.isEmpty()) {
            retTree.leafFlag = true;
            retTree.leafValue = currData.getMaxYClass();
        }
    }
}
