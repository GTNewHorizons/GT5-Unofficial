package gregtech.api.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.structurelib.alignment.IAlignment;

import gregtech.api.casing.ICasing;
import gregtech.api.casing.ICasingGroup;
import gregtech.api.enums.StructureError;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * An object that stores specifics about an instance multi (casing counts mainly).
 */
public class StructureWrapperInstanceInfo<MTE extends MTEMultiBlockBase & IAlignment & IStructureProvider<MTE>>
    implements IStructureInstance<MTE> {

    public final StructureWrapper<MTE> structure;

    public Char2IntArrayMap actualCasingCounts = new Char2IntArrayMap();

    public final Object2IntOpenHashMap<ICasingGroup> casingTiers = new Object2IntOpenHashMap<>();

    /** Hatches that must have their textures updated */
    private final List<PendingHatch<MTE>> pendingHatches = new ArrayList<>();

    @Desugar
    private record PendingHatch<MTE> (MTEHatch hatch, ICasing casing, ICasing.CasingElementContext<MTE> context) {

    }

    public StructureWrapperInstanceInfo(StructureWrapper<MTE> structure) {
        this.structure = structure;
    }

    /**
     * Clears various fields in this instance info. Should be called in {@link MTEMultiBlockBase#clearHatches()}.
     */
    public void clearHatches() {
        actualCasingCounts.clear();
        casingTiers.clear();
        pendingHatches.clear();
    }

    /**
     * Performs any post-structure check operations.
     * Currently, this just updates the hatches with their proper texture. This is only needed if a hatch is within a
     * tiered casing, since tiered casings typically have different background textures for each tier.
     */
    public void onPostCheck(MTE multi) {
        for (PendingHatch<MTE> hatch : pendingHatches) {
            int textureId = hatch.casing.getTextureId(multi, hatch.context);

            hatch.hatch.updateTexture(textureId);
        }

        // free some memory since this data won't be needed after this point
        pendingHatches.clear();
    }

    /**
     * Validates this multi. Currently only checks casing counts.
     */
    public void validate(Collection<StructureError> errors, NBTTagCompound context) {
        NBTTagList data = new NBTTagList();

        for (var e : structure.casings.char2ObjectEntrySet()) {
            if (!actualCasingCounts.containsKey(e.getCharKey())) continue;

            int presentCasings = actualCasingCounts.get(e.getCharKey());
            int minCasings = structure.getCasingMin(e.getCharKey());

            if (presentCasings < minCasings) {
                NBTTagCompound error = new NBTTagCompound();

                error.setString("casing", Character.toString(e.getCharKey()));
                error.setInteger("req", minCasings);
                error.setInteger("pres", presentCasings);

                data.appendTag(error);
            }
        }

        if (!data.tagList.isEmpty()) {
            errors.add(StructureError.MISSING_STRUCTURE_WRAPPER_CASINGS);
            context.setTag("structureWrapper", data);
        }
    }

    @SuppressWarnings("unchecked")
    public void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context, List<String> lines) {
        if (!errors.contains(StructureError.MISSING_STRUCTURE_WRAPPER_CASINGS)) return;

        NBTTagList list = context.getTagList("structureWrapper", Constants.NBT.TAG_COMPOUND);

        for (NBTTagCompound tag : (List<NBTTagCompound>) list.tagList) {
            char casing = tag.getString("casing")
                .charAt(0);

            lines.add(
                GTUtility.translate(
                    "GT5U.gui.missing_casings_specific",
                    structure.casings.get(casing).casing.getLocalizedName(),
                    tag.getInteger("req"),
                    tag.getInteger("pres")));
        }
    }

    @Override
    public void onCasingEncountered(char casing) {
        actualCasingCounts.put(casing, actualCasingCounts.get(casing) + 1);
    }

    @Override
    public int getCasingTier(ICasingGroup casing, int unset) {
        return casingTiers.getOrDefault(casing, unset);
    }

    @Override
    public void setCasingTier(ICasingGroup casing, int tier) {
        casingTiers.put(casing, tier);
    }

    @Override
    public void addTieredHatch(MTEHatch hatch, ICasing casing, ICasing.CasingElementContext<MTE> context) {
        pendingHatches.add(new PendingHatch<>(hatch, casing, context));
    }
}
