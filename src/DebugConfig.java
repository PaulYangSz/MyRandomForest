/**
 * This class is used to control precompile.
 */

/**
 * @author Yang
 *
 */
public abstract class DebugConfig {
    public static final boolean PRINT_THIS = true;
    public static final boolean PRINT_READ_DATA = false;
    public static final boolean PRINT_EPSILON = false;
    public static final boolean TRACE_TREE_GEN = false;
    public static final boolean TRACE_GAIN = false;
    public static final boolean USE_RANDOM_FOREST = true;
}

class StatisInfo {
    public static final boolean STATIS_G_FLAG = true;
    static double maxGain = 0.0;
    static double minGain = 0.0;
    static double sumGain = 0.0;
    static int numGain = 0;
    static double meanGain = 0.0;
    static double maxGratio = 0.0;
    static double minGratio = 0.0;
    static double sumGratio = 0.0;
    static int numGratio = 0;
    static double meanGratio = 0.0;
}