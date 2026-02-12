package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorKuba;
import static gregtech.api.enums.GTValues.AuthorPxx500;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.tooltip.TooltipHelper;
import kubatech.loaders.ArcFurnaceElectrode;

public class MTEIndustrialArcFurnace extends MTEExtendedPowerMultiBlockBase<MTEIndustrialArcFurnace>
    implements ISurvivalConstructable {

    public MTEIndustrialArcFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEIndustrialArcFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialArcFurnace(this.mName);
    }

    private int mCasing = 0;

    private static final int OFFSET_H = 10;
    private static final int OFFSET_V = 7;
    private static final int OFFSET_D = 1;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEIndustrialArcFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialArcFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { // spotless:off
                    {"                 ","                 ","                 ","                 ","         DDD     ","        D   D    ","        D   D    ","        D   D    ","         DDD     ","         D D     ","         D D     ","         D D     ","         D D     ","         D D     ","         D D     ","         D D     ","         D D     ","         DDD     ","                 "},
                    {"                 ","                 ","                 ","                 ","                 ","         HDH     ","         D D     ","         DHD     ","          D      ","          D      ","          D      ","          D      ","          D      ","          D      ","          D      ","        A D A    ","        AD DA    ","                 ","         DGD     "},
                    {"                 ","                 ","                 ","                 ","                 ","         H H     ","                 ","          H      ","                 ","                 ","                 ","                 ","  E              "," EDE             ","  E              ","        A   A    ","        DD DD    ","        A   A    ","         DGD     "},
                    {"                 ","                 ","                 ","                 ","                 ","         H H     ","                 ","          H      ","                 ","                 ","                 ","          F      ","  E      FF      "," E EFFFFFF       ","  E              ","        A   A    ","        DD DD    ","        A   A    ","         GGG     "},
                    {"                 ","                 ","                 ","          B      ","        BBBBB    ","        BH HB    ","       BB   BB   ","        BBHBB    ","        BBBBB    ","                 ","          F      ","          F      "," DED             "," E E             "," DED             ","        A   A    ","        DD DD    ","        A   A    ","         GGG     "},
                    {"                 ","        EEEEE    ","       EEBBBEE   ","      EBBB BBBE  ","     EEB     BEE ","     EBB H H BBE ","     EB       BE ","     EBB  H  BBE ","     EEB     BEE ","      EBBBBBBBE  ","       EEBBBEE   ","        EEEEE    "," DED             "," E E             "," DED             ","        A   A    ","        DD DD    ","        A   A    ","         GGG     "},
                    {"        D   D    ","       AABABAA   ","      A       A  ","     A         A ","    DA         AD","     B         B ","     C         C ","     B         B ","    DA         AD","     A         A ","      A       A  ","       AABCBAA   "," DED    D   D    "," E E             "," DED             ","        A   A    ","        DD DD    ","        A   A    ","         GGG     "},
                    {"        D   D    ","       GAB~BAG   ","      G       G  ","     G         G ","    DA         AD","     B         B ","     C         C ","     B         B ","    DA         AD","     G         G ","      G       G  ","       GABCBAG   "," DED    D   D    "," E E             "," DED             ","        A   A    ","        DD DD    ","        A   A    ","         GGG     "},
                    {"        D   D    ","       AABABAA   ","      A       A  ","     A         A ","    DA         AD","     B         B ","     C         C ","     B         B ","    DA         AD","     A         A ","      A       A  ","       AABCBAA   "," DED    D   D    "," E E             "," DED    A   A    ","        A   A    ","        DD DD    ","        A   A    ","         DGD     "},
                    {"        D   D    ","        A   A    ","       AAAAAAA   ","      AAAACAAAA  ","    DAAAACCCAAAAD","      AACC CCAA  ","      CCC   CCC  ","      AACC CCAA  ","    DAAAACCCAAAAD","      AAAACAAAA  ","       AAACAAA   "," BBB             ","BEEEB   D   D    ","BE EB            ","BEEEB   A   A    "," BBB    A   A    ","        DD DD    ","        A   A    ","        ADDDA    "},
                    {"        D   D    ","      DDA   ADD  ","     D   AAA   D ","     D    A    D ","    DA    A    AD","    D     A     D","    D    AAA    D","    D     A     D","    DA         AD","     D         D ","     D         D "," BBB  DD     DD  ","B   B   D   D    ","B   B            ","B   B   A   A    "," BBB    A   A    ","        AD DA    ","        A   A    ","        ADDDA    "}
                } // spotless:on
            ))
        .addElement(
            'A',
            buildHatchAdder(MTEIndustrialArcFurnace.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Muffler, Energy, ExoticEnergy)
                .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(e -> e.mCasing++, Casings.SolidSteelMachineCasing.asElement())))
        .addElement('B', Casings.SteelPipeCasing.asElement())
        .addElement('C', Casings.CupronickelCoilBlock.asElement())
        .addElement('D', ofBlock(GregTechAPI.sBlockFrames, 305))
        .addElement('E', Casings.CleanStainlessSteelCasing.asElement())
        .addElement('F', Casings.InsulatedFluidPipeCasing.asElement())
        .addElement('G', Casings.HeatProofCokeOvenCasing.asElement())
        .addElement('H', ofBlock(GregTechAPI.sBlockCasings13, 4)) // Block.getBlockFromName(Mods.NewHorizonsCoreMod.ID +
                                                                  // ":tile.CompressedGraphite")
        .build();

    @Override
    public IStructureDefinition<MTEIndustrialArcFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_H, OFFSET_V, OFFSET_D);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_H,
            OFFSET_V,
            OFFSET_D,
            elementBudget,
            env,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_H, OFFSET_V, OFFSET_D)) return false;
        if (mCasing < 10) return false;
        return true;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Arc Furnace, Plasma Arc Furnace, IAF")
            .addInfo(TooltipHelper.parallelText("Width * Voltage Tier") + " Parallels")
            .addInfo(TooltipHelper.parallelText("8x") + " Parallels in Plasma Mode")
            .addStaticSpeedInfo(3.5f)
            .addStaticEuEffInfo(1f)
            .addInfo("Right-click controller with a Screwdriver to change modes")
            .addInfo("Max Size required to process Plasma recipes")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(17, 11, 19, false)
            .addController("Front center")
            .addCasingInfoMin("Solid Steel Machine Casing", 10, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addAuthors(AuthorKuba, AuthorPxx500)
            .addStructureAuthors(EnumChatFormatting.LIGHT_PURPLE + "Sol_IX")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        final ITexture casingTexture = Casings.SolidSteelMachineCasing.getCasingTexture();
        if (side == facing) {
            if (active) return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexture };
    }

    enum ArcFurnacePhase {
        Standby,
        ArcIgnition,
        Processing,
        ArcShutdown

    }

    ArcFurnacePhase phase = ArcFurnacePhase.Standby;

    ArcFurnaceElectrode electrode = ArcFurnaceElectrode.TritaniumElectrode;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return arcFurnaceRecipes;
    }

    // GTUtility.getTier(this.getMaxInputVoltage())
    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setSpeedBonus(1d / electrode.speedModifier);
        logic.setMaxParallel(electrode.parallelLimit);
        logic.setOverclock(electrode.OCSpeedFactor, electrode.OCPowerFactor);
        logic.setEuModifier(electrode.amperagePerParallel);
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(getMaxInputAmps());
        // logic.setAmperageOC(false);
        logic.setMaxTierSkips(0);
        logic.noRecipeCaching();
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        if (mMaxProgresstime <= 0 && phase != ArcFurnacePhase.Standby) {
            phase = ArcFurnacePhase.ArcShutdown;
            mMaxProgresstime = 20 * 6;
            lEUt = 0;
            checkRecipeResult = SimpleCheckRecipeResult.ofSuccess("arc_shutdown");
        }
    }

    @Override
    protected void outputAfterRecipe() {
        super.outputAfterRecipe();
        if (phase == ArcFurnacePhase.ArcIgnition) {
            phase = ArcFurnacePhase.Processing;
        } else if (phase == ArcFurnacePhase.ArcShutdown) {
            phase = ArcFurnacePhase.Standby;
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);
        if (phase == ArcFurnacePhase.ArcIgnition) {
            phase = ArcFurnacePhase.Standby;
        } else if (phase == ArcFurnacePhase.Processing) {
            phase = ArcFurnacePhase.ArcShutdown;
        }
    }

    @Override
    protected @NotNull CheckRecipeResult postCheckRecipe(@NotNull CheckRecipeResult result,
        @NotNull ProcessingLogic processingLogic) {
        result = super.postCheckRecipe(result, processingLogic);
        // if (!result.wasSuccessful()) {
        // if (phase != ArcFurnacePhase.Standby) {
        // phase = ArcFurnacePhase.ArcShutdown;
        // processingLogic.overwriteCalculatedDuration(20 * 6);
        // processingLogic.overwriteCalculatedEut(0);
        // processingLogic.overwriteOutputFluids();
        // processingLogic.overwriteOutputItems();
        // result = SimpleCheckRecipeResult.ofSuccess("arc_shutdown");
        // }
        // }
        return result;
    }

    private GTRecipe fakeRecipe() {
        return new GTRecipe(
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            20 * 6,
            (int) (getAverageInputVoltage() * 30 / 32 * electrode.parallelLimit * (electrode.startupSurge + 1d)),
            0);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                return super.onRecipeStart(recipe);
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (phase == ArcFurnacePhase.ArcIgnition) return SimpleCheckRecipeResult.ofSuccess("arc_ignition");
                return super.validateRecipe(recipe);
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                if (phase == ArcFurnacePhase.ArcIgnition)
                    return super.createOverclockCalculator(fakeRecipe()).setNoOverclock(true)
                        .setAmperage(1)
                        .setEUt(this.availableVoltage * this.availableAmperage)
                        .setDurationModifier(1);
                return super.createOverclockCalculator(recipe)
                    .setMaxOverclocks((int) GTUtility.log4(this.availableVoltage / Math.max((long) recipe.mEUt, 32)));
            }

            @Override
            protected @NotNull ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                if (phase == ArcFurnacePhase.Standby) {
                    phase = ArcFurnacePhase.ArcIgnition;

                    return super.createParallelHelper(fakeRecipe()).setConsumption(false)
                        .setMaxParallel(1);
                }

                return super.createParallelHelper(recipe);
            }

        };
    }
}
