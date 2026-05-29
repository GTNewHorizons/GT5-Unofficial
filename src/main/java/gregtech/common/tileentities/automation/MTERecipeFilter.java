package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER_GLOW;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTESpecialFilter;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.MTERecipeFilterGui;

public class MTERecipeFilter extends MTESpecialFilter {

    private static final String TT_machineType = "GT5U.MBTT.MachineType";
    private static final String REPRESENTATION_SLOT_TOOLTIP = "GT5U.recipe_filter.representation_slot.tooltip";
    private static final String EMPTY_REPRESENTATION_SLOT_TOOLTIP = "GT5U.recipe_filter.empty_representation_slot.tooltip";
    public RecipeMap<?> mRecipeMap;
    private List<ItemStack> filteredMachines = new ArrayList<>();
    public int mRotationIndex = 0;

    public MTERecipeFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Filters 1 Recipe Type", "Use Screwdriver to regulate output stack size" });
    }

    public MTERecipeFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public String getRecipeMapName() {
        return mRecipeMap != null ? mRecipeMap.unlocalizedName : "";
    }

    public void setRecipeMap(String recipeMapName) {
        mRecipeMap = !recipeMapName.isEmpty() ? RecipeMap.ALL_RECIPE_MAPS.get(recipeMapName) : null;
    }

    public List<ItemStack> getFilteredMachines() {
        return filteredMachines;
    }

    public void setFilteredMachines(List<ItemStack> filteredMachines) {
        this.filteredMachines = filteredMachines;
    }

    public int getRotationIndex() {
        return mRotationIndex;
    }

    public void setRotationIndex(int rotationIndex) {
        this.mRotationIndex = rotationIndex;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((!getBaseMetaTileEntity().isServerSide()) || ((aTick % 8L != 0L) && mRotationIndex != -1)) return;
        if (this.filteredMachines.isEmpty()) {
            return;
        }
        this.mInventory[FILTER_SLOT_INDEX] = GTUtility.copyAmount(
            1,
            this.filteredMachines.get(this.mRotationIndex = (this.mRotationIndex + 1) % this.filteredMachines.size()));
        if (this.mInventory[FILTER_SLOT_INDEX] == null) return;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERecipeFilter(
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
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTERecipeFilterGui(this).build(guiData, syncManager, uiSettings);
    }
}
