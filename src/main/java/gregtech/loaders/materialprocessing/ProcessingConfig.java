package gregtech.loaders.materialprocessing;

import gregtech.api.enums.Materials;

public class ProcessingConfig implements gregtech.api.interfaces.IMaterialHandler {

    public ProcessingConfig() {
        Materials.add(this);
    }

    /**
     * To add a new material, please see the following example: <blockquote>
     *
     * <pre>
     * int numberOfMaterialSlots = GregTechAPI.sMaterialProperties.get("general", "AmountOfCustomMaterialSlots", 16);
     * for (int i = 0; i < numberOfMaterialSlots; i++) {
     *     String aID = (i < 10 ? "0" : "") + i;
     *     new Materials(
     *         -1,
     *         TextureSet.SET_METALLIC,
     *         1.0F,
     *         0,
     *         0,
     *         0,
     *         255,
     *         255,
     *         255,
     *         0,
     *         "CustomMat" + aID,
     *         "CustomMat" + aID,
     *         0,
     *         0,
     *         0,
     *         0,
     *         false,
     *         false,
     *         1,
     *         1,
     *         1,
     *         Dyes._NULL,
     *         "custom",
     *         true,
     *         aID);
     * }
     * </pre>
     *
     * </blockquote>
     */
    @SuppressWarnings("unused")
    @Override
    public void onMaterialsInit() {}
}
