package detrav.items.tools;

public class DetravProspector extends DetravToolElectricProspectorBase {

    private final int tier;

    public DetravProspector(int tier) {
        this.tier = tier;
    }

    @Override
    public int getBaseQuality() {
        return tier;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        double x = tier + 1;
        return (float) (0.00625D + (1.25D * x / 6D) * Math.tanh(Math.pow(x, (x / 8D)) / 25D));
    }
}
