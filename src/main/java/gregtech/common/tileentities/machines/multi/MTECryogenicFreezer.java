package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.CryotheumHatch;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class MTECryogenicFreezer extends MTEExtendedPowerMultiBlockBase<MTECryogenicFreezer>
    implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    public static String CASING_NAME;
    public static String HATCH_NAME;
    public static FluidStack CRYO_STACK;
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MTECryogenicFreezer> STRUCTURE_DEFINITION = null;

    private int casingAmount;

    private final ArrayList<MTEHatchCustomFluidBase> mCryotheumHatches = new ArrayList<>();

    public MTECryogenicFreezer(final int aID, final String aName, final String aNameRegional) {
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

    protected MTECryogenicFreezer(MTECryogenicFreezer prototype) {
        super(prototype.mName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTECryogenicFreezer(this);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Vacuum Freezer")
            .addInfo("Factory Grade Advanced Vacuum Freezer")
            .addStaticParallelInfo(8)
            .addStaticSpeedInfo(2.2f)
            .addStaticEuEffInfo(0.9f)
            .addInfo("Consumes 10L of " + CRYO_STACK.getLocalizedName() + "/s during operation")
            .addInfo("Constructed exactly the same as a normal Vacuum Freezer")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
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
    public IStructureDefinition<MTECryogenicFreezer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTECryogenicFreezer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { "     ", " BBB ", " B~B ", " BBB " }, { " ABA ", "ABBBA", "ABBBA", "ABBBA" },
                        { "  B  ", " BBB ", " BBB ", " BBB " }, { " ABA ", "ABBBA", "ABBBA", "ABBBA" },
                        { "  B  ", " BBB ", " BBB ", " BBB " }, { " ABA ", "ABBBA", "ABBBA", "ABBBA" },
                        { "     ", " BBB ", " BBB ", " BBB " } })
                .addElement('A', ofFrame(Materials.Steel))
                .addElement(
                    'B',
                    ofChain(
                        buildHatchAdder(MTECryogenicFreezer.class)
                            .atLeast(
                                InputBus,
                                OutputBus,
                                Maintenance,
                                Energy,
                                Muffler,
                                InputHatch,
                                OutputHatch,
                                CryotheumHatch)
                            .casingIndex(Casings.AdvancedCryogenicCasing.textureId)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.casingAmount, Casings.AdvancedCryogenicCasing.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        casingAmount = 0;
        mCryotheumHatches.clear();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        super.validateStructure(errors, context);

        if (casingAmount < 10) {
            errors.add(StructureError.TOO_FEW_CASINGS);
            context.setInteger("casings", casingAmount);
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

    @Override
    public void updateSlots() {
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mCryotheumHatches)) tHatch.updateSlots();
        super.updateSlots();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.AdvancedCryogenicCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialVacuumFreezerActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialVacuumFreezerActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.AdvancedCryogenicCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAIndustrialVacuumFreezer)
                .extFacing()
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialVacuumFreezerGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.AdvancedCryogenicCasing.getCasingTexture() };
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
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (this.mStartUpCheck < 0) {
            if (this.mMaxProgresstime > 0 && this.mProgresstime != 0 || this.getBaseMetaTileEntity()
                .hasWorkJustBeenEnabled()) {
                if (aTick % 20 == 0 || this.getBaseMetaTileEntity()
                    .hasWorkJustBeenEnabled()) {

                    if (!drainCryotheum(10)) {
                        this.causeMaintenanceIssue();
                        this.stopMachine(
                            ShutDownReasonRegistry.outOfFluid(new FluidStack(TFFluids.fluidCryotheum, 10)));
                    }

                }
            }
        }
    }

    private boolean drainCryotheum(int amount) {
        FluidStack toDrain = new FluidStack(TFFluids.fluidCryotheum, amount);
        for (MTEHatchCustomFluidBase hatch : validMTEList(mCryotheumHatches)) {
            FluidStack drained = hatch.drain(ForgeDirection.UNKNOWN, toDrain, true);
            if (drained != null && drained.amount >= amount) {
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_ADV_FREEZER_LOOP;
    }
}
