package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;

public class MTEOilDrillInfinite extends MTEOilDrillBase {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 13;
    private static final int OFFSET_Z = 0;
    private int casingAmount;
    private static final int parallels = 2;

    private static IStructureDefinition<MTEOilDrillInfinite> STRUCTURE_DEFINITION = null;

    @Override
    @SuppressWarnings("unchecked")
    public IStructureDefinition<MTEDrillerBase> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEOilDrillInfinite>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] {
                        { "         ", "         ", "         ", "         ", "         ", "         ", "         ",
                            "         ", "         ", "         ", "         ", "         ", "         ", "  BB~BB  " },
                        { "         ", "         ", "         ", "         ", "         ", "         ", "         ",
                            "         ", "         ", "         ", "         ", "  B   B  ", " GB   BG ", " BBFFFBB " },
                        { "         ", "         ", "         ", "         ", "         ", "         ", "    G    ",
                            "    G    ", "   GBG   ", "   B B   ", "  GB BG  ", " BB   BB ", " BB   BB ", "BBBCCCBBB" },
                        { "         ", "         ", "         ", "    G    ", "    G    ", "   GBG   ", "   BBB   ",
                            "   BBB   ", "  GBABG  ", "  B A B  ", "  B A B  ", "    A    ", "    A    ", "BFCDDDCFB" },
                        { "    G    ", "    G    ", "    B    ", "   GBG   ", "   GBG   ", "   BHB   ", "  GBHBG  ",
                            "  GBHBG  ", "  BAHAB  ", "   AHA   ", "   AHA   ", "   AHA   ", "   AHA   ", "BFCDDDCFB" },
                        { "         ", "         ", "         ", "    G    ", "    G    ", "   GBG   ", "   BBB   ",
                            "   BBB   ", "  GBABG  ", "  B A B  ", "  B A B  ", "    A    ", "    A    ", "BFCDDDCFB" },
                        { "         ", "         ", "         ", "         ", "         ", "         ", "    G    ",
                            "    G    ", "   GBG   ", "   B B   ", "  GB BG  ", " BB   BB ", " BB   BB ", "BBBCCCBBB" },
                        { "         ", "         ", "         ", "         ", "         ", "         ", "         ",
                            "         ", "         ", "         ", "         ", "  B   B  ", " GB   BG ", " BBFFFBB " },
                        { "         ", "         ", "         ", "         ", "         ", "         ", "         ",
                            "         ", "         ", "         ", "         ", "         ", "         ",
                            "  BBBBB  " } })
                .addElement('A', chainAllGlasses())
                .addElement(
                    'B',
                    buildHatchAdder(MTEOilDrillInfinite.class).atLeast(InputBus, OutputHatch, Maintenance, Energy)
                        .casingIndex(Casings.MiningNeutroniumCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.MiningNeutroniumCasing.asElement())))
                .addElement('C', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
                .addElement('D', Casings.PBIPipeCasing.asElement())
                .addElement('F', Casings.AdvancedComputerCasing.asElement())
                .addElement('G', ofFrame(Materials.Neutronium))
                .addElement('H', ofSheetMetal(Materials.Infinity))
                .build();
        }
        return (IStructureDefinition<MTEDrillerBase>) (IStructureDefinition<?>) STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        final int baseCycleTime = calculateMaxProgressTime(getMinTier(), true);
        tt.addMachineType("Pump, FDR")
            .addInfo(
                EnumChatFormatting.GOLD + formatNumber(parallels)
                    + EnumChatFormatting.GRAY
                    + " Parallels per "
                    + EnumChatFormatting.WHITE
                    + "Energy Hatch"
                    + EnumChatFormatting.GRAY
                    + " Tier above "
                    + GTUtility.getColoredTierNameFromTier((byte) getMinTier()))
            .addInfo("Works on " + getRangeInChunks() + "x" + getRangeInChunks() + " chunks")
            .addInfo("Use a Screwdriver to configure range")
            .addInfo("Use Programmed Circuits to ignore near exhausted oil field")
            .addInfo("If total circuit # is greater than output per operation, the machine will halt.")
            .addInfo("Minimum energy hatch tier: " + GTUtility.getColoredTierNameFromTier((byte) getMinTier()))
            .addInfo(
                "Base cycle time: "
                    + (baseCycleTime < 20 ? formatNumber(baseCycleTime) + (baseCycleTime == 1 ? " tick" : " ticks")
                        : formatNumber(baseCycleTime / 20.0) + " seconds"))
            .beginStructureBlock(9, 9, 14, false)
            .addController("Front bottom center")
            .addCasing("90-103", "Mining Neutronium Casing", false)
            .addCasing("38", "Neutronium Frame Box", false)
            .addCasing("20", "Any Tiered Glass", false)
            .addCasing("12", "Advanced Iridium Plated Machine Casing", false)
            .addCasing("12", "Advanced Computer Casing", false)
            .addCasing("9", "PBI Pipe Casing", false)
            .addCasing("8", "Infinity Sheetmetal", false)
            .addEnergyHatch("1", "Any mining casing (UHV+)", 1)
            .addMaintenanceHatch("1", "Any mining casing", 1)
            .addInputBus("0-1", "Any mining casing", 1)
            .addOutputHatch("1", "Any mining casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.Rain"))
            .addStructureAuthors(EnumChatFormatting.GOLD + "Pix3lated")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        updateCoordinates();
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 90);
        checkHatches(errors);
        if (!mEnergyHatches.isEmpty()) {
            if (GTUtility.getTier(getMaxInputVoltage()) < getMinTier()) {
                errors.add(StructureErrors.energyHatchTierTooLow(getMinTier()));
            }
        }
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

    public MTEOilDrillInfinite(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrillInfinite(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOilDrillInfinite(mName);
    }

    @Override
    protected FluidStack pumpOil(float speed, boolean simulate) {
        // always simulate to not deplete vein
        return super.pumpOil(speed, true);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    protected boolean requiresMiningPipes() {
        return false;
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_MiningNeutronium;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Neutronium;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 178;
    }

    @Override
    protected int getRangeInChunks() {
        return 8;
    }

    @Override
    protected float computeSpeed() {
        return .5F + (GTUtility.getTier(getMaxInputVoltage()) - getMinTier() + 5) * .25F;
    }

    @Override
    protected int getMinTier() {
        return 9;
    }

    @Override
    protected FluidStack adjustPumpedOil(FluidStack pumpedOil) {
        if (pumpedOil == null) {
            return null;
        }
        pumpedOil.amount *= getParallelCount();
        return pumpedOil;
    }

    protected int getParallelCount() {
        return (GTUtility.getTier(getMaxInputVoltage()) - getMinTier()) * parallels + 1;
    }
}
