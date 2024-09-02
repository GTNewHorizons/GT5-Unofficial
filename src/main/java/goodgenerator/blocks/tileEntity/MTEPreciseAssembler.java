package goodgenerator.blocks.tileEntity;

import static bartworks.util.BWUtil.ofGlassTieredMixed;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.client.GUI.GGUITextures;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.tileentities.machines.IDualInputHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class MTEPreciseAssembler extends MTEExtendedPowerMultiBlockBase<MTEPreciseAssembler>
    implements IConstructable, ISurvivalConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_QCHEST_GLOW");

    protected IStructureDefinition<MTEPreciseAssembler> multiDefinition = null;
    protected int casingAmount;
    protected int casingTier;
    protected int machineTier;
    protected int mode;
    protected int energyHatchTier;
    private static final int CASING_INDEX = 1541;

    public MTEPreciseAssembler(String name) {
        super(name);
    }

    public MTEPreciseAssembler(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IStructureDefinition<MTEPreciseAssembler> getStructureDefinition() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTEPreciseAssembler>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC" },
                            { "F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F" },
                            { "F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F" },
                            { "F       F", "CGGGGGGGC", "C-------C", "CGGGGGGGC", "F       F" },
                            { "CCCC~CCCC", "CMMMMMMMC", "CMMMMMMMC", "CMMMMMMMC", "CCCCCCCCC" } }))
                .addElement(
                    'C',
                    withChannel(
                        "unit casing",
                        HatchElementBuilder.<MTEPreciseAssembler>builder()
                            .atLeast(
                                InputBus,
                                InputHatch,
                                OutputHatch,
                                OutputBus,
                                Maintenance,
                                Muffler,
                                ExoticEnergy.or(Energy))
                            .adder(MTEPreciseAssembler::addToPAssList)
                            .casingIndex(CASING_INDEX)
                            .dot(1)
                            .buildAndChain(
                                onElementPass(
                                    x -> x.casingAmount++,
                                    StructureUtility.ofBlocksTiered(
                                        (block, meta) -> block == Loaders.impreciseUnitCasing ? -1
                                            : block == Loaders.preciseUnitCasing ? meta : -2,
                                        ImmutableList.of(
                                            Pair.of(Loaders.impreciseUnitCasing, 0),
                                            Pair.of(Loaders.preciseUnitCasing, 0),
                                            Pair.of(Loaders.preciseUnitCasing, 1),
                                            Pair.of(Loaders.preciseUnitCasing, 2),
                                            Pair.of(Loaders.preciseUnitCasing, 3)),
                                        -3,
                                        MTEPreciseAssembler::setCasingTier,
                                        MTEPreciseAssembler::getCasingTier)))))
                .addElement('F', ofFrame(Materials.TungstenSteel))
                .addElement('G', withChannel("glass", ofGlassTieredMixed((byte) 4, (byte) 127, 2)))
                .addElement(
                    'M',
                    withChannel(
                        "machine casing",
                        StructureUtility.ofBlocksTiered(
                            (block, meta) -> block == GregTechAPI.sBlockCasings1 ? meta : -2,
                            IntStream.range(0, 10)
                                .mapToObj(
                                    meta -> org.apache.commons.lang3.tuple.Pair.of(GregTechAPI.sBlockCasings1, meta))
                                .collect(Collectors.toList()),
                            -1,
                            MTEPreciseAssembler::setMachineTier,
                            MTEPreciseAssembler::getMachineTier)))
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
        if (aMetaTileEntity instanceof MTEHatchInput) {
            return mInputHatches.add((MTEHatchInput) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof IDualInputHatch) {
            return mDualInputHatches.add((IDualInputHatch) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchInputBus) {
            return mInputBusses.add((MTEHatchInputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput) {
            return mOutputHatches.add((MTEHatchOutput) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchOutputBus) {
            return mOutputBusses.add((MTEHatchOutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchEnergy) {
            return mEnergyHatches.add((MTEHatchEnergy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchMaintenance) {
            return mMaintenanceHatches.add((MTEHatchMaintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchMuffler) {
            return mMufflerHatches.add((MTEHatchMuffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchEnergyMulti) {
            return mExoticEnergyHatches.add((MTEHatchEnergyMulti) aMetaTileEntity);
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
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("preciseassembler.chat." + this.mode));
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (mode == 0) {
                    if (recipe.mSpecialValue > (casingTier + 1)) {
                        return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                    }
                }
                if (availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setSpeedBoost(mode == 0 ? 1 : 0.5F);
            }
        }.setMaxParallelSupplier(() -> mode == 0 ? 1 : (int) Math.pow(2, 4 + (casingTier + 1)));
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        boolean useSingleAmp = mEnergyHatches.size() == 1 && mExoticEnergyHatches.isEmpty();
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
        if (machineTier >= 9) return GTValues.V[energyHatchTier];
        else return GTValues.V[Math.min(machineTier, energyHatchTier)];
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (this.mode == 0) return GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
        else return RecipeMaps.assemblerRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GoodGeneratorRecipeMaps.preciseAssemblerRecipes, RecipeMaps.assemblerRecipes);
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
        this.casingTier = -3;
        this.energyHatchTier = 0;
        if (checkPiece(mName, 4, 4, 0)) {
            energyHatchTier = checkEnergyHatchTier();
            if (casingTier >= -1) {
                reUpdate(CASING_INDEX + casingTier);
            }
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
            return casingAmount >= 42 && machineTier >= 0
                && casingTier >= -1
                && mMaintenanceHatches.size() == 1
                && !mMufflerHatches.isEmpty();
        }
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Precise Assembler/Assembler")
            .addInfo("Controller block for the Precise Assembler")
            .addInfo("The error is no more than 7nm.")
            .addInfo("Can assemble precise component in Precise Mode.")
            .addInfo("Can work like a normal assembler in Normal Mode.")
            .addInfo("Use screwdriver to change mode.")
            .addInfo("Machine Casing and Energy Hatch limits the voltage tier the machine can work on.")
            .addInfo("UHV Machine Casing will unlock all voltage, but you still need good Energy Hatch.")
            .addInfo("Precise Electronic Unit Casing won't limit recipe in Normal Mode.")
            .addInfo("But gives more parallel with more advanced one.")
            .addInfo("It is 100% faster in Normal Mode.")
            .addInfo("Imprecise (MK-0) = 16x, MK-I = 32x, MK-II = 64x, MK-III = 128x, MK-IV = 256x")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(9, 5, 5, true)
            .addController("Front bottom")
            .addCasingInfoExactly("Machine Casing", 21, true)
            .addCasingInfoExactly("Glass (EV+)", 42, false)
            .addCasingInfoRange("Precise Electronic Unit Casing", 42, 86, true)
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
        return new MTEPreciseAssembler(this.mName);
    }

    private int checkEnergyHatchTier() {
        int tier = 0;
        for (MTEHatchEnergy tHatch : filterValidMTEs(mEnergyHatches)) {
            tier = Math.max(tHatch.mTier, tier);
        }
        for (MTEHatch tHatch : filterValidMTEs(mExoticEnergyHatches)) {
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
            if (((MetaTileEntity) hatch).isValid()) {
                hatch.updateTexture(texture);
            }
        }
        for (MTEHatch hatch : filterValidMTEs(mInputHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mInputBusses)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mOutputHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mOutputBusses)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mEnergyHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mMaintenanceHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mMufflerHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mExoticEnergyHatches)) {
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
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA && ((aValue & 0x80) == 0 || aValue == -1)) {
            casingTier = aValue;
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        int t = Math.max(getCasingTier(), -1);
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX + t),
                TextureFactory.of(textureFontOn), TextureFactory.builder()
                    .addIcon(textureFontOn_Glow)
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX + t),
                TextureFactory.of(textureFontOff), TextureFactory.builder()
                    .addIcon(textureFontOff_Glow)
                    .glow()
                    .build() };
        } else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX + t) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new CycleButtonWidget().setToggle(() -> mode == 1, val -> mode = val ? 1 : 0)
                .setTextureGetter(
                    state -> state == 1 ? GGUITextures.OVERLAY_BUTTON_ASSEMBLER_MODE
                        : GGUITextures.OVERLAY_BUTTON_PRECISE_MODE)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(80, 91)
                .setSize(16, 16)
                .dynamicTooltip(
                    () -> Collections.singletonList(StatCollector.translateToLocal("preciseassembler.chat." + mode)))
                .setUpdateTooltipEveryTick(true)
                .setTooltipShowUpDelay(TOOLTIP_DELAY));
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

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("mode", mode);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal("GT5U.GTPP_MULTI_PRECISE_ASSEMBLER.mode." + tag.getInteger("mode"))
                + EnumChatFormatting.RESET);
    }

}
