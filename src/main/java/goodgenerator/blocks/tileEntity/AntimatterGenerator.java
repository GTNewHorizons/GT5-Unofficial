package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.structures.AntimatterStructures;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import kekztech.client.gui.KTUITextures;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class AntimatterGenerator extends MTEExtendedPowerMultiBlockBase
    implements IConstructable, ISurvivalConstructable {

    public static final String MAIN_NAME = "antimatterGenerator";
    protected IStructureDefinition<AntimatterGenerator> multiDefinition = null;
    protected int times = 1;
    private UUID owner_uuid;
    private boolean wirelessEnabled = false;
    private boolean canUseWireless = true;
    private long euLastCycle = 0;
    private float annihilationEfficiency = 0f;
    public static final long ANTIMATTER_FUEL_VALUE = 1_000_000_000_000L;
    private final List<Float> avgEff = new ArrayList<>(10);

    private static final ClassValue<IStructureDefinition<AntimatterGenerator>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<AntimatterGenerator> computeValue(Class<?> type) {
            return StructureDefinition.<AntimatterGenerator>builder()
                .addShape(MAIN_NAME, AntimatterStructures.ANTIMATTER_GENERATOR)
                .addElement('F', lazy(x -> ofFrame(Materials.Naquadria))) // Naquadria Frame Box
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCasingMeta(1)))) // Black Casing
                .addElement('G', lazy(x -> ofBlock(x.getCoilBlock(1), x.getCoilMeta(1)))) // Annihilation Coil
                .addElement('B', lazy(x -> ofBlock(x.getCoilBlock(2), x.getCoilMeta(2)))) // Containment Coil
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta(2)))) // White Casing
                .addElement('A', lazy(x -> ofBlock(x.getGlassBlock(), x.getGlassMeta()))) // Glass
                .addElement('E', lazy(x -> ofBlock(GregTechAPI.sBlockCasings9, 1))) // Filter Casing
                .addElement(
                    'H',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterGenerator>builder()
                            .anyOf(HatchElement.ExoticEnergy)
                            .adder(AntimatterGenerator::addLaserSource)
                            .casingIndex(x.textureIndex(2))
                            .dot(2)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
                    'I',
                    lazy(
                        x -> buildHatchAdder(AntimatterGenerator.class).atLeast(HatchElement.InputHatch)
                            .casingIndex(x.textureIndex(1))
                            .dot(1)
                            .buildAndChain(x.getCasingBlock(1), x.getCasingMeta(1))))
                .build();
        }
    };

    private boolean addLaserSource(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchDynamoTunnel tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            return mExoticEnergyHatches.add(tHatch);
        }
        return false;
    }

    public AntimatterGenerator(String name) {
        super(name);
    }

    public AntimatterGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public CheckRecipeResult checkProcessing() {
        startRecipeProcessing();
        List<FluidStack> inputFluids = getStoredFluids();
        long containedAntimatter = 0;
        FluidStack catalystFluid = null;
        int i = 0;

        while (i < inputFluids.size()) {
            FluidStack inputFluid = inputFluids.get(i);
            if (inputFluid.isFluidEqual(MaterialsUEVplus.Antimatter.getFluid(1))) {
                containedAntimatter += inputFluid.amount;
            } else {
                catalystFluid = inputFluid.copy();
            }
            // We annihilate everything, even if it was the wrong fluid
            inputFluid.amount = 0;
            i++;
        }
        // If i != 2, we iterated more than 2 times and have too many fluids.
        if (i == 2 && containedAntimatter > 0 && catalystFluid != null) {
            createEU(containedAntimatter, catalystFluid);
        }
        // Set stats if one fluid supplied.
        if ((containedAntimatter == 0 && catalystFluid != null) || (containedAntimatter > 0 && catalystFluid == null)) {
            this.annihilationEfficiency = 0;
            this.euLastCycle = 0;
            setAvgEff(0f);
        }

        endRecipeProcessing();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    // (Antimatter^(EXP) * 1e12 )/(Math.min((Antimatter/Matter),(Matter/Antimatter)))
    public void createEU(long antimatter, FluidStack catalyst) {
        Float modifier = null;

        if (catalyst.isFluidEqual(Materials.Copper.getMolten(1L))) {
            modifier = 1.0F;
        } else if (catalyst.isFluidEqual(Materials.SuperconductorUIVBase.getMolten(1L))) {
            modifier = 1.02F;
        } else if (catalyst.isFluidEqual(Materials.SuperconductorUMVBase.getMolten(1L))) {
            modifier = 1.03F;
        } else if (catalyst.isFluidEqual(MaterialsUEVplus.BlackDwarfMatter.getMolten(1L))) {
            modifier = 1.04F;
        }
        long catalystCount = catalyst.amount;
        long generatedEU = 0;

        if (modifier != null) {
            float efficiency = Math
                .min(((float) antimatter / (float) catalystCount), ((float) catalystCount / (float) antimatter));
            this.annihilationEfficiency = efficiency;
            setAvgEff(efficiency);
            generatedEU = (long) ((Math.pow(antimatter, modifier) * ANTIMATTER_FUEL_VALUE) * efficiency);
        } else { // Set stats and return if supplied antimatter with incorrect fluid.
            this.annihilationEfficiency = 0;
            this.euLastCycle = 0;
            setAvgEff(0f);
            return;
        }

        if (wirelessEnabled && modifier >= 1.03F) {
            // Clamp the EU to the maximum of the hatches so wireless cannot bypass the limitations
            long euCapacity = 0;
            for (MTEHatch tHatch : getExoticEnergyHatches()) {
                if (tHatch instanceof MTEHatchDynamoTunnel tLaserSource) {
                    euCapacity += tLaserSource.maxEUStore();
                }
            }

            // Prevent -Generation when long overflow
            if (generatedEU < 0) generatedEU = Long.MAX_VALUE;
            if (euCapacity < 0) euCapacity = Long.MAX_VALUE;

            generatedEU = Math.min(generatedEU, euCapacity);
            this.euLastCycle = generatedEU;
            addEUToGlobalEnergyMap(owner_uuid, generatedEU);
        } else {
            this.euLastCycle = generatedEU;
            float invHatchCount = 1.0F / (float) mExoticEnergyHatches.size();
            for (MTEHatch tHatch : getExoticEnergyHatches()) {
                if (tHatch instanceof MTEHatchDynamoTunnel tLaserSource) {
                    tLaserSource.setEUVar(tLaserSource.getEUVar() + (long) (generatedEU * invHatchCount));
                }
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(MAIN_NAME, 17, 41, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(MAIN_NAME, stackSize, 17, 41, 0, realBudget, env, false, true);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece(MAIN_NAME, itemStack, hintsOnly, 17, 41, 0);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        nbt.setBoolean("wirelessEnabled", wirelessEnabled);
        super.saveNBTData(nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        wirelessEnabled = nbt.getBoolean("wirelessEnabled");
        super.loadNBTData(nbt);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
        return new AntimatterGenerator(MAIN_NAME);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        wirelessEnabled = !wirelessEnabled;
        GTUtility.sendChatToPlayer(aPlayer, "Wireless network mode " + (wirelessEnabled ? "enabled." : "disabled."));
        if (wirelessEnabled) {
            GTUtility.sendChatToPlayer(aPlayer, "Wireless only works with UMV Superconductor Base or better.");
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // On first tick find the player name and attempt to add them to the map.
            if (aTick == 1) {

                // UUID and username of the owner.
                owner_uuid = aBaseMetaTileEntity.getOwnerUuid();

                strongCheckOrAddUser(owner_uuid);
            }
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Antimatter Generator")
            .addInfo("Annihilating antimatter like it's 2205!")
            .addSeparator()
            .addInfo("Generates energy by reacting Semi-Stable Antimatter with matter")
            .addInfo("Annihilation uses an equal amount of antimatter and matter")
            .addInfo(
                "Consumes " + EnumChatFormatting.GOLD
                    + "all inputs"
                    + EnumChatFormatting.GRAY
                    + " every processing cycle")
            .addInfo(
                "Imbalance between antimatter and matter " + EnumChatFormatting.RED
                    + "will waste energy!"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "Any EU that does not fit in laser hatches will be " + EnumChatFormatting.RED
                    + "voided"
                    + EnumChatFormatting.GRAY)
            .addSeparator()
            .addInfo("Antimatter base energy value: " + GTUtility.formatNumbers(ANTIMATTER_FUEL_VALUE) + " EU/L")
            .addInfo("Cannot produce more than 9.2e18 EU per cycle")
            .addInfo("Energy production is exponentially increased depending on the matter used:")
            .addInfo("Molten Copper: 1.00")
            .addInfo("Molten SC UIV Base: 1.02")
            .addInfo("Molten SC UMV Base: 1.03")
            .addInfo("Molten Black Dwarf Matter: 1.04")
            .addSeparator()
            .addInfo("Enable wireless EU mode with screwdriver")
            .addInfo("Wireless mode requires SC UMV Base or better")
            .addInfo("Wireless mode uses hatch capacity limit")
            .beginStructureBlock(35, 43, 35, false)
            .addCasingInfoMin("Transcendentally Reinforced Borosilicate Glass", 1008, false)
            .addCasingInfoMin("Magnetic Flux Casing", 4122, false)
            .addCasingInfoMin("Gravity Stabilization Casing", 2418, false)
            .addCasingInfoMin("Protomatter Activation Coil", 32, false)
            .addCasingInfoMin("Antimatter Annihilation Matrix", 600, false)
            .addCasingInfoMin("Naquadria Frame Box", 293, false)
            .addCasingInfoMin("Advanced Filter Casing", 209, false)
            .addInputHatch("2, Hint block with dot 1", 1)
            .addOtherStructurePart(
                StatCollector.translateToLocal("gg.structure.tooltip.laser_source_hatch"),
                "1-64, Hint Block with dot 2",
                2)
            .toolTipFinisher();
        return tt;
    }

    protected boolean canUseWireless() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                canUseWireless = canUseWireless();
            }
            if (canUseWireless) {
                wirelessEnabled = !wirelessEnabled;
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                if (canUseWireless) {
                    if (wirelessEnabled) {
                        ret.add(KTUITextures.OVERLAY_BUTTON_WIRELESS_ON);
                    } else {
                        ret.add(KTUITextures.OVERLAY_BUTTON_WIRELESS_OFF);
                    }
                } else {
                    ret.add(KTUITextures.OVERLAY_BUTTON_WIRELESS_OFF_DISABLED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(80, 91)
            .setSize(16, 16)
            .addTooltip(StatCollector.translateToLocal("gui.kekztech_lapotronicenergyunit.wireless"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> wirelessEnabled, val -> wirelessEnabled = val))
            .widget(new FakeSyncWidget.BooleanSyncer(this::canUseWireless, val -> canUseWireless = val));
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;

        for (MTEHatch tHatch : mExoticEnergyHatches) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        // Prevent -Value when long overflow
        if (storedEnergy < 0) storedEnergy = Long.MAX_VALUE;
        if (maxEnergy < 0) maxEnergy = Long.MAX_VALUE;

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("gg.info.antimatter_forge")
                + " "
                + EnumChatFormatting.GRAY,
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mProgresstime)
                + EnumChatFormatting.RESET
                + "t / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMaxProgresstime)
                + EnumChatFormatting.RESET
                + "t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("gui.AntimatterGenerator.0") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(this.euLastCycle)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": "
                + EnumChatFormatting.AQUA
                + GTUtility.formatNumbers(Math.ceil(this.annihilationEfficiency * 100))
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": ⟨ "
                + EnumChatFormatting.AQUA
                + GTUtility.formatNumbers(Math.ceil(this.avgEffCache * 100))
                + EnumChatFormatting.RESET
                + " % ⟩₁₀" };
    }

    private long getEnergyProduced() {
        return this.euLastCycle;
    }

    private float getEfficiency() {
        return this.annihilationEfficiency;
    }

    private int n = 0;

    private void setAvgEff(float a) {
        if (n == 10) n = 0;
        if (this.avgEff.size() < 10) {
            this.avgEff.add(a);
        } else {
            this.avgEff.set(n, a);
            n++;
        }

        float b = 0;
        for (float c : this.avgEff) {
            b += c;
        }
        this.avgEffCache = b == 0 ? 0 : b / this.avgEff.size();
    }

    private float getAvgEfficiency() {
        return this.avgEffCache;
    }

    protected long energyProducedCache;
    protected float efficiencyCache;
    protected float avgEffCache;
    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    protected static DecimalFormat standardFormat;

    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        standardFormat = new DecimalFormat("0.00E0", dfs);
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.AntimatterGenerator.0") + ": "
                            + EnumChatFormatting.BLUE
                            + standardFormat.format(energyProducedCache)
                            + EnumChatFormatting.WHITE
                            + " EU")
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get()))
            .widget(new FakeSyncWidget.LongSyncer(this::getEnergyProduced, val -> energyProducedCache = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": "
                            + EnumChatFormatting.RED
                            + numberFormat.format(Math.ceil(efficiencyCache * 100))
                            + EnumChatFormatting.WHITE
                            + " %")
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get()))
            .widget(new FakeSyncWidget.FloatSyncer(this::getEfficiency, val -> efficiencyCache = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": ⟨ "
                            + EnumChatFormatting.RED
                            + numberFormat.format(Math.ceil(avgEffCache * 100))
                            + EnumChatFormatting.WHITE
                            + " % ⟩₁₀")
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get()))
            .widget(new FakeSyncWidget.FloatSyncer(this::getAvgEfficiency, val -> avgEffCache = val));
    }

    @Override
    public IStructureDefinition<AntimatterGenerator> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FUSION1)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FUSION1_GLOW)
                .extFacing()
                .glow()
                .build() };
        if (aActive) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_ANTIMATTER_GLOW)
                .extFacing()
                .glow()
                .build() };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build() };
    }

    public Block getCoilBlock(int type) {
        switch (type) {
            case 1:
                return Loaders.antimatterAnnihilationMatrix;
            case 2:
                return Loaders.protomatterActivationCoil;
            default:
                return Loaders.antimatterAnnihilationMatrix;
        }
    }

    public int getCoilMeta(int type) {
        return 0;
    }

    public Block getCasingBlock(int type) {
        switch (type) {
            case 1:
                return Loaders.magneticFluxCasing;
            case 2:
                return Loaders.gravityStabilizationCasing;
            default:
                return Loaders.magneticFluxCasing;
        }
    }

    public int getCasingMeta(int type) {
        return 0;
    }

    public Block getFrameBlock() {
        return Loaders.antimatterContainmentCasing;
    }

    public int getFrameMeta() {
        return 0;
    }

    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas2;
    }

    public int getGlassMeta() {
        return 0;
    }

    public int textureIndex(int type) {
        switch (type) {
            case 1:
                return (12 << 7) + 9;
            case 2:
                return (12 << 7) + 10;
            default:
                return (12 << 7) + 9;
        }
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

}
