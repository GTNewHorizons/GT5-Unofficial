package goodgenerator.blocks.tileEntity;

import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static goodgenerator.util.StructureHelper.addTieredBlock;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.network.NetworkRegistry;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.main.GoodGenerator;
import goodgenerator.network.MessageResetTileTexture;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public class PreciseAssembler extends GT_MetaTileEntity_TooltipMultiBlockBase_EM implements IConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow =
            new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow =
            new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST_GLOW");

    protected IStructureDefinition<PreciseAssembler> multiDefinition = null;
    protected int casingAmount;
    protected int casingTier;
    protected int machineTier;
    protected int mode;
    protected int energyHatchTier;

    public PreciseAssembler(String name) {
        super(name);
    }

    public PreciseAssembler(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IStructureDefinition<PreciseAssembler> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<PreciseAssembler>builder()
                    .addShape(mName, transpose(new String[][] {
                        {"CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC"},
                        {"F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F"},
                        {"F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F"},
                        {"F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F"},
                        {"CCCC~CCCC", "CMMMMMMMC", "CMMMMMMMC", "CMMMMMMMC", "CCCCCCCCC"}
                    }))
                    .addElement(
                            'C',
                            ofChain(
                                    ofHatchAdder(PreciseAssembler::addToPAssList, 0, 1),
                                    onElementPass(
                                            x -> x.casingAmount++,
                                            addTieredBlock(
                                                    Loaders.preciseUnitCasing,
                                                    PreciseAssembler::setCasingTier,
                                                    PreciseAssembler::getCasingTier,
                                                    3))))
                    .addElement('F', ofFrame(Materials.TungstenSteel))
                    .addElement('G', ofBlock(Block.getBlockFromItem(Ic2Items.reinforcedGlass.getItem()), 0))
                    .addElement(
                            'M',
                            addTieredBlock(
                                    GregTech_API.sBlockCasings1,
                                    PreciseAssembler::setMachineTier,
                                    PreciseAssembler::getMachineTier,
                                    10))
                    .build();
        }
        return multiDefinition;
    }

    public boolean addToPAssList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        casingTier = aNBT.getInteger("casingTier");
        machineTier = aNBT.getInteger("machineTier");
        mode = aNBT.getInteger("RunningMode");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("casingTier", casingTier);
        aNBT.setInteger("machineTier", machineTier);
        aNBT.setInteger("RunningMode", mode);
        super.saveNBTData(aNBT);
    }

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("preciseassembler.chat." + this.mode));
        }
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
    }

    protected void calculateOverclockedNessMultiPara(int aEUt, int aDuration, int mAmperage, long maxInputPower) {
        while (aEUt <= maxInputPower && aDuration >= 1) {
            aEUt = aEUt << 2;
            aDuration = aDuration >> 1;
        }
        aEUt = aEUt >> 2;
        aDuration = aDuration << 1;
        if (aDuration == 0) aDuration = 1;
        if (aEUt == maxInputPower) aEUt = (int) (maxInputPower * 0.9);
        this.mEUt = aEUt;
        this.mMaxProgresstime = aDuration;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        if (casingTier <= 0 || machineTier <= 0) return false;
        FluidStack[] inputFluids = getStoredFluids().toArray(new FluidStack[0]);
        if (this.mode == 0) {
            for (GT_MetaTileEntity_Hatch_InputBus bus : mInputBusses) {
                if (!isValidMetaTileEntity(bus)) continue;
                GT_Recipe tRecipe = getRecipeMap()
                        .findRecipe(
                                this.getBaseMetaTileEntity(),
                                false,
                                Math.min(getMachineVoltageLimit(), getMaxInputEnergyPA()),
                                inputFluids,
                                getStoredItemFromHatch(bus));
                if (tRecipe != null && tRecipe.mSpecialValue <= casingTier) {
                    this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    tRecipe.isRecipeInputEqual(true, inputFluids, getStoredItemFromHatch(bus));
                    mOutputItems = tRecipe.mOutputs;
                    calculateOverclockedNessMultiPara(
                            tRecipe.mEUt,
                            tRecipe.mDuration,
                            1,
                            Math.min(getMachineVoltageLimit(), getMaxInputEnergyPA()));
                    this.updateSlots();
                    if (this.mEUt > 0) {
                        this.mEUt = (-this.mEUt);
                    }
                    return true;
                }
            }
        } else {
            for (GT_MetaTileEntity_Hatch_InputBus bus : mInputBusses) {
                if (!isValidMetaTileEntity(bus) || getStoredItemFromHatch(bus).length < 1) continue;
                GT_Recipe tRecipe = getRecipeMap()
                        .findRecipe(
                                this.getBaseMetaTileEntity(),
                                false,
                                Math.min(getMachineVoltageLimit(), getMaxInputEnergyPA()),
                                inputFluids,
                                getStoredItemFromHatch(bus));
                if (tRecipe != null) {
                    this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    long fullInput = getMaxInputEnergy_EM();
                    int pall = handleParallelRecipe(tRecipe, inputFluids, getStoredItemFromHatch(bus), (int)
                            Math.min((long) Math.pow(2, 4 + casingTier), fullInput / tRecipe.mEUt));
                    if (pall <= 0) continue;
                    Pair<ArrayList<FluidStack>, ArrayList<ItemStack>> Outputs = getMultiOutput(tRecipe, pall);
                    long lEUt = (long) tRecipe.mEUt * (long) pall;
                    int time = tRecipe.mDuration / 2;
                    int modifier = 1;
                    while (lEUt >= Integer.MAX_VALUE - 1) {
                        lEUt = (long) tRecipe.mEUt * (long) pall / modifier;
                        time = tRecipe.mDuration / 2 * modifier;
                        modifier++;
                    }
                    mOutputItems = Outputs.getValue().toArray(new ItemStack[0]);
                    calculateOverclockedNessMultiPara(
                            (int) lEUt, time, 1, Math.min(Integer.MAX_VALUE - 1, getMaxInputEnergy_EM()));
                    this.updateSlots();
                    if (this.mEUt > 0) {
                        this.mEUt = (-this.mEUt);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Modified version of {@link #getMaxInputEnergy()}
     */
    private long getMaxInputEnergyPA() {
        long rEnergy = 0;
        if (mEnergyHatches.size() == 1) {
            // it works like most of the gt multies
            return mEnergyHatches.get(0).getBaseMetaTileEntity().getInputVoltage();
        }
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                rEnergy += tHatch.getBaseMetaTileEntity().getInputVoltage()
                        * tHatch.getBaseMetaTileEntity().getInputAmperage();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                rEnergy += tHatch.getBaseMetaTileEntity().getInputVoltage()
                        * tHatch.getBaseMetaTileEntity().getInputAmperage();
            }
        }
        return rEnergy;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public long getMachineVoltageLimit() {
        if (machineTier <= 0) return 0;
        if (machineTier >= 10) return GT_Values.V[energyHatchTier];
        else return GT_Values.V[Math.min(machineTier - 1, energyHatchTier)];
    }

    public ItemStack[] getStoredItemFromHatch(GT_MetaTileEntity_Hatch_InputBus tHatch) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
            if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) != null)
                rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
        }
        return rList.toArray(new ItemStack[0]);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        if (this.mode == 0) return MyRecipeAdder.instance.PA;
        else return GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
    }

    @Override
    public boolean drainEnergyInput_EM(long EUtTierVoltage, long EUtEffective, long Amperes) {
        long EUuse = EUtEffective * Amperes;
        if (EUuse < 0) {
            EUuse = -EUuse;
        }
        for (GT_MetaTileEntity_Hatch tHatch : mEnergyHatches) {
            long tDrain = Math.min(tHatch.getBaseMetaTileEntity().getStoredEU(), EUuse);
            tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tDrain, false);
            EUuse -= tDrain;
        }
        for (GT_MetaTileEntity_Hatch tHatch : eEnergyMulti) {
            long tDrain = Math.min(tHatch.getBaseMetaTileEntity().getStoredEU(), EUuse);
            tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tDrain, false);
            EUuse -= tDrain;
        }
        return EUuse <= 0;
    }

    @Override
    public boolean drainEnergyInput(long EUtEffective, long Amperes) {
        long EUuse = EUtEffective * Amperes;
        if (EUuse < 0) {
            EUuse = -EUuse;
        }
        for (GT_MetaTileEntity_Hatch tHatch : mEnergyHatches) {
            long tDrain = Math.min(tHatch.getBaseMetaTileEntity().getStoredEU(), EUuse);
            tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tDrain, false);
            EUuse -= tDrain;
        }
        for (GT_MetaTileEntity_Hatch tHatch : eEnergyMulti) {
            long tDrain = Math.min(tHatch.getBaseMetaTileEntity().getStoredEU(), EUuse);
            tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tDrain, false);
            EUuse -= tDrain;
        }
        return EUuse <= 0;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 4, 4, 0, stackSize, hintsOnly);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.machineTier = 0;
        this.casingAmount = 0;
        this.casingTier = 0;
        this.energyHatchTier = 0;
        if (structureCheck_EM(mName, 4, 4, 0)) {
            energyHatchTier = checkEnergyHatchTier();
            if (casingTier != 0) {
                reUpdate(1538 + casingTier);
            }
            GoodGenerator.CHANNEL.sendToAllAround(
                    new MessageResetTileTexture(aBaseMetaTileEntity, casingTier),
                    new NetworkRegistry.TargetPoint(
                            aBaseMetaTileEntity.getWorld().provider.dimensionId,
                            aBaseMetaTileEntity.getXCoord(),
                            aBaseMetaTileEntity.getYCoord(),
                            aBaseMetaTileEntity.getZCoord(),
                            16));
            return casingAmount >= 42
                    && machineTier != 0
                    && casingTier != 0
                    && mMaintenanceHatches.size() == 1
                    && !mMufflerHatches.isEmpty();
        }
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Precise Assembler/Assembler")
                .addInfo("Controller block for the Precise Assembler")
                .addInfo("The error is no more than 0.03mm.")
                .addInfo("Can assemble precise component in Precise Mode.")
                .addInfo("Can work like a normal assembler in Normal Mode.")
                .addInfo("Use screwdriver to change mode.")
                .addInfo("Machine Casing and Energy Hatch limits the voltage tier the machine can work on.")
                .addInfo("UHV Machine Casing will unlock all voltage, but you still need good Energy Hatch.")
                .addInfo("Precise Electronic Unit Casing won't limit recipe in Normal Mode.")
                .addInfo("But gives more parallel with more advanced one.")
                .addInfo("It is 100% faster in Normal Mode.")
                .addInfo("MK-I = 32x, MK-II = 64x, MK-III = 128x")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addInfo("The structure is too complex!")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .addController("Front bottom")
                .addInputHatch("Any Casing")
                .addInputBus("Any Casing")
                .addOutputHatch("Any Casing")
                .addOutputBus("Any Casing")
                .addEnergyHatch("Any Casing")
                .addMufflerHatch("Any Casing")
                .addMaintenanceHatch("Any Casing")
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 780;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("PreciseAssembler.hint", 6);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new PreciseAssembler(this.mName);
    }

    private int checkEnergyHatchTier() {
        int tier = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                tier = Math.max(tHatch.mTier, tier);
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                tier = Math.max(tHatch.mTier, tier);
            }
        }
        return tier;
    }

    public int getCasingTier() {
        return casingTier;
    }

    public void setCasingTier(int i) {
        casingTier = i;
    }

    public int getMachineTier() {
        return machineTier;
    }

    public void setMachineTier(int i) {
        machineTier = i;
    }

    public void reUpdate(int texture) {
        for (GT_MetaTileEntity_Hatch hatch : mInputHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mInputBusses) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mOutputHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mOutputBusses) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mEnergyHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mMaintenanceHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : mMufflerHatches) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : eEnergyMulti) {
            hatch.updateTexture(texture);
        }
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        int t = 1;
        if (getCasingTier() != 0) t = getCasingTier();
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1538 + t),
                    TextureFactory.of(textureFontOn),
                    TextureFactory.builder().addIcon(textureFontOn_Glow).glow().build()
                };
            else
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(1538 + t),
                    TextureFactory.of(textureFontOff),
                    TextureFactory.builder().addIcon(textureFontOff_Glow).glow().build()
                };
        } else return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(1538 + t)};
    }
}
