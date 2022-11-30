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
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("SpellCheckingInspection")
public class GT_MetaTileEntity_PCBFactory
        extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_PCBFactory>
        implements ISurvivalConstructable {
    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";
    private static final String tier3 = "tier3";
    private static final String bioUpgrade = "bioUpgrade";
    private static final String ocTier1Upgrade = "ocTier1Upgrade";
    private static final String ocTier2Upgrade = "ocTier2Upgrade";
    private boolean mSeparate = false;
    private float mRoughnessMultiplier = 1;
    private int mTier = 1, mSetTier = 1, mUpgradesInstalled = 0;
    private boolean mBioUpgrade = false, mBioRotate = false, mOCTier1 = false, mOCTier2 = false;
    private int[] mBioOffsets = new int[] {-5, -1},
            mOCTier1Offsets = new int[] {2, -11},
            mOCTier2Offsets = new int[] {2, -11};
    private GT_MetaTileEntity_Hatch_Input mCoolantInputHatch;
    private static final int mBioBitMap = 0b1000;
    private static final int mTier3BitMap = 0b100;
    private static final int mTier2BitMap = 0b10;
    private static final int mTier1BitMap = 0b1;
    private static final int COOLANT_CONSUMED_PER_SEC = 10;
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
                    .addShape(bioUpgrade, transpose(new String[][] {
                        // spotless:off
                        {"            ","            ","   LLLLLL   ","            ","            "},
                        {"            ","            ","  L      L  ","            ","            "},
                        {"E   E  E   E"," LLL    LLL "," LLL    LLL "," LLL    LLL ","E   E  E   E"},
                        {"EAAAE  EAAAE","A   A  A   A","A   A  A   A","A   A  A   A","EAAAE  EAAAE"},
                        {"EAAAE  EAAAE","A   A  A   A","A   A  A   A","A   A  A   A","EAAAE  EAAAE"},
                        {"EAAAE  EAAAE","A   A  A   A","A   A  A   A","A   A  A   A","EAAAE  EAAAE"},
                        {"ELLLE  ELLLE","LLLLL  LLLLL","LLLLL  LLLLL","LLLLL  LLLLL","ELLLE  ELLLE"}
                        //spotless:on
                    }))
                    .addShape(ocTier1Upgrade, transpose(new String[][] {
                        // spotless:off
                        {"EKKKE","K   K","K   K","K   K","EKKKE"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"E   E"," NNN "," N N "," NNN ","E   E"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"EOOOE","OKKKO","OK KO","OKKKO","EOOOE"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"ENNNE","NKKKN","NK KN","NKKKN","ENNNE"},
                        {"EMMME","MMMMM","MMMMM","MMMMM","EMMME"}
                        //spotless:on
                    }))
                    .addShape(ocTier2Upgrade, transpose(new String[][] {
                        // spotless:off
                        {"RGGGR","G   G","G   G","G   G","RGGGR"},
                        {"R   R"," GGG "," GPG "," GGG ","R   R"},
                        {"R   R"," NNN "," NPN "," NNN ","R   R"},
                        {"R   R"," QQQ "," QPQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QPQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QPQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QPQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QPQ "," QQQ ","R   R"},
                        {"RNNNR","NQQQN","NQPQN","NQQQN","RNNNR"},
                        {"RSSSR","SSSSS","SSSSS","SSSSS","RSSSR"}
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
                                    .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
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
                    .addElement('L', ofBlock(GregTech_API.sBlockCasings4, 1))
                    .addElement(
                            'M',
                            // spotless:off
                            ofChain(
                                ofChain(InputHatch.withAdder(GT_MetaTileEntity_PCBFactory::addCoolantInputToMachineList)
                                        .withCount(t -> isValidMetaTileEntity(t.mCoolantInputHatch) ? 1 : 0)
                                        .newAny(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(10),2),
                                    ofBlock(GregTech_API.sBlockCasings8, 12))))
                            //spotless:on
                    .addElement('N', ofBlock(GregTech_API.sBlockCasings2, 15))
                    .addElement('O', ofBlock(GregTech_API.sBlockCasings8, 4))
                    .addElement(
                            'S',
                            // spotless:off
                                ofChain(InputHatch.withAdder(GT_MetaTileEntity_PCBFactory::addCoolantInputToMachineList)
                                        .withCount(t -> isValidMetaTileEntity(t.mCoolantInputHatch) ? 1 : 0)
                                        .newAny(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(12),2),
                                    ofBlock(GregTech_API.sBlockCasings8, 12)))
                                //spotless:on
                    .addElement('R', ofFrame(Materials.Americium))
                    .addElement('Q', ofBlock(GregTech_API.sBlockCasings8, 14))
                    .addElement('P', ofBlock(GregTech_API.sBlockCasings1, 15))
                    .build();

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (mSetTier < 3) {
            buildPiece(tier1, stackSize, hintsOnly, 3, 5, 0);
            if (mSetTier == 2) {
                buildPiece(tier2, stackSize, hintsOnly, 7, 6, 2);
            }
        } else {
            buildPiece(tier3, stackSize, hintsOnly, 3, 21, 0);
        }

        if (mBioUpgrade) {
            if (mBioRotate) {
                final IGregTechTileEntity tTile = getBaseMetaTileEntity();
                getStructureDefinition()
                        .buildOrHints(
                                this,
                                stackSize,
                                bioUpgrade,
                                tTile.getWorld(),
                                transformFacing(getExtendedFacing()),
                                tTile.getXCoord(),
                                tTile.getYCoord(),
                                tTile.getZCoord(),
                                mBioOffsets[1],
                                6,
                                mBioOffsets[0],
                                hintsOnly);
            } else {
                buildPiece(bioUpgrade, stackSize, hintsOnly, mBioOffsets[0], 6, mBioOffsets[1]);
            }
        }

        if (mOCTier1 && !mOCTier2) {
            buildPiece(ocTier1Upgrade, stackSize, hintsOnly, mOCTier1Offsets[0], 9, mOCTier1Offsets[1]);
        }

        if (!mOCTier1 && mOCTier2) {
            buildPiece(ocTier2Upgrade, stackSize, hintsOnly, mOCTier2Offsets[0], 9, mOCTier2Offsets[1]);
        }
    }

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        if (mSetTier < 3) {
            built += survivialBuildPiece(tier1, stackSize, 3, 5, 0, elementBudget, env, false, true);
            if (mSetTier == 2) {
                built += survivialBuildPiece(tier2, stackSize, 7, 6, 2, elementBudget, env, false, true);
            }
        } else {
            built += survivialBuildPiece(tier3, stackSize, 3, 21, 0, elementBudget, env, false, true);
        }

        if (mBioUpgrade) {
            if (mBioRotate) {
                final IGregTechTileEntity tTile = getBaseMetaTileEntity();
                getStructureDefinition()
                        .survivalBuild(
                                this,
                                stackSize,
                                bioUpgrade,
                                tTile.getWorld(),
                                transformFacing(getExtendedFacing()),
                                tTile.getXCoord(),
                                tTile.getYCoord(),
                                tTile.getZCoord(),
                                mBioOffsets[1],
                                6,
                                mBioOffsets[0],
                                elementBudget,
                                env,
                                false);
            } else {
                built += survivialBuildPiece(
                        bioUpgrade, stackSize, mBioOffsets[0], 6, mBioOffsets[2], elementBudget, env, false, true);
            }
        }

        if (mOCTier1 && !mOCTier2) {
            built += survivialBuildPiece(
                    ocTier1Upgrade,
                    stackSize,
                    mOCTier1Offsets[0],
                    9,
                    mOCTier1Offsets[1],
                    elementBudget,
                    env,
                    false,
                    true);
        }
        if (!mOCTier1 && mOCTier2) {
            built += survivialBuildPiece(
                    ocTier2Upgrade,
                    stackSize,
                    mOCTier2Offsets[0],
                    9,
                    mOCTier2Offsets[1],
                    elementBudget,
                    env,
                    false,
                    true);
        }

        return built;
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
                            mTier < 3
                                    ? ((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11)
                                    : ((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(13)),
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
                BlockIcons.getCasingTextureForId(
                        mTier < 3
                                ? ((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11)
                                : ((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(13)),
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (mSetTier < 3) {
            if (!checkPiece(tier1, 3, 5, 0)) {
                return false;
            }

            if (mSetTier == 2 && checkPiece(tier2, 7, 6, 2)) {
                mTier = 2;
            } else {
                mTier = 1;
            }
        } else {
            if (!checkPiece(tier3, 3, 21, 9)) {
                return false;
            }
            mTier = 3;
        }

        if (mBioUpgrade) {
            if (mBioRotate) {
                final IGregTechTileEntity tTile = getBaseMetaTileEntity();
                if (!getStructureDefinition()
                        .check(
                                this,
                                bioUpgrade,
                                tTile.getWorld(),
                                transformFacing(getExtendedFacing()),
                                tTile.getXCoord(),
                                tTile.getYCoord(),
                                tTile.getZCoord(),
                                mBioOffsets[1],
                                6,
                                mBioOffsets[0],
                                !mMachine)) {
                    return false;
                }
            } else {
                if (!checkPiece(bioUpgrade, mBioOffsets[0], 6, mBioOffsets[1])) {
                    return false;
                }
            }
            mUpgradesInstalled++;
        }

        if (mOCTier1 && !mOCTier2) {
            if (!checkPiece(ocTier1Upgrade, mOCTier1Offsets[0], 9, mOCTier1Offsets[1])) {
                return false;
            }
            mUpgradesInstalled++;
        }

        if (mOCTier2 && !mOCTier1) {
            if (!checkPiece(ocTier2Upgrade, mOCTier2Offsets[0], 9, mOCTier2Offsets[1])) {
                return false;
            }
            mUpgradesInstalled++;
        }

        if (mMaintenanceHatches.size() != 1
                || mOutputBusses.size() < 1
                || mInputBusses.size() < 1
                || mInputHatches.size() < 1) {
            return false;
        }

        if (mExoticEnergyHatches.size() + mEnergyHatches.size() < 1) {
            return false;
        }

        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sPCBFactory;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        GT_Recipe.GT_Recipe_Map aMap = getRecipeMap();
        FluidStack[] tFluidInputs = getCompactedFluids();
        if (mSeparate) {
            ArrayList<ItemStack> tInputList = new ArrayList<ItemStack>();
            for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
                for (int i = tBus.getSizeInventory() - 1; i >= 0; i--) {
                    if (tBus.getStackInSlot(i) != null) tInputList.add(tBus.getStackInSlot(i));
                }
                ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
                if (processRecipe(aStack, tInputs, tFluidInputs, aMap)) return true;
                else tInputList.clear();
            }
        } else {
            ItemStack[] tItemInputs = getStoredInputs().toArray(new ItemStack[0]);
            return processRecipe(aStack, tItemInputs, tFluidInputs, aMap);
        }

        return false;
    }

    private boolean processRecipe(
            ItemStack aStack, ItemStack[] tItemInputs, FluidStack[] aFluidInputs, GT_Recipe.GT_Recipe_Map aMap) {
        mOutputItems = null;
        mOutputFluids = null;
        if (tItemInputs == null || aFluidInputs == null) {
            return false;
        }

        long voltage = GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList());
        long amps = GT_ExoticEnergyInputHelper.getMaxInputAmpsMulti(getExoticAndNormalEnergyHatchList());
        long tTotalEU = voltage / getExoticAndNormalEnergyHatchList().size() * amps;

        GT_Recipe tRecipe = aMap.findRecipe(getBaseMetaTileEntity(), true, true, voltage, aFluidInputs, tItemInputs);

        if (tRecipe == null) {
            return false;
        }

        int recipeBitMap = tRecipe.mSpecialValue;

        int aNanitesOfRecipe = 0;

        ItemStack aNanite = tRecipe.getRepresentativeInput(0);
        if (GT_OreDictUnificator.getAssociation(aNanite).mPrefix.equals(OrePrefixes.nanite)) {
            for (ItemStack aItem : tItemInputs) {
                if (aItem.isItemEqual(aNanite)) {
                    aNanitesOfRecipe += aItem.stackSize;
                }
            }
        }

        int aMaxParallel = (int) Math.ceil(Math.log(aNanitesOfRecipe) / Math.log(2));
        float aExtraPower = (float) Math.ceil(Math.sqrt(mUpgradesInstalled == 0 ? 1 : mUpgradesInstalled));

        if (((recipeBitMap & mTier1BitMap) == 1
                        || (recipeBitMap & mTier2BitMap) == 1
                        || (recipeBitMap & mTier3BitMap) == 1)
                && ((recipeBitMap & mBioBitMap) == 0 || (recipeBitMap & mBioBitMap) == 1 == mBioUpgrade)) {

            int aCurrentParallel = 1;
            for (int i = 0; i < aMaxParallel; i++) {
                if (tRecipe.isRecipeInputEqual(true, aFluidInputs, tItemInputs)) {
                    aCurrentParallel++;
                } else {
                    break;
                }
            }

            this.mEfficiency = (getMaxEfficiency(aStack) - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = getMaxEfficiency(aStack);
            this.lEUt = (long) -Math.ceil(tRecipe.mEUt * aCurrentParallel * aExtraPower);
            this.mMaxProgresstime = (int) Math.ceil(tRecipe.mDuration * mRoughnessMultiplier);

            if (mOCTier1 || mOCTier2) {
                calculateOverclockedNessMultiInternal(
                        (long) Math.ceil(tRecipe.mEUt * aCurrentParallel * aExtraPower),
                        (int) Math.ceil(tRecipe.mDuration * mRoughnessMultiplier),
                        1,
                        tTotalEU,
                        mOCTier2);
            }

            if (this.lEUt == Long.MAX_VALUE - 1 || this.mProgresstime == Integer.MAX_VALUE - 1) return false;

            mOutputItems = new ItemStack[tRecipe.mOutputs.length];
            ArrayList<ItemStack> tOutputs = new ArrayList<ItemStack>();
            int remainingEfficiency = getMaxEfficiency(aStack);
            int repeats = (int) Math.ceil(getMaxEfficiency(aStack) / 10000);
            for (int j = 0; j < repeats; j++) {
                for (int i = tItemInputs.length - 1; i >= 0; i--) {
                    if (getBaseMetaTileEntity().getRandomNumber(10000) < remainingEfficiency) {
                        tOutputs.add(tRecipe.getOutput(i));
                    }
                }
                remainingEfficiency -= 10000;
            }
            mOutputItems = tOutputs.toArray(new ItemStack[0]);
            mOutputFluids = tRecipe.mFluidOutputs.clone();
            updateSlots();
            return true;
        }

        return false;
    }

    private int ticker = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            criticalStopMachine();
            return false;
        }

        if (ticker % 20 == 0) {
            if (mOCTier1) {
                if (!depleteInput(GT_ModHandler.getDistilledWater(COOLANT_CONSUMED_PER_SEC))) {
                    criticalStopMachine();
                    return false;
                }
            }

            if (mOCTier2) {
                Fluid superCoolant = FluidRegistry.getFluid("supercoolant");
                if (!depleteInput(new FluidStack(superCoolant, COOLANT_CONSUMED_PER_SEC))) {
                    criticalStopMachine();
                    return false;
                }
            }
            ticker = 0;
        }

        ticker++;

        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return (int) (mRoughnessMultiplier * 10000);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        // TODO Auto-generated method stub
        return 0;
    }

    private ExtendedFacing transformFacing(ExtendedFacing facing) {
        ForgeDirection curDirection = facing.getDirection();
        Rotation curRotation = facing.getRotation();
        Flip curFlip = facing.getFlip();
        ForgeDirection newDirection = curDirection;
        Rotation newRotation = curRotation;
        Flip newFlip = curFlip;

        if (curDirection == ForgeDirection.UP || curDirection == ForgeDirection.DOWN) {
            switch (curRotation) {
                case CLOCKWISE:
                case COUNTER_CLOCKWISE:
                    newFlip = curFlip == Flip.NONE ? Flip.HORIZONTAL : Flip.NONE;
                    newDirection = curDirection == ForgeDirection.UP ? ForgeDirection.NORTH : ForgeDirection.SOUTH;
                    break;
                case NORMAL:
                    newRotation = curDirection == ForgeDirection.UP ? Rotation.CLOCKWISE : Rotation.COUNTER_CLOCKWISE;
                    newDirection = curDirection == ForgeDirection.UP ? ForgeDirection.EAST : ForgeDirection.WEST;
                    newFlip = Flip.NONE;
                    break;
                case UPSIDE_DOWN:
                    newRotation = curDirection == ForgeDirection.UP ? Rotation.COUNTER_CLOCKWISE : Rotation.CLOCKWISE;
                    newDirection = curDirection == ForgeDirection.UP ? ForgeDirection.EAST : ForgeDirection.WEST;
                    newFlip = Flip.NONE;
                    break;
            }
        } else if (curRotation == Rotation.CLOCKWISE || curRotation == Rotation.COUNTER_CLOCKWISE) {
            newFlip = curRotation == Rotation.CLOCKWISE
                    ? curFlip == Flip.NONE ? Flip.NONE : Flip.HORIZONTAL
                    : curFlip != Flip.NONE ? Flip.NONE : Flip.HORIZONTAL;
            newDirection = curRotation == Rotation.CLOCKWISE ? ForgeDirection.UP : ForgeDirection.DOWN;
        } else {
            switch (curDirection) {
                case EAST:
                    newDirection = ForgeDirection.SOUTH;
                    break;
                case NORTH:
                    newDirection = ForgeDirection.EAST;
                    break;
                case WEST:
                    newDirection = ForgeDirection.NORTH;
                    break;
                case SOUTH:
                    newDirection = ForgeDirection.WEST;
                    break;
                default:
                    newDirection = curDirection;
            }
        }

        if (curRotation == Rotation.UPSIDE_DOWN) {
            if (curDirection != ForgeDirection.UP && curDirection != ForgeDirection.DOWN) {
                newFlip = curFlip == Flip.NONE ? Flip.HORIZONTAL : Flip.NONE;
            }
        }

        return ExtendedFacing.of(newDirection, newRotation, newFlip);
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            mCoolantInputHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Circuit Board Fabricator")
                .addInfo("Controller block for the PCB Factory")
                .addInfo(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "IMPORTANT!"
                        + " Check out the configurations menu before building.")
                .addInfo("The tier is selected in the controller GUI. Determines avaliable recipes.")
                .addInfo("The configuration menu can be used to add upgrades.")
                .addInfo("The parallel of the current recipe is Log₂(nanites used), rounded up.")
                .addInfo("Coolant tier determines overclock ability. No cooler allows no overclocking.")
                .addInfo("Tier " + EnumChatFormatting.DARK_PURPLE + 1 + EnumChatFormatting.GRAY
                        + " cooler allows regular overclocking, takes 10L/s of coolant.")
                .addInfo("Tier " + EnumChatFormatting.DARK_PURPLE + 2 + EnumChatFormatting.GRAY
                        + " allows perfect overclocking, takes 10L/s of space coolant.")
                .addInfo("Machine power consumption multiplies by sqrt(upgrade count).")
                .addInfo("I.e. cooler + bio upgrade = sqrt(2) power consumption multiplier.")
                .addInfo(AuthorBlueWeabo)
                .beginStructureBlock(30, 38, 13, false)
                .addSeparator()
                .addMaintenanceHatch(EnumChatFormatting.GOLD + "1", 1)
                .addEnergyHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
                .addInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
                .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
                .addInputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
                .addStructureInfo("Coolant Hatch (Input Hatch): " + EnumChatFormatting.GOLD + "1")
                .addStructureInfo(EnumChatFormatting.BLUE + "Base Multi (Tier " + EnumChatFormatting.DARK_PURPLE + 1
                        + EnumChatFormatting.BLUE + "):")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Damascus Steel Frame Box")
                .addStructureInfo(EnumChatFormatting.GOLD + "9" + EnumChatFormatting.GRAY + " Vibrant Alloy Frame Box")
                .addStructureInfo(EnumChatFormatting.GOLD + "25" + EnumChatFormatting.GRAY + " Reinforced Glass")
                .addStructureInfo(EnumChatFormatting.GOLD + "77" + EnumChatFormatting.GRAY
                        + " Basic Photolithography Framework Casing")
                .addStructureInfo(EnumChatFormatting.GOLD + "12" + EnumChatFormatting.GRAY + " Grate Machine Casing")
                .addStructureInfo(EnumChatFormatting.GOLD + "25" + EnumChatFormatting.GRAY + " Plascrete Block")
                .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 2
                        + EnumChatFormatting.BLUE + " (Adds to Tier " + EnumChatFormatting.DARK_PURPLE + 1
                        + EnumChatFormatting.BLUE + "):")
                .addStructureInfo(EnumChatFormatting.GOLD + "34" + EnumChatFormatting.GRAY + " Duranium Frame Box")
                .addStructureInfo(EnumChatFormatting.GOLD + "158" + EnumChatFormatting.GRAY
                        + " Reinforced Photolithography Framework Casing")
                .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 3
                        + EnumChatFormatting.BLUE + ":")
                .addStructureInfo(EnumChatFormatting.GOLD + "292" + EnumChatFormatting.GRAY
                        + " Radiation Proof Photolithography Framework Casing")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "76" + EnumChatFormatting.GRAY + " Radiant Naqudah Alloy Casing")
                .addStructureInfo(EnumChatFormatting.BLUE + "Bio Upgrade")
                .addStructureInfo(EnumChatFormatting.GOLD + "68" + EnumChatFormatting.GRAY
                        + " Clean Stainless Steel Machine Casing")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Damascus Steel Frame Box")
                .addStructureInfo(EnumChatFormatting.GOLD + "72" + EnumChatFormatting.GRAY + " Reinforced Glass")
                .addStructureInfo(EnumChatFormatting.BLUE + "Cooler Upgrade Tier " + EnumChatFormatting.DARK_PURPLE + 1
                        + EnumChatFormatting.BLUE + ":")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Damascus Steel Frame Box")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "68" + EnumChatFormatting.GRAY + " Radiant Naqudah Alloy Casing")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "12" + EnumChatFormatting.GRAY + " Extreme Engine Intake Casing")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "20" + EnumChatFormatting.GRAY + " Tungstensteel Pipe Casing")
                .addStructureInfo(EnumChatFormatting.GOLD + "21" + EnumChatFormatting.GRAY
                        + " Reinforced Photolithography Framework Casing")
                .addStructureInfo(EnumChatFormatting.BLUE + "Cooler Upgrade Tier " + EnumChatFormatting.DARK_PURPLE + 2
                        + EnumChatFormatting.BLUE + ":")
                .addStructureInfo(EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Americium Frame Box")
                .addStructureInfo(EnumChatFormatting.GOLD + "41" + EnumChatFormatting.GRAY
                        + " Reinforced Photolithography Framework Casing")
                .addStructureInfo(EnumChatFormatting.GOLD + "8" + EnumChatFormatting.GRAY
                        + " Basic Photolithography Framework Casing")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "20" + EnumChatFormatting.GRAY + " Tungstensteel Pipe Casing")
                .addStructureInfo(EnumChatFormatting.GOLD + "48" + EnumChatFormatting.GRAY + " Infinity Cooled Casing")
                .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mSeparate", mSeparate);
        aNBT.setBoolean("mBioUpgrade", mBioUpgrade);
        aNBT.setIntArray("mBioOffsets", mBioOffsets);
        aNBT.setBoolean("mOCTier1Upgrade", mOCTier1);
        aNBT.setIntArray("mOCTier1Offsets", mOCTier1Offsets);
        aNBT.setBoolean("mOCTier2Upgrade", mOCTier2);
        aNBT.setIntArray("mOCTier2Offsets", mOCTier2Offsets);
        aNBT.setFloat("mRoughnessMultiplier", mRoughnessMultiplier);
        aNBT.setInteger("mSetTier", mSetTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mSeparate = aNBT.getBoolean("mSeparate");
        mBioUpgrade = aNBT.getBoolean("mBioUpgrade");
        mBioOffsets = aNBT.getIntArray("mBioOffsets");
        mOCTier1 = aNBT.getBoolean("mOCTier1Upgrade");
        mOCTier1Offsets = aNBT.getIntArray("mOCTier1Offsets");
        mOCTier2 = aNBT.getBoolean("mOCTier2Upgrade");
        mOCTier2Offsets = aNBT.getIntArray("mOCTier2Offsets");
        mRoughnessMultiplier = aNBT.getFloat("mRoughnessMultiplier");
        mSetTier = aNBT.getInteger("mSetTier");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        // not needed here
        return false;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(10, this::createConfigurationWindow);
        builder.widget(new ButtonWidget()
                        .setOnClick((clickData, widget) -> {
                            if (!widget.isClient()) widget.getContext().openSyncedWindow(10);
                        })
                        .setSize(18, 18)
                        .setBackground(GT_UITextures.BUTTON_STANDARD)
                        .setBackground(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                        .addTooltip("Configuration Menu")
                        .setPos(151, 24))
                .widget(new TextWidget(new Text("Tier"))
                        .setTextAlignment(Alignment.Center)
                        .setScale(0.91f)
                        .setSize(20, 16)
                        .setPos(152, 46))
                .widget(new TextFieldWidget()
                        .setGetterInt(() -> mSetTier)
                        .setSetterInt(val -> {
                            mSetTier = val;
                        })
                        .setNumbers(-16, 16)
                        .setTextColor(Color.WHITE.normal)
                        .setTextAlignment(Alignment.Center)
                        .addTooltip("PCB Factory Tier")
                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                        .setSize(18, 18)
                        .setPos(151, 61));
    }

    protected ModularWindow createConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 160);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(new DrawableWidget()
                        .setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                        .setPos(5, 5)
                        .setSize(16, 16))
                .widget(new TextWidget("Configuration Menu").setPos(25, 9))
                .widget(ButtonWidget.closeWindowButton(true).setPos(185, 3))
                .widget(new DynamicPositionedColumn()
                        .setSynced(false)
                        .widget(new MultiChildWidget()
                                .addChild(new CycleButtonWidget()
                                        .setToggle(() -> mBioUpgrade, val -> {
                                            mBioUpgrade = val;
                                            if (!mBioUpgrade) {
                                                GT_Utility.sendChatToPlayer(
                                                        player, GT_Utility.trans("339.1", "Bio Upgrade Disabled"));
                                            } else {
                                                GT_Utility.sendChatToPlayer(
                                                        player, GT_Utility.trans("339", "Bio Upgrade Enabled"));
                                            }
                                        })
                                        .setVariableBackgroundGetter(state -> state == 0
                                                ? new IDrawable[] {GT_UITextures.BUTTON_STANDARD}
                                                : new IDrawable[] {ModularUITextures.ITEM_SLOT})
                                        .setSize(90, 18)
                                        .addTooltip("Required for Bioware and Wetware boards."))
                                .addChild(new DrawableWidget()
                                        .setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                        .setSize(18, 18))
                                .addChild(new TextWidget("Bio Upgrade")
                                        .setTextAlignment(Alignment.Center)
                                        .setPos(23, 5))
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new MultiChildWidget()
                                .addChild(new CycleButtonWidget()
                                        .setToggle(() -> mBioRotate, val -> {
                                            mBioRotate = val;
                                            if (!mBioRotate) {
                                                GT_Utility.sendChatToPlayer(
                                                        player,
                                                        GT_Utility.trans(
                                                                "340.1", "Rotate Bio Upgrade 90 Degrees Disabled"));
                                            } else {
                                                GT_Utility.sendChatToPlayer(
                                                        player,
                                                        GT_Utility.trans(
                                                                "340", "Rotate Bio Upgrade 90 Degrees Enabled"));
                                            }
                                        })
                                        .setVariableBackgroundGetter(state -> state == 0
                                                ? new IDrawable[] {GT_UITextures.BUTTON_STANDARD}
                                                : new IDrawable[] {ModularUITextures.ITEM_SLOT})
                                        .setSize(90, 18)
                                        .addTooltip("Switches around the X and Z axis, rotates the shape 90 degrees"))
                                .addChild(new DrawableWidget()
                                        .setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                        .setSize(18, 18))
                                .addChild(new TextWidget("Bio Rotation")
                                        .setTextAlignment(Alignment.Center)
                                        .setPos(23, 5))
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new MultiChildWidget()
                                .addChild(
                                        new CycleButtonWidget()
                                                .setToggle(() -> mOCTier1, val -> {
                                                    mOCTier1 = val;
                                                    if (!mOCTier1) {
                                                        GT_Utility.sendChatToPlayer(
                                                                player,
                                                                GT_Utility.trans("341.1", "Tier 1 OC Disabled"));
                                                    } else {
                                                        GT_Utility.sendChatToPlayer(
                                                                player, GT_Utility.trans("341", "Tier 1 OC Enabled"));
                                                    }
                                                })
                                                .setVariableBackgroundGetter(state -> state == 0
                                                        ? new IDrawable[] {GT_UITextures.BUTTON_STANDARD}
                                                        : new IDrawable[] {ModularUITextures.ITEM_SLOT})
                                                .setSize(90, 18)
                                                .addTooltip(
                                                        "Incompatible with Tier 2, Requires a constant supply of distilled water. Allows for overclocking"))
                                .addChild(new DrawableWidget()
                                        .setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                        .setSize(18, 18))
                                .addChild(new TextWidget("Cooler Tier 1")
                                        .setTextAlignment(Alignment.Center)
                                        .setPos(20, 5))
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new MultiChildWidget()
                                .addChild(
                                        new CycleButtonWidget()
                                                .setToggle(() -> mOCTier2, val -> {
                                                    mOCTier2 = val;
                                                    if (!mOCTier2) {
                                                        GT_Utility.sendChatToPlayer(
                                                                player,
                                                                GT_Utility.trans("342.1", "Tier 2 OC Disabled"));
                                                    } else {
                                                        GT_Utility.sendChatToPlayer(
                                                                player, GT_Utility.trans("342", "Tier 2 OC Enabled"));
                                                    }
                                                })
                                                .setVariableBackgroundGetter(state -> state == 0
                                                        ? new IDrawable[] {GT_UITextures.BUTTON_STANDARD}
                                                        : new IDrawable[] {ModularUITextures.ITEM_SLOT})
                                                .setSize(90, 18)
                                                .addTooltip(
                                                        "Incompatible with Tier 1, Requires a constant supply of super coolant. Allows for perfect overclocking"))
                                .addChild(new DrawableWidget()
                                        .setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                        .setSize(18, 18))
                                .addChild(new TextWidget("Cooler Tier 2")
                                        .setTextAlignment(Alignment.Center)
                                        .setPos(20, 5))
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new TextWidget(new Text("Roughness Multiplier"))
                                .setSize(90, 18)
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                                .setPos(0, 4))
                        .widget(new TextFieldWidget()
                                .setGetterInt(() -> (int) (mRoughnessMultiplier * 10000))
                                .setSetterInt(val -> {
                                    mRoughnessMultiplier = val / 10000f;
                                })
                                .setNumbers(100, 100000)
                                .setTextColor(Color.WHITE.normal)
                                .setTextAlignment(Alignment.Center)
                                .addTooltip("The roughness multiplier is multiplied by 10,000 before displaying!")
                                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                .setSize(90, 16))
                        .widget(new DrawableWidget()
                                .setDrawable(GT_UITextures.OVERLAY_BUTTON_CROSS)
                                .setSize(18, 18)
                                .addTooltip(
                                        new Text("Can't change configuration when running !").color(Color.RED.dark(3)))
                                .setEnabled(widget -> getBaseMetaTileEntity().isActive()))
                        .setPos(10, 25))
                .widget(new DynamicPositionedColumn()
                        .setSynced(false)
                        .widget(new TextWidget(new Text("Bio Upgrade Offsets"))
                                .setSize(72, 18)
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new DynamicPositionedRow()
                                .setSynced(false)
                                .widget(new TextFieldWidget()
                                        .setGetterInt(() -> mBioOffsets[0])
                                        .setSetterInt(val -> {
                                            mBioOffsets[0] = val;
                                        })
                                        .setNumbers(-16, 16)
                                        .setTextColor(Color.WHITE.normal)
                                        .setTextAlignment(Alignment.Center)
                                        .addTooltip("X Offset")
                                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                        .setSize(36, 18))
                                .widget(new TextFieldWidget()
                                        .setGetterInt(() -> mBioOffsets[1])
                                        .setSetterInt(val -> {
                                            mBioOffsets[1] = val;
                                        })
                                        .setNumbers(-16, 16)
                                        .setTextColor(Color.WHITE.normal)
                                        .setTextAlignment(Alignment.Center)
                                        .addTooltip("Z Offset")
                                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                        .setSize(36, 18))
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new TextWidget(new Text("Cooler Tier 1 Offsets"))
                                .setSize(72, 18)
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new DynamicPositionedRow()
                                .setSynced(false)
                                .widget(new TextFieldWidget()
                                        .setGetterInt(() -> mOCTier1Offsets[0])
                                        .setSetterInt(val -> {
                                            mOCTier1Offsets[0] = val;
                                        })
                                        .setNumbers(-16, 16)
                                        .setTextColor(Color.WHITE.normal)
                                        .setTextAlignment(Alignment.Center)
                                        .addTooltip("X Offset")
                                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                        .setSize(36, 18))
                                .widget(new TextFieldWidget()
                                        .setGetterInt(() -> mOCTier1Offsets[1])
                                        .setSetterInt(val -> {
                                            mOCTier1Offsets[1] = val;
                                        })
                                        .setNumbers(-16, 16)
                                        .setTextColor(Color.WHITE.normal)
                                        .setTextAlignment(Alignment.Center)
                                        .addTooltip("Z Offset")
                                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                        .setSize(36, 18))
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new TextWidget(new Text("Cooler Tier 2 Offsets"))
                                .setSize(72, 18)
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .widget(new DynamicPositionedRow()
                                .setSynced(false)
                                .widget(new TextFieldWidget()
                                        .setGetterInt(() -> mOCTier2Offsets[0])
                                        .setSetterInt(val -> {
                                            mOCTier2Offsets[0] = val;
                                        })
                                        .setNumbers(-16, 16)
                                        .setTextColor(Color.WHITE.normal)
                                        .setTextAlignment(Alignment.Center)
                                        .addTooltip("X Offset")
                                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                        .setSize(36, 18))
                                .widget(new TextFieldWidget()
                                        .setGetterInt(() -> mOCTier2Offsets[1])
                                        .setSetterInt(val -> {
                                            mOCTier2Offsets[1] = val;
                                        })
                                        .setNumbers(-16, 16)
                                        .setTextColor(Color.WHITE.normal)
                                        .setTextAlignment(Alignment.Center)
                                        .addTooltip("Z Offset")
                                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                        .setSize(36, 18))
                                .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                        .setPos(110, 25));
        return builder.build();
    }
}
