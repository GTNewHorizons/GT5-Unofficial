package gregtech.api.structure;

import java.util.Collection;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.gtnewhorizon.structurelib.alignment.IAlignment;

import gregtech.api.casing.ICasingGroup;
import gregtech.api.enums.StructureError;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

/**
 * An object that stores specifics about an instance multi (casing counts mainly).
 */
public class StructureWrapperInstanceInfo<MTE extends MTEMultiBlockBase & IAlignment & IStructureProvider<MTE>>
    implements IStructureInstance {

    public final StructureWrapper<MTE> structure;

    public Char2IntArrayMap actualCasingCounts = new Char2IntArrayMap();

    public final Reference2IntOpenHashMap<ICasingGroup> casingTiers = new Reference2IntOpenHashMap<>();

    public StructureWrapperInstanceInfo(StructureWrapper<MTE> structure) {
        this.structure = structure;
    }

    public void clearHatches() {
        actualCasingCounts.clear();
        casingTiers.clear();
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
}
