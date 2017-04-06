import java.util.ArrayList;

/**
 * Use C4.5 algorithm to calculate the gain ratio --- G_r(D,A)
 */

/**
 * @author Paul Yang
 *
 */
public class C4p5Algo extends ID3Algo{

    public C4p5Algo(double para) {
        super(para);
        // TODO Auto-generated constructor stub
    }

    @Override
    /**
     * Calculate the information gain ratio --- Gain-ratio(D,A)
     */
    public double calcG(TrainStrDataSet dataSet, int feaAIdx) {
        double gain = 0.0;
        double ratio = 0.0;
        double H_D = Ent(dataSet);
        
        //Get every sub data set---D_i
        TrainStrDataSet[] subDi = new TrainStrDataSet[dataSet.xColValuList.get(feaAIdx).size()];
        int xValIdx = 0;
        for(String strXVal : dataSet.xColValuList.get(feaAIdx)) {
            subDi[xValIdx] = TrainStrDataSet.getSubDataSet(dataSet, feaAIdx, strXVal);
            xValIdx++;
        }

        //And also calculate the intrinsic value --- IV(a)
        double IV_a = 0.0;
        double H_DA = 0.0;
        for(int i = 0; i < dataSet.xColValuList.get(feaAIdx).size(); i++) {
            double tmpDiv = subDi[xValIdx].rowNum / dataSet.rowNum;
            H_DA += tmpDiv * Ent(subDi[xValIdx]);
            IV_a -= tmpDiv * ( Math.log(tmpDiv) / Math.log(2) );
        }
        
        gain = H_D - H_DA;
        ratio = gain / IV_a;
        
        return ratio;
    }
    

    @Override
    public int selcFeaDevi(TrainStrDataSet dataSet, ArrayList<Integer> alterFeaList)  {
        double gMax = 0.0;
        int retAIdx = -1;
        
        double[] gainArr = new double[alterFeaList.size()];
        double meanGain = 0.0;
        for(int i = 0; i < alterFeaList.size(); i++) {
            gainArr[i] = super.calcG(dataSet, alterFeaList.get(i).intValue()); //ID3's Gain
            meanGain += gainArr[i];
        }
        meanGain = meanGain/alterFeaList.size();

        double tmpGr = 0.0;
        for(int i = 0; i < alterFeaList.size(); i++) {
            if(gainArr[i] >= meanGain) {
                tmpGr = calcG(dataSet, alterFeaList.get(i).intValue()); //C4.5's Gain Ratio
                if(gMax < tmpGr){
                    gMax = tmpGr;
                    retAIdx = i;
                }
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
