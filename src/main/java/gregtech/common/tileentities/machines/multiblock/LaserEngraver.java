package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.multitileentity.enums.PartMode.ENERGY_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.NOTHING;
import static gregtech.api.util.GT_StructureUtilityMuTE.*;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.multiblock.base.ComplexParallelController;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.common.tileentities.machines.multiblock.logic.LaserEngraverProcessingLogic;

public class LaserEngraver extends ComplexParallelController<LaserEngraver, LaserEngraverProcessingLogic> {

    private static IStructureDefinition<LaserEngraver> STRUCTURE_DEFINITION = null;
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
    protected RecipeMap<?> recipeMap;
    private UUID LaserEngraver;

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
                        // spotless:off
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
                        {"              ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BMLMB   G  ", "   BBBBB  FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  M HHH M  G  ", "  BBBBBBB FBF "},
                        {"KKKKKK        ", "  BABIBAB     ", "  G     G     ", "  G     G     ", "  L HHH L     ", "  BBBBBBB     "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  M HHH M  G  ", "  BBBBBBB FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"              ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BMLMB   G  ", "   BBBBB  FBF "}})
                .addShape(
                    STRUCTURE_PIECE_T4,
                    new String[][]{
                        {"             ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BNLNB   ", "FBF  BBBBB   "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  N HHH N  ", "FBF BBBBBBB  "},
                        {"       KKKKKK", "    BABIBAB  ", "    G     G  ", "    G     G  ", "    L HHH L  ", "    BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  N HHH N  ", "FBF BBBBBBB  "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"             ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BNLNB   ", "FBF  BBBBB   "}})
                .addShape(
                    STRUCTURE_PIECE_T5,
                    new String[][]{
                        {"              ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BOLOB   G  ", "   BBBBB  FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  O HHH O  G  ", "  BBBBBBB FBF "},
                        {"     K        ", "  BABIBAB     ", "  G     G     ", "  G     G     ", "  L HHH L     ", "  BBBBBBB     "},
                        {"     K        ", "  BABBBAB FBF ", "  G     G  G  ", "  G     G  G  ", "  O HHH O  G  ", "  BBBBBBB FBF "},
                        {"     KKKKKKK  ", "  BBAAABB BIB ", "  B     B G G ", "  B     B G G ", "  B     B G G ", "  BBBBBBB BHB "},
                        {"     K        ", "   BBBBB  FBF ", "   BGGGB   G  ", "   BGGGB   G  ", "   BOLOB   G  ", "   BBBBB  FBF "},
                        {"     K  ", "       ", "       ", "       ", "       "},
                        {"     K  ", "       ", "       ", "       ", "       "},
                        {"     K  ", "       ", "       ", "       ", "       "}})
                .addShape(
                    STRUCTURE_PIECE_T6,
                    new String[][]{
                        {"             ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BPLPB   ", "FBF  BBBBB   "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  P HHH P  ", "FBF BBBBBBB  "},
                        {"       K     ", "    BABIBAB  ", "    G     G  ", "    G     G  ", "    L HHH L  ", "    BBBBBBB  "},
                        {"       K     ", "FBF BABBBAB  ", " G  G     G  ", " G  G     G  ", " G  P HHH P  ", "FBF BBBBBBB  "},
                        {" KKKKKKK     ", "BIB BBAAABB  ", "G G B     B  ", "G G B     B  ", "G G B     B  ", "BHB BBBBBBB  "},
                        {"       K     ", "FBF  BBBBB   ", " G   BGGGB   ", " G   BGGGB   ", " G   BPLPB   ", "FBF  BBBBB   "},
                        {"       K  ", "       ", "       ", "       ", "       "},
                        {"       K  ", "       ", "       ", "       ", "       "},
                        {"       K  ", "       ", "       ", "       ", "       "}})
                // spotless:on
                .addElement(
                    'A',
                    ofMuTECasings(
                        FLUID_INPUT.getValue() | ITEM_INPUT.getValue()
                            | FLUID_OUTPUT.getValue()
                            | ITEM_OUTPUT.getValue()
                            | ENERGY_INPUT.getValue(),
                        GT_MultiTileCasing.LaserEngraver.getCasing()))
                .addElement(
                    'B',
                    ofMuTECasings(
                        FLUID_INPUT.getValue() | ITEM_INPUT.getValue()
                            | FLUID_OUTPUT.getValue()
                            | ITEM_OUTPUT.getValue()
                            | ENERGY_INPUT.getValue(),
                        GT_MultiTileCasing.BlackLaserEngraverCasing.getCasing()))
                .addElement(
                    'C',
                    ofMuTECasings(NOTHING.getValue(), CLEANROOM_CASINGS, GT_MultiTileCasing.LaserEngraver.getCasing()))
                .addElement(
                    'D',
                    ofMuTECasings(NOTHING.getValue(), WIRELESS_CASINGS, GT_MultiTileCasing.LaserEngraver.getCasing()))
                .addElement('E', ofMuTECasings(NOTHING.getValue(), MOTOR_CASINGS))
                .addElement('F', GT_StructureUtility.ofFrame(Materials.Naquadah)

                )
                .addElement('H', ofMuTECasings(NOTHING.getValue(), GT_MultiTileCasing.Mirror.getCasing()))

                .addElement(
                    'G',
                    ofChain(
                        ofBlockUnlocalizedName(IndustrialCraft2.ID, "blockAlloyGlass", 0, true),
                        ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks", 0, true),
                        ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks2", 0, true),
                        ofBlockUnlocalizedName(Thaumcraft.ID, "blockCosmeticOpaque", 2, false)))
                .addElement('I', ofMuTECasings(NOTHING.getValue(), EMITTER_CASINGS))
                .addElement('K', ofBlock(GregTech_API.sBlockCasings3, 11))
                .addElement('L', ofMuTECasings(NOTHING.getValue(), ROBOT_ARM_CASINGS))
                .addElement(
                    'M',
                    ofMuTECasings(NOTHING.getValue(), GT_MultiTileCasing.LaserEngraverUpgrade1.getCasing()))
                .addElement(
                    'N',
                    ofMuTECasings(NOTHING.getValue(), GT_MultiTileCasing.LaserEngraverUpgrade2.getCasing()))
                .addElement(
                    'O',
                    ofMuTECasings(NOTHING.getValue(), GT_MultiTileCasing.LaserEngraverUpgrade3.getCasing()))
                .addElement(
                    'P',
                    ofMuTECasings(NOTHING.getValue(), GT_MultiTileCasing.LaserEngraverUpgrade4.getCasing()))
                .build();
            buildState.stopBuilding();
        }
        return STRUCTURE_DEFINITION;
    }

    protected MultiChildWidget createMainPage(IWidgetBuilder<?> builder) {
        MultiChildWidget child = new MultiChildWidget();
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
            new NumericWidget().setGetter(() -> maxComplexParallels)
                .setSetter(parallel -> setMaxComplexParallels((int) parallel, true))
                .setBounds(1, MAX_PROCESSES)
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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        setMaxComplexParallels(nbt.getInteger("processors"), false);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("processors", maxComplexParallels);
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
            .toolTipFinisher(GT_Values.AuthorTheEpicGamer274);
        return tt;
    }

    @Override
    @Nonnull
    protected LaserEngraverProcessingLogic createProcessingLogic() {
        return new LaserEngraverProcessingLogic();
    }

}
