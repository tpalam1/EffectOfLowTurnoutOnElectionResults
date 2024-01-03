/**
 * Simulates a single voter in the American two-party system.
 */
public class Voter {
    /* Denotes the party preference of this voter. */
    private Party party;

    /* Denotes the chance that this voter goes to vote. */
    private double turnout;

    public Voter(){
        party = null;
        turnout = 0;
    }

    /**
     * Creates a new Voter of the given party and turnout affiliations.
     */
    public Voter(Party p, double d){
        party = p;
        turnout = d;
    }

    /**
     * Casts a vote, aligning with this Voter's party.
     * @return In absentia, if this voter does not turnout.
     */
    public Party vote(){
        if(Math.random() < turnout){
            return party;
        }
        return Party.IN_ABSENTIA;
    }
}
