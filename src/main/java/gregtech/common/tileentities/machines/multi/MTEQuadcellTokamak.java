package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_GLOW_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_ON;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTEQuadcellTokamakGui;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.core.material.MaterialsElements;

public class MTEQuadcellTokamak extends MTEExtendedPowerMultiBlockBase<MTEQuadcellTokamak>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final XSTR random = XSTR.XSTR_INSTANCE;
    private static final int WIDTH_OFFSET = 11;
    private static final int HEIGHT_OFFSET = 6;
    private static final int DEPTH_OFFSET = 1;

    private static final int FORCE_DENSITY = 200_000;
    private static final int RUNITE_DENSITY = 800_000;
    private static final int CELESTIAL_TUNGSTEN_DENSITY = 3_200_000;
    private static final int ORIKALKUM_DENSITY = 8_000_000;

    public static final int FORCE_MAX_DR = 10_000;
    public static final int RUNITE_MAX_DR = 20_000;
    public static final int CELESTIAL_TUNGSTEN_MAX_DR = 80_000;
    public static final int ORIKALKUM_MAX_DR = 320_000;

    private static final float FORCE_MAX_BOOST = 0.15f; // up to 15% more EU/L
    private static final float RUNITE_MAX_BOOST = 0.10f; // up to 10% chance to not consume plasma
    private static final float CELESTIAL_TUNGSTEN_MAX_BOOST = 0.05f; // up to 5% more EU/L and 5% chance to not consume
                                                                     // plasma
    private static final float ORIKALKUM_MAX_BOOST = 1.5f; // up to 50% bonus modifier on top of the others

    public int FORCE_CURRENT_DR = 0;
    public int RUNITE_CURRENT_DR = 0;
    public int CELESTIAL_TUNGSTEN_CURRENT_DR = 0;
    public int ORIKALKUM_CURRENT_DR = 0;

    public float FORCE_CURRENT_BOOST = 0f;
    public float RUNITE_CURRENT_BOOST = 0f;
    public float CELESTIAL_TUNGSTEN_CURRENT_BOOST = 0f;
    public float ORIKALKUM_CURRENT_BOOST = 1f;

    public boolean terminalSwitch = false;

    private static final IStructureDefinition<MTEQuadcellTokamak> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEQuadcellTokamak>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"                       ","          BBB          ","          BBB          "," BB     BBBIBBB     BB ","BIIB    BIIIIIB    BIIB"," BB     BBBIBBB     BB ","          BBB          ","          BBB          ","                       "},
                {"                       ","                       ","                       ","B                     B","B C      C I C      C B","B                     B","                       ","                       ","                       "},
                {"                       ","                       ","                       ","B   E  G       F  D   B","B C E  G C I C F  D C B","B   E  G       F  D   B","                       ","                       ","                       "},
                {"                       ","                       ","    E  G       F  D    ","B   E  G   B   F  D   B","BBCCCCCCCCBIBCCCCCCCCBB","B   E  G   B   F  D   B","    E  G       F  D    ","                       ","                       "},
                {"                       ","    E  G       F  D    ","B   E  G   B   F  D   B","IB        HHH        BI","IC       BHIHB       CI","IB        HHH        BI","B   E  G   B   F  D   B","    E  G       F  D    ","                       "},
                {"    E  G       F  D    ","B   E  G   B   F  D   B","IB        HHH        BI","BC       HHHHH       CB","BB       HHIHH       BB","BC       HHHHH       CB","IB        HHH        BI","B   E  G   B   F  D   B","    E  G       F  D    "},
                {"    E  G       F  D    ","BBCCCCCCCCB~BCCCCCCCCBB","IC       BHHHB       CI","BB       HHHHH       BB","BIAAAAAAAIIIIIAAAAAAAIB","BB       HHHHH       BB","IC       BHHHB       CI","BBCCCCCCCCBBBCCCCCCCCBB","    E  G       F  D    "},
                {"    E  G       F  D    ","B   E  G   B   F  D   B","IB        HHH        BI","BC       HHHHH       CB","BB       HHIHH       BB","BC       HHHHH       CB","IB        HHH        BI","B   E  G   B   F  D   B","    E  G       F  D    "},
                {"                       ","    E  G       F  D    ","B   E  G   B   F  D   B","IB        HHH        BI","IC       BHIHB       CI","IB        HHH        BI","B   E  G   B   F  D   B","    E  G       F  D    ","                       "},
                {"                       ","                       ","    E  G       F  D    ","B   E  G   B   F  D   B","BBCCCCCCCCBIBCCCCCCCCBB","B   E  G   B   F  D   B","    E  G       F  D    ","                       ","                       "},
                {"                       ","                       ","                       ","B   E  G       F  D   B","B C E  G C I C F  D C B","B   E  G       F  D   B","                       ","                       ","                       "},
                {"                       ","                       ","                       ","B                     B","B C      C I C      C B","B                     B","                       ","                       ","                       "},
                {"                       ","          BBB          ","          BBB          "," BB     BBBIBBB     BB ","BIIB    BIIIIIB    BIIB"," BB     BBBIBBB     BB ","          BBB          ","          BBB          ","                       "},

            }))
        //spotless:on
        .addElement('A', Casings.BlackPlutoniumItemPipeCasing.asElement())
        .addElement(
            'B',
            buildHatchAdder(MTEQuadcellTokamak.class).atLeast(Dynamo.or(ExoticDynamo), InputHatch, OutputHatch)
                .casingIndex(Casings.AdvancedIridiumPlatedMachineCasing.textureId)
                .hint(1)
                .buildAndChain(Casings.AdvancedIridiumPlatedMachineCasing.asElement()))
        .addElement('C', ofFrame(Materials.Naquadah))
        .addElement('D', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        .addElement('E', ofBlock(GregTechAPI.sBlockTintedGlass, 5))
        .addElement('F', ofBlock(GregTechAPI.sBlockTintedGlass, 7))
        .addElement('G', ofBlock(GregTechAPI.sBlockTintedGlass, 11))
        .addElement('H', ofSheetMetal(Materials.Americium))
        .addElement('I', ofSheetMetal(Materials.Naquadria))
        .build();

    public MTEQuadcellTokamak(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEQuadcellTokamak(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEQuadcellTokamak> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEQuadcellTokamak(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_TOKAMAK_OFF,
            OVERLAY_TOKAMAK_GLOW,
            OVERLAY_TOKAMAK_ON,
            OVERLAY_TOKAMAK_GLOW_ON);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.AdvancedIridiumPlatedMachineCasing.getCasingTexture();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Tokamak, QT")
            .beginStructureBlock(23, 13, 9, true)
            .addController("Front center, 2nd layer")
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    private static final int CYCLE_TIME = 20;

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {

        // Increment Bonuses, if needed, up to the cap.

        // Bonuses increment every cycle until they are at the cap of their respective bonus.
        // It takes 10 full cycles to increment the bonus all the way (10 seconds)
        // The bonus cap is equal to [SET_DR / MAX_DR] * MAX_BONUS

        if (FORCE_CURRENT_BOOST != FORCE_MAX_BOOST) {
            float FORCE_CURRENT_CAP = FORCE_MAX_BOOST * FORCE_CURRENT_DR / FORCE_MAX_DR;
            float step = FORCE_CURRENT_CAP / 10;
            FORCE_CURRENT_BOOST = Math.min(FORCE_MAX_BOOST, FORCE_CURRENT_BOOST + step);
        }

        if (RUNITE_CURRENT_BOOST != RUNITE_MAX_BOOST) {
            float RUNITE_CURRENT_CAP = RUNITE_MAX_BOOST * RUNITE_CURRENT_DR / RUNITE_MAX_DR;
            float step = RUNITE_CURRENT_CAP / 10;
            RUNITE_CURRENT_BOOST = Math.min(RUNITE_MAX_BOOST, RUNITE_CURRENT_BOOST + step);
        }

        if (CELESTIAL_TUNGSTEN_CURRENT_BOOST != CELESTIAL_TUNGSTEN_MAX_BOOST) {
            float CELESTIAL_TUNGSTEN_CURRENT_CAP = CELESTIAL_TUNGSTEN_MAX_BOOST * CELESTIAL_TUNGSTEN_CURRENT_DR
                / CELESTIAL_TUNGSTEN_MAX_DR;
            float step = CELESTIAL_TUNGSTEN_CURRENT_CAP / 10;
            CELESTIAL_TUNGSTEN_CURRENT_BOOST = Math
                .min(CELESTIAL_TUNGSTEN_MAX_BOOST, CELESTIAL_TUNGSTEN_CURRENT_BOOST + step);
        }

        if (ORIKALKUM_CURRENT_BOOST != ORIKALKUM_MAX_BOOST) {
            float ORIKALKUM_CURRENT_CAP = ORIKALKUM_MAX_BOOST * ORIKALKUM_CURRENT_DR / ORIKALKUM_MAX_DR;
            float step = ORIKALKUM_CURRENT_CAP / 10;
            ORIKALKUM_CURRENT_BOOST = Math.min(ORIKALKUM_MAX_BOOST, ORIKALKUM_CURRENT_BOOST + step);
        }

        float eutBoost = 1 + ((FORCE_CURRENT_BOOST + CELESTIAL_TUNGSTEN_CURRENT_BOOST) * ORIKALKUM_CURRENT_BOOST); // >1
        float nonDrainChance = (RUNITE_CURRENT_BOOST + CELESTIAL_TUNGSTEN_CURRENT_BOOST) * ORIKALKUM_CURRENT_BOOST; // <1
        lEUt = 0;
        // Try to drain all configured Fluids
        FluidStack forceStack = new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), FORCE_CURRENT_DR);
        if (FORCE_CURRENT_DR > 0 && random.nextFloat() > nonDrainChance && !this.depleteInput(forceStack)) {
            // noinspection ConstantConditions
            crashMachine(forceStack);
        }
        lEUt += (long) (eutBoost * FORCE_CURRENT_DR * FORCE_DENSITY / 20);

        FluidStack runiteStack = new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), RUNITE_CURRENT_DR);
        if (RUNITE_CURRENT_DR > 0 && random.nextFloat() > nonDrainChance && !this.depleteInput(runiteStack)) {
            // noinspection ConstantConditions
            crashMachine(runiteStack);
        }
        lEUt += (long) (eutBoost * RUNITE_CURRENT_DR * RUNITE_DENSITY / 20);

        FluidStack celestialtungstenStack = new FluidStack(
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(),
            CELESTIAL_TUNGSTEN_CURRENT_DR);
        if (CELESTIAL_TUNGSTEN_CURRENT_DR > 0 && random.nextFloat() > nonDrainChance
            && !this.depleteInput(celestialtungstenStack)) {
            // noinspection ConstantConditions
            crashMachine(celestialtungstenStack);
        }
        lEUt += (long) (eutBoost * CELESTIAL_TUNGSTEN_CURRENT_DR * CELESTIAL_TUNGSTEN_DENSITY / 20);

        FluidStack orikalkumStack = Materials.Orikalkum.getPlasma(ORIKALKUM_CURRENT_DR);
        if (ORIKALKUM_CURRENT_DR > 0 && random.nextFloat() > nonDrainChance && !this.depleteInput(orikalkumStack)) {
            // noinspection ConstantConditions
            crashMachine(orikalkumStack);
        }
        lEUt += (long) (eutBoost * ORIKALKUM_CURRENT_DR * ORIKALKUM_DENSITY / 20);

        // if fluid can't be drained, turn off and clear all bonuses

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        recipesDone++;

        return super.checkProcessing();
    }

    public void crashMachine(FluidStack stack) {
        FORCE_CURRENT_BOOST = 0;
        RUNITE_CURRENT_BOOST = 0;
        CELESTIAL_TUNGSTEN_CURRENT_BOOST = 0;
        ORIKALKUM_CURRENT_BOOST = 1;
        stopMachine(ShutDownReasonRegistry.outOfFluid(stack));
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // cannot have maintenance issues, so do nothing
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, WIDTH_OFFSET, HEIGHT_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            WIDTH_OFFSET,
            HEIGHT_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, WIDTH_OFFSET, HEIGHT_OFFSET, DEPTH_OFFSET, errors)) return;
        checkOneDynamoHatchMaybeExotic(errors);
        checkHasInputHatch(errors);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        aNBT.setInteger("ForceDR", FORCE_CURRENT_DR);
        aNBT.setInteger("RuniteDR", RUNITE_CURRENT_DR);
        aNBT.setInteger("CelestialDR", CELESTIAL_TUNGSTEN_CURRENT_DR);
        aNBT.setInteger("OrikalkumDR", ORIKALKUM_CURRENT_DR);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        this.FORCE_CURRENT_DR = aNBT.getInteger("ForceDR");
        this.RUNITE_CURRENT_DR = aNBT.getInteger("RuniteDR");
        this.CELESTIAL_TUNGSTEN_CURRENT_DR = aNBT.getInteger("CelestialDR");
        this.ORIKALKUM_CURRENT_DR = aNBT.getInteger("OrikalkumDR");
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    protected @NotNull MTEQuadcellTokamakGui getGui() {
        return new MTEQuadcellTokamakGui(this);
    }
}
