package gtPlusPlus.core.material.state;

public enum MaterialState {

    SOLID(0),
    LIQUID(1),
    GAS(2),
    PLASMA(3),
    PURE_LIQUID(4),
    ORE(5),
    PURE_GAS(6);

    private final int STATE;

    private MaterialState(final int State) {
        this.STATE = State;
    }

    public int ID() {
        return this.STATE;
    }
}
