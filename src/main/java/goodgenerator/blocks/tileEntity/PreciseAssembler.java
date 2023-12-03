package goodgenerator.blocks.tileEntity;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.ofGlassTieredMixed;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;

import goodgenerator.client.GUI.GG_UITextures;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class PreciseAssembler extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<PreciseAssembler>
        implements IConstructable, ISurvivalConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon(
            "iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon(
            "iconsets/OVERLAY_QCHEST_GLOW");

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
    public IStructureDefinition<PreciseAssembler> getStructureDefinition() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<PreciseAssembler>builder().addShape(
                    mName,
                    transpose(
                            new String[][] { { "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC" },
                                    { "F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F" },
                                    { "F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F" },
                                    { "F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F" },
                                    { "CCCC~CCCC", "CMMMMMMMC", "CMMMMMMMC", "CMMMMMMMC", "CCCCCCCCC" } }))
                    .addElement(
                            'C',
                            GT_HatchElementBuilder.<PreciseAssembler>builder()
                                    .atLeast(
                                            InputBus,
                                            InputHatch,
                                            OutputHatch,
                                            OutputBus,
                                            Maintenance,
                                            Muffler,
                                            ExoticEnergy.or(Energy))
                                    .adder(PreciseAssembler::addToPAssList).casingIndex(1539).dot(1).buildAndChain(
                                            onElementPass(
                                                    x -> x.casingAmount++,
                                                    StructureUtility.ofBlocksTiered(
                                                            (block, meta) -> block == Loaders.preciseUnitCasing ? meta
                                                                    : -2,
                                                            IntStream.range(0, 3).mapToObj(
                                                                    meta -> org.apache.commons.lang3.tuple.Pair
                                                                            .of(Loaders.preciseUnitCasing, meta))
                                                                    .collect(Collectors.toList()),
                                                            -1,
                                                            PreciseAssembler::setCasingTier,
                                                            PreciseAssembler::getCasingTier))))
                    .addElement('F', ofFrame(Materials.TungstenSteel))
                    .addElement('G', ofGlassTieredMixed((byte) 4, (byte) 127, 2))
                    .addElement(
                            'M',
                            StructureUtility.ofBlocksTiered(
                                    (block, meta) -> block == GregTech_API.sBlockCasings1 ? meta : -2,
                                    IntStream.range(0, 10)
                                            .mapToObj(
                                                    meta -> org.apache.commons.lang3.tuple.Pair
                                                            .of(GregTech_API.sBlockCasings1, meta))
                                            .collect(Collectors.toList()),
                                    -1,
                                    PreciseAssembler::setMachineTier,
                                    PreciseAssembler::getMachineTier))
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
        if (aMetaTileEntity instanceof IDualInputHatch) {
            return mDualInputHatches.add((IDualInputHatch) aMetaTileEntity);
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
            return mExoticEnergyHatches.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("casingTier", casingTier);
        aNBT.setInteger("machineTier", machineTier);
        aNBT.setInteger("RunningMode", mode);
        super.saveNBTData(aNBT);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("preciseassembler.chat." + this.mode));
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (mode == 0) {
                    if (recipe.mSpecialValue > (casingTier + 1)) {
                        return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                    }
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected FindRecipeResult findRecipe(@Nullable GT_Recipe_Map map) {
                if (map == null) return FindRecipeResult.NOT_FOUND;
                return map.findRecipeWithResult(
                        lastRecipe,
                        false,
                        false,
                        availableVoltage,
                        inputFluids,
                        specialSlotItem,
                        inputItems);
            }

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setSpeedBoost(mode == 0 ? 1 : 0.5F);
            }
        }.setMaxParallelSupplier(() -> mode == 0 ? 1 : (int) Math.pow(2, 4 + (casingTier + 1)));
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        boolean useSingleAmp = mEnergyHatches.size() == 1 && mExoticEnergyHatches.size() == 0;
        logic.setAvailableVoltage(getMachineVoltageLimit());
        logic.setAvailableAmperage(useSingleAmp ? 1 : getMaxInputAmps());
        logic.setAmperageOC(true);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public long getMachineVoltageLimit() {
        if (machineTier < 0) return 0;
        if (machineTier >= 9) return GT_Values.V[energyHatchTier];
        else return GT_Values.V[Math.min(machineTier, energyHatchTier)];
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        if (this.mode == 0) return MyRecipeAdder.instance.PA;
        else return GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 4, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 4, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.machineTier = -1;
        this.casingAmount = 0;
        this.casingTier = -1;
        this.energyHatchTier = 0;
        if (checkPiece(mName, 4, 4, 0)) {
            energyHatchTier = checkEnergyHatchTier();
            if (casingTier >= 0) {
                reUpdate(1539 + casingTier);
            }
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
            return casingAmount >= 42 && machineTier >= 0
                    && casingTier >= 0
                    && mMaintenanceHatches.size() == 1
                    && !mMufflerHatches.isEmpty();
        }
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Precise Assembler/Assembler").addInfo("Controller block for the Precise Assembler")
                .addInfo("The error is no more than 7nm.").addInfo("Can assemble precise component in Precise Mode.")
                .addInfo("Can work like a normal assembler in Normal Mode.").addInfo("Use screwdriver to change mode.")
                .addInfo("Machine Casing and Energy Hatch limits the voltage tier the machine can work on.")
                .addInfo("UHV Machine Casing will unlock all voltage, but you still need good Energy Hatch.")
                .addInfo("Precise Electronic Unit Casing won't limit recipe in Normal Mode.")
                .addInfo("But gives more parallel with more advanced one.").addInfo("It is 100% faster in Normal Mode.")
                .addInfo("MK-I = 32x, MK-II = 64x, MK-III = 128x").addPollutionAmount(getPollutionPerSecond(null))
                .addInfo("The structure is too complex!").addInfo(BLUE_PRINT_INFO).addSeparator()
                .beginStructureBlock(9, 5, 5, true).addController("Front bottom")
                .addCasingInfoExactly("Machine Casing", 21, true).addCasingInfoExactly("Glass (EV+)", 42, false)
                .addCasingInfoRange("Precise Electronic Unit Casing", 42, 86, true).addInputHatch("Any Casing")
                .addInputBus("Any Casing").addOutputHatch("Any Casing").addOutputBus("Any Casing")
                .addEnergyHatch("Any Casing").addMufflerHatch("Any Casing").addMaintenanceHatch("Any Casing")
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 780;
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
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("PreciseAssembler.hint", 7);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new PreciseAssembler(this.mName);
    }

    private int checkEnergyHatchTier() {
        int tier = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            tier = Math.max(tHatch.mTier, tier);
        }
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mExoticEnergyHatches)) {
            tier = Math.max(tHatch.mTier, tier);
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
        for (IDualInputHatch hatch : mDualInputHatches) {
            if (isValidMetaTileEntity((MetaTileEntity) hatch)) {
                hatch.updateTexture(texture);
            }
        }
        for (GT_MetaTileEntity_Hatch hatch : mInputHatches) {
            if (isValidMetaTileEntity(hatch)) {
                hatch.updateTexture(texture);
            }
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
        for (GT_MetaTileEntity_Hatch hatch : mExoticEnergyHatches) {
            hatch.updateTexture(texture);
        }
    }

    @Override
    public byte getUpdateData() {
        return (byte) casingTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        super.receiveClientEvent(aEventID, aValue);
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA && (aValue & 0x80) == 0) {
            casingTier = aValue;
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        int t = Math.max(getCasingTier(), 0);
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1539 + t),
                    TextureFactory.of(textureFontOn),
                    TextureFactory.builder().addIcon(textureFontOn_Glow).glow().build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1539 + t),
                    TextureFactory.of(textureFontOff),
                    TextureFactory.builder().addIcon(textureFontOff_Glow).glow().build() };
        } else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1539 + t) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
                new CycleButtonWidget().setToggle(() -> mode == 1, val -> mode = val ? 1 : 0)
                        .setTextureGetter(
                                state -> state == 1 ? GG_UITextures.OVERLAY_BUTTON_ASSEMBLER_MODE
                                        : GG_UITextures.OVERLAY_BUTTON_PRECISE_MODE)
                        .setBackground(GT_UITextures.BUTTON_STANDARD).setPos(80, 91).setSize(16, 16)
                        .dynamicTooltip(
                                () -> Collections
                                        .singletonList(StatCollector.translateToLocal("preciseassembler.chat." + mode)))
                        .setUpdateTooltipEveryTick(true).setTooltipShowUpDelay(TOOLTIP_DELAY));
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
