package galacticgreg.auxiliary;

import gregtech.api.interfaces.IMaterial;

/**
 * Just a simple container to wrap 4 GT Ore-Meta ids into one var
 */
public class GTOreGroup {

    public IMaterial Primary;
    public IMaterial Secondary;
    public IMaterial SporadicBetween;
    public IMaterial SporadicAround;

    public GTOreGroup(IMaterial pPrimary, IMaterial pSecondary, IMaterial pSporadicBetween, IMaterial pSporadicAround) {
        Primary = pPrimary;
        Secondary = pSecondary;
        SporadicBetween = pSporadicBetween;
        SporadicAround = pSporadicAround;
    }
}
