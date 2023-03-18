package gregtech.api.logic;

public class PollutionLogic {
    private int pollutionAmount;

    public PollutionLogic() {}
    
    public PollutionLogic setPollutionAmount(int pollutionAmount) {
        this.pollutionAmount = pollutionAmount;
        return this;
    }

    public int getPollutionAmount() {
        return pollutionAmount;
    }
}
