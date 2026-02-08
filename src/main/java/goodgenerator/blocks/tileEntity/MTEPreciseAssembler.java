package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
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
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.client.GUI.GGUITextures;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
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
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputHatch;

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
    private static final int MACHINEMODE_PRECISE = 0;
    private static final int MACHINEMODE_ASSEMBLER = 1;
    protected int energyHatchTier;
    private static final int CASING_INDEX = 1541;
    private int glassTier = -1;

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
                    GTStructureChannels.PRASS_UNIT_CASING.use(
                        buildHatchAdder(MTEPreciseAssembler.class)
                            .atLeast(
                                InputBus,
                                InputHatch,
                                OutputHatch,
                                OutputBus,
                                Maintenance,
                                Muffler,
                                ExoticEnergy.or(Energy))
                            .casingIndex(CASING_INDEX)
                            .hint(1)
                            .buildAndChain(
                                onElementPass(
                                    x -> x.casingAmount++,
                                    StructureUtility.ofBlocksTiered(
                                        MTEPreciseAssembler::getCasingBlockTier,
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
                .addElement('G', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement(
                    'M',
                    GTStructureChannels.TIER_MACHINE_CASING.use(
                        StructureUtility.ofBlocksTiered(
                            (block, meta) -> (block == GregTechAPI.sBlockCasings1 && meta >= 0 && meta <= 9) ? meta
                                : null,
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

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        casingTier = aNBT.getInteger("casingTier");
        machineTier = aNBT.getInteger("machineTier");
        if (aNBT.hasKey("RunningMode")) {
            machineMode = aNBT.getInteger("RunningMode");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("casingTier", casingTier);
        aNBT.setInteger("machineTier", machineTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.machineMode = (this.machineMode + 1) % 2;
            GTUtility.sendChatTrans(aPlayer, "preciseassembler.chat." + this.machineMode);
        }
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (machineMode == 0) {
                    if (recipe.mSpecialValue > (casingTier + 1)) {
                        return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                    }
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setDurationModifier(machineMode == 0 ? 1 : 0.5);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return machineMode == 0 ? 1 : (int) GTUtility.powInt(2, 4 + (casingTier + 1));
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        boolean useSingleAmp = !debugEnergyPresent && mEnergyHatches.size() == 1 && mExoticEnergyHatches.isEmpty();
        logic.setAvailableVoltage(getMachineVoltageLimit());
        logic.setAvailableAmperage(useSingleAmp ? 1 : getMaxInputAmps());
        logic.setAmperageOC(true);
        logic.setMaxTierSkips(0);
    }

    public long getMachineVoltageLimit() {
        if (machineTier < 0) return 0;
        if (machineTier >= 9) return GTValues.V[energyHatchTier];
        else return GTValues.V[Math.min(machineTier, energyHatchTier)];
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if (this.machineMode == 0) return GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
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
        return survivalBuildPiece(mName, stackSize, 4, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.machineTier = -1;
        this.casingAmount = 0;
        this.casingTier = -3;
        this.glassTier = -1;
        this.energyHatchTier = 0;
        if (checkPiece(mName, 4, 4, 0)) {
            energyHatchTier = checkEnergyHatchTier();
            if (casingTier >= -1) {
                reUpdate(CASING_INDEX + casingTier);
            }
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
            return casingAmount >= 42 && mMaintenanceHatches.size() == 1
                && glassTier >= VoltageIndex.EV
                && !mMufflerHatches.isEmpty();
        }
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Precise Assembler, Assembler, PrAss")
            .addInfo("No more than 7nm of error")
            .addInfo("Has Two Modes: Precise and Normal")
            .addInfo("Use a Screwdriver to change modes")
            .addSeparator()
            .addInfo("Precise Mode unlocks the ability to assemble precise components")
            .addInfo("Casing Tier determines Maximum Recipe Tier")
            .addSeparator()
            .addInfo("Normal Mode allows standard assembler recipes")
            .addInfo(
                EnumChatFormatting.WHITE + "Precise Casing"
                    + EnumChatFormatting.GRAY
                    + " Tier determines "
                    + TooltipHelper.parallelText("Parallels"))
            .addInfo(
                tieredTextLine("Imprecise", "Mk-I", "MK-II", "MK-III", "MK-IV") + "->"
                    + tieredTextLine("16", "32", "64", "128", "256")
                    + " Parallels")
            .addStaticSpeedInfo(2f)
            .addSeparator()
            .addInfo(
                "Machine Casing limits the voltage tier the machine can work on, "
                    + GTValues.TIER_COLORS[VoltageIndex.UHV]
                    + "UHV"
                    + EnumChatFormatting.GRAY
                    + "-tier Machine Casing unlocks all.")
            .addTecTechHatchInfo()
            .addNoTierSkips()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 5, 5, true)
            .addController("Front bottom")
            .addCasingInfoExactly("Machine Casing", 21, true)
            .addCasingInfoExactly("Any Tiered Glass (EV+)", 42, false)
            .addCasingInfoRange("Precise Electronic Unit Casing", 42, 86, true)
            .addInputHatch("Any Casing")
            .addInputBus("Any Casing")
            .addOutputHatch("Any Casing")
            .addOutputBus("Any Casing")
            .addEnergyHatch("Any Casing")
            .addMufflerHatch("Any Casing")
            .addMaintenanceHatch("Any Casing")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.PRASS_UNIT_CASING)
            .addSubChannelUsage(GTStructureChannels.TIER_MACHINE_CASING)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 780;
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
        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
            tier = Math.max(tHatch.mTier, tier);
        }
        for (MTEHatch tHatch : validMTEList(mExoticEnergyHatches)) {
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

    @Nullable
    public static Integer getCasingBlockTier(Block block, int meta) {
        if (block == Loaders.impreciseUnitCasing) return -1;
        else if (block == Loaders.preciseUnitCasing) return meta;
        else return null;
    }

    public void reUpdate(int texture) {
        for (IDualInputHatch hatch : mDualInputHatches) {
            if (((MetaTileEntity) hatch).isValid()) {
                hatch.updateTexture(texture);
            }
        }
        for (MTEHatch hatch : validMTEList(mInputHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : validMTEList(mInputBusses)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : validMTEList(mOutputHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : validMTEList(mOutputBusses)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : validMTEList(mEnergyHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : validMTEList(mMaintenanceHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : validMTEList(mMufflerHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : validMTEList(mExoticEnergyHatches)) {
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
                TextureFactory.builder()
                    .addIcon(textureFontOn)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(textureFontOn_Glow)
                    .extFacing()
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX + t),
                TextureFactory.builder()
                    .addIcon(textureFontOff)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(textureFontOff_Glow)
                    .extFacing()
                    .glow()
                    .build() };
        } else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX + t) };
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_PRECISE_MODE,
            GTGuiTextures.OVERLAY_BUTTON_ASSEMBLER_MODE);
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_ASSEMBLER) return MACHINEMODE_PRECISE;
        return MACHINEMODE_ASSEMBLER;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new CycleButtonWidget().setToggle(() -> machineMode == 1, val -> machineMode = val ? 1 : 0)
                .setTextureGetter(
                    state -> state == 1 ? GGUITextures.OVERLAY_BUTTON_ASSEMBLER_MODE
                        : GGUITextures.OVERLAY_BUTTON_PRECISE_MODE)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(80, 91)
                .setSize(16, 16)
                .dynamicTooltip(
                    () -> Collections
                        .singletonList(StatCollector.translateToLocal("preciseassembler.chat." + machineMode)))
                .setUpdateTooltipEveryTick(true)
                .setTooltipShowUpDelay(TOOLTIP_DELAY));
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    public boolean supportsMachineModeSwitch() {
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
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("GT5U.GTPP_MULTI_PRECISE_ASSEMBLER.mode." + machineMode);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_PRECISE_LOOP;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> (machineTier > 0 && machineTier < 9) ? StatCollector.translateToLocalFormatted(
                            "GT5U.multiblock.preciseassemblercasing",
                            GTUtility.getColoredTierNameFromVoltage(GTValues.V[machineTier])) : "")
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(ignored -> machineTier > 0 && machineTier < 9))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> machineTier, tier -> machineTier = tier));
        super.drawTexts(screenElements, inventorySlot);
    }

    private String tieredTextLine(String mk0, String mk1, String mk2, String mk3, String mk4) {
        return EnumChatFormatting.GREEN + mk0
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.BLUE
            + mk1
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.LIGHT_PURPLE
            + mk2
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.GOLD
            + mk3
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.RED
            + mk4
            + EnumChatFormatting.GRAY;
    }
}
