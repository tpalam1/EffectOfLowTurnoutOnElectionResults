import java.util.ArrayList;

/**
 * Many American Muslims have mentioned they are not going to vote for Biden this election,
 * due to his funding of Israel. This has lead to Democrats criticizing them that
 * low turnouts will bolster a Republican victory.
 *
 * Q: does low turnout for a party encourage the chance that an opposing party wins?
 *
 * Let: P(A) = the chance that Republicans win, given Democrats have the same turnout rate as Republicans.
 * Let: P(B) = the chance that Republicans win, given Democrats have a lower turnout rate than Republicans.
 *
 * H0: P(A) = P(B) -- low Democratic turnout does not boost the chance that Republicans win.
 * Ha: P(A) < P(B) -- low Democratic turnout does boost the chance that Republicans win.
 *
 * Assumptions:
 * - Two party system, Democrats and Republicans.
 * - Voters may vote for either party, or choose not to vote.
 * - Each party has its own turnout rate among its voters.
 *
 * Q: does low turnout for a party encourage the chance that an opposing party wins?
 * A: This simulation concludes that members of a party protesting the election (e.g: not voting)
 * creates a statistically significant increase in the chance that the opposing party wins.
 */
public class Main {
    public static void main(String[] args) {
        int ELECTORATE_SIZE = 1001; // odd number to ensure tie-breaks
        int NUM_ROUNDS = 10000;
        double COUNT_REPUBLICAN_VICTORIES = 0;

        double democraticTurnout;
        double republicanTurnout;
        democraticTurnout = republicanTurnout = 1;

        // Case A: Assume both parties have same turnout rate
        for(int i = 0; i < NUM_ROUNDS; i++){
            if(runRound(ELECTORATE_SIZE, democraticTurnout, republicanTurnout)){
                COUNT_REPUBLICAN_VICTORIES++;
            }
        }

        double P_REPUBLICAN_VICTORY = COUNT_REPUBLICAN_VICTORIES / NUM_ROUNDS;
        ArrayList<Double> equalTurnoutConfidenceInterval = ConfidenceInterval.getConfidenceInterval(P_REPUBLICAN_VICTORY, NUM_ROUNDS);

        // Case B: Assume Democrats have lower turnout
        // Assume Muslim Americans compose 5% of the Democratic party, and that they will all protest the vote in absentia.
        democraticTurnout = 1 - 0.05;
        COUNT_REPUBLICAN_VICTORIES = 0;

        for(int i = 0; i < NUM_ROUNDS; i++){
            if(runRound(ELECTORATE_SIZE, democraticTurnout, republicanTurnout)){
                COUNT_REPUBLICAN_VICTORIES++;
            }
        }
        P_REPUBLICAN_VICTORY = COUNT_REPUBLICAN_VICTORIES / NUM_ROUNDS;
        ArrayList<Double> inequalTurnoutConfidenceInterval = ConfidenceInterval.getConfidenceInterval(P_REPUBLICAN_VICTORY, NUM_ROUNDS);

        String s = "These two populations are ";
        if(!ConfidenceInterval.hasOverlap(equalTurnoutConfidenceInterval, inequalTurnoutConfidenceInterval)){
            s += "NOT";
        }
        s += " equal.";


        System.out.println("P(Republicans win | equal voter turnout) = " + equalTurnoutConfidenceInterval + " (n = " + NUM_ROUNDS + ").");
        System.out.println("P(Republicans win | ~equal voter turnout) = " + inequalTurnoutConfidenceInterval + " (n = " + NUM_ROUNDS + ").");
        System.out.println(">> Conclusion: " + s);
    }

    /**
     * Creates an electorate of the given size,
     * conducts an election,
     * and returns whether Republicans won that election.
     *
     * @param ELECTORATE_SIZE the number of voters in this election
     * @param democraticTurnout the chance that Democrats go out to vote
     * @param republicanTurnout the chance that Republicans go out to vote
     */
    public static boolean runRound(int ELECTORATE_SIZE, double democraticTurnout, double republicanTurnout){
        ArrayList<Voter> electorate = initElectorate(ELECTORATE_SIZE, democraticTurnout, republicanTurnout);

        ArrayList<Integer> electionResults = runElection(electorate);
        return isRepublicanVictory(electionResults);
    }

    /**
     * Checks if there are more votes cast for Republicans than Democrats.
     * @param electionResults an array representing the number of votes cast for Democrats, Republicans, and in-absentia ballots.
     */
    private static boolean isRepublicanVictory(ArrayList<Integer> electionResults) {
        int COUNT_DEMOCRATIC_VOTES = electionResults.get(0);
        int COUNT_REPUBLICAN_VOTES = electionResults.get(1);
        return COUNT_REPUBLICAN_VOTES > COUNT_DEMOCRATIC_VOTES;
    }

    /**
     * Returns the number of Democratic, Republican, and absentee votes this election.
     * @param electorate the electorate who will vote in this election
     */
    private static ArrayList<Integer> runElection(ArrayList<Voter> electorate) {
        int COUNT_DEMOCRATIC_VOTES = 0;
        int COUNT_REPUBLICAN_VOTES = 0;
        int COUNT_ABSENTIA_VOTES = 0;

        for(Voter v : electorate){
            Party voteCast = v.vote();
            if(voteCast == Party.DEMOCRAT){
                COUNT_DEMOCRATIC_VOTES++;
            } else if(voteCast == Party.REPUBLICAN){
                COUNT_REPUBLICAN_VOTES++;
            } else {
                COUNT_ABSENTIA_VOTES++;
            }
        }

        ArrayList<Integer> output = new ArrayList<>();
        output.add(COUNT_DEMOCRATIC_VOTES);
        output.add(COUNT_REPUBLICAN_VOTES);
        output.add(COUNT_ABSENTIA_VOTES);

        return output;
    }

    /**
     * Creates an electorate of voters. Assumes Democratic and Republican turnouts are even.
     * @param ELECTORATE_SIZE the number of voters that will comprise the electorate.
     */
    private static ArrayList<Voter> initElectorate(int ELECTORATE_SIZE) {
        ArrayList<Voter> output = new ArrayList<>();
        for(int i = 0; i < ELECTORATE_SIZE; i++){
            output.add(new Voter(getRandParty(), 1));
        }
        return output;
    }

    /**
     * Creates an electorate of voters. Does not assume Democratic and Republican turnouts are equal.
     * @param ELECTORATE_SIZE the number of voters that will comprise the electorate.
     * @param democraticTurnout the percentage of Democrats that will vote
     * @param republicanTurnout the percentage of Republicans that will vote
     */
    private static ArrayList<Voter> initElectorate(int ELECTORATE_SIZE, double democraticTurnout, double republicanTurnout) {
        ArrayList<Voter> output = new ArrayList<>();
        for(int i = 0; i < ELECTORATE_SIZE; i++){
            Party p = getRandParty(); // the Party to assign to the new voter

            Voter newVoter;
            if(p == Party.DEMOCRAT){
                newVoter = new Voter(p, democraticTurnout);
            } else {
                newVoter = new Voter(p, republicanTurnout);
            }
            output.add(newVoter);
        }
        return output;
    }

    private static Party getRandParty() {
        if(Math.random() < 0.5){
            return Party.DEMOCRAT;
        }
        return Party.REPUBLICAN;
    }
}