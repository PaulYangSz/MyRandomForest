import java.util.ArrayList;

/**
 * Use ID3 algorithm to calculate the Gain(D,A)
 */

/**
 * @author Paul Yang
 *
 */
public class ID3Algo extends DevisionMethod{

    public ID3Algo (double para) {
        epsilon = para;
    }
    
    /**
     * Calculate the information entropy --- Ent(D)
     * @param dataSet
     * @return Entropy
     */
    public static double Ent(TrainStrDataSet dataSet) {
        if(dataSet.rowNum == 0) { //Maybe a sub data set is null
            return 0.0;
        }
        
        int[] yiCount = new int[dataSet.yClassNum];
        for(int i = 0; i < dataSet.rowNum; i++) {
            yiCount[dataSet.yDataList.get(i)]++;
        }
        
        double tmpDi = 0.0;
        double entropy = 0.0;
        for(int yValue_i = 0; yValue_i < dataSet.yClassNum; yValue_i++) {
            tmpDi = (double)yiCount[yValue_i] / dataSet.rowNum; //Maybe y_i's count = 0 !!!
            if(tmpDi != 0) {
                entropy -= tmpDi * Math.log(tmpDi) / Math.log(2);
            }
            else {
                entropy -= 0.0;
            }
        }
        
        return entropy;
    }
    
    @Override
    /**
     * Calculate the information gain --- Gain(D,A)
     */
    public double calcG(TrainStrDataSet dataSet, int feaAIdx) {
        double gain = 0.0;
        double H_D = Ent(dataSet);
        
        //Get every sub data set---D_i
        TrainStrDataSet[] subDi = new TrainStrDataSet[dataSet.xColValuList.get(feaAIdx).size()];
        int xValIdx = 0;
        for(String strXVal : dataSet.xColValuList.get(feaAIdx)) {
            subDi[xValIdx] = TrainStrDataSet.getSubDataSet(dataSet, feaAIdx, strXVal);
            xValIdx++;
        }
        
        double H_DA = 0.0;
        for(int i = 0; i < xValIdx; i++) {
            H_DA += ((double)subDi[i].rowNum / dataSet.rowNum) * Ent(subDi[i]);
        }
        
        gain = H_D - H_DA;
        
        if(DebugConfig.TRACE_GAIN) {
            System.out.println("++ID3++G(D," + feaAIdx + ") = " + gain);
        }
        if(StatisInfo.STATIS_G_FLAG) {
            StatisInfo.maxGain = StatisInfo.maxGain < gain ? gain : StatisInfo.maxGain;
            StatisInfo.minGain = StatisInfo.minGain > gain ? gain : StatisInfo.minGain;
            StatisInfo.sumGain += gain;
            StatisInfo.numGain++;
        }
        return gain;
    }

    @Override
    public int selcFeaDevi(TrainStrDataSet dataSet, ArrayList<Integer> alterFeaList)  {
        double gMax = 0.0;
        double tmpG = 0.0;
        int retAIdx = -1;
        
        for(int i = 0; i < alterFeaList.size(); i++) {
            /**
             * [Hint!]: not need calc Ent(D) in this loop.
             */
            tmpG = calcG(dataSet, alterFeaList.get(i).intValue());
            if(gMax < tmpG){
                gMax = tmpG;
                retAIdx = i;
            }
        }
        
        if(gMax < epsilon) {
            retAIdx = -1;
        }
        else {
            if(DebugConfig.PRINT_EPSILON) {
                System.out.println("~~[epsilon]~~gMax = " + gMax);
            }
        }
        
        return retAIdx;
    }

}
