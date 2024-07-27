package kubatech.api.enums;

public enum EIGSetupPhase {

    Operation(0, "Operation"),
    Input(1, "Input"),
    Output(2, "Output");

    public final int id;
    public final String name;

    private EIGSetupPhase(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
