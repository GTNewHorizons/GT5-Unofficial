package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialArcFurnace extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialArcFurnace> implements ISurvivalConstructable {

    // 862
    private static final int mCasingTextureID = TAE.getIndexFromPage(3, 3);
    public static String mCasingName = "Tempered Arc Furnace Casing";
    private boolean mPlasmaMode = false;
    private int mSize = 0;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialArcFurnace> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialArcFurnace(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialArcFurnace(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialArcFurnace(this.mName);
    }

    @Override
    public String getMachineType() {
        return "(Plasma/Electric) Arc Furnace";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for Industrial Arc Furnace")
                .addInfo("250% faster than using single block machines of the same voltage")
                .addInfo("Processes 8 items per voltage tier * W/L")
                .addInfo("Right-click controller with a Screwdriver to change modes")
                .addInfo("Max Size required to process Plasma recipes").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().addController("Top center").addStructureInfo("Size: nxnx3 [WxHxL] (Hollow)")
                .addStructureInfo("n can be 3, 5 or 7").addCasingInfoMin(mCasingName, 10, false)
                .addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1)
                .addOutputHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1)
                .addMufflerHatch("Any Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialArcFurnace> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialArcFurnace>builder().addShape(
                    mName + "3",
                    new String[][] { { "CCC", "C~C", "CCC" }, { "CCC", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, })
                    .addShape(
                            mName + "5",
                            new String[][] { { "CCCCC", "CCCCC", "CC~CC", "CCCCC", "CCCCC" },
                                    { "CCCCC", "C---C", "C---C", "C---C", "CCCCC" },
                                    { "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" }, })
                    .addShape(
                            mName + "7",
                            new String[][] {
                                    { "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCC~CCC", "CCCCCCC", "CCCCCCC", "CCCCCCC" },
                                    { "CCCCCCC", "C-----C", "C-----C", "C-----C", "C-----C", "C-----C", "CCCCCCC" },
                                    { "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC" }, })
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialArcFurnace.class)
                                    .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(getCasingTextureIndex()).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings4Misc, 3))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private int getSizeFromHint(ItemStack stackSize) {
        return switch (stackSize.stackSize) {
            case 1 -> 3;
            case 2 -> 5;
            default -> 7;
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int size = getSizeFromHint(stackSize);
        buildPiece(mName + size, stackSize, hintsOnly, (size - 1) / 2, (size - 1) / 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int size = getSizeFromHint(stackSize);
        return survivialBuildPiece(
                mName + size,
                stackSize,
                (size - 1) / 2,
                (size - 1) / 2,
                0,
                elementBudget,
                env,
                false,
                true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mSize = 0;
        if (checkPiece(mName + "3", 1, 1, 0)) {
            mSize = 3;
            return mCasing >= 10 && checkHatch();
        }
        mCasing = 0;
        if (checkPiece(mName + "5", 2, 2, 0)) {
            mSize = 5;
            return mCasing >= 10 && checkHatch();
        }
        mCasing = 0;
        if (checkPiece(mName + "7", 3, 3, 0)) {
            mSize = 7;
            return mCasing >= 10 && checkHatch();
        }
        return false;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default;
    }

    @Override
    protected int getCasingTextureId() {
        return mCasingTextureID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return mPlasmaMode ? RecipeMaps.plasmaArcFurnaceRecipes : RecipeMaps.arcFurnaceRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.arcFurnaceRecipes, RecipeMaps.plasmaArcFurnaceRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3.5F).setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (this.mSize * 8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialArcFurnace;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings4Misc;
    }

    public byte getCasingMeta() {
        return 3;
    }

    public byte getCasingTextureIndex() {
        return (byte) mCasingTextureID;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (this.mSize > 5) {
            this.mPlasmaMode = !mPlasmaMode;
            if (mPlasmaMode) {
                PlayerUtils.messagePlayer(
                        aPlayer,
                        "[" + EnumChatFormatting.RED
                                + "MODE"
                                + EnumChatFormatting.RESET
                                + "] "
                                + EnumChatFormatting.LIGHT_PURPLE
                                + "Plasma"
                                + EnumChatFormatting.RESET);
            } else {
                PlayerUtils.messagePlayer(
                        aPlayer,
                        "[" + EnumChatFormatting.RED
                                + "MODE"
                                + EnumChatFormatting.RESET
                                + "] "
                                + EnumChatFormatting.YELLOW
                                + "Electric"
                                + EnumChatFormatting.RESET);
            }
        } else {
            PlayerUtils.messagePlayer(
                    aPlayer,
                    "[" + EnumChatFormatting.RED
                            + "MODE"
                            + EnumChatFormatting.RESET
                            + "] "
                            + EnumChatFormatting.GRAY
                            + "Cannot change mode, structure not large enough."
                            + EnumChatFormatting.RESET);
        }
        mLastRecipe = null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mPlasmaMode", mPlasmaMode);
        aNBT.setInteger("mSize", mSize);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mPlasmaMode = aNBT.getBoolean("mPlasmaMode");
        mSize = aNBT.getInteger("mSize");
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 100;
    }
}
