package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorBlueWeabo;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.blocks.GT_Block_Casings8;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class GT_MetaTileEntity_PCBFactory extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_PCBFactory>
        implements ISurvivalConstructable {
    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";
    private static final String tier3 = "tier3";
    private static final String bioUpgrade = "bioUpgrade";
    private static final IStructureDefinition<GT_MetaTileEntity_PCBFactory> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_MetaTileEntity_PCBFactory>builder()
                    .addShape(tier1, transpose(new String[][] {
                        // spotless:off
                        {"       ","E     E","E     E","EEEEEEE","E     E","E     E","       "},
                        {"EEEEEEE","CAAAAAC","CAAAAAC","CCCCCCC","CCCCCCC","CCCCCCC","E     E"},
                        {"EAAAAAE","C-----C","C-----C","C-----C","C-----C","C-----C","ECCCCCE"},
                        {"EAAAAAE","C-----C","B-----B","B-----B","B-----B","C-----C","ECCCCCE"},
                        {"EAAAAAE","C-----C","B-FFF-B","B-FFF-B","B-FFF-B","C-----C","EPPPPPE"},
                        {"ECC~CCE","CDDDDDC","CDDDDDC","CDDDDDC","CDDDDDC","CDDDDDC","EPPPPPE"}
                        //spotless:on
                    }))
                    .addShape(tier2, transpose(new String[][] {
                        // spotless:off
                        {"    ","    ","    ","HGGH","HGGH","HGGH","HGGH","HGGH","    ","    ","    "},
                        {"    ","    ","HGGH","GGGG","GGGG","GGGG","GGGG","GGGG","HGGH","    ","    "},
                        {"    ","HGGH","GGGG","G  G","G  G","G  G","G  G","G  G","GGGG","HGGH","    "},
                        {"    ","HGGH","G  G","G  G","G  G","G  G","G  G","G  G","G  G","HGGH","    "},
                        {"HGGH","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","HGGH"},
                        {"HGGH","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","HGGH"},
                        {"HGGH","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","HGGH"}
                        //spotless:on
                    }))
                    .addShape(tier3, transpose(new String[][] {
                        // spotless:off
                        {"       ","       ","       ","       ","   I   ","   I   ","       ","       ","       ","       "},
                        {"       ","       ","       ","   I   ","   I   ","   I   ","   I   ","       ","       ","       "},
                        {"       ","       ","  KKK  ","  KIK  ","  K K  ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I ","  K K  ","  KIK  ","       ","       ","       "},
                        {"       ","  I I  "," I K I "," I   I "," I   I ","  K K  ","  KIK  ","       ","       ","       "},
                        {"       ","  I I  "," I K I "," I   I "," I   I ","  K K  ","  K K  ","  KKK  ","       ","       "},
                        {"       ","  I I  "," I K I "," I   I "," I   I ","  K K  ","  K K  ","  KKK  ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I ","  K K  ","  K K  ","  K K  ","  K K  "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {" II~II ","IIJJJII","IJJJJJI","IJJJJJI","IJJJJJI","IJJJJJI","IJJJJJI","IJJJJJI","IIJJJII"," IIIII "}
                        //spotless:on
                    }))
                    .addElement('E', ofFrame(Materials.DamascusSteel))
                    .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 11))
                    .addElement('D', ofBlock(GregTech_API.sBlockReinforced, 2))
                    .addElement(
                            'A',
                            ofChain(
                                    ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true),
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks", 0, true),
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks2", 0, true),
                                    // warded glass
                                    ofBlockUnlocalizedName("Thaumcraft", "blockCosmeticOpaque", 2, false)))
                    .addElement('B', ofBlock(GregTech_API.sBlockCasings3, 10))
                    .addElement('F', ofFrame(Materials.VibrantAlloy))
                    .addElement(
                            'P',
                            buildHatchAdder(GT_MetaTileEntity_PCBFactory.class)
                                    .atLeast(InputHatch, OutputBus, InputBus, Maintenance, ExoticEnergy, Energy)
                                    .dot(1)
                                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11))
                                    .buildAndChain(GregTech_API.sBlockCasings8, 11))
                    .addElement('H', ofFrame(Materials.Duranium))
                    .addElement('G', ofBlock(GregTech_API.sBlockCasings8, 12))
                    .addElement('I', ofBlock(GregTech_API.sBlockCasings8, 13))
                    .addElement('K', ofBlock(GregTech_API.sBlockCasings8, 10))
                    .addElement(
                            'J',
                            buildHatchAdder(GT_MetaTileEntity_PCBFactory.class)
                                    .atLeast(InputHatch, OutputBus, InputBus, Maintenance, ExoticEnergy, Energy)
                                    .dot(1)
                                    .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(13))
                                    .buildAndChain(GregTech_API.sBlockCasings8, 13))
                    .build();

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (this.mTier < 3) {
            buildPiece(tier1, stackSize, hintsOnly, 3, 5, 0);
            if (this.mTier == 2) {
                buildPiece(tier2, stackSize, hintsOnly, 7, 6, -1);
            }
        } else {
            buildPiece(tier3, stackSize, hintsOnly, 3, 21, 0);
        }
    }

    public GT_MetaTileEntity_PCBFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PCBFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PCBFactory(this.mName);
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    BlockIcons.getCasingTextureForId(
                            ((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11)),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                BlockIcons.getCasingTextureForId(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11)),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {
            BlockIcons.getCasingTextureForId(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11))
        };
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PCBFactory> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        // TODO Auto-generated method stub
        return false;
    }

    private float mRoughnessMultiplier = 1;
    private float mSpeedMultiplier = 1;
    private int mTier = 1;
    private boolean mBioUpgrade = false;
    private byte mBioXOffset, mBioZOffset;

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(tier1, 3, 5, 0)) {
            return false;
        }

        if (mRoughnessMultiplier <= 0.5 || mSpeedMultiplier <= 0 || mTier <= 0 || mTier >= 4) {
            return false;
        }

        if (mMaintenanceHatches.size() != 1 || mOutputBusses.size() != 1) {
            return false;
        }
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return (int) (mTier * mRoughnessMultiplier * 10000);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("PCB Factory")
                .addInfo("Controller block for the PCB Factory")
                .addInfo(EnumChatFormatting.BOLD + (EnumChatFormatting.GOLD + "!!IMPORTANT!!"))
                .addInfo("Choose the tier of the multi before you use the schematic")
                .addInfo("Each tier the multi gains a new building next to it")
                .addInfo("Putting a nanite in the controller allows the user to choose the tier")
                .addInfo("Requires a Carbon Nanite to use tier 1")
                .addInfo("Requires a Neutronium Nanite to use tier 2")
                .addInfo("Requires a Transcendent Metal Nanite to use tier 3")
                .addInfo("If a recipe's tier is lower than the tier of the Nano Forge")
                .addInfo("it gains perfect overclock")
                .addInfo("The amount of nanites inside determine the max parallel")
                .addInfo(AuthorBlueWeabo)
                .addSeparator()
                .beginStructureBlock(30, 38, 13, false)
                .addStructureInfo("Make sure to choose the tier before using the schematic")
                .addStructureInfo("PCB Factory Structure is too complex! See schematic for details.")
                .addStructureInfo("Stellar Alloy Frames")
                .addEnergyHatch("Any Energy Hatch, Determines Power Tier", 1)
                .addMaintenanceHatch("Required 1", 1)
                .addInputBus("Required 1", 1)
                .addOutputBus("Required 1", 1)
                .addInputHatch("Required 0", 1)
                .toolTipFinisher("GregTech");
        return tt;
    }
}
