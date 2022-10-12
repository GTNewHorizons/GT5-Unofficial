package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.*;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CoolantTower extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<CoolantTower> multiDefinition = null;
    private final int CASING_INDEX = 1542;

    public CoolantTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public CoolantTower(String name) {
        super(name);
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<CoolantTower>builder()
                    .addShape(mName, transpose(new String[][] {
                        {
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   B   B   ",
                            "  B     B  ",
                            "  B     B  ",
                            "  B     B  ",
                            "   B   B   ",
                            "    BBB    ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   BBBBB   ",
                            "  BB   BB  ",
                            "  BB   BB  ",
                            "  BB   BB  ",
                            "   BBBBB   ",
                            "    BBB    ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   B   B   ",
                            "   B   B   ",
                            "   B   B   ",
                            "    BBB    ",
                            "           ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   B   B   ",
                            "   B   B   ",
                            "   B   B   ",
                            "    BBB    ",
                            "           ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   B   B   ",
                            "   B   B   ",
                            "   B   B   ",
                            "    BBB    ",
                            "           ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   BBBBB   ",
                            "  BB   BB  ",
                            "  BB   BB  ",
                            "  BB   BB  ",
                            "   BBBBB   ",
                            "    BBB    ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   B   B   ",
                            "  B     B  ",
                            "  B     B  ",
                            "  B     B  ",
                            "   B   B   ",
                            "    BBB    ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "           ",
                            "    BBB    ",
                            "   B   B   ",
                            "  B     B  ",
                            "  B     B  ",
                            "  B     B  ",
                            "   B   B   ",
                            "    BBB    ",
                            "           ",
                            "           "
                        },
                        {
                            "           ",
                            "    BBB    ",
                            "   BBBBB   ",
                            "  BB   BB  ",
                            " BB     BB ",
                            " BB     BB ",
                            " BB     BB ",
                            "  BB   BB  ",
                            "   BBBBB   ",
                            "    BBB    ",
                            "           "
                        },
                        {
                            "           ",
                            "    BBB    ",
                            "   B   B   ",
                            "  B     B  ",
                            " B       B ",
                            " B       B ",
                            " B       B ",
                            "  B     B  ",
                            "   B   B   ",
                            "    BBB    ",
                            "           "
                        },
                        {
                            "           ",
                            "   BBBBB   ",
                            "  BB   BB  ",
                            " BB     BB ",
                            " B       B ",
                            " B       B ",
                            " B       B ",
                            " BB     BB ",
                            "  BB   BB  ",
                            "   BBBBB   ",
                            "           "
                        },
                        {
                            "   HH~HH   ",
                            "  HBBBBBH  ",
                            " HB     BH ",
                            "HB       BH",
                            "HB       BH",
                            "HB       BH",
                            "HB       BH",
                            "HB       BH",
                            " HB     BH ",
                            "  HBBBBBH  ",
                            "   HHHHH   "
                        },
                        {
                            "   CCCCC   ",
                            "  C     C  ",
                            " C       C ",
                            "C         C",
                            "C         C",
                            "C         C",
                            "C         C",
                            "C         C",
                            " C       C ",
                            "  C     C  ",
                            "   CCCCC   "
                        },
                    }))
                    .addElement('B', ofBlockAnyMeta(GregTech_API.sBlockConcretes, 8))
                    .addElement('C', ofFrame(Materials.TungstenCarbide))
                    .addElement(
                            'H',
                            ofChain(buildHatchAdder(CoolantTower.class)
                                    .atLeast(GT_HatchElement.InputHatch, GT_HatchElement.OutputHatch)
                                    .casingIndex(CASING_INDEX)
                                    .dot(1)
                                    .buildAndChain(ofBlockAnyMeta(GregTech_API.sBlockConcretes, 8))))
                    .build();
        }
        return multiDefinition;
    }

    public final boolean addIOFluidToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
        return structureCheck_EM(mName, 5, 11, 0);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Coolant Tower")
                .addInfo("Controller block for the Coolant Tower.")
                .addInfo("Turn Steam back to Distilled Water.")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .addController("Mid of the second layer.")
                .addInputHatch("Input Hatch", 1)
                .addOutputHatch("Output Hatch", 1)
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 5, 11, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("CoolantTower.hint", 3);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new CoolantTower(mName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 72000 == 0) {
            mWrench = true;
            mScrewdriver = true;
            mSoftHammer = true;
            mHardHammer = true;
            mSolderingTool = true;
            mCrowbar = true;
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        this.mMaxProgresstime = 100;
        int steam = 0;
        for (FluidStack steams : getStoredFluids()) {
            if (GT_Utility.areFluidsEqual(steams, GT_ModHandler.getSteam(1))) {
                steam += steams.amount;
            }
        }
        steam = steam / 160 * 160;
        depleteInput(GT_ModHandler.getSteam(steam));
        addOutput(GT_ModHandler.getDistilledWater(steam / 160));
        return true;
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
                    casingTexturePages[12][6],
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                casingTexturePages[12][6],
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {casingTexturePages[12][6]};
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 5, 11, 0, elementBudget, source, actor, false, true);
    }
}
