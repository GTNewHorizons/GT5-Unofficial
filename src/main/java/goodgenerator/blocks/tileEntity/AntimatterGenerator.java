package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoTunnel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.List;

public class AntimatterGenerator extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
    implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<goodgenerator.blocks.tileEntity.AntimatterGenerator> multiDefinition = null;
    protected long trueOutput = 0;
    protected int trueEff = 0;
    protected int times = 1;

    List<GT_MetaTileEntity_Hatch_DynamoTunnel> laserSources = new ArrayList<>();
    List<GT_MetaTileEntity_Hatch_Input> inputHatches = new ArrayList<>();

    @Override
    public IStructureDefinition<goodgenerator.blocks.tileEntity.AntimatterGenerator> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<goodgenerator.blocks.tileEntity.AntimatterGenerator>builder()
                .addShape(
                    mName, structure)
                .addElement('A', lazy(x -> Glasses.chainAllGlasses()))
                .addElement('B', lazy(x -> ofFrame(Materials.Iron)))
                .addElement('C', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCasingMeta())))
                .addElement('E', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta())))
                .addElement('F', lazy(x -> buildHatchAdder(AntimatterGenerator.class)
                    .atLeast(HatchElement.DynamoMulti)
                    .casingIndex(x.getCasingMeta())
                    .dot(1)
                    .build()))
                .addElement('G', lazy(x -> buildHatchAdder(AntimatterGenerator.class)
                    .atLeast(GT_HatchElement.InputHatch)
                    .casingIndex(x.getCasingMeta())
                    .dot(1)
                    .build()))
                .build();
        }
        return multiDefinition;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("AntimatterGenerator.hint", 8);
    }

    public AntimatterGenerator(String name) {
        super(name);
    }

    public AntimatterGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {

        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {

        super.saveNBTData(aNBT);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = "Probably makes: " + EnumChatFormatting.RED
            + GT_Utility.formatNumbers(Math.abs(this.trueOutput))
            + EnumChatFormatting.RESET
            + " EU/t";
        info[6] = "Problems: " + EnumChatFormatting.RED
            + (this.getIdealStatus() - this.getRepairStatus())
            + EnumChatFormatting.RESET
            + " Efficiency: "
            + EnumChatFormatting.YELLOW
            + trueEff
            + EnumChatFormatting.RESET
            + " %";
        return info;
    }


    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 3, 7, 0) && mMaintenanceHatches.size() == 1
            && mDynamoHatches.size() + eDynamoMulti.size() == 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new goodgenerator.blocks.tileEntity.AntimatterGenerator(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Antimatter Generator")
            .addInfo("Controller block for the Shielded Lagrangian Annihilation Matrix")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(7, 8, 7, true)
            .addController("Front bottom")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44),
                new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44),
                new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 3, 7, 0, elementBudget, env, false, true);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 3, 7, 0, itemStack, hintsOnly);
    }

    public Block getCoilBlock() {
        return Loaders.protomatterActivationCoil;
    }

    public int getCoilMeta() {
        return 0;
    }

    public Block getCasingBlock(int type) {
        switch(type) {
            case 1:
                return Loaders.antimatterContainmentCasing;
            case 2:
                return ItemList.Casing_AdvancedRadiationProof.getBlock();
            default:
                return Loaders.antimatterContainmentCasing;
        }
    }

    public int getCasingMeta() {
        return 0;
    }

    String[][] structure = new String[][]{{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 F                 ",
        "                 F                 ",
        "                 F                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                FDF                ",
        "                FDF                ",
        "                FDF                ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                EDE                ",
        "            EEEE D EEEE            ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "            EEEE D EEEE            ",
        "                EDE                ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "          EE     D     EE          ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "          EE     D     EE          ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "              EEEDEEE              ",
        "          EEEE   D   EEEE          ",
        "         E               E         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "       F                   F       ",
        "       F                   F       ",
        "       F                   F       ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         E               E         ",
        "          EEEE   D   EEEE          ",
        "              EEEDEEE              ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "       D EEE     D     EEE D       ",
        "       DE                 ED       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       DE                 ED       ",
        "       D EEE     D     EEE D       ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "        D EEEEE  D  EEEEE D        ",
        "        EE               EE        ",
        "       ED                 DE       ",
        "                                   ",
        "                                   ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "     BB                     BB     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     BB                     BB     ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "                                   ",
        "                                   ",
        "       ED                 DE       ",
        "        EE               EE        ",
        "        D EEEEE  D  EEEEE D        ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "         D   EEEEDEEEE   D         ",
        "         EEEE    D    EEEE         ",
        "       EED               DEE       ",
        "      E                     E      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      E                     E      ",
        "       EED               DEE       ",
        "         EEEE    D    EEEE         ",
        "         D   EEEEDEEEE   D         ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "          DEEEEEEDEEEEEED          ",
        "        EEE      D      EEE        ",
        "      EE  D             D  EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    B                         B    ",
        "          D             D          ",
        "          D             D          ",
        "               EEEEE               ",
        "                                   ",
        "               EEEEE               ",
        "          D             D          ",
        "          D             D          ",
        "    B                         B    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE  D             D  EE      ",
        "        EEE      D      EEE        ",
        "          DEEEEEEDEEEEEED          ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "           D     D     D           ",
        "          EEEEEEEDEEEEEEE          ",
        "        EE D     D     D EE        ",
        "      EE                   EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "           D           D           ",
        "            BEEAAAAAEEBD           ",
        "               CCCCC               ",
        "           DBEEAAAAAEEBD           ",
        "           D           D           ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE                   EE      ",
        "        EE D     D     D EE        ",
        "          EEEEEEEDEEEEEEE          ",
        "           D     D     D           ",
        "                                   "
    },{
        "                 D                 ",
        "            D  EEDEE  D            ",
        "          EEEEE  D  EEEEE          ",
        "       EEE  D         D  EEE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           BEAAEEEEEAAEB           ",
        "             CCEECEECC             ",
        "           BEAAEEEEEAAEB           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EEE  D         D  EEE       ",
        "          EEEEE  D  EEEEE          ",
        "            D  EEDEE  D            ",
        "                 D                 "
    },{
        "                 D                 ",
        "             DEEEDEEED             ",
        "         EEEEE   D   EEEEE         ",
        "       EE    D       D    EE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           EAEE     EEAE           ",
        "            CEE  C  EEC            ",
        "           EAEE     EEAE           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EE    D       D    EE       ",
        "         EEEEE   D   EEEEE         ",
        "             DEEEDEEED             ",
        "                 D                 "
    },{
        "              D  D  D              ",
        "             EEEEDEEEE             ",
        "         EEEE D  D  D EEEE         ",
        "      EEE                 EEE      ",
        "     E                       E     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "           EAE       EAE           ",
        "            CE   C   EC            ",
        "           EAE       EAE           ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     E                       E     ",
        "      EEE                 EEE      ",
        "         EEEE D  D  D EEEE         ",
        "             EEEEDEEEE             ",
        "              D  D  D              "
    },{
        "               DDDDD               ",
        "            EEEEEDEEEEE            ",
        "        EEEE   DDDDD   EEEE        ",
        "      EE       D   D       EE      ",
        "     E         D   D         E     ",
        "   EE                         EE   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "          EAE         EAE          ",
        "           CE    C    EC           ",
        "          EAE         EAE          ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   EE                         EE   ",
        "     E         D   D         E     ",
        "      EE       D   D       EE      ",
        "        EEEE   DDDDD   EEEE        ",
        "            EEEEEDEEEEE            ",
        "               DDDDD               "
    },{
        "               D   D               ",
        "            EEEEEEEEEEE            ",
        "        EEEE   DEEED   EEEE        ",
        "      EE        EEE        EE      ",
        "    EE          EEE          EE    ",
        "   E            DED            E   ",
        "                DED                ",
        "                DED                ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        " F        EAE    E    EAE        F ",
        " F         CE   ECE   EC         F ",
        " F        EAE    E    EAE        F ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "                DED                ",
        "                DED                ",
        "   E            DED            E   ",
        "    EE          EEE          EE    ",
        "      EE        E~E        EE      ",
        "        EEEE   DEEED   EEEE        ",
        "            EEEEEEEEEEE            ",
        "               D   D               "
    },{
        "            DDDD   DDDD            ",
        "        DDDDDDDDEGEDDDDDDDD        ",
        "      DDDDDDDDDDECEDDDDDDDDDD      ",
        "   DDDDDDDDD    ECE    DDDDDDDDD   ",
        "  DDDDDD        ECE        DDDDDD  ",
        "  DDDD          ECE          DDDD  ",
        " DDD            ECE            DD  ",
        " D              ECE             DD ",
        " D              ACA              D ",
        " D              ACA              D ",
        " D              ACA              D ",
        " DB              C              BD ",
        " D               C               D ",
        " D               C               D ",
        "FD        EAE   ECE   EAE        DF",
        "FD         CCCCCCCCCCCCC         DF",
        "FD        EAE   ECE   EAE        DF",
        " D               C               D ",
        " D               C               D ",
        " DB              C              BD ",
        " D              ACA              D ",
        " D              ACA              D ",
        " D              ACA              D ",
        " DD             ECE             DD ",
        "  DD            ECE            DD  ",
        "  DDDD          ECE          DDDD  ",
        "  DDDDDD        ECE        DDDDDD  ",
        "   DDDDDDDDD    ECE    DDDDDDDDD   ",
        "      DDDDDDDDDDECEDDDDDDDDDD      ",
        "        DDDDDDDDEGEDDDDDDDD        ",
        "            DDDD   DDDD            "
    },{
        "               D   D               ",
        "            EEEEEEEEEEE            ",
        "        EEEE   DEEED   EEEE        ",
        "      EE        EEE        EE      ",
        "    EE          EEE          EE    ",
        "   E            DED            E   ",
        "                DED                ",
        "                DED                ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        " F        EAE    E    EAE        F ",
        " F         CE   ECE   EC         F ",
        " F        EAE    E    EAE        F ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "                DED                ",
        "                DED                ",
        "   E            DED            E   ",
        "    EE          EEE          EE    ",
        "      EE        EEE        EE      ",
        "        EEEE   DEEED   EEEE        ",
        "            EEEEEEEEEEE            ",
        "               D   D               "
    },{
        "               DDDDD               ",
        "            EEEEEDEEEEE            ",
        "        EEEE   DDDDD   EEEE        ",
        "      EE       D   D       EE      ",
        "     E         D   D         E     ",
        "   EE                         EE   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "          EAE         EAE          ",
        "           CE    C    EC           ",
        "          EAE         EAE          ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   EE                         EE   ",
        "     E         D   D         E     ",
        "      EE       D   D       EE      ",
        "        EEEE   DDDDD   EEEE        ",
        "            EEEEEDEEEEE            ",
        "               DDDDD               "
    },{
        "              D  D  D              ",
        "             EEEEDEEEE             ",
        "         EEEE D  D  D EEEE         ",
        "      EEE                 EEE      ",
        "     E                       E     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "           EAE       EAE           ",
        "            CE   C   EC            ",
        "           EAE       EAE           ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     E                       E     ",
        "      EEE                 EEE      ",
        "         EEEE D  D  D EEEE         ",
        "             EEEEDEEEE             ",
        "              D  D  D              "
    },{
        "                 D                 ",
        "             DEEEDEEED             ",
        "         EEEEE   D   EEEEE         ",
        "       EE    D       D    EE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           EAEE     EEAE           ",
        "            CEE  C  EEC            ",
        "           EAEE     EEAE           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EE    D       D    EE       ",
        "         EEEEE   D   EEEEE         ",
        "             DEEEDEEED             ",
        "                 D                 "
    },{
        "                 D                 ",
        "            D  EEDEE  D            ",
        "          EEEEE  D  EEEEE          ",
        "       EEE  D         D  EEE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           BEAAEEEEEAAEB           ",
        "             CCEECEECC             ",
        "           BEAAEEEEEAAEB           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EEE  D         D  EEE       ",
        "          EEEEE  D  EEEEE          ",
        "            D  EEDEE  D            ",
        "                 D                 "
    },{
        "                                   ",
        "           D     D     D           ",
        "          EEEEEEEDEEEEEEE          ",
        "        EE D     D     D EE        ",
        "      EE                   EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "           D           D           ",
        "            BEEAAAAAEEBD           ",
        "               CCCCC               ",
        "           DBEEAAAAAEEBD           ",
        "           D           D           ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE                   EE      ",
        "        EE D     D     D EE        ",
        "          EEEEEEEDEEEEEEE          ",
        "           D     D     D           ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "          DEEEEEEDEEEEEED          ",
        "        EEE      D      EEE        ",
        "      EE  D             D  EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    B                         B    ",
        "          D             D          ",
        "          D             D          ",
        "               EEEEE               ",
        "                                   ",
        "               EEEEE               ",
        "          D             D          ",
        "          D             D          ",
        "    B                         B    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE  D             D  EE      ",
        "        EEE      D      EEE        ",
        "          DEEEEEEDEEEEEED          ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "         D   EEEEDEEEE   D         ",
        "         EEEE    D    EEEE         ",
        "       EED               DEE       ",
        "      E                     E      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      E                     E      ",
        "       EED               DEE       ",
        "         EEEE    D    EEEE         ",
        "         D   EEEEDEEEE   D         ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "        D EEEEE  D  EEEEE D        ",
        "        EE               EE        ",
        "       ED                 DE       ",
        "                                   ",
        "                                   ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "     BB                     BB     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     BB                     BB     ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "                                   ",
        "                                   ",
        "       ED                 DE       ",
        "        EE               EE        ",
        "        D EEEEE  D  EEEEE D        ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "       D EEE     D     EEE D       ",
        "       DE                 ED       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       DE                 ED       ",
        "       D EEE     D     EEE D       ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "              EEEDEEE              ",
        "          EEEE   D   EEEE          ",
        "         E               E         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "       F                   F       ",
        "       F                   F       ",
        "       F                   F       ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         E               E         ",
        "          EEEE   D   EEEE          ",
        "              EEEDEEE              ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "          EE     D     EE          ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "          EE     D     EE          ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                EDE                ",
        "            EEEE D EEEE            ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "            EEEE D EEEE            ",
        "                EDE                ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                FDF                ",
        "                FDF                ",
        "                FDF                ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 F                 ",
        "                 F                 ",
        "                 F                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    }};
}
