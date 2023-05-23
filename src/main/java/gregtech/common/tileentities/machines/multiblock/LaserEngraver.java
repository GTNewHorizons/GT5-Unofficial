package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.*;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ENERGY_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_OUT;
import static gregtech.loaders.preload.GT_Loader_MultiTileEntities.CASING_REGISTRY_NAME;
import static gregtech.loaders.preload.GT_Loader_MultiTileEntities.UPGRADE_CASING_REGISTRY_NAME;

import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;
import gregtech.api.util.GT_ParallelHelper;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.enums.GT_MultiTileUpgradeCasing;
import gregtech.api.multitileentity.multiblock.base.ComplexParallelController;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_StructureUtility;
import gregtech.common.tileentities.machines.multiblock.logic.GenericProcessingLogic;
import scala.collection.Parallel;

import java.util.ArrayList;
import java.util.List;

public class LaserEngraver extends ComplexParallelController<LaserEngraver> implements ProcessingLogicHost {

    private static IStructureDefinition<LaserEngraver> STRUCTURE_DEFINITION = null;
    private final ProcessingLogic PROCESSING_LOGIC = new GenericProcessingLogic(
        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes);
    protected static final String STRUCTURE_MAIN = "Main";
    protected static final String STRUCTURE_PIECE_T1 = "T1";
    protected static final String STRUCTURE_PIECE_T2 = "T2";
    protected static final String STRUCTURE_PIECE_T3 = "T3";
    protected static final String STRUCTURE_PIECE_T4 = "T4";
    protected static final String STRUCTURE_PIECE_T5 = "T5";
    protected static final String STRUCTURE_PIECE_T6 = "T6";
    protected static final int PROCESS_WINDOW_BASE_ID = 100;
    protected static final Vec3Impl STRUCTURE_OFFSET_T1 = new Vec3Impl(3, 1, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T2 = new Vec3Impl(1, 3, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T3 = new Vec3Impl(-6, 0, -5);
    protected static final Vec3Impl STRUCTURE_OFFSET_T4 = new Vec3Impl(18, 0, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T5 = new Vec3Impl(-18, 0, 9);
    protected static final Vec3Impl STRUCTURE_OFFSET_T6 = new Vec3Impl(18, 0, 0);
    protected static final int MAX_PROCESSES = 6;
    protected static final String Parallel = "MAX_PARALLEL";


    public LaserEngraver() {
        super();
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.laserengraver";
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return STRUCTURE_OFFSET_T1;
    }

    @Override
    public boolean checkMachine() {
        buildState.startBuilding(getStartingStructureOffset());
        if (!checkPiece(STRUCTURE_PIECE_T1, buildState.getCurrentOffset())) return buildState.failBuilding();
        if (maxComplexParallels > 1) {
            buildState.addOffset(STRUCTURE_OFFSET_T2);
            if (!checkPiece(STRUCTURE_PIECE_T2, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 2) {
            buildState.addOffset(STRUCTURE_OFFSET_T3);
            if (!checkPiece(STRUCTURE_PIECE_T3, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 3) {
            buildState.addOffset(STRUCTURE_OFFSET_T4);
            if (!checkPiece(STRUCTURE_PIECE_T4, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 4) {
            buildState.addOffset(STRUCTURE_OFFSET_T5);
            if (!checkPiece(STRUCTURE_PIECE_T5, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 5) {
            buildState.addOffset(STRUCTURE_OFFSET_T6);
            if (!checkPiece(STRUCTURE_PIECE_T6, buildState.getCurrentOffset())) return buildState.failBuilding();
        }

        buildState.stopBuilding();
        return super.checkMachine();
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(STRUCTURE_PIECE_T1, trigger, hintsOnly, buildState.getCurrentOffset());
        if (maxComplexParallels > 1) {
            buildState.addOffset(STRUCTURE_OFFSET_T2);
            buildPiece(STRUCTURE_PIECE_T2, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 2) {
            buildState.addOffset(STRUCTURE_OFFSET_T3);
            buildPiece(STRUCTURE_PIECE_T3, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 3) {
            buildState.addOffset(STRUCTURE_OFFSET_T4);
            buildPiece(STRUCTURE_PIECE_T4, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 4) {
            buildState.addOffset(STRUCTURE_OFFSET_T5);
            buildPiece(STRUCTURE_PIECE_T5, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 5) {
            buildState.addOffset(STRUCTURE_OFFSET_T6);
            buildPiece(STRUCTURE_PIECE_T6, trigger, hintsOnly, buildState.getCurrentOffset());
        }

        buildState.stopBuilding();
    }

    @Override
    public IStructureDefinition<LaserEngraver> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<LaserEngraver>builder()
                .addShape(
                    STRUCTURE_PIECE_T1,
                    transpose(
                        new String[][]{{"ACADA", "AAAAA", "AAAAA"}, {"GGA~A", "H   I", "GGAAA"},
                            {"AAAAA", "ABBBA", "AAAAA"}}))
                .addShape(
                    STRUCTURE_PIECE_T2,
                    new String[][]{{"       ", "       ", "       ", "       ", "       ", "       "},
                        {"  K    ", "  K    ", "  K    ", "       ", "       ", "       "},
                        {"  K    ", "       ", "       ", "       ", "       ", "       "},
                        {"  K    ", "       ", "       ", "       ", "       ", "       "},
                        {"  K    ", "       ", "       ", "       ", "       ", "       "},
                        {"  K    ", "FBF FBF", " G   G ", " G   G ", " G   G ", "FBF FBF"},
                        {" KKKKK ", "BIB BIB", "G G G G", "G G G G", "G G G G", "BHB BHB"},
                        {" K   K ", "FBF FBF", " G   G ", " G   G ", " G   G ", "FBF FBF"},
                        {" K   K ", "       ", "       ", "       ", "       ", "       "},
                        {" K   K ", "FBF FBF", " G   G ", " G   G ", " G   G ", "FBF FBF"},
                        {" KKKKK ", "BIB BIB", "G G G G", "G G G G", "G G G G", "BHB BHB"},
                        {"       ", "FBF FBF", " G   G ", " G   G ", " G   G ", "FBF FBF",}})
                .addShape(
                    STRUCTURE_PIECE_T3,
                    new String[][]{
                        {"              ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BALAB   G  ", "   BBBBB  FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  A HHH A  G  ", "  BBBBBBB FBF "},
                        {"KKKKKK        ", "  BABIBAB     ", "  G     G     ", "  G     G     ", "  L HHH L     ", "  BBBBBBB     "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  A HHH A  G  ", "  BBBBBBB FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"              ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BALAB   G  ", "   BBBBB  FBF "}})
                .addShape(
                    STRUCTURE_PIECE_T4,
                    new String[][]{
                        {"             ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BALAB   ", "FBF  BBBBB   "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  A HHH A  ", "FBF BBBBBBB  "},
                        {"       KKKKKK", "    BABIBAB  ", "    G     G  ", "    G     G  ", "    L HHH L  ", "    BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  A HHH A  ", "FBF BBBBBBB  "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"             ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BALAB   ", "FBF  BBBBB   "}})
                .addShape(
                    STRUCTURE_PIECE_T5,
                    new String[][]{
                        {"              ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BALAB   G  ", "   BBBBB  FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  A HHH A  G  ", "  BBBBBBB FBF "},
                        {"     K        ", "  BABIBAB     ", "  G     G     ", "  G     G     ", "  L HHH L     ", "  BBBBBBB     "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  A HHH A  G  ", "  BBBBBBB FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"     K        ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BALAB   G  ", "   BBBBB  FBF "},
                        {"     K  ", "       ", "       ", "       ", "       "},
                        {"     K  ", "       ", "       ", "       ", "       "},
                        {"     K  ", "       ", "       ", "       ", "       "}})
                .addShape(
                    STRUCTURE_PIECE_T6,
                    new String[][]{
                        {"             ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BALAB   ", "FBF  BBBBB   "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  A HHH A  ", "FBF BBBBBBB  "},
                        {"       K     ", "    BABIBAB  ", "    G     G  ", "    G     G  ", "    L HHH L  ", "    BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  A HHH A  ", "FBF BBBBBBB  "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"       K     ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BALAB   ", "FBF  BBBBB   "},
                        {"       K  ", "       ", "       ", "       ", "       "},
                        {"       K  ", "       ", "       ", "       ", "       "},
                        {"       K  ", "       ", "       ", "       ", "       "}})

                .addElement(
                    'A',
                    ofChain(
                        addMultiTileCasing(
                            "gt.multitileentity.casings",
                            getCasingMeta(),
                            ENERGY_IN | FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT)))
                .addElement(
                    'B',
                    ofChain(
                        addMultiTileCasing(
                            CASING_REGISTRY_NAME,
                            GT_MultiTileCasing.BlackLaserEngraverCasing.getId(),
                            NOTHING)))
                .addElement(
                    'C',
                    ofChain(
                        addMultiTileCasing(
                            UPGRADE_CASING_REGISTRY_NAME,
                            GT_MultiTileUpgradeCasing.Cleanroom.getId(),
                            NOTHING),
                        addMultiTileCasing(
                            "gt.multitileentity.casings",
                            getCasingMeta(),
                            FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT | ENERGY_IN)))
                .addElement(
                    'D',
                    ofChain(
                        addMultiTileCasing(
                            UPGRADE_CASING_REGISTRY_NAME,
                            GT_MultiTileUpgradeCasing.Wireless.getId(),
                            NOTHING),
                        addMultiTileCasing(
                            "gt.multitileentity.casings",
                            getCasingMeta(),
                            FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT | ENERGY_IN)))
                .addElement('E', addMotorCasings(NOTHING))
                .addElement('F', GT_StructureUtility.ofFrame(Materials.Naquadah)

                )
                .addElement(
                    'H',
                    ofChain(addMultiTileCasing(CASING_REGISTRY_NAME, GT_MultiTileCasing.Mirror.getId(), NOTHING)))

                .addElement(
                    'G',
                    ofChain(
                        ofBlockUnlocalizedName(IndustrialCraft2.ID, "blockAlloyGlass", 0, true),
                        ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks", 0, true),
                        ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks2", 0, true),
                        ofBlockUnlocalizedName(Thaumcraft.ID, "blockCosmeticOpaque", 2, false)))
                .addElement('I', addEmitterCasings(NOTHING))
                .addElement('K', ofBlock(GregTech_API.sBlockCasings3, 11))
                .addElement('L', addRobotArmCasings(NOTHING))
                .build();
            buildState.stopBuilding();
        }
        return STRUCTURE_DEFINITION;
    }

    protected void calculateTier() {

    }

    @Override
    protected void setMaxComplexParallels(int parallel, boolean stopMachine) {
        super.setMaxComplexParallels(parallel, stopMachine);
        onStructureChange();
    }

    @Override
    protected MultiChildWidget createMainPage() {
        MultiChildWidget child = super.createMainPage();
        for (int i = 0; i < MAX_PROCESSES; i++) {
            final int processIndex = i;
            child.addChild(
                new ButtonWidget().setPlayClickSound(true)
                    .setOnClick(
                        (clickData, widget) -> {
                            if (!widget.isClient()) widget.getContext()
                                .openSyncedWindow(PROCESS_WINDOW_BASE_ID + processIndex);
                        })
                    .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_WHITELIST)
                    .setSize(18, 18)
                    .setEnabled((widget -> processIndex < maxComplexParallels))
                    .setPos(20 * (i % 4) + 18, 18 + (i / 4) * 20));
        }
        child.addChild(
            new TextFieldWidget().setGetterInt(() -> maxComplexParallels)
                .setSetterInt(parallel -> setMaxComplexParallels(parallel, true))
                .setNumbers(1, MAX_PROCESSES)
                .setTextColor(Color.WHITE.normal)
                .setTextAlignment(Alignment.Center)
                .addTooltip("Tier")
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                .setSize(18, 18)
                .setPos(130, 85));
        return child;
    }

    @Override
    public short getCasingRegistryID() {
        return 0;
    }

    @Override
    public int getCasingMeta() {
        return GT_MultiTileCasing.LaserEngraver.getId();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Laser Engraver")
            .addInfo("Used for Engraving")
            .addSeparator()
            .beginStructureBlock(3, 3, 5, true)
            .addController("Front right center")
            .addCasingInfoExactly("Laser Engraver Casing", 25, false)
            .toolTipFinisher(GT_Values.AuthorTheEpicGamer274);
        return tt;
    }

    public ProcessingLogic getProcessingLogic() {
        return PROCESSING_LOGIC;
    }
}
