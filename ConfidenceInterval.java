import java.util.ArrayList;

/**
 * This class is used to generate 95% confidence intervals for
 * proportions and means.
 *
 * This is used for hypothesis testing.
 */
public final class ConfidenceInterval {
    /**
     * Returns the 95% confidence z-interval for the given sample proportion.
     * @param sampleProportion the sample proportion
     * @param sampleSize the number of sample observations
     * @return the 95% CI for the population proportion.
     */
    public static ArrayList<Double> getConfidenceInterval(double sampleProportion, int sampleSize){
        ArrayList<Double> output = new ArrayList<>();

        double marginOfError = getMarginOfError(sampleProportion, sampleSize);

        double lowerBound = sampleProportion - marginOfError;
        double upperBound = sampleProportion + marginOfError;

        output.add(lowerBound); output.add(upperBound);

        return output;
    }

    /**
     * Checks whether the given confidence intervals have overlap.
     * If so, they can be said to be equal.
     */
    public static boolean hasOverlap(ArrayList<Double> confidenceInterval_1, ArrayList<Double> confidenceInterval_2){
        double lowerBound_1 = confidenceInterval_1.get(0);
        double upperBound_1 = confidenceInterval_1.get(1);

        double lowerBound_2 = confidenceInterval_2.get(0);
        double upperBound_2 = confidenceInterval_2.get(1);

        return (lowerBound_2 < upperBound_1 && upperBound_1 < upperBound_2) || (lowerBound_1 < upperBound_2 && upperBound_2 < upperBound_1);
    }

    /**
     * Calculates the margin of error for a sample of proportions.
     * @param sampleProportion the mean of the sample
     * @param sampleSize the number of observations of the sample
     * @return the margin of error for the true population proportion estimate, at 95% confidence.
     */
    private static double getMarginOfError(double sampleProportion, int sampleSize){
        return 1.96 * getStandardError(sampleProportion, sampleSize);
    }

    /**
     * Calculates the standard error for a proportion.
     * @param sampleProportion the mean of the sample
     * @param sampleSize the number of sample observations
     * @return the standard error of the sample
     */
    private static double getStandardError(double sampleProportion, int sampleSize){
        return Math.sqrt((sampleProportion * (1 - sampleProportion))/(double) sampleSize);
    }
}
