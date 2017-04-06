import java.util.ArrayList;

/**
 * Devision method and can be implement to ID3 or C4.5 and so on.
 */

/**
 * @author Paul Yang
 *
 */
public abstract class DevisionMethod {
    double epsilon = 0.0; //Threshold value

    /**
     * Calculate the G(D,A) or G_r(D,A)
     * @param dataSet
     * @param feaA
     * @return
     */
    public abstract double calcG(TrainStrDataSet dataSet, int feaA);
    
    /**
     * Use given data set and feature list, select the devision feature.
     * @param dataSet: data set
     * @param alterFeaList
     * @return Index of selected feature --- index of alterFeaList[index].
     */
    public abstract int selcFeaDevi(TrainStrDataSet dataSet, ArrayList<Integer> alterFeaList);
}
