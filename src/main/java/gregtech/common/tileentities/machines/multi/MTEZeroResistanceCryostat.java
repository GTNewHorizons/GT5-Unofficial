package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;

import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import gregtech.api.structure.error.StructureError;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.core.material.MaterialsAlloy;

import java.util.List;

public class MTEZeroResistanceCryostat extends MTEExtendedPowerMultiBlockBase<MTEZeroResistanceCryostat> {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 11;
    private static final int OFFSET_Y = 9;
    private static final int OFFSET_Z = 0;

    private int casingAmount;
    private  Byte solenoidLevel;

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.zero_resistance_cryostat"));
        tt.toolTipFinisher();
        return tt;
    }

    private static final IStructureDefinition<MTEZeroResistanceCryostat> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEZeroResistanceCryostat>builder()
        .addShape(
            "main",
            transpose(
                new String[][] {
                    // spotless:off
                {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           G               ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","          GGG              ","          GFFFFFFF         ","          GGG              ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","           G               ","          GFG    FFFF      ","           G               ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                    JJJ    ","                    FFF    ","                    JJJ    ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                      JJJ  ","                      FFF  ","                      JJJ  ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                       GGG ","                       GGG ","                       GGG ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                {"                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                       GGGG","                      GGGGG","                      GGGGG","                      GGGGG","                       GGGG","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           ","                           "},
                {"                           ","  GGG  D       D  GGG      "," GGG   DD     DD   GGG     "," GG    DD     DD    GG     "," G      D     D      G     ","                           ","                           "," DDD               DDD     ","  DDD             DDD   GGG","                           ","                    GG     ","                    GG     ","                    GG     ","                           ","  DDD             DDD   GGG"," DDD               DDD     ","                           ","                           "," G      D     D      G     "," GG    DD     DD    GG     "," GGG   DD     DD   GGG     ","  GGG  D       D  GGG      ","                           "},
                {"       D HHHHH D           ","  IIG DDHHHHHHHDD GII      "," IGGG HHAAAAAAAHH GGGI     "," IGG HAAAHHHHHAAAH GGI     "," GG HAAHHD   DHHAAH GG     ","   HAAH D     D HAAH       "," DHAAH           HAAHD     ","DDHAH             HAHDD    "," HAAHD           DHAAH  GGG"," HAHD             DHAH     "," HAH               HAH   C "," HAH               HAH   C "," HAH               HAH   C "," HAHD             DHAH     "," HAAHD           DHAAH  GGG","DDHAH             HAHDD    "," DHAAH           HAAHD     ","   HAAH D     D HAAH       "," GG HAAHHD   DHHAAH GG     "," IGG HAAAHHHHHAAAH GGI     "," IGGG HHAAAAAAAHH GGGI     ","  IIG DDHHHHHHHDD GII      ","       D       D           "},
                {"       DHHH~HHHD           ","  IIG DHEEEEEEEHD GII      "," IGGGHEEKKKKKKKEEHGGGI     "," IGGHEKKKEEEEEKKKEHGGI     "," GGHEKKEEDHHHDEEKKEHGG     ","  HEKKEHD     DHEKKEH      "," DEKKEH         HEKKED     ","DHEKEH           HEKEHD    ","HEKKED           DEKKEH GGG","HEKED             DEKEH  B ","HEKEH             HEKEH CBC","HEKEH             HEKEH CBC","HEKEH             HEKEH CBC","HEKED             DEKEH  B ","HEKKED           DEKKEH GGG","DHEKEH           HEKEHD    "," DEKKEH         HEKKED     ","  HEKKEHD     DHEKKEH      "," GGHEKKEEDHHHDEEKKEHGG     "," IGGHEKKKEEEEEKKKEHGGI     "," IGGGHEEKKKKKKKEEHGGGI     ","  IIG DHEEEEEEEHD GII      ","       DHHHHHHHD           "},
                {"       D HHHHH D           ","  IIG DDHHHHHHHDD GII      "," IGGG HHHHHHHHHHH GGGI     "," IGGGHHHHHHHHHHHHHGGGI     "," GGGHHHHHD   DHHHHHGGG     ","   HHHH D     D HHHH       "," DHHHH           HHHHD     ","DDHHH             HHHDD    "," HHHHD           DHHHH  GGG"," HHHD             DHHH     "," HHH               HHH   C "," HHH               HHH   C "," HHH               HHH   C "," HHHD             DHHH     "," HHHHD           DHHHH  GGG","DDHHH             HHHDD    "," DHHHH           HHHHD     ","   HHHH D     D HHHH       "," GGGHHHHHD   DHHHHHGGG     "," IGGGHHHHHHHHHHHHHGGGI     "," IGGG HHHHHHHHHHH GGGI     ","  IIG DDHHHHHHHDD GII      ","       D       D           "},
                {"                           ","  GGG  D       D  GGG      "," GGGG  DD     DD  GGGG     "," GGGG  DD     DD  GGGG     "," GGG    D     D    GGG     ","                           ","                           "," DDD               DDD     ","  DDD             DDD   GGG","                           ","                           ","                           ","                           ","                           ","  DDD             DDD   GGG"," DDD               DDD     ","                           ","                           "," GGG    D     D    GGG     "," GGGG  DD     DD  GGGG     "," GGGG  DD     DD  GGGG     ","  GGG  D       D  GGG      ","                           "},
                {"                           ","  GGG             GGG      "," GGGG             GGGG     "," GGGG             GGGG     "," GGG               GGG     ","                           ","                           ","                           ","                        GGG","                        GGG","                        GGG","                        GGG","                        GGG","                        GGG","                        GGG","                           ","                           ","                           "," GGG               GGG     "," GGGG             GGGG     "," GGGG             GGGG     ","  GGG             GGG      ","                           "}}))
                // spotless:on

        // A: any tiered glass
        .addElement('A', chainAllGlasses())
        // B: Pikyonium frame (GT++ material, so no ofFrame)
        .addElement(
            'B',
            lazy(
                t -> ofBlock(
                    Block.getBlockFromItem(
                        MaterialsAlloy.PIKYONIUM.getFrameBox(1)
                            .getItem()),
                    0)))
        .addElement('C', Casings.SuperconductingCoilBlock.asElement())
        // D: solenoids, the tiered piece
        .addElement(
            'D',
            GTStructureChannels.SOLENOID.use(
                ofSolenoidCoil(
                    MTEZeroResistanceCryostat::setSolenoidLevel,
                    MTEZeroResistanceCryostat::getSolenoidLevel)))
        .addElement('E', Casings.CoolantDuct.asElement())
        .addElement('F', Casings.InsulatedFluidPipeCasing.asElement())
        // G: Magnetic Anchor Casing, where hatches can go
        .addElement(
            'G',
            ofChain(
                buildHatchAdder(MTEZeroResistanceCryostat.class)
                    .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                    .casingIndex(Casings.MagneticAnchorCasing.textureId)
                    .hint(1)
                    .build(),
                onElementPass(x -> x.casingAmount++, Casings.MagneticAnchorCasing.asElement())))
        .addElement('H', Casings.ColliderCasing.asElement())
        .addElement('I', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
        .addElement('J', ofFrame(Materials.SuperconductorZPMBase))
        // K: Block of Magnetic Iron
        .addElement('K', ofBlock(GregTechAPI.sBlockMetal3, 13))
        .build();

    public MTEZeroResistanceCryostat(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEZeroResistanceCryostat(final String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN, stackSize, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        solenoidLevel = null;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 300);
        checkHasAnyInput(errors);
        checkHasOutputBus(errors);
        checkHasAnyEnergy(errors);
        checkHasMaintenanceHatch(errors);
    }

    private Byte getSolenoidLevel() {
        return solenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        solenoidLevel = level;
    }


    @Override
    public IStructureDefinition<MTEZeroResistanceCryostat> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }



    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEZeroResistanceCryostat(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(16) };
    }
}
