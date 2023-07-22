package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER_GLOW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler;
import com.gtnewhorizons.modularui.api.drawable.Text;

import codechicken.nei.recipe.RecipeCatalysts;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SpecialFilter;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_MetaTileEntity_RecipeFilter extends GT_MetaTileEntity_SpecialFilter {

    private static final String TT_machineType = StatCollector.translateToLocal("GT5U.MBTT.MachineType");
    private static final String REPRESENTATION_SLOT_TOOLTIP = "GT5U.recipe_filter.representation_slot.tooltip";
    private static final String EMPTY_REPRESENTATION_SLOT_TOOLTIP = "GT5U.recipe_filter.empty_representation_slot.tooltip";
    public GT_Recipe.GT_Recipe_Map mRecipeMap;
    private List<ItemStack> filteredMachines;
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

    @Override
    public void clickTypeIcon(boolean aRightClick, ItemStack aHandStack) {
        mRecipeMap = getItemStackMachineRecipeMap(aHandStack);
        if (mRecipeMap != null) {
            loadFilteredMachines();
            return;
        }
        filteredMachines = Collections.emptyList();
        mInventory[FILTER_SLOT_INDEX] = null;
        mRotationIndex = -1;
    }

    private static GT_Recipe.GT_Recipe_Map getItemStackMachineRecipeMap(ItemStack stack) {
        GT_Recipe.GT_Recipe_Map recipeMap = null;
        if (stack != null
            && GT_Item_Machines.getMetaTileEntity(stack) instanceof GT_MetaTileEntity_BasicMachine machine) {
            recipeMap = machine.getRecipeList();
        }
        return recipeMap;
    }

    private void loadFilteredMachines() {
        filteredMachines = RecipeCatalysts.getRecipeCatalysts(mRecipeMap.mUnlocalizedName)
            .stream()
            .map(positionedStack -> positionedStack.item)
            .collect(Collectors.toList());
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((!getBaseMetaTileEntity().isServerSide()) || ((aTick % 8L != 0L) && mRotationIndex != -1)) return;
        if (this.filteredMachines.isEmpty()) {
            this.mInventory[FILTER_SLOT_INDEX] = null;
            return;
        }
        this.mInventory[FILTER_SLOT_INDEX] = GT_Utility.copyAmount(
            1L,
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
        if (mRecipeMap != null) aNBT.setString("mRecipeMap", this.mRecipeMap.mUniqueIdentifier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mRecipeMap = GT_Recipe.GT_Recipe_Map.sIndexedMappings.getOrDefault(aNBT.getString("mRecipeMap"), null);
        if (mRecipeMap != null) {
            loadFilteredMachines();
        }
    }

    @Override
    protected boolean isStackAllowed(ItemStack aStack) {
        return mRecipeMap != null && mRecipeMap.containsInput(aStack);
    }

    @Override
    protected List<Text> getEmptySlotTooltip() {
        return Collections.singletonList(Text.localised(EMPTY_REPRESENTATION_SLOT_TOOLTIP));
    }

    @Override
    public Function<List<String>, List<String>> getItemStackReplacementTooltip() {
        GT_Recipe.GT_Recipe_Map recipeMap = getItemStackMachineRecipeMap(mInventory[FILTER_SLOT_INDEX]);
        if (recipeMap != null) {
            List<String> tooltip = assembleItemStackReplacementTooltip(recipeMap);
            return list -> tooltip;
        }
        return super.getItemStackReplacementTooltip();
    }

    @NotNull
    private List<String> assembleItemStackReplacementTooltip(GT_Recipe.GT_Recipe_Map recipeMap) {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(
            TT_machineType + ": "
                + AnimatedTooltipHandler.YELLOW
                + StatCollector.translateToLocal(recipeMap.mUnlocalizedName)
                + AnimatedTooltipHandler.RESET);
        tooltip.addAll(mTooltipCache.getData(REPRESENTATION_SLOT_TOOLTIP).text);
        return tooltip;
    }
}
