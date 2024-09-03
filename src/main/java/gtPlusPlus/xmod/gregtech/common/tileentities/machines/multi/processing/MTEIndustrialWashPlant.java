package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialWashPlant extends GTPPMultiBlockBase<MTEIndustrialWashPlant>
    implements ISurvivalConstructable {

    private int mCasing;

    private static IStructureDefinition<MTEIndustrialWashPlant> STRUCTURE_DEFINITION = null;

    private static final int MACHINEMODE_OREWASH = 0;
    private static final int MACHINEMODE_SIMPLEWASH = 1;
    private static final int MACHINEMODE_CHEMBATH = 2;

    public MTEIndustrialWashPlant(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialWashPlant(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialWashPlant(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Ore Washer, Simple Washer, Chemical Bath";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Industrial Wash Plant")
            .addInfo("Can be configured with a screwdriver to also do Simple Washer and process Chemical Bathing")
            .addInfo("400% faster than using single block machines of the same voltage")
            .addInfo("Processes four item per voltage tier")
            .addInfo("Always requires an Input Hatch full of water to refill structure")
            .addInfo("Need to be filled with water.")
            .addInfo("Will automatically fill water from input hatch.")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(5, 3, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Wash Plant Casings", 40, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialWashPlant> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialWashPlant>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCCCC", "CwwwC", "CwwwC", "CwwwC", "CwwwC", "CwwwC", "CCCCC" },
                            { "CC~CC", "CwwwC", "CwwwC", "CwwwC", "CwwwC", "CwwwC", "CCCCC" },
                            { "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialWashPlant.class)
                        .atLeast(InputBus, InputHatch, OutputHatch, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(getCasingTextureIndex())
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
                .addElement(
                    'w',
                    ofChain(
                        isAir(),
                        ofBlockAnyMeta(Blocks.water),
                        ofBlockAnyMeta(Blocks.flowing_water),
                        ofBlockAnyMeta(BlocksItems.getFluidBlock(InternalName.fluidDistilledWater))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 2, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 2, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 2, 1, 0) && mCasing >= 40 && checkHatch();
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a washer, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialWashPlantActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialWashPlant;
    }

    @Override
    protected int getCasingTextureId() {
        return getCasingTextureIndex();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (machineMode) {
            case MACHINEMODE_OREWASH -> {
                return RecipeMaps.oreWasherRecipes;
            }
            case MACHINEMODE_SIMPLEWASH -> {
                return GTPPRecipeMaps.simpleWasherRecipes;
            }
            default -> {
                return RecipeMaps.chemicalBathRecipes;
            }
        }
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays
            .asList(RecipeMaps.oreWasherRecipes, GTPPRecipeMaps.simpleWasherRecipes, RecipeMaps.chemicalBathRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (checkForWater()) {
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                return SimpleCheckRecipeResult.ofFailure("no_water");
            }
        }.setSpeedBonus(1F / 5F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        if (machineMode == MACHINEMODE_CHEMBATH)
            return GTPPCore.ConfigSwitches.pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath;
        return GTPPCore.ConfigSwitches.pollutionPerSecondMultiIndustrialWashPlant_ModeWasher;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings2Misc;
    }

    public byte getCasingMeta() {
        return 4;
    }

    public byte getCasingTextureIndex() {
        return (byte) TAE.GTPP_INDEX(11);
    }

    public boolean checkForWater() {

        // Get Facing direction
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        int mDirectionX = aBaseMetaTileEntity.getBackFacing().offsetX;
        int mCurrentDirectionX;
        int mCurrentDirectionZ;
        int mOffsetX_Lower = 0;
        int mOffsetX_Upper = 0;
        int mOffsetZ_Lower = 0;
        int mOffsetZ_Upper = 0;

        if (mDirectionX == 0) {
            mCurrentDirectionX = 2;
            mCurrentDirectionZ = 3;
            mOffsetX_Lower = -2;
            mOffsetX_Upper = 2;
            mOffsetZ_Lower = -3;
            mOffsetZ_Upper = 3;
        } else {
            mCurrentDirectionX = 3;
            mCurrentDirectionZ = 2;
            mOffsetX_Lower = -3;
            mOffsetX_Upper = 3;
            mOffsetZ_Lower = -2;
            mOffsetZ_Upper = 2;
        }

        // if (aBaseMetaTileEntity.fac)

        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * mCurrentDirectionX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * mCurrentDirectionZ;

        int tAmount = 0;
        for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
            for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {
                for (int h = 0; h < 2; ++h) {
                    Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                    if (tBlock == Blocks.air || tBlock == Blocks.flowing_water || tBlock == Blocks.water) {
                        if (this.getStoredFluids() != null) {
                            for (FluidStack stored : this.getStoredFluids()) {
                                if (stored.isFluidEqual(FluidUtils.getFluidStack("water", 1))) {
                                    if (stored.amount >= 1000) {
                                        // Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
                                        stored.amount -= 1000;
                                        Block fluidUsed = null;
                                        if (tBlock == Blocks.air || tBlock == Blocks.flowing_water) {
                                            fluidUsed = Blocks.water;
                                        }
                                        if (tBlock == Blocks.water) {
                                            fluidUsed = BlocksItems.getFluidBlock(InternalName.fluidDistilledWater);
                                        }
                                        aBaseMetaTileEntity.getWorld()
                                            .setBlock(
                                                aBaseMetaTileEntity.getXCoord() + xDir + i,
                                                aBaseMetaTileEntity.getYCoord() + h,
                                                aBaseMetaTileEntity.getZCoord() + zDir + j,
                                                fluidUsed);
                                    }
                                }
                            }
                        }
                    }
                    if (tBlock == Blocks.water) {
                        ++tAmount;
                        // Utils.LOG_WARNING("Found Water");
                    } else if (tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)) {
                        ++tAmount;
                        ++tAmount;
                        // Utils.LOG_WARNING("Found Distilled Water");
                    }
                }
            }
        }

        boolean isValidWater = tAmount >= 45;
        if (isValidWater) {
            Logger.WARNING("Filled structure.");
        } else {
            Logger.WARNING("Did not fill structure.");
        }
        return isValidWater;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mChemicalMode")) {
            boolean aTempMode = aNBT.getBoolean("mChemicalMode");
            if (aTempMode) {
                machineMode = 2;
            } else {
                machineMode = 0;
            }
            aNBT.removeTag("mChemicalMode");
        }
        if (aNBT.hasKey("mMode")) {
            machineMode = aNBT.getInteger("mMode");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.GTPP_MULTI_WASH_PLANT.mode." + machineMode);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("mode", machineMode);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal("GT5U.GTPP_MULTI_WASH_PLANT.mode." + tag.getInteger("mode"))
                + EnumChatFormatting.RESET);
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        PlayerUtils.messagePlayer(
            aPlayer,
            String.format(StatCollector.translateToLocal("GT5U.MULTI_MACHINE_CHANGE"), getMachineModeName()));
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_OREWASH) return MACHINEMODE_SIMPLEWASH;
        else if (machineMode == MACHINEMODE_SIMPLEWASH) return MACHINEMODE_CHEMBATH;
        else return MACHINEMODE_OREWASH;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.clear();
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH);
    }
}
