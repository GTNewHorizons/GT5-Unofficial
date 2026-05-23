package gregtech.common.gui.modularui.singleblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import codechicken.nei.recipe.RecipeCatalysts;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.gui.modularui.singleblock.base.MTESpecialFilterBaseGui;
import gregtech.common.tileentities.automation.MTERecipeFilter;

public class MTERecipeFilterGui extends MTESpecialFilterBaseGui<MTERecipeFilter> {

    public MTERecipeFilterGui(MTERecipeFilter machine) {
        super(machine);
    }

    @Override
    protected List<String> getEmptyFilterSlotTooltip(ModularPanel panel, PanelSyncManager syncManager) {
        return Collections.singletonList(GTUtility.translate("GT5U.recipe_filter.empty_representation_slot.tooltip"));
    }

    @Override
    protected List<String> getFilledFilterSlotTooltip(ModularPanel panel, PanelSyncManager syncManager) {
        String recipeMapName = syncManager.findSyncHandler("recipeMap", StringSyncValue.class)
            .getValue();

        List<String> tooltip = new ArrayList<>();

        if (!recipeMapName.isEmpty()) {

            tooltip.add(
                GTUtility.translate("GT5U.MBTT.MachineType") + ": "
                    + EnumChatFormatting.YELLOW
                    + GTUtility.translate(recipeMapName)
                    + EnumChatFormatting.RESET);
            int recipeSize = RecipeMap.ALL_RECIPE_MAPS.get(recipeMapName)
                .getAllRecipes()
                .size();
            if (recipeSize > 0) {
                tooltip.add("Filter size: §e" + recipeSize + "§r");
            }
            tooltip.add(GTUtility.translate("GT5U.recipe_filter.representation_slot.tooltip"));
        }

        return tooltip;
    }

    @Override
    protected ItemSlot createFilterSlotBase(ModularPanel panel, PanelSyncManager syncManager) {
        @SuppressWarnings("unchecked")
        GenericListSyncHandler<ItemStack> filteredMachinesSyncer = syncManager
            .findSyncHandler("filteredMachines", GenericListSyncHandler.class);
        StringSyncValue recipeMapSyncer = syncManager.findSyncHandler("recipeMap", StringSyncValue.class);
        IntSyncValue rotationIndexSyncer = syncManager.findSyncHandler("rotationIndex", IntSyncValue.class);

        return new PhantomItemSlot() {

            private RecipeMap<?> getItemStackMachineRecipeMap(ItemStack stack) {
                if (stack != null) {
                    IMetaTileEntity metaTileEntity = ItemMachines.getMetaTileEntity(stack);
                    if (metaTileEntity != null) {
                        return getMetaTileEntityRecipeMap(metaTileEntity);
                    }
                }
                return null;
            }

            private RecipeMap<?> getMetaTileEntityRecipeMap(IMetaTileEntity metaTileEntity) {
                if (metaTileEntity instanceof RecipeMapWorkable recipeMapWorkable) {
                    return recipeMapWorkable.getRecipeMap();
                }
                return null;
            }

            private List<ItemStack> getFilteredMachines(RecipeMap<?> recipeMap) {
                return RecipeCatalysts.getRecipeCatalysts(
                    recipeMap.getFrontend()
                        .getUIProperties().neiTransferRectId)
                    .stream()
                    .map(positionedStack -> positionedStack.item)
                    .collect(Collectors.toList());
            }

            @Override
            public PhantomItemSlot slot(ModularSlot slot) {

                return syncHandler(new PhantomItemSlotSH(slot) {

                    @Override
                    protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
                        super.phantomClick(mouseData, cursorStack);
                        updateAndSendRecipeMapToServer(getItemStackMachineRecipeMap(cursorStack));
                    }

                    private void updateAndSendRecipeMapToServer(RecipeMap<?> recipeMap) {
                        if (recipeMap != null) {
                            filteredMachinesSyncer.setValue(getFilteredMachines(recipeMap));
                        } else {
                            filteredMachinesSyncer.setValue(new ArrayList<>());
                            getSlot().putStack(null);
                        }
                        rotationIndexSyncer.setValue(-1);

                        recipeMapSyncer.setValue(recipeMap != null ? recipeMap.unlocalizedName : "");
                    }
                });
            }
        };
    }

    @Override
    protected boolean supportsStocking() {
        return false;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.registerSlotGroup("item_inv", 3);

        syncManager.syncValue(
            "filteredMachines",
            GenericListSyncHandler.<ItemStack>builder()
                .getter(machine::getFilteredMachines)
                .setter(machine::setFilteredMachines)
                .deserializer(NetworkUtils::readItemStack)
                .serializer(NetworkUtils::writeItemStack)
                .build());

        syncManager.syncValue("recipeMap", new StringSyncValue(machine::getRecipeMapName, machine::setRecipeMap));
        syncManager.syncValue("rotationIndex", new IntSyncValue(machine::getRotationIndex, machine::setRotationIndex));
    }
}
