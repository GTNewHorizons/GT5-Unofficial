package gregtech.common.tileentities.machines.multi.acal;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ADVANCED_CIRCUIT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ADVANCED_CIRCUIT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ADVANCED_CIRCUIT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.recipe.RecipeMaps.advancedCircuitAssemblylineRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static gregtech.common.misc.WirelessNetworkManager.processInitialSettings;
import static kekztech.util.Util.toStandardForm;

import java.math.BigInteger;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.MTEAdvancedCircuitAssemblyLineGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;

public class MTEAdvancedCircuitAssemblyLine extends MTEEnhancedMultiBlockBase<MTEAdvancedCircuitAssemblyLine>
    implements ISurvivalConstructable {

    private UUID ownerUUID;
    private int eParallel = 64;
    private int eDuration = 70;
    BigInteger finalConsumption = BigInteger.ZERO;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    // Struct lib debug writer: Offsets: 0 16 -2
    private static final int HORIZONTAL_OFFSET = 7;
    private static final int VERTICAL_OFFSET = 7;
    private static final int DEPTH_OFFSET = 1;
    private static final String[][] structure = new String[][] {
        // spotless:off
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB   B   BBB ", "BBB    B    BBB", "BB   BBBBB   BB", "BB  BB   BB  BB", "BB  B     B  BB", "BBBBB     BBBBB", "BB  B     B  BB", "BB  BB   BB  BB", "BB   BBBBB   BB", "BBB    B    BBB", " BBB   B   BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BEE       EEB ", "BBE         EBB", "BE  BBBBBBB  EB", "BE  BFJJJFB  EB", "BE  BJJJJJB  EB", "BE  BJJ~JJB  EB", "BE  BJJJJJB  EB", "BE  BFJJJFB  EB", "BE  BBBBBBB  EB", "BBE         EBB", " BEE       EEB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BEE       EEB ", "BBE         EBB", "BE  BAAAAAB  EB", "BE  A CGC A  EB", "BE  A     A  EB", "BE  AG D GA  EB", "BE  A     A  EB", "BE  A CGC A  EB", "BE  BBBBBBB  EB", "BBE         EBB", " BEE       EEB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BEE       EEB ", "BBE         EBB", "BE  BAAAAAB  EB", "BE  A CGC A  EB", "BE  A CCC A  EB", "BE  AGCDCGA  EB", "BE  A CCC A  EB", "BE  A CGC A  EB", "BE  BBBBBBB  EB", "BBE         EBB", " BEE       EEB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB  BAAAAAB  BB", "BB  A CGC A  BB", "BB  A     A  BB", "BB  AG D GA  BB", "BB  A     A  BB", "BB  A CGC A  BB", "BB  BBBBBBB  BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BBBBBBB    ", "    BGCGCGB    ", "    BGDDDGB    ", "    BGDDDGB    ", "    BGDDDGB    ", "    BGCGCGB    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A CCC A    ", "    AGCDCGA    ", "    A CCC A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB  BAAAAAB  BB", "BB  AFFFFFA  BB", "BB  AFFFFFA  BB", "BB  AFFCFFA  BB", "BB  A FC  A  BB", "BB  A CGC A  BB", "BB  BBBBBBB  BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBGGGGGGGBB  ", " BBG       GBB ", "BBG         GBB", "BG           GB", "BG           GB", "BG           GB", "BG           GB", "BG   FFCFF   GB", "BG           GB", "BG           GB", "BBG         GBB", " BBG       GBB ", "  BBGGGGGGGBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBGGGGGGGBB  ", " BBG   C   GBB ", "BBG    C    GBB", "BG    CCC    GB", "BG   C   C   GB", "BCCCCC   CCCCCB", "BG   C   C   GB", "BG   FCCCF   GB", "BG           GB", "BG           GB", "BBG         GBB", " BBG       GBB ", "  BBGGGGGGGBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBGGGGGGGBB  ", " BBG       GBB ", "BBG         GBB", "BG           GB", "BG           GB", "BG           GB", "BG           GB", "BG   FFCFF   GB", "BG           GB", "BG           GB", "BBG         GBB", " BBG       GBB ", "  BBGGGGGGGBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB  BBBBBBB  BB", "BB  BFFFFFB  BB", "BB  BFFFFFB  BB", "BB  BFFCFFB  BB", "BB  BBFCFBB  BB", "BB  BBBBBBB  BB", "BB   BBBBB   BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A CCC A    ", "    AGCDCGA    ", "    A CCC A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BBBBBBB    ", "    BAAAAAB    ", "    BAFFFAB    ", "    BAFFFAB    ", "    BAFFFAB    ", "    BAFFFAB    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB           BB", "BB           BB", "BB           BB", "BB           BB", "BB     C     BB", "BB           BB", "BB   BBBBB   BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BBE       EBB ", "BBE         EBB", "BE           EB", "BE           EB", "BE           EB", "BE           EB", "BE     C     EB", "BE           EB", "BE           EB", "BBE         EBB", " BBE       EBB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BBE   C   EBB ", "BBE    C    EBB", "BE   CCCCC   EB", "BE   C C C   EB", "BE   C H C   EB", "BE           EB", "BE           EB", "BE   C H C   EB", "BE   C C C   EB", "BBE  CCCCC  EBB", " BBE   C   EBB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BBE       EBB ", "BBE         EBB", "BE           EB", "BE           EB", "BE           EB", "BE           EB", "BE     C     EB", "BE           EB", "BE           EB", "BBE         EBB", " BBE       EBB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB           BB", "BB           BB", "BB           BB", "BB           BB", "BB     C     BB", "BB           BB", "BB   BBBBB   BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "               ", "               ", "               ", "               ", "    BBBBBBB    ", "    BAAAAAB    ", "    BAFFFAB    ", "    BAFFFAB    ", "    BAFFFAB    ", "    BAFFFAB    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    AGCGC A    ", "    AGDDD A    ", "    AGDDDGA    ", "    A  DD A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A CCC A    ", "    AGCDCGA    ", "    A CCC A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB  BAAAAAB  BB", "BB  AFFFFFA  BB", "BB  AFFFFFA  BB", "BB  AFFCFFA  BB", "BB  A FC  A  BB", "BB  A CGC A  BB", "BB  BBBBBBB  BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBGGGGGGGBB  ", " BBG       GBB ", "BBG         GBB", "BG           GB", "BG           GB", "BG           GB", "BG           GB", "BG   FFCFF   GB", "BG           GB", "BG           GB", "BBG         GBB", " BBG       GBB ", "  BBGGGGGGGBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBGGGGGGGBB  ", " BBG   C   GBB ", "BBG    C    GBB", "BG    CCC    GB", "BG   C   C   GB", "BCCCCC   CCCCCB", "BG   C   C   GB", "BG   FCCCF   GB", "BG           GB", "BG           GB", "BBG         GBB", " BBG       GBB ", "  BBGGGGGGGBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBGGGGGGGBB  ", " BBG       GBB ", "BBG         GBB", "BG           GB", "BG           GB", "BG           GB", "BG           GB", "BG   FFCFF   GB", "BG           GB", "BG           GB", "BBG         GBB", " BBG       GBB ", "  BBGGGGGGGBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB  BBBBBBB  BB", "BB  BFFFFFB  BB", "BB  BFFFFFB  BB", "BB  BFFCFFB  BB", "BB  BBFCFBB  BB", "BB  BBBBBBB  BB", "BB   BBBBB   BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A CCC A    ", "    AGCDCGA    ", "    A CCC A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BAAAAAB    ", "    A CGC A    ", "    A     A    ", "    AG D GA    ", "    A     A    ", "    A CGC A    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "               ", "               ", "               ", "               ", "    BBBBBBB    ", "    BGCGCGB    ", "    BGDDDGB    ", "    BGDDDGB    ", "    BGDDDGB    ", "    BGCGCGB    ", "    BBBBBBB    ", "               ", "               ", "               ", "               " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB       BBB ", "BBB         BBB", "BB  BAAAAAB  BB", "BB  A CGC A  BB", "BB  A     A  BB", "BB  AG D GA  BB", "BB  A     A  BB", "BB  A CGC A  BB", "BB  BBBBBBB  BB", "BBB         BBB", " BBB       BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BBE       EBB ", "BBE         EBB", "BE  BAAAAAB  EB", "BE  A CGC A  EB", "BE  A     A  EB", "BE  AG D GA  EB", "BE  A     A  EB", "BE  A CGC A  EB", "BE  BBBBBBB  EB", "BBE         EBB", " BBE       EBB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BBE       EBB ", "BBE         EBB", "BE  BAAAAAB  EB", "BE  A CGC A  EB", "BE  A CCC A  EB", "BE  AGCDCGA  EB", "BE  A CCC A  EB", "BE  A CGC A  EB", "BE  BBBBBBB  EB", "BBE         EBB", " BBE       EBB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBEEEEEEEBB  ", " BBE       EBB ", "BBE         EBB", "BE  BAAAAAB  EB", "BE  A CGC A  EB", "BE  A     A  EB", "BE  AG D GA  EB", "BE  A     A  EB", "BE  A CGC A  EB", "BE  BBBBBBB  EB", "BBE         EBB", " BBE       EBB ", "  BBEEEEEEEBB  ", "   BBBBBBBBB   " },
        { "   BBBBBBBBB   ", "  BBBBBBBBBBB  ", " BBB   B   BBB ", "BBB    B    BBB", "BB  BBBBBBB  BB", "BB  BBBBBBB  BB", "BB  BBFFFBB  BB", "BBBBBBFIFBBBBBB", "BB  BBFFFBB  BB", "BB  BBCGCBB  BB", "BB  BBBBBBB  BB", "BBB    B    BBB", " BBB   B   BBB ", "  BBBBBBBBBBB  ", "   BBBBBBBBB   " },
        { "               ", "               ", "               ", "               ", "               ", "     BBBBB     ", "     B   B     ", "     B   B     ", "     B   B     ", "     BBBBB     ", "               ", "               ", "               ", "               ", "               " }
        // spotless:on
    };

    private static final IStructureDefinition<MTEAdvancedCircuitAssemblyLine> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEAdvancedCircuitAssemblyLine>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', chainAllGlasses())
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 0))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 5))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 9))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings5, 13))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings8, 12))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 14))
        .addElement('H', ofBlock(GregTechAPI.sBlockGem2, 11))
        .addElement(
            'I',

            buildHatchAdder(MTEAdvancedCircuitAssemblyLine.class).atLeast(OutputBus)
                .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12))
                .hint(2)
                .build())
        .addElement(
            'J',
            ofChain(
                buildHatchAdder(MTEAdvancedCircuitAssemblyLine.class)
                    .atLeast(ImmutableMap.of(InputHatch, 1, InputBus, 1))
                    .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12))
                    .hint(1)
                    .buildAndChain(GregTechAPI.sBlockCasings8, 12)))
        .build();

    public MTEAdvancedCircuitAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvancedCircuitAssemblyLine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvancedCircuitAssemblyLine(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Advanced Circuit Assembly Line, Never Actually Coming, ACAL")
            .beginStructureBlock(7, 7, 13, false)
            .addInfo("Do you remember that bottleneck?")
            .addInfo("Set the parallel and the expected craft duration")
            .addInfo("All EU is deducted from wireless EU networks when the recipe start.")
            .addInfo("You should be careful to override default settings on the gui panel.")
            .addInfo("This machine will consume your wireless energy network as easily as you eat a donut.")
            .addStructureInfo("Require no energy hatch and no maintenance hatch")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addInputBus("Any Reinforced Photolithographic Framework Casing", 1)
            .addInputHatch("Any Reinforced Photolithographic Framework Casing", 1)
            .addOutputHatch("Middle Back Reinforced Photolithographic Framework Casing", 2)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ADVANCED_CIRCUIT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ADVANCED_CIRCUIT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ADVANCED_CIRCUIT_ASSEMBLY_LINE)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][16] };
    }

    public int geteParallel() {
        return eParallel;
    }

    public void seteParallel(int eParallel) {
        this.eParallel = eParallel;
    }

    public int geteDuration() {
        return eDuration;
    }

    public void seteDuration(int eDuration) {
        this.eDuration = eDuration;
    }

    @Override
    public int getMaxParallelRecipes() {
        return eParallel;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        // The voltage is only used for recipe finding
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
        logic.setUnlimitedTierSkips();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return advancedCircuitAssemblylineRecipes;
    }

    @Override
    public IStructureDefinition<MTEAdvancedCircuitAssemblyLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            BigInteger recipeEU;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                BigInteger availableEU = getUserEU(ownerUUID);
                long multiplier = (long) recipe.getMetadataOrDefault(GTRecipeConstants.EU_MULTIPLIER, 10);
                recipeEU = BigInteger.valueOf(multiplier * recipe.mEUt * recipe.mDuration);
                if (availableEU.compareTo(recipeEU) < 0) {
                    finalConsumption = BigInteger.ZERO;
                    return CheckRecipeResultRegistry.insufficientStartupPower(recipeEU);
                }
                maxParallel = availableEU.divide(recipeEU)
                    .min(BigInteger.valueOf(maxParallel))
                    .intValue();
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                finalConsumption = recipeEU.multiply(BigInteger.valueOf(-calculatedParallels));
                // This will void the inputs if wireless energy has dropped
                // below the required amount between validateRecipe and here.
                if (!addEUToGlobalEnergyMap(ownerUUID, finalConsumption)) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(finalConsumption);
                }
                // Energy consumed all at once from wireless net.
                overwriteCalculatedEut(0);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                // x2 to match the input value and computed value
                // x10 to shift decimal point into seconds
                // =x20, with theses 2 adjustment when you enter 378 it will run the recipe for 378 seconds
                return new OverclockCalculator().setDuration(geteDuration() * 20);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide() && (aTick == 1)) {
            // Adds player to the wireless network if they do not already exist on it.
            ownerUUID = processInitialSettings(aBaseMetaTileEntity);
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("eParallel", eParallel);
        aNBT.setInteger("eDuration", eDuration);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        eParallel = aNBT.getInteger("eParallel");
        eDuration = aNBT.getInteger("eDuration");
        super.loadNBTData(aNBT);
    }

    public BigInteger getFinalConsumption() {
        return finalConsumption;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + (mMaxProgresstime == 0 ? "0"
                    : toStandardForm(finalConsumption.divide(BigInteger.valueOf(-mMaxProgresstime))))
                + EnumChatFormatting.RESET
                + " EU" };
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEAdvancedCircuitAssemblyLineGui(this);
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

}
