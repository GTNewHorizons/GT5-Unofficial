package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
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
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class MTEIndustrialVacuumFreezer extends GTPPMultiBlockBase<MTEIndustrialVacuumFreezer>
    implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    public static String CASING_NAME;
    public static String HATCH_NAME;
    public static FluidStack CRYO_STACK;
    private static IStructureDefinition<MTEIndustrialVacuumFreezer> STRUCTURE_DEFINITION = null;

    private int mCasing;

    private final ArrayList<MTEHatchCustomFluidBase> mCryotheumHatches = new ArrayList<>();

    public MTEIndustrialVacuumFreezer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 10);

        GregTechAPI.sAfterGTLoad.add(() -> {
            CRYO_STACK = new FluidStack(TFFluids.fluidCryotheum, 1);
            CASING_NAME = GregtechItemList.Casing_AdvancedVacuum.get(1)
                .getDisplayName();
            HATCH_NAME = GregtechItemList.Hatch_Input_Cryotheum.get(1)
                .getDisplayName();
        });
    }

    protected MTEIndustrialVacuumFreezer(MTEIndustrialVacuumFreezer prototype) {
        super(prototype.mName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialVacuumFreezer(this);
    }

    @Override
    public String getMachineType() {
        return "Vacuum Freezer";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Factory Grade Advanced Vacuum Freezer")
            .addStaticParallelInfo(8)
            .addStaticSpeedInfo(2.2f)
            .addStaticEuEffInfo(0.9f)
            .addInfo("Consumes 10L of " + CRYO_STACK.getLocalizedName() + "/s during operation")
            .addInfo("Constructed exactly the same as a normal Vacuum Freezer")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoMin(CASING_NAME, 10, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addOtherStructurePart(HATCH_NAME, "Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialVacuumFreezer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialVacuumFreezer>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEIndustrialVacuumFreezer.class)
                            .adder(MTEIndustrialVacuumFreezer::addCryotheumHatch)
                            .shouldReject(x -> !x.mCryotheumHatches.isEmpty())
                            .hatchId(MetaTileEntityIDs.Hatch_Input_Cryotheum.ID)
                            .casingIndex(CASING_TEXTURE_ID)
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTEIndustrialVacuumFreezer.class)
                            .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                            .casingIndex(CASING_TEXTURE_ID)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 10))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mCasing = 0;
        mCryotheumHatches.clear();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(mName, 1, 1, 0);
    }

    @Override
    public void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        super.validateStructure(errors, context);

        if (mCasing < 10) {
            errors.add(StructureError.TOO_FEW_CASINGS);
            context.setInteger("casings", mCasing);
        }

        if (mCryotheumHatches.isEmpty()) {
            errors.add(StructureError.MISSING_CRYO_HATCH);
        }

        if (mCryotheumHatches.size() > 1) {
            errors.add(StructureError.TOO_MANY_CRYO_HATCHES);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {
        super.localizeStructureErrors(errors, context, lines);

        if (errors.contains(StructureError.TOO_FEW_CASINGS)) {
            lines.add(
                StatCollector.translateToLocalFormatted("GT5U.gui.missing_casings", 10, context.getInteger("casings")));
        }

        if (errors.contains(StructureError.MISSING_CRYO_HATCH)) {
            lines.add(StatCollector.translateToLocalFormatted("GT5U.gui.missing_hatch", HATCH_NAME));
        }

        if (errors.contains(StructureError.TOO_MANY_CRYO_HATCHES)) {
            lines.add(StatCollector.translateToLocalFormatted("GT5U.gui.too_many_hatches", HATCH_NAME, 1));
        }
    }

    private boolean addCryotheumHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchCustomFluidBase && aMetaTileEntity.getBaseMetaTileEntity()
                .getMetaTileID() == MetaTileEntityIDs.Hatch_Input_Cryotheum.ID) {
                return addToMachineListInternal(mCryotheumHatches, aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    public void updateSlots() {
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mCryotheumHatches)) tHatch.updateSlots();
        super.updateSlots();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialVacuumFreezerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialVacuumFreezerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialVacuumFreezer;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialVacuumFreezerGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.advancedFreezerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 2.2F)
            .setEuModifier(0.9F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialVacuumFreezer;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        /*
         * if (this.getBaseMetaTileEntity().isActive()) { if (!this.depleteInput(mFuelStack.copy())) {
         * this.getBaseMetaTileEntity().setActive(false); } }
         */
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (this.mStartUpCheck < 0) {
            if (this.mMaxProgresstime > 0 && this.mProgresstime != 0 || this.getBaseMetaTileEntity()
                .hasWorkJustBeenEnabled()) {
                if (aTick % 20 == 0 || this.getBaseMetaTileEntity()
                    .hasWorkJustBeenEnabled()) {
                    if (!this.depleteInputFromRestrictedHatches(this.mCryotheumHatches, 10)) {
                        this.causeMaintenanceIssue();
                        this.stopMachine(
                            ShutDownReasonRegistry.outOfFluid(new FluidStack(TFFluids.fluidCryotheum, 10)));
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_ADV_FREEZER_LOOP;
    }
}
