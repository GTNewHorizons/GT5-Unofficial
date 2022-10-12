package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class FuelRefineFactory extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements TecTechEnabledMulti, IConstructable, ISurvivalConstructable {

    private IStructureDefinition<FuelRefineFactory> multiDefinition = null;
    private int Tier = -1;
    private int[] cnt = new int[] {0, 0, 0};
    private static final Block[] coils = new Block[] {Loaders.FRF_Coil_1, Loaders.FRF_Coil_2, Loaders.FRF_Coil_3};

    public FuelRefineFactory(String name) {
        super(name);
    }

    public FuelRefineFactory(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyTunnel> getTecTechEnergyTunnels() {
        return new ArrayList<>();
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyMulti> getTecTechEnergyMultis() {
        return new ArrayList<>();
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 7, 12, 1, itemStack, hintsOnly);
    }

    @Override
    public IStructureDefinition<FuelRefineFactory> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<FuelRefineFactory>builder()
                    .addShape(mName, transpose(new String[][] {
                        {"               ", "      CCC      ", "               "},
                        {"      XGX      ", "    CCFFFCC    ", "      XGX      "},
                        {"    CC   CC    ", "   CFFCCCFFC   ", "    CC   CC    "},
                        {"   C       C   ", "  CFCC   CCFC  ", "   C       C   "},
                        {"  C         C  ", " CFC       CFC ", "  C         C  "},
                        {"  C         C  ", " CFC       CFC ", "  C         C  "},
                        {" X           X ", "CFC         CFC", " X           X "},
                        {" G           G ", "CFC         CFC", " G           G "},
                        {" X           X ", "CFC         CFC", " X           X "},
                        {"  C         C  ", " CFC       CFC ", "  C         C  "},
                        {"  C         C  ", " CFC       CFC ", "  C         C  "},
                        {"   C       C   ", "  CFCC   CCFC  ", "   C       C   "},
                        {"    CC   CC    ", "   CFFC~CFFC   ", "    CC   CC    "},
                        {"      XGX      ", "    CCFFFCC    ", "      XGX      "},
                        {"               ", "      CCC      ", "               "}
                    }))
                    .addElement(
                            'X',
                            buildHatchAdder(FuelRefineFactory.class)
                                    .atLeast(
                                            GT_HatchElement.Maintenance,
                                            GT_HatchElement.InputHatch,
                                            GT_HatchElement.InputBus,
                                            GT_HatchElement.OutputHatch,
                                            HatchElement.EnergyMulti.or(GT_HatchElement.Energy))
                                    .casingIndex(50)
                                    .dot(1)
                                    .buildAndChain(ofBlock(Loaders.FRF_Casings, 0)))
                    .addElement('C', ofBlock(Loaders.FRF_Casings, 0))
                    .addElement('G', ofBlock(Loaders.fieldRestrictingGlass, 0))
                    .addElement(
                            'F',
                            ofChain(
                                    onElementPass(x -> ++x.cnt[0], ofFieldCoil(0)),
                                    onElementPass(x -> ++x.cnt[1], ofFieldCoil(1)),
                                    onElementPass(x -> ++x.cnt[2], ofFieldCoil(2))))
                    .build();
        }
        return multiDefinition;
    }

    public static <T> IStructureElement<T> ofFieldCoil(int aIndex) {
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                return block.equals(coils[aIndex]);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, coils[getIndex(trigger)], 0);
                return true;
            }

            private int getIndex(ItemStack trigger) {
                int s = trigger.stackSize;
                if (s > 3 || s <= 0) s = 3;
                return s - 1;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, coils[getIndex(trigger)], 0, 3);
            }
        };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Naquadah Fuel Refinery")
                .addInfo("Controller block for the Naquadah Fuel Refinery")
                .addInfo("But at what cost?")
                .addInfo("Produce the endgame naquadah fuel.")
                .addInfo("Need field restriction coil to control the fatal radiation.")
                .addInfo("Use higher tier coil to unlock more fuel and reduce the process time.")
                .addInfo("The structure is too complex!")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .beginStructureBlock(3, 15, 15, false)
                .addInputHatch("The casings adjoin the field restriction glass.")
                .addInputBus("The casings adjoin the field restriction glass.")
                .addOutputHatch("The casings adjoin the field restriction glass.")
                .addEnergyHatch("The casings adjoin the field restriction glass.")
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.Tier = aNBT.getInteger("mTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mTier", this.Tier);
        super.saveNBTData(aNBT);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("FuelRefineFactory.hint", 8);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        cnt[0] = 0;
        cnt[1] = 0;
        cnt[2] = 0;
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
        return structureCheck_EM(mName, 7, 12, 1) && getTier() != -1;
    }

    public int getTier() {
        for (int i = 0; i < 3; i++) {
            if (cnt[i] == 32) {
                Tier = i + 1;
                return i;
            }
        }
        Tier = -1;
        return -1;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {

        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;

        ArrayList<FluidStack> tFluids = getStoredFluids();
        ArrayList<ItemStack> tItems = getStoredInputs();
        MyRecipeAdder.NaqFuelRefineMapper tRecipes = MyRecipeAdder.instance.FRF;

        for (int i = 0; i < tFluids.size() - 1; i++) {
            for (int j = i + 1; j < tFluids.size(); j++) {
                if (GT_Utility.areFluidsEqual(tFluids.get(i), tFluids.get(j))) {
                    if ((tFluids.get(i)).amount >= (tFluids.get(j)).amount) {
                        tFluids.remove(j--);
                    } else {
                        tFluids.remove(i--);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < tItems.size() - 1; i++) {
            for (int j = i + 1; j < tItems.size(); j++) {
                if (GT_Utility.areStacksEqual(tItems.get(i), tItems.get(j))) {
                    if ((tItems.get(i)).stackSize >= (tItems.get(j)).stackSize) {
                        tItems.remove(j--);
                    } else {
                        tItems.remove(i--);
                        break;
                    }
                }
            }
        }

        FluidStack[] inFluids = tFluids.toArray(new FluidStack[0]);
        ItemStack[] inItems = tItems.toArray(new ItemStack[0]);
        this.mEfficiency = 10000;

        long tPower = getMaxInputEnergy_EM();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tPower));
        GT_Recipe recipe = tRecipes.findRecipe(this.getBaseMetaTileEntity(), false, V[tTier], inFluids, inItems);
        if (recipe != null) {
            if (recipe.mSpecialValue > Tier) return false;
            if (recipe.isRecipeInputEqual(true, inFluids, inItems)) {
                mEUt = recipe.mEUt * (1 << (Tier - recipe.mSpecialValue));
                mEUt = -Math.abs(mEUt);
                mMaxProgresstime = recipe.mDuration / (1 << (Tier - recipe.mSpecialValue));
                this.mOutputFluids = recipe.mFluidOutputs;
                this.updateSlots();
                return true;
            }
        }
        return false;
    }

    public final boolean addToFRFList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                }
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                    return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
                    return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                    return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
                    return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
                    return this.eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new FuelRefineFactory(this.mName);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        String[] infoData = new String[super.getInfoData().length + 1];
        System.arraycopy(super.getInfoData(), 0, infoData, 0, super.getInfoData().length);
        infoData[super.getInfoData().length] = StatCollector.translateToLocal("scanner.info.FRF") + " " + this.Tier;
        return infoData;
    }

    @Override
    @SuppressWarnings("ALL")
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
                    Textures.BlockIcons.getCasingTextureForId(179),
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE),
                    TextureFactory.builder()
                            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                            .glow()
                            .build()
                };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(179),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE),
                TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(179)};
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 7, 12, 1, elementBudget, source, actor, false, true);
    }
}
