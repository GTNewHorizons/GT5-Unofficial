package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER_GLOW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import codechicken.nei.recipe.RecipeCatalysts;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SpecialFilter;
import gregtech.api.multitileentity.MultiTileEntityItem;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_MetaTileEntity_RecipeFilter extends GT_MetaTileEntity_SpecialFilter {

    private static final String TT_machineType = "GT5U.MBTT.MachineType";
    private static final String REPRESENTATION_SLOT_TOOLTIP = "GT5U.recipe_filter.representation_slot.tooltip";
    private static final String EMPTY_REPRESENTATION_SLOT_TOOLTIP = "GT5U.recipe_filter.empty_representation_slot.tooltip";
    public RecipeMap<?> mRecipeMap;
    private List<ItemStack> filteredMachines = new ArrayList<>();
    public int mRotationIndex = 0;

    public GT_MetaTileEntity_RecipeFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Filters 1 Recipe Type", "Use Screwdriver to regulate output stack size" });
    }

    public GT_MetaTileEntity_RecipeFilter(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_RecipeFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    private static RecipeMap<?> getItemStackMachineRecipeMap(ItemStack stack) {
        if (stack != null) {
            IMetaTileEntity metaTileEntity = GT_Item_Machines.getMetaTileEntity(stack);
            if (metaTileEntity != null) {
                return getMetaTileEntityRecipeMap(metaTileEntity);
            } else if (stack.getItem() instanceof MultiTileEntityItem) {
                return getMuTeRecipeMap(stack);
            }
        }
        return null;
    }

    private static RecipeMap<?> getMetaTileEntityRecipeMap(IMetaTileEntity metaTileEntity) {
        if (metaTileEntity instanceof RecipeMapWorkable recipeMapWorkable) {
            return recipeMapWorkable.getRecipeMap();
        }
        return null;
    }

    private static RecipeMap<?> getMuTeRecipeMap(@NotNull ItemStack stack) {
        return null;
    }

    private static List<ItemStack> getFilteredMachines(RecipeMap<?> recipeMap) {
        return RecipeCatalysts.getRecipeCatalysts(
            recipeMap.getFrontend()
                .getUIProperties().neiTransferRectId)
            .stream()
            .map(positionedStack -> positionedStack.item)
            .collect(Collectors.toList());
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((!getBaseMetaTileEntity().isServerSide()) || ((aTick % 8L != 0L) && mRotationIndex != -1)) return;
        if (this.filteredMachines.isEmpty()) {
            return;
        }
        this.mInventory[FILTER_SLOT_INDEX] = GT_Utility.copyAmount(
            1,
            this.filteredMachines.get(this.mRotationIndex = (this.mRotationIndex + 1) % this.filteredMachines.size()));
        if (this.mInventory[FILTER_SLOT_INDEX] == null) return;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_RecipeFilter(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_RECIPEFILTER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_RECIPEFILTER_GLOW)
                .glow()
                .build());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (mRecipeMap != null) {
            aNBT.setString("mRecipeMap", this.mRecipeMap.unlocalizedName);
        }
        NBTTagList tagList = new NBTTagList();
        for (ItemStack filteredMachine : filteredMachines) {
            tagList.appendTag(filteredMachine.writeToNBT(new NBTTagCompound()));
        }
        aNBT.setTag("filteredMachines", tagList);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mRecipeMap = RecipeMap.getFromOldIdentifier(aNBT.getString("mRecipeMap"));
        filteredMachines.clear();
        NBTTagList tagList = aNBT.getTagList("filteredMachines", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            ItemStack readStack = ItemStack.loadItemStackFromNBT(tagList.getCompoundTagAt(i));
            if (readStack != null) {
                filteredMachines.add(readStack);
            }
        }
    }

    @Override
    protected boolean isStackAllowed(ItemStack aStack) {
        return mRecipeMap != null && mRecipeMap.containsInput(aStack);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new FakeSyncWidget.StringSyncer(
                () -> this.mRecipeMap == null ? "" : this.mRecipeMap.unlocalizedName,
                id -> this.mRecipeMap = RecipeMap.ALL_RECIPE_MAPS.get(id)));
    }

    @Override
    protected List<Text> getEmptySlotTooltip() {
        return Collections.singletonList(Text.localised(EMPTY_REPRESENTATION_SLOT_TOOLTIP));
    }

    @Override
    public Function<List<String>, List<String>> getItemStackReplacementTooltip() {
        if (mRecipeMap != null) {
            List<String> tooltip = assembleItemStackReplacementTooltip(mRecipeMap);
            return list -> tooltip;
        }
        return super.getItemStackReplacementTooltip();
    }

    @NotNull
    private List<String> assembleItemStackReplacementTooltip(RecipeMap<?> recipeMap) {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(
            StatCollector.translateToLocal(TT_machineType) + ": "
                + EnumChatFormatting.YELLOW
                + StatCollector.translateToLocal(recipeMap.unlocalizedName)
                + EnumChatFormatting.RESET);
        int recipeSize = recipeMap.getAllRecipes()
            .size();
        if (recipeSize > 0) {
            tooltip.add("Filter size: §e" + recipeSize + "§r");
        }
        tooltip.addAll(mTooltipCache.getData(REPRESENTATION_SLOT_TOOLTIP).text);
        return tooltip;
    }

    @Override
    protected SlotWidget createFilterIconSlot(BaseSlot slot) {
        return new RecipeFilterIconSlotWidget(slot);
    }

    private class RecipeFilterIconSlotWidget extends FilterIconSlotWidget {

        private static final int SYNC_RECIPEMAP_C2S = 98;
        private static final int REQUEST_FILTERED_MACHINES_S2C = 99;

        public RecipeFilterIconSlotWidget(BaseSlot slot) {
            super(slot);
        }

        @Override
        protected void phantomClick(ClickData clickData, ItemStack cursorStack) {}

        // region client

        @Override
        public ClickResult onClick(int buttonId, boolean doubleClick) {
            updateAndSendRecipeMapToServer(
                getContext().getCursor()
                    .getItemStack());
            return ClickResult.SUCCESS;
        }

        @Override
        public boolean handleDragAndDrop(ItemStack draggedStack, int button) {
            updateAndSendRecipeMapToServer(draggedStack);
            draggedStack.stackSize = 0;
            return true;
        }

        private void updateAndSendRecipeMapToServer(ItemStack stack) {
            mRecipeMap = getItemStackMachineRecipeMap(stack);
            updateAndSendRecipeMapToServer(mRecipeMap);
        }

        private void updateAndSendRecipeMapToServer(RecipeMap<?> recipeMap) {
            if (recipeMap != null) {
                filteredMachines = getFilteredMachines(recipeMap);
            } else {
                filteredMachines = new ArrayList<>();
                mInventory[FILTER_SLOT_INDEX] = null;
            }
            mRotationIndex = -1;
            syncToServer(SYNC_RECIPEMAP_C2S, buffer -> {
                NetworkUtils.writeStringSafe(buffer, recipeMap != null ? recipeMap.unlocalizedName : null);
                buffer.writeVarIntToBuffer(filteredMachines.size());
                for (ItemStack filteredMachine : filteredMachines) {
                    NetworkUtils.writeItemStack(buffer, filteredMachine);
                }
            });
        }

        @Override
        public void readOnClient(int id, PacketBuffer buf) {
            if (id != REQUEST_FILTERED_MACHINES_S2C) {
                super.readOnClient(id, buf);
                return;
            }

            String recipeMapName = NetworkUtils.readStringSafe(buf);
            mRecipeMap = recipeMapName != null ? RecipeMap.ALL_RECIPE_MAPS.get(recipeMapName) : null;
            if (mRecipeMap != null) {
                updateAndSendRecipeMapToServer(mRecipeMap);
            }
        }

        // endregion

        // region server

        @Override
        public void readOnServer(int id, PacketBuffer buf) throws IOException {
            if (id != SYNC_RECIPEMAP_C2S) {
                super.readOnServer(id, buf);
                return;
            }

            String recipeMapName = NetworkUtils.readStringSafe(buf);
            mRecipeMap = recipeMapName != null ? RecipeMap.getFromOldIdentifier(recipeMapName) : null;
            mRotationIndex = -1;
            mInventory[FILTER_SLOT_INDEX] = null;
            filteredMachines.clear();

            if (mRecipeMap != null) {
                int filteredMachineSize = buf.readVarIntFromBuffer();
                filteredMachineSize = Math.min(filteredMachineSize, 256); // Prevent storing too many items
                for (int i = 0; i < filteredMachineSize; i++) {
                    ItemStack stack = NetworkUtils.readItemStack(buf);
                    if (stack != null) {
                        filteredMachines.add(stack);
                    }
                }
            }
        }

        @Override
        public void detectAndSendChanges(boolean init) {
            super.detectAndSendChanges(init);
            if (init && mRecipeMap != null && filteredMachines.isEmpty()) {
                // backward compatibility: This machine used to store only mRecipeMap, not filteredMachines
                syncToClient(
                    REQUEST_FILTERED_MACHINES_S2C,
                    buffer -> NetworkUtils.writeStringSafe(buffer, mRecipeMap.unlocalizedName));
            }
        }

        // endregion
    }
}
