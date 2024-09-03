package galacticgreg.auxiliary;

/**
 * Just a simple container to wrap 4 GT Ore-Meta ids into one var
 */
public class GTOreGroup {

    public short PrimaryMeta;
    public short SecondaryMeta;
    public short SporadicBetweenMeta;
    public short SporadicAroundMeta;

    public GTOreGroup(short pPrimaryMeta, short pSecondaryMeta, short pSporadicBetweenMeta, short pSporadicAroundMeta) {
        PrimaryMeta = pPrimaryMeta;
        SecondaryMeta = pSecondaryMeta;
        SporadicBetweenMeta = pSporadicBetweenMeta;
        SporadicAroundMeta = pSporadicAroundMeta;
    }
}
