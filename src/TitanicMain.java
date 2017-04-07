/**
 * Predict the Kaggle-Titanic problem.
 */

/**
 * @author Paul Yang
 *
 */
public class TitanicMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        /**
         * 1st, read processed training data to generate the FileHelper.
         */
        StrFileHelper readData = null;
        readData = new StrFileHelper("./data/kaggle/gen_train_str_data.csv");

        /**
         * 2nd, save FileHelper to a TrainStrDataSet.
         */
        TrainStrDataSet trainSet = null;
        trainSet = new TrainStrDataSet(readData);
        TrainStrDataSet.pirntTrainData(trainSet);
        
        /**
         * 3rd, select a division method, which means ID3 or C4.5 and so on.
         */
        @SuppressWarnings("unused")
        ID3Algo id3 = new ID3Algo(0.14);
        C4p5Algo c4p5 = new C4p5Algo(0.23);
        
        /**
         * 4th, construct a decision tree model, and train it.
         */
        DecisionTreeModel deciTreeModel = null;
        deciTreeModel = new DecisionTreeModel(trainSet, "", c4p5);
        deciTreeModel.startTraining();
        
        /**
         * 5th, predict the original training data.
         */
        int corrNum = 0;
        double corrRate = 0.0;
        for(int i = 0; i < trainSet.rowNum; i++) {
            int tmpY = deciTreeModel.doPredict(trainSet.xRowDataList.get(i));
            if(tmpY == trainSet.yDataList.get(i)) {
                corrNum++;
            }
            else {
                System.out.printf("Wrong predict=%d Y=%d, X(%d)\n", tmpY, trainSet.yDataList.get(i), i);
                //System.out.println(trainSet.xRowDataList.get(i) + "");
            }
        }
        corrRate = (double)corrNum / trainSet.rowNum;
        System.out.println("Predict original training data, Rate: " + corrRate);
    }

}
