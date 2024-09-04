package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GTValues.AuthorTotto;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;

public class MTEMeteorMiner extends MTEEnhancedMultiBlockBase<MTEMeteorMiner> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MTEMeteorMiner> STRUCTURE_DEFINITION = null;
    private static final int BASE_CASING_COUNT = 469;

    @Override
    public IStructureDefinition<MTEMeteorMiner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEMeteorMiner>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    (transpose(
                        // spotless:off
                new String[][]{
                    {"               ","               ","               ","               ","               ","               ","       D       ","      D D      ","       D       ","               ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","       D       ","      D D      ","     D   D     ","      D D      ","       D       ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","       D       ","     D   D     ","               ","    D     D    ","               ","     D   D     ","       D       ","               ","               ","               ","               "},
                    {"               ","               ","               ","       D       ","    D     D    ","               ","               ","   D   B   D   ","               ","               ","    D     D    ","       D       ","               ","               ","               "},
                    {"               ","               ","       D       ","   D       D   ","               ","               ","               ","  D    B    D  ","               ","               ","               ","   D       D   ","       D       ","               ","               "},
                    {"               ","       D       ","  D         D  ","               ","               ","               ","       C       "," D    CBC    D ","       C       ","               ","               ","               ","  D         D  ","       D       ","               "},
                    {"  DDDDDDDDDDD  "," DDFFFFFFFFFDD ","DDFF       FFDD","DFF         FFD","DF           FD","DF           FD","DF     C     FD","DF    CBC    FD","DF     C     FD","DF           FD","DF           FD","DFF         FFD","DDFF       FFDD"," DDFFFFFFFFFDD ","  DDDDDDDDDDD  "},
                    {"               ","       D       ","    FFFFFFF    ","   FF     FF   ","  FF       FF  ","  F         F  ","  F    C    F  "," DF   CBC   FD ","  F    C    F  ","  F         F  ","  FF       FF  ","   FF     FF   ","    FFFFFFF    ","       D       ","               "},
                    {"               ","               ","       D       ","     FFFFF     ","    FF   FF    ","   FF  C  FF   ","   F  CCC  F   ","  DF CCBCC FD  ","   F  CCC  F   ","   FF  C  FF   ","    FF   FF    ","     FFFFF     ","       D       ","               ","               "},
                    {"               ","               ","               ","       D       ","      FFF      ","     FFFFF     ","    FFFFFFF    ","   DFFFGFFFD   ","    FFFFFFF    ","     FFFFF     ","      FFF      ","       D       ","               ","               ","               "},
                    {"               ","               ","               ","               ","       D       ","      DDD      ","     DEEED     ","    DDEGEDD    ","     DEEED     ","      DDD      ","       D       ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","      EEE      ","      EGE      ","      EEE      ","               ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","      EEE      ","      EGE      ","      EEE      ","               ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","     EEAEE     ","     EEGEE     ","     EEEEE     ","      EEE      ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","    EE A EE    ","    EEAGAEE    ","    EE A EE    ","      EEE      ","      EEE      ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","       J       ","       A       ","   EE  A  EE   ","   EE AGA EE   ","   EE  A  EE   ","               ","      EEE      ","      EEE      ","               ","               ","               "},
                    {"               ","               ","               ","               ","      J~J      ","      AGA      ","  EE  AGA  EE  ","  EE  AGA  EE  ","  EE   A   EE  ","               ","               ","      EEE      ","      EEE      ","               ","               "},
                    {"               ","               ","               ","               ","       I       ","       A       "," HHH   A   HHH "," HHH   A   HHH "," HHH       HHH ","               ","               ","      HHH      ","      HHH      ","      HHH      ","               "}
                // spotless:on
                        })))
                .addElement('A', ofBlock(ModBlocks.blockCasings4Misc, 2))// Glasses.chainAllGlasses())
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 15))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings5, 5))
                .addElement('D', ofFrame(Materials.StainlessSteel))
                .addElement(
                    'H',
                    buildHatchAdder(MTEMeteorMiner.class).atLeast(HatchElement.OutputBus, HatchElement.Energy)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(1)
                        .buildAndChain(
                            onElementPass(
                                MTEMeteorMiner::onCasingAdded,
                                ofBlock(ModBlocks.blockSpecialMultiCasings, 6))))
                .addElement('F', ofBlock(ModBlocks.blockSpecialMultiCasings, 8))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement('E', ofBlock(ModBlocks.blockSpecialMultiCasings, 6))
                .addElement(
                    'I',
                    buildHatchAdder(MTEMeteorMiner.class).atLeast(HatchElement.Maintenance)
                        .casingIndex(TAE.getIndexFromPage(0, 8))
                        .dot(2)
                        .buildAndChain(
                            onElementPass(MTEMeteorMiner::onCasingAdded, ofBlock(ModBlocks.blockCasings4Misc, 2))))
                .addElement('J', ofBlock(ModBlocks.blockCasings4Misc, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public MTEMeteorMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMeteorMiner(String aName) {
        super(aName);
    }

    protected int aCasingAmount;

    @Override
    public void clearHatches() {
        super.clearHatches();

        aCasingAmount = 0;
    }

    private void onCasingAdded() {
        aCasingAmount++;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 16, 4);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 16, 4, elementBudget, env, false, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMeteorMiner(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Miner")
            .addInfo("Controller Block for the Meteor Miner")
            .addInfo(EnumChatFormatting.BLUE + "Finally some good Meteors!")
            .addInfo(AuthorTotto)
            .addSeparator()
            .beginStructureBlock(15, 18, 15, false)
            .addController("Second Layer Center")
            .addOutputBus("Any Structural Solar Casing", 1)
            .addEnergyHatch("Any Structural Solar Casing", 1)
            .addMaintenanceHatch("Below the Controller", 2)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        aCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 7, 16, 4)
        && !mEnergyHatches.isEmpty()
        && mMaintenanceHatches.size() == 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
