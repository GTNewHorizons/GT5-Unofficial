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
import net.minecraftforge.common.util.ForgeDirection;

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
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEQuadcellTokamakGui;
import gregtech.common.misc.GTStructureChannels;

public class MTEQuadcellTokamak extends MTEExtendedPowerMultiBlockBase<MTEQuadcellTokamak>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int WIDTH_OFFSET = 11;
    private static final int HEIGHT_OFFSET = 6;
    private static final int DEPTH_OFFSET = 1;

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
