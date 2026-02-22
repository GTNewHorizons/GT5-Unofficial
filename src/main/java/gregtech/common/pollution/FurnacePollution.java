package gregtech.common.pollution;

public enum FurnacePollution {

    FURNACE(1.0f),
    BLAST_FURNACE(2.0f),
    SMOKER(2.0f),
    IRON_FURNACE(1.2f),
    ALCHEMICAL_FURNACE(1.0f),
    ADVANCED_ALCHEMICAL_FURNACE(1.0f),
    NETHER_FURNACE(0.5f),
    SLAB_FURNACE(1.0f);

    private final float pollutionMultiplier;

    FurnacePollution(float pollutionMultiplier) {
        this.pollutionMultiplier = pollutionMultiplier;
    }

    public int getPollution() {
        return Math.round(PollutionConfig.furnacePollutionAmount * pollutionMultiplier);
    }

}
