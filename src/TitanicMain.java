import java.text.SimpleDateFormat;
import java.util.Date;

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
        DecisionTreeModel deciTreeModel = null;
        RandomForestModel rdmForestModel = null;
        
        /**
         * 1st, read processed training data to generate the FileHelper.
         */
        StrFileHelper readData = null;
        readData = new StrFileHelper("./data/kaggle/gen_train_AgeFare.csv");

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
        ID3Algo id3 = new ID3Algo(-0.2);
        C4p5Algo c4p5 = new C4p5Algo(-0.15);
        
        /**
         * 4th, construct a decision tree model, and train it.
         */
        if(DebugConfig.USE_RANDOM_FOREST) {
            rdmForestModel = new RandomForestModel(trainSet, "", c4p5);
            rdmForestModel.genRandomForest(101);
        }
        else {
            deciTreeModel = new DecisionTreeModel(trainSet, "", c4p5);
            deciTreeModel.startTraining();
        }
        
        /**
         * 5th, predict the original training data.
         */
        int corrNum = 0;
        double corrRate = 0.0;
        for(int i = 0; i < trainSet.rowNum; i++) {
            int tmpY = -1;
            if(DebugConfig.USE_RANDOM_FOREST) {
                tmpY = rdmForestModel.doPredict(trainSet.xRowDataList.get(i));
            }
            else {
                tmpY = deciTreeModel.doPredict(trainSet.xRowDataList.get(i));
            }
            if(tmpY == trainSet.yDataList.get(i)) {
                corrNum++;
            }
            else {
                //System.out.printf("Wrong predict=%d Y=%d, X(%d)\n", tmpY, trainSet.yDataList.get(i), i);
                //System.out.println(trainSet.xRowDataList.get(i) + "");
            }
        }
        corrRate = (double)corrNum / trainSet.rowNum;
        System.out.println("Predict original training data, Rate: " + corrRate);
        
        /**
         * 6th, predict the test data and generate the submission CSV data file.
         */
        StrFileHelper testData = null;
        testData = new StrFileHelper("./data/kaggle/gen_test_AgeFare.csv");
        int[] yResult = new int[testData.data.size()];
        int[] passId = new int[testData.data.size()];
        for(int i = 0; i < testData.data.size(); i++) {
            passId[i] = 892 + i;
            if(DebugConfig.USE_RANDOM_FOREST) {
                yResult[i] = rdmForestModel.doPredict(testData.data.get(i));
            }
            else {
                yResult[i] = deciTreeModel.doPredict(testData.data.get(i));
            }
        }

        Date now = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy_MM_dd-HH_mm");
        StrFileHelper.writeToFile("PassengerId,Survived", passId, yResult, "./data/kaggle/Yang_submit-" + dateformat.format(now) + ".csv");
    }

}
