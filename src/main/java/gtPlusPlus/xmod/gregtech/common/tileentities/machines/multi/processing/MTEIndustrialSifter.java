package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
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

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialSifter extends GTPPMultiBlockBase<MTEIndustrialSifter> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<MTEIndustrialSifter> STRUCTURE_DEFINITION = null;

    public MTEIndustrialSifter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialSifter(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialSifter(this.mName);
    }

    @Override
    public String getMachineType() {
        return "gt.recipe.sifter";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addBulkMachineInfo(4, 5f, 0.75f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 3, 5, false)
            .addController("front_bottom_middle")
            .addCasingInfoMin("gtplusplus.blockcasings.2.6.name", 18)
            .addCasingInfoMin("gtplusplus.blockcasings.2.5.name", 35)
            .addInputBus("<casing>", 1)
            .addOutputBus("<casing>", 1)
            .addInputHatch("<casing>", 1)
            .addOutputHatch("<casing>", 1)
            .addEnergyHatch("<casing>", 1)
            .addMaintenanceHatch("<casing>", 1)
            .addMufflerHatch("<casing>", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialSifter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialSifter>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCCCC", "CMMMC", "CMMMC", "CMMMC", "CCCCC" },
                            { "CCCCC", "CMMMC", "CMMMC", "CMMMC", "CCCCC" },
                            { "CC~CC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialSifter.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(TAE.GTPP_INDEX(21))
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 5))))
                .addElement('M', ofBlock(ModBlocks.blockCasings2Misc, 6))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 2, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 2, 2, 0) && mCasing >= 35 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialSifterActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialSifterActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialSifter;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialSifterGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(21);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.sifterRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())
            && (aBaseMetaTileEntity.getFrontFacing() != ForgeDirection.UP)
            && (!aBaseMetaTileEntity.hasCoverAtSide(ForgeDirection.UP))
            && (!aBaseMetaTileEntity.getOpacityAtSide(ForgeDirection.UP))) {
            final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
            if (tRandom.nextFloat() > 0.4) return;

            final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * 2;
            final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * 2;

            aBaseMetaTileEntity.getWorld()
                .spawnParticle(
                    "smoke",
                    (aBaseMetaTileEntity.getXCoord() + xDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
                    aBaseMetaTileEntity.getYCoord() + 2.5f + (tRandom.nextFloat() * 1.2F),
                    (aBaseMetaTileEntity.getZCoord() + zDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
                    0.0,
                    0.0,
                    0.0);
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 5F)
            .setEuModifier(0.75F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialSifter;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_SIFTER_LOOP;
    }
}
