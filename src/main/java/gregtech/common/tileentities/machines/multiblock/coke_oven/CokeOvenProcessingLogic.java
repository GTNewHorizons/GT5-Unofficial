package gregtech.common.tileentities.machines.multiblock.coke_oven;

import static net.minecraftforge.oredict.OreDictionary.getOreID;
import static net.minecraftforge.oredict.OreDictionary.getOreIDs;

import net.minecraft.init.Items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.ashley.core.Entity;
import com.cleanroommc.modularui.utils.item.IItemStackLong;
import com.cleanroommc.modularui.utils.item.ItemStackLong;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemOutputInventory;

import gregtech.api.enums.Mods;
import gregtech.api.logic.MuTEProcessingLogic;
import gregtech.api.multitileentity.data.ProcessingData;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class CokeOvenProcessingLogic extends MuTEProcessingLogic<CokeOvenProcessingLogic> {

    private static final int NORMAL_RECIPE_TIME = 1800;
    private static final int WOOD_ORE_ID = getOreID("logWood");
    private static final int COAL_ORE_ID = getOreID("coal");
    private static final int COAL_BLOCK_ORE_ID = getOreID("blockCoal");
    private static final int SUGARCANE_ORE_ID = getOreID("sugarcane");
    private static final int CACTUS_ORE_ID = getOreID("blockCactus");
    private static final int CACTUS_CHARCOAL_ORE_ID = getOreID("itemCharcoalCactus");
    private static final int SUGAR_CHARCOAL_ORE_ID = getOreID("itemCharcoalSugar");
    private int timeMultiplier = 1;

    @Override
    protected Class<? extends ProcessingData> getProcessingDataClass() {
        return CokeOvenData.class;
    }

    @Override
    protected void process(Entity entity) {
        timeMultiplier = 1;
        CokeOvenData cokeOvenData = entity.getComponent(CokeOvenData.class);
        ItemInputInventory inInv = entity.getComponent(ItemInputInventory.class);
        ItemOutputInventory outInv = entity.getComponent(ItemOutputInventory.class);
        if (cokeOvenData.getDuration() > 0) {
            cokeOvenData.setProgress(cokeOvenData.getProgress() + 1);
            if (cokeOvenData.getProgress() >= cokeOvenData.getDuration()) {
                IItemStackLong output = cokeOvenData.getOutputItem();
                for (int i = 0; i < outInv.getSize(); i++) {
                    IItemStackLong item = outInv.get(i);
                    if (item != null && !item.isItemEqual(output)) {
                        continue;
                    }
                    if (item == null) {
                        outInv.set(i, output.copy());
                        cokeOvenData.setOutputItem(null);
                        break;
                    }
                    if (item.getStackSize() >= outInv.getLimit()) continue;
                    item.setStackSize(item.getStackSize() + output.getStackSize());
                    cokeOvenData.setOutputItem(null);
                    break;
                }

            }
        }

        for (int i = 0; i < inInv.getSize(); i++) {
            IItemStackLong item = inInv.get(i);
            IItemStackLong output = findRecipe(item);
            if (item == null) {
                continue;
            }
            cokeOvenData.setDuration(NORMAL_RECIPE_TIME * timeMultiplier);
            cokeOvenData.setOutputItem(output.copy());
            break;
        }
    }

    @Nullable
    private IItemStackLong findRecipe(@NotNull IItemStackLong input) {
        for (int oreId : getOreIDs(input.getAsItemStack())) {
            if (oreId == COAL_ORE_ID) {
                return new ItemStackLong(GTOreDictUnificator.get("fuelCoke", null, 1));
            } else if (oreId == COAL_BLOCK_ORE_ID) {
                timeMultiplier = 9;
                return new ItemStackLong(GTModHandler.getModItem(Mods.Railcraft.ID, "cube", 1, 0));
            } else if (oreId == WOOD_ORE_ID) {
                return new ItemStackLong(Items.coal, 64, 1, 1);
            } else if (oreId == SUGARCANE_ORE_ID) {
                return new ItemStackLong(GTOreDictUnificator.get("itemCharcoalSugar", null, 1));
            } else if (oreId == SUGAR_CHARCOAL_ORE_ID) {
                return new ItemStackLong(GTOreDictUnificator.get("itemCokeSugar", null, 1));
            } else if (oreId == CACTUS_ORE_ID) {
                return new ItemStackLong(GTOreDictUnificator.get("itemCharcoalCactus", null, 1));
            } else if (oreId == CACTUS_CHARCOAL_ORE_ID) {
                return new ItemStackLong(GTOreDictUnificator.get("itemCokeCactus", null, 1));
            }
        }
        return null;
    }
}
