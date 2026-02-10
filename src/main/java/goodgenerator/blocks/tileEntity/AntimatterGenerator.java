package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.structures.AntimatterStructures;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.AntimatterGeneratorGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
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
                            .anyOf(HatchElement.ExoticDynamo)
                            .casingIndex(x.textureIndex(2))
                            .hint(2)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
                    'I',
                    lazy(
                        x -> buildHatchAdder(AntimatterGenerator.class).atLeast(HatchElement.InputHatch)
                            .casingIndex(x.textureIndex(1))
                            .hint(1)
                            .buildAndChain(x.getCasingBlock(1), x.getCasingMeta(1))))
                .build();
        }
    };

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
    public CheckRecipeResult checkProcessing() {
        List<FluidStack> inputFluids = getStoredFluids();
        long containedAntimatter = 0;
        FluidStack catalystFluid = null;
        int i = 0;

        while (i < inputFluids.size()) {
            FluidStack inputFluid = inputFluids.get(i);
            if (inputFluid.isFluidEqual(Materials.Antimatter.getFluid(1))) {
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

        recipesDone++;
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
            for (MTEHatch tHatch : getExoticDynamoHatches()) {
                euCapacity += tHatch.maxEUStore();
            }

            // Prevent -Generation when long overflow
            if (generatedEU < 0) generatedEU = Long.MAX_VALUE;
            if (euCapacity < 0) euCapacity = Long.MAX_VALUE;

            generatedEU = Math.min(generatedEU, euCapacity);
            this.euLastCycle = generatedEU;
            addEUToGlobalEnergyMap(owner_uuid, generatedEU);
        } else {
            this.euLastCycle = generatedEU;
            float invHatchCount = 1.0F / (float) mExoticDynamoHatches.size();
            for (MTEHatch tHatch : getExoticDynamoHatches()) {
                tHatch.setEUVar(tHatch.getEUVar() + (long) (generatedEU * invHatchCount));
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
        return survivalBuildPiece(MAIN_NAME, stackSize, 17, 41, 0, realBudget, env, false, true);
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AntimatterGenerator(MAIN_NAME);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        wirelessEnabled = !wirelessEnabled;
        GTUtility.sendChatTrans(
            aPlayer,
            wirelessEnabled ? "gg.chat.antimatter_generator.wireless_mode.enable"
                : "gg.chat.antimatter_generator.wireless_mode.disable");
        if (wirelessEnabled) {
            GTUtility.sendChatTrans(aPlayer, "gg.chat.antimatter_generator.wireless_mode.enable.hint");
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
        tt.addMachineType("Antimatter Generator, SLAM")
            .addInfo("Annihilating Antimatter like it's 2205!")
            .addSeparator()
            .addInfo(
                "Generates energy by reacting " + EnumChatFormatting.AQUA
                    + "Semi-Stable Antimatter"
                    + EnumChatFormatting.GRAY
                    + " with "
                    + EnumChatFormatting.GOLD
                    + "Matter")
            .addInfo(
                "Annihilation uses an equal amount of " + EnumChatFormatting.AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.GOLD
                    + "Matter")
            .addInfo(
                "Consumes " + EnumChatFormatting.GOLD
                    + "all inputs"
                    + EnumChatFormatting.GRAY
                    + " every processing cycle")
            .addInfo(EnumChatFormatting.RED + "Voids any invalid fluid!")
            .addInfo("A cycle lasts 5 seconds")
            .addInfo(
                "An imbalance between " + EnumChatFormatting.AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.GOLD
                    + "Matter"
                    + EnumChatFormatting.RED
                    + " decreases efficiency!"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "Efficiency formula: Min(" + EnumChatFormatting.AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.GOLD
                    + "Matter"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.GOLD
                    + "Matter"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addInfo("Any excess EU generated will be " + EnumChatFormatting.RED + "voided!" + EnumChatFormatting.GRAY)
            .addInfo(
                "Cannot produce more than " + EnumChatFormatting.GREEN
                    + GTUtility.scientificFormat(Long.MAX_VALUE)
                    + EnumChatFormatting.GRAY
                    + " EU per cycle")
            .addSeparator()
            .addInfo(
                "Energy production formula: " + EnumChatFormatting.GREEN
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "A"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " * ("
                    + EnumChatFormatting.AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + " ^ "
                    + EnumChatFormatting.GOLD
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "E"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + ") EU/Cycle")
            .addInfo(
                "" + EnumChatFormatting.GREEN
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "A"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GREEN
                    + "ntimatter energy"
                    + EnumChatFormatting.GRAY
                    + " base value: "
                    + EnumChatFormatting.GREEN
                    + GTUtility.scientificFormat(ANTIMATTER_FUEL_VALUE)
                    + EnumChatFormatting.GRAY
                    + " EU/L")
            .addInfo(
                "" + EnumChatFormatting.GOLD
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "E"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GOLD
                    + "nergy production boost"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + ":")
            .addInfo("1. Molten Copper: " + EnumChatFormatting.GOLD + "1.00")
            .addInfo("2. Molten Superconductor Base UIV: " + EnumChatFormatting.GOLD + "1.02")
            .addInfo("3. Molten Superconductor Base UMV: " + EnumChatFormatting.GOLD + "1.03")
            .addSeparator()
            .addInfo("Switch the power destination to your wireless network with a screwdriver")
            .addInfo("Wireless mode requires Superconductor Base UMV to work")
            .addInfo("Wireless mode is still limited by hatch capacity")
            .beginStructureBlock(35, 43, 35, false)
            .addCasingInfoMin("Transcendentally Reinforced Borosilicate Glass", 1008, false)
            .addCasingInfoMin("Magnetic Flux Casing", 4122, false)
            .addCasingInfoMin("Gravity Stabilization Casing", 2418, false)
            .addCasingInfoMin("Protomatter Activation Coil", 32, false)
            .addCasingInfoMin("Antimatter Annihilation Matrix", 600, false)
            .addCasingInfoMin("Naquadria Frame Box", 293, false)
            .addCasingInfoMin("Advanced Filter Casing", 209, false)
            .addInputHatch("2, Hint Block Number 1", 1)
            .addOtherStructurePart(
                StatCollector.translateToLocal("gg.structure.tooltip.laser_source_hatch"),
                "1-64, Hint Block Number 2",
                2)
            .toolTipFinisher();
        return tt;
    }

    public boolean canUseWireless() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;

        for (MTEHatch tHatch : mExoticDynamoHatches) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        // Prevent -Value when long overflow
        if (storedEnergy < 0) storedEnergy = Long.MAX_VALUE;
        if (maxEnergy < 0) maxEnergy = Long.MAX_VALUE;

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("gg.scanner.info.antimatter_generator")
                + " "
                + EnumChatFormatting.GRAY,
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(mProgresstime)
                + EnumChatFormatting.RESET
                + "t / "
                + EnumChatFormatting.YELLOW
                + formatNumber(mMaxProgresstime)
                + EnumChatFormatting.RESET
                + "t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("gui.AntimatterGenerator.0") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(this.euLastCycle)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": "
                + EnumChatFormatting.AQUA
                + formatNumber(Math.ceil(this.annihilationEfficiency * 100))
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": ⟨ "
                + EnumChatFormatting.AQUA
                + formatNumber(Math.ceil(this.avgEffCache * 100))
                + EnumChatFormatting.RESET
                + " % ⟩₁₀",
            translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    public long getEnergyProduced() {
        return this.euLastCycle;
    }

    public float getEfficiency() {
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

    public float getAvgEfficiency() {
        return this.avgEffCache;
    }

    protected long energyProducedCache;
    protected float efficiencyCache;
    protected float avgEffCache;

    @Override
    public IStructureDefinition<AntimatterGenerator> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        currentTip.add(
            StatCollector.translateToLocal("gui.AntimatterGenerator.0") + ": "
                + EnumChatFormatting.BLUE
                + GTUtility.scientificFormat(energyProducedCache)
                + EnumChatFormatting.WHITE
                + " EU");
        currentTip.add(
            StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": "
                + EnumChatFormatting.RED
                + formatNumber(Math.ceil(efficiencyCache * 100))
                + EnumChatFormatting.WHITE
                + " %");
        currentTip.add(
            StatCollector.translateToLocal("gui.AntimatterGenerator.1") + ": ⟨ "
                + EnumChatFormatting.RED
                + formatNumber(Math.ceil(avgEffCache * 100))
                + EnumChatFormatting.WHITE
                + " % ⟩₁₀");
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
        if (type == 2) {
            return Loaders.protomatterActivationCoil;
        }
        return Loaders.antimatterAnnihilationMatrix;
    }

    public int getCoilMeta(int type) {
        return 0;
    }

    public Block getCasingBlock(int type) {
        if (type == 2) return Loaders.gravityStabilizationCasing;
        return Loaders.magneticFluxCasing;
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
        if (type == 2) {
            return (12 << 7) + 10;
        }
        return (12 << 7) + 9;
    }

    @Override
    protected @NotNull AntimatterGeneratorGui getGui() {
        return new AntimatterGeneratorGui(this);
    }

    @Override
    public boolean canBeMuffled() {
        return false;
    }

    public boolean getWirelessMode() {
        return this.wirelessEnabled;
    }

    public void setWirelessEnabled(boolean b) {
        wirelessEnabled = b;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

}
