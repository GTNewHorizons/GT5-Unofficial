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
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_PCBFactoryManager;
import gregtech.api.util.GT_Recipe;
import gregtech.common.blocks.GT_Block_Casings8;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

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
    private float mSpeedMultiplier = 1;
    private byte mTier = 1, mSetTier = 1, mUpgradesInstalled = 0;
    private boolean mBioUpgrade = false, mBioRotate = false, mOCTier1 = false, mOCTier2 = false;
    private byte[] mBioOffsets = new byte[2], mOCTier1Offsets = new byte[2], mOCTier2Offsets = new byte[2];
    private GT_MetaTileEntity_Hatch_Input mCoolantInputHatch;
    private static final int mBioBitMap = 0b1000;
    private static final int mTier3BitMap = 0b100;
    private static final int mTier2BitMap = 0b10;
    private static final int mTier1BitMap = 0b1;
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
                buildPiece(tier2, stackSize, hintsOnly, 7, 6, -1);
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
            buildPiece(ocTier1Upgrade, stackSize, hintsOnly, mOCTier1Offsets[0], 10, mOCTier1Offsets[1]);
        }
    }

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        if (mSetTier < 3) {
            built += survivialBuildPiece(tier1, stackSize, 3, 5, 0, elementBudget, env, false, true);
            if (mSetTier == 2) {
                built += survivialBuildPiece(tier2, stackSize, 7, 6, -1, elementBudget, env, false, true);
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
                    10,
                    mOCTier1Offsets[1],
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

            if (mSetTier == 2 && checkPiece(tier2, 7, 6, -1)) {
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
            if (!checkPiece(ocTier1Upgrade, mOCTier1Offsets[0], 10, mOCTier1Offsets[1])) {
                return false;
            }
            mUpgradesInstalled++;
        }

        if (mOCTier2 && !mOCTier1) {
            if (!checkPiece(ocTier2Upgrade, mOCTier2Offsets[0], 0, mOCTier2Offsets[1])) {
                return false;
            }
            mUpgradesInstalled++;
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
            ItemStack[] tItemInputs = getCompactedInputs();
            return processRecipe(aStack, tItemInputs, tFluidInputs, aMap);
        }

        return false;
    }

    private boolean processRecipe(
            ItemStack aStack, ItemStack[] aInputs, FluidStack[] aFluidInputs, GT_Recipe.GT_Recipe_Map aMap) {
        mOutputItems = null;
        mOutputFluids = null;
        if (aInputs == null || aFluidInputs == null) {
            return false;
        }

        long voltage = GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList());

        GT_Recipe tRecipe = aMap.findRecipe(getBaseMetaTileEntity(), true, true, voltage, aFluidInputs, aInputs);

        if (tRecipe == null) {
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
        tt.addMachineType("PCB Factory")
                .addInfo("Controller block for the PCB Factory")
                .addInfo(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + "!IMPORTANT!"
                        + " Check out the configurations menu before building")
                .addInfo(AuthorBlueWeabo)
                .addSeparator()
                .beginStructureBlock(30, 38, 13, false)
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

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mSeparate", mSeparate);
        aNBT.setBoolean("mBioUpgrade", mBioUpgrade);
        aNBT.setByteArray("mBioOffsets", mBioOffsets);
        aNBT.setBoolean("mOCTier1Upgrade", mOCTier1);
        aNBT.setByteArray("mOCTier1Offsets", mOCTier1Offsets);
        aNBT.setBoolean("mOCTier2Upgrade", mOCTier2);
        aNBT.setByteArray("mOCTier2Offsets", mOCTier2Offsets);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mSeparate = aNBT.getBoolean("mSeparate");
        mBioUpgrade = aNBT.getBoolean("mBioUpgrade");
        mBioOffsets = aNBT.getByteArray("mBioOffsets");
        mOCTier1 = aNBT.getBoolean("mOCTier1Upgrade");
        mOCTier1Offsets = aNBT.getByteArray("mOCTier1Offsets");
        mOCTier2 = aNBT.getBoolean("mOCTier2Upgrade");
        mOCTier2Offsets = aNBT.getByteArray("mOCTier2Offsets");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        // not needed here
        return false;
    }

    private int findPlasticTier(ItemStack[] aItems) {
        int maxTier = 0;
        for (ItemStack aItem : aItems) {
            Materials aMaterial = GT_OreDictUnificator.getAssociation(aItem).mMaterial.mMaterial;
            int curTier = GT_PCBFactoryManager.getPlasticTier(aMaterial);
            if (curTier > maxTier) {
                maxTier = curTier;
            }
        }

        return maxTier;
    }
}
