package tectech.thing.metaTileEntity.multi.godforge;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;
import static tectech.thing.casing.TTCasingsContainer.forgeOfGodsRenderBlock;
import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.*;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.allowModuleConnection;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateEnergyDiscountForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateFuelConsumption;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateMaxHeatForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateMaxParallelForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateProcessingVoltageForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateSpeedBonusForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateStartupFuelConsumption;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.determineCatalystMilestone;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.determineChargeMilestone;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.determineCompositionMilestone;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.determineConversionMilestone;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.factorChangeDuringRecipeAntiCheese;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.queryMilestoneStats;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.setMiscModuleParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.threads.RunnableMachineUpdate;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTEForgeOfGodsGui;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import tectech.loader.ConfigHandler;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.TileEntityForgeOfGods;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsRingsStructureString;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsStructureString;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class MTEForgeOfGods extends TTMultiblockBase implements IConstructable, ISurvivalConstructable {

    private static Textures.BlockIcons.CustomIcon ScreenON;

    public ArrayList<MTEBaseModule> moduleHatches = new ArrayList<>();

    private final ForgeOfGodsData data = new ForgeOfGodsData();

    private static final int TEXTURE_INDEX = 960;
    private static final long SOUND_LOOP_LENGTH = 440;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final String STRUCTURE_PIECE_SHAFT = "beam_shaft";
    protected static final String STRUCTURE_PIECE_FIRST_RING = "first_ring";
    protected static final String STRUCTURE_PIECE_FIRST_RING_AIR = "first_ring_air";
    protected static final String STRUCTURE_PIECE_SECOND_RING = "second_ring";
    protected static final String STRUCTURE_PIECE_SECOND_RING_AIR = "second_ring_air";
    protected static final String STRUCTURE_PIECE_THIRD_RING = "third_ring";
    protected static final String STRUCTURE_PIECE_THIRD_RING_AIR = "third_ring_air";
    private static final String SCANNER_INFO_BAR = EnumChatFormatting.BLUE.toString() + EnumChatFormatting.STRIKETHROUGH
        + "--------------------------------------------";
    private static final ItemStack STELLAR_FUEL = Avaritia.isModLoaded() ? getModItem(Avaritia.ID, "Resource", 1, 8)
        : GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 1);

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        // 1000 blocks max per placement.
        int realBudget = elementBudget >= 1000 ? elementBudget : Math.min(1000, elementBudget * 5);
        int built = 0;

        survivalBuildPiece(STRUCTURE_PIECE_SHAFT, stackSize, 63, 14, 1, realBudget, env, false, true);

        if ((stackSize.stackSize > 0 && !data.isRenderActive())) {
            built += survivalBuildPiece(
                STRUCTURE_PIECE_FIRST_RING,
                stackSize,
                63,
                14,
                -59,
                realBudget,
                env,
                false,
                true);
        }

        if (stackSize.stackSize > 1 && data.getRingAmount() < 2) {
            built += survivalBuildPiece(
                STRUCTURE_PIECE_SECOND_RING,
                stackSize,
                55,
                11,
                -67,
                realBudget,
                env,
                false,
                true);
        }

        if (stackSize.stackSize > 2 && data.getRingAmount() < 3) {
            built += survivalBuildPiece(
                STRUCTURE_PIECE_THIRD_RING,
                stackSize,
                47,
                13,
                -76,
                realBudget,
                env,
                false,
                true);
        }
        return built;
    }

    @Override
    public IStructureDefinition<MTEForgeOfGods> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public static final IStructureDefinition<MTEForgeOfGods> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEForgeOfGods>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ForgeOfGodsStructureString.MAIN_STRUCTURE)
        .addShape(STRUCTURE_PIECE_SHAFT, ForgeOfGodsStructureString.BEAM_SHAFT)
        .addShape(STRUCTURE_PIECE_FIRST_RING, ForgeOfGodsStructureString.FIRST_RING)
        .addShape(STRUCTURE_PIECE_FIRST_RING_AIR, ForgeOfGodsStructureString.FIRST_RING_AIR)
        .addShape(STRUCTURE_PIECE_SECOND_RING, ForgeOfGodsRingsStructureString.SECOND_RING)
        .addShape(STRUCTURE_PIECE_SECOND_RING_AIR, ForgeOfGodsRingsStructureString.SECOND_RING_AIR)
        .addShape(STRUCTURE_PIECE_THIRD_RING, ForgeOfGodsRingsStructureString.THIRD_RING)
        .addShape(STRUCTURE_PIECE_THIRD_RING_AIR, ForgeOfGodsRingsStructureString.THIRD_RING_AIR)
        .addElement(
            'A',
            HatchElementBuilder.<MTEForgeOfGods>builder()
                .atLeast(InputBus, InputHatch, OutputBus)
                .casingIndex(TEXTURE_INDEX + 3)
                .hint(1)
                .buildAndChain(GodforgeCasings, 3))
        .addElement('B', ofBlock(GodforgeCasings, 0))
        .addElement('C', ofBlock(GodforgeCasings, 1))
        .addElement('D', ofBlock(GodforgeCasings, 2))
        .addElement('E', ofBlock(GodforgeCasings, 3))
        .addElement('F', ofBlock(GodforgeCasings, 4))
        .addElement('G', ofBlock(GodforgeCasings, 5))
        .addElement('H', ofBlock(BlockGodforgeGlass.INSTANCE, 0))
        .addElement('I', ofBlock(GodforgeCasings, 7))
        .addElement(
            'J',
            HatchElementBuilder.<MTEForgeOfGods>builder()
                .atLeast(moduleElement.Module)
                .casingIndex(TEXTURE_INDEX)
                .hint(2)
                .buildAndChain(GodforgeCasings, 0))
        .addElement('K', ofBlock(GodforgeCasings, 6))
        .addElement('L', ofBlock(Blocks.air, 0))
        .build();

    public MTEForgeOfGods(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEForgeOfGods(String aName) {
        super(aName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEForgeOfGods(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/GODFORGE_CONTROLLER");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX + 1),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX + 1) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 63, 14, 1, stackSize, hintsOnly);
        if (stackSize.stackSize > 1) {
            buildPiece(STRUCTURE_PIECE_SECOND_RING, stackSize, hintsOnly, 55, 11, -67);
        }
        if (stackSize.stackSize > 2) {
            buildPiece(STRUCTURE_PIECE_THIRD_RING, stackSize, hintsOnly, 47, 13, -76);
        }
    }

    private final ArrayList<FluidStack> validFuelList = new ArrayList<>() {

        {
            add(Materials.DTR.getFluid(1));
            add(Materials.RawStarMatter.getFluid(1));
            add(Materials.MHDCSM.getMolten(1));
        }
    };

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {

        moduleHatches.clear();
        // Check structure of multi
        if (data.isRenderActive()) {
            if (!structureCheck_EM(STRUCTURE_PIECE_SHAFT, 63, 14, 1)
                || !structureCheck_EM(STRUCTURE_PIECE_FIRST_RING_AIR, 63, 14, -59)) {
                destroyRenderer();
                return false;
            }
        } else if (!structureCheck_EM(STRUCTURE_PIECE_MAIN, 63, 14, 1)) {
            return false;
        }

        if (data.getInternalBattery() != 0 && !data.isRenderActive() && !data.isRendererDisabled()) {
            createRenderer();
        }
        // Check there is 1 input bus
        if (mInputBusses.size() != 1) {
            return false;
        }

        // Check there is 1 me output bus
        {
            if (mOutputBusses.size() != 1) {
                return false;
            }

            if (!(mOutputBusses.get(0) instanceof MTEHatchOutputBusME)) {
                return false;
            }
        }
        // Make sure there are no energy hatches
        {
            if (!mEnergyHatches.isEmpty()) {
                return false;
            }

            if (!mExoticEnergyHatches.isEmpty()) {
                return false;
            }
        }

        // Make sure there is 1 input hatch
        if (mInputHatches.size() != 1) {
            return false;
        }

        if (data.isUpgradeActive(CD)) {
            if (checkPiece(STRUCTURE_PIECE_SECOND_RING, 55, 11, -67)) {
                data.setRingAmount(2);
                if (!data.isRendererDisabled()) {
                    destroySecondRing();
                    updateRenderer();
                }
            }
            if (data.isRenderActive() && data.getRingAmount() >= 2
                && !checkPiece(STRUCTURE_PIECE_SECOND_RING_AIR, 55, 11, -67)) {
                destroyRenderer();
            }
        } else {
            if (data.getRingAmount() == 3) {
                buildThirdRing();
            }
            if (data.getRingAmount() >= 2) {
                data.setRingAmount(1);
                if (!data.isRendererDisabled()) {
                    updateRenderer();
                }
                buildSecondRing();
            }
        }

        if (data.isUpgradeActive(END)) {
            if (checkPiece(STRUCTURE_PIECE_THIRD_RING, 47, 13, -76)) {
                data.setRingAmount(3);
                if (!data.isRendererDisabled()) {
                    destroyThirdRing();
                    updateRenderer();
                }
            }
            if (data.isRenderActive() && data.getRingAmount() == 3
                && !checkPiece(STRUCTURE_PIECE_THIRD_RING_AIR, 47, 13, -76)) {
                destroyRenderer();
            }
        } else {
            if (data.getRingAmount() == 3) {
                data.setRingAmount(2);
                if (!data.isRendererDisabled()) {
                    updateRenderer();
                }
                buildThirdRing();
            }
        }

        return true;
    }

    long ticker = 0;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick == 1) {
                updateRenderer();
            }
            ticker++;
            // Check and drain fuel
            if (ticker % (5 * SECONDS) == 0) {
                startRecipeProcessing();

                int maxModuleCount = 8;
                if (data.isUpgradeActive(CD)) {
                    maxModuleCount += 4;
                }
                if (data.isUpgradeActive(END)) {
                    maxModuleCount += 4;
                }

                if (!mInputBusses.isEmpty()) {
                    if (data.getInternalBattery() == 0 || data.isUpgradeActive(END)) {
                        MTEHatchInputBus inputBus = mInputBusses.get(0);

                        ItemStack itemToAbsorb = STELLAR_FUEL;
                        if (data.isUpgradeActive(END) && data.getInternalBattery() != 0) {
                            itemToAbsorb = GTOreDictUnificator.get(OrePrefixes.gem, Materials.GravitonShard, 1);
                        }

                        int invLength = inputBus.getSizeInventory();
                        for (int i = 0; i < invLength; i++) {
                            ItemStack itemStack = inputBus.getStackInSlot(i);
                            if (itemStack != null && itemStack.isItemEqual(itemToAbsorb)) {
                                int stacksize = Math
                                    .min(itemStack.stackSize, Integer.MAX_VALUE - data.getStellarFuelAmount());
                                inputBus.decrStackSize(i, stacksize);
                                if (data.getInternalBattery() == 0) {
                                    data.setStellarFuelAmount(data.getStellarFuelAmount() + stacksize);
                                } else {
                                    data.setGravitonShardsAvailable(data.getGravitonShardsAvailable() + stacksize);
                                    data.setGravitonShardsSpent(data.getGravitonShardsSpent() - stacksize);
                                }
                                inputBus.updateSlots();
                            }
                        }

                        if (data.getInternalBattery() == 0) {
                            data.setNeededStartupFuel(calculateStartupFuelConsumption(data));
                            if (data.getStellarFuelAmount() >= data.getNeededStartupFuel()) {
                                data.setStellarFuelAmount(data.getStellarFuelAmount() - data.getNeededStartupFuel());
                                increaseBattery(data.getNeededStartupFuel());
                                if (!data.isRendererDisabled()) createRenderer();
                            }
                        }
                    }
                }

                if (data.getInternalBattery() != 0) {
                    drainFuel();
                }

                determineCompositionMilestoneLevel();
                determineMilestoneProgress();
                checkInversionStatus();
                if (!ConfigHandler.debug.DEBUG_MODE) {
                    determineGravitonShardAmount();
                }
                if (data.isUpgradeActive(END) && data.isGravitonShardEjection()) {
                    ejectGravitonShards();
                }

                // Do module calculations and checks
                if (!moduleHatches.isEmpty() && data.getInternalBattery() > 0
                    && moduleHatches.size() <= maxModuleCount) {
                    for (MTEBaseModule module : moduleHatches) {
                        if (allowModuleConnection(module, data)) {
                            module.connect();
                            calculateMaxHeatForModules(module, data);
                            calculateSpeedBonusForModules(module, data);
                            calculateMaxParallelForModules(module, data);
                            calculateEnergyDiscountForModules(module, data);
                            setMiscModuleParameters(module, data);
                            queryMilestoneStats(module, data);
                            if (!data.isUpgradeActive(TBF)) {
                                calculateProcessingVoltageForModules(module, data);
                            }
                            if (factorChangeDuringRecipeAntiCheese(module)) {
                                module.disconnect();
                            }
                        } else {
                            module.disconnect();
                        }
                    }
                } else if (moduleHatches.size() > maxModuleCount) {
                    for (MTEBaseModule module : moduleHatches) {
                        module.disconnect();
                    }
                }
                if (mEfficiency < 0) mEfficiency = 0;
                endRecipeProcessing();
            }

            if (ticker % SOUND_LOOP_LENGTH == 0 && data.isRenderActive()) {
                sendLoopStart((byte) 1);
            }
        }
    }

    private void drainFuel() {
        int fuelConsumptionFactor = data.getFuelConsumptionFactor();

        if (data.getSelectedFuelType() == 0) {
            if (data.isUpgradeActive(STEM)) {
                if (fuelConsumptionFactor > ForgeOfGodsData.MAX_RESIDUE_FACTOR_DISCOUNTED) {
                    data.setFuelConsumptionFactor(ForgeOfGodsData.MAX_RESIDUE_FACTOR_DISCOUNTED);
                }
            } else if (fuelConsumptionFactor > ForgeOfGodsData.MAX_RESIDUE_FACTOR) {
                data.setFuelConsumptionFactor(ForgeOfGodsData.MAX_RESIDUE_FACTOR);
            }
        } else if (data.getSelectedFuelType() == 1) {
            if (data.isUpgradeActive(STEM)) {
                if (fuelConsumptionFactor > ForgeOfGodsData.MAX_STELLAR_PLASMA_FACTOR_DISCOUNTED) {
                    data.setFuelConsumptionFactor(ForgeOfGodsData.MAX_STELLAR_PLASMA_FACTOR_DISCOUNTED);
                }
            } else if (fuelConsumptionFactor > ForgeOfGodsData.MAX_STELLAR_PLASMA_FACTOR) {
                data.setFuelConsumptionFactor(ForgeOfGodsData.MAX_STELLAR_PLASMA_FACTOR);
            }
        }

        int updatedFuelConsumptionFactor = data.getFuelConsumptionFactor();
        data.setFuelConsumption(
            (long) Math.max(calculateFuelConsumption(data) * 5 * (data.isBatteryCharging() ? 2 : 1), 1));
        if (data.getFuelConsumption() >= Integer.MAX_VALUE) {
            reduceBattery(updatedFuelConsumptionFactor);
            return;
        }

        FluidStack fuelToDrain = new FluidStack(
            validFuelList.get(data.getSelectedFuelType()),
            (int) data.getFuelConsumption());
        for (MTEHatchInput hatch : filterValidMTEs(mInputHatches)) {
            FluidStack drained = hatch.drain(ForgeDirection.UNKNOWN, fuelToDrain, true);
            if (drained == null) {
                continue;
            }

            fuelToDrain.amount -= drained.amount;

            if (fuelToDrain.amount == 0) {
                data.setTotalFuelConsumed(data.getTotalFuelConsumed() + updatedFuelConsumptionFactor);
                if (data.isBatteryCharging()) {
                    increaseBattery(updatedFuelConsumptionFactor);
                }
                return;
            }
        }
        reduceBattery(updatedFuelConsumptionFactor);
    }

    public boolean addModuleToMachineList(IGregTechTileEntity tileEntity, int baseCasingIndex) {
        if (tileEntity == null) {
            return false;
        }
        IMetaTileEntity metaTileEntity = tileEntity.getMetaTileEntity();
        if (metaTileEntity == null) {
            return false;
        }
        if (metaTileEntity instanceof MTEBaseModule) {
            return moduleHatches.add((MTEBaseModule) metaTileEntity);
        }
        return false;
    }

    public enum moduleElement implements IHatchElement<MTEForgeOfGods> {

        Module(MTEForgeOfGods::addModuleToMachineList, MTEBaseModule.class) {

            @Override
            public long count(MTEForgeOfGods tileEntity) {
                return tileEntity.moduleHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEForgeOfGods> adder;

        @SafeVarargs
        moduleElement(IGTHatchAdder<MTEForgeOfGods> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEForgeOfGods> adder() {
            return adder;
        }
    }

    private TileEntityForgeOfGods getRenderer() {
        ChunkCoordinates renderPos = getRenderPos();
        TileEntity tile = this.getBaseMetaTileEntity()
            .getWorld()
            .getTileEntity(renderPos.posX, renderPos.posY, renderPos.posZ);

        if (tile instanceof TileEntityForgeOfGods forgeTile) {
            return forgeTile;
        }
        return null;
    }

    public void updateRenderer() {
        TileEntityForgeOfGods tile = getRenderer();
        if (tile == null) return;

        tile.setRingCount(data.getRingAmount());
        tile.setStarRadius(data.getStarSize());
        tile.setRotationSpeed(data.getRotationSpeed());
        tile.setColor(
            data.getStarColors()
                .getByName(data.getSelectedStarColor()));

        tile.updateToClient();
    }

    private void createRenderer() {
        ChunkCoordinates renderPos = getRenderPos();

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(renderPos.posX, renderPos.posY, renderPos.posZ, Blocks.air);
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(renderPos.posX, renderPos.posY, renderPos.posZ, forgeOfGodsRenderBlock);
        TileEntityForgeOfGods rendererTileEntity = (TileEntityForgeOfGods) this.getBaseMetaTileEntity()
            .getWorld()
            .getTileEntity(renderPos.posX, renderPos.posY, renderPos.posZ);

        boolean wasStructureCheckEnabled = RunnableMachineUpdate.isEnabled();
        RunnableMachineUpdate.setEnabled(false);

        switch (data.getRingAmount()) {
            case 2 -> {
                destroyFirstRing();
                destroySecondRing();
            }
            case 3 -> {
                destroyFirstRing();
                destroySecondRing();
                destroyThirdRing();
            }
            default -> destroyFirstRing();
        }

        RunnableMachineUpdate.setEnabled(wasStructureCheckEnabled);

        rendererTileEntity.setRenderRotation(getRotation(), getDirection());
        updateRenderer();

        data.setRenderActive(true);
        enableWorking();
    }

    public void destroyRenderer() {
        ChunkCoordinates renderPos = getRenderPos();
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(renderPos.posX, renderPos.posY, renderPos.posZ, Blocks.air);

        boolean wasStructureCheckEnabled = RunnableMachineUpdate.isEnabled();
        RunnableMachineUpdate.setEnabled(false);

        switch (data.getRingAmount()) {
            case 2 -> {
                buildFirstRing();
                buildSecondRing();
            }
            case 3 -> {
                buildFirstRing();
                buildSecondRing();
                buildThirdRing();
            }
            default -> buildFirstRing();
        }

        RunnableMachineUpdate.setEnabled(wasStructureCheckEnabled);

        data.setRenderActive(false);
        disableWorking();
    }

    private ChunkCoordinates getRenderPos() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();
        double xOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double yOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        double zOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        return new ChunkCoordinates((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset));
    }

    private void destroyFirstRing() {
        buildPiece(STRUCTURE_PIECE_FIRST_RING_AIR, null, false, 63, 14, -59);
    }

    private void destroySecondRing() {
        buildPiece(STRUCTURE_PIECE_SECOND_RING_AIR, null, false, 55, 11, -67);
    }

    private void destroyThirdRing() {
        buildPiece(STRUCTURE_PIECE_THIRD_RING_AIR, null, false, 47, 13, -76);
    }

    private void buildFirstRing() {
        buildPiece(STRUCTURE_PIECE_FIRST_RING, null, false, 63, 14, -59);
    }

    private void buildSecondRing() {
        buildPiece(STRUCTURE_PIECE_SECOND_RING, null, false, 55, 11, -67);
    }

    private void buildThirdRing() {
        buildPiece(STRUCTURE_PIECE_THIRD_RING, null, false, 47, 13, -76);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (data.isRendererDisabled()) {
            data.setRendererDisabled(false);
            // let the renderer automatically rebuild itself as needed through normal logic
        } else {
            data.setRendererDisabled(true);
            if (data.isRenderActive()) destroyRenderer();
        }
        aPlayer.addChatMessage(
            new ChatComponentText("Animations are now " + (data.isRendererDisabled() ? "disabled" : "enabled") + "."));
    }

    @Override
    public boolean isFlipChangeAllowed() {
        // If the machine is formed, or if rings are taken into the controller
        if (mMachine || data.isRenderActive()) return false;
        return super.isFlipChangeAllowed();
    }

    @Override
    public boolean isRotationChangeAllowed() {
        // If the machine is formed, or if rings are taken into the controller
        if (mMachine || data.isRenderActive()) return false;
        return super.isRotationChangeAllowed();
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        if (data.isRenderActive()) {
            destroyRenderer();
        }
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add(SCANNER_INFO_BAR);
        str.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.fog.rings",
                "" + EnumChatFormatting.GOLD + data.getRingAmount()));
        str.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.fog.upgrades.unlocked",
                "" + EnumChatFormatting.GOLD
                    + data.getUpgrades()
                        .getTotalActiveUpgrades()));
        str.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.fog.connected",
                "" + EnumChatFormatting.GOLD + moduleHatches.size()));
        str.add(SCANNER_INFO_BAR);
        return str.toArray(new String[0]);
    }

    @Override
    public void onRemoval() {
        if (moduleHatches != null && !moduleHatches.isEmpty()) {
            for (MTEBaseModule module : moduleHatches) {
                module.disconnect();
            }
        }
        super.onRemoval();
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEForgeOfGodsGui(this);
    }

    @Override
    public boolean supportsLogo() {
        return false;
    }

    @Override
    public boolean supportsShutdownReasonHoverable() {
        return false;
    }

    @Override
    public boolean supportsMaintenanceIssueHoverable() {
        return false;
    }

    @Override
    public boolean supportsTerminalLeftCornerColumn() {
        return true;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Stellar Forge")
            .addInfo(EnumChatFormatting.ITALIC + "Also known as Godforge or Gorge for short")
            .addSeparator(EnumChatFormatting.AQUA, 73)
            .addInfo("A massive structure harnessing the thermal, gravitational and")
            .addInfo("kinetic energy of a stabilised neutron star for material processing")
            .addInfo(
                "This multiblock can house " + EnumChatFormatting.RED
                    + "up to 16 modules "
                    + EnumChatFormatting.GRAY
                    + "which utilize the star to energize materials")
            .addInfo("to varying degrees, ranging from regular smelting to matter degeneration")
            .addInfo("EU requirements for all modules are handled via wireless energy directly")
            .addSeparator(EnumChatFormatting.AQUA, 73)
            .addInfo(
                "This multiblock has an " + EnumChatFormatting.GOLD
                    + "extensive upgrade tree "
                    + EnumChatFormatting.GRAY
                    + "which influences all of its functions,")
            .addInfo(
                "such as " + EnumChatFormatting.GOLD
                    + "unlocking new module types"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.GOLD
                    + "increasing heat levels "
                    + EnumChatFormatting.GRAY
                    + "and "
                    + EnumChatFormatting.GOLD
                    + "granting")
            .addInfo(
                EnumChatFormatting.GOLD + "various processing speed bonuses"
                    + EnumChatFormatting.GRAY
                    + ". "
                    + EnumChatFormatting.GRAY
                    + "These upgrades can be unlocked by reaching")
            .addInfo("certain milestones and/or spending materials")
            .addSeparator(EnumChatFormatting.AQUA, 73)
            .addInfo(
                EnumChatFormatting.GREEN
                    + "Clicking on the logo in the controller gui opens an extensive information window"
                    + EnumChatFormatting.GRAY
                    + ",")
            .addInfo("explaining everything there is to know about this multiblock")
            .beginStructureBlock(127, 29, 186, false)
            .addStructureInfo("Total blocks needed for the structure with " + getRingText("1", "2", "3") + "rings:")
            .addStructureInfo(
                getRingText("3943", "7279", "11005") + "Transcendentally Amplified Magnetic Confinement Casing")
            .addStructureInfo(getRingText("2818", "4831", "6567") + "Singularity Reinforced Stellar Shielding Casing")
            .addStructureInfo(getRingText("272", "512", "824") + "Celestial Matter Guidance Casing")
            .addStructureInfo(getRingText("130", "144", "158") + "Boundless Gravitationally Severed Structure Casing")
            .addStructureInfo(getRingText("9", "54", "155") + "Spatially Transcendent Gravitational Lens Block")
            .addStructureInfo(
                getRingText("345", "357", "397") + getRingText("Remote", "Medial", "Central")
                    + "Graviton Flow Modulator")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "36" + EnumChatFormatting.GRAY + " Stellar Energy Siphon Casing")
            .addStructureInfoSeparator()
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Input Hatch")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Output Bus (ME)")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Input Bus")
            .toolTipFinisher(EnumChatFormatting.AQUA, 73);
        return tt;
    }

    private static String getRingText(String oneRing, String twoRings, String threeRings) {
        return EnumChatFormatting.DARK_PURPLE + oneRing
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.DARK_GREEN
            + twoRings
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.AQUA
            + threeRings
            + EnumChatFormatting.GRAY
            + " ";
    }

    @Override
    public boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        return true;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] { EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.FOG.hint.0"),
            translateToLocal("gt.blockmachines.multimachine.FOG.hint.1") };
    }

    public ForgeOfGodsData getData() {
        return data;
    }

    private void checkInversionStatus() {
        int inversionChecker = 0;
        for (int progress : data.getAllMilestoneProgress()) {
            if (progress < 7) {
                break;
            }
            inversionChecker++;
        }
        data.setInversion(inversionChecker == 4);
    }

    private void determineCompositionMilestoneLevel() {
        int[] uniqueModuleCount = new int[5];
        int smelting = 0;
        int molten = 0;
        int plasma = 0;
        int exotic = 0;
        int exoticMagmatter = 0;
        for (MTEBaseModule module : moduleHatches) {
            if (module instanceof MTESmeltingModule) {
                uniqueModuleCount[0] = 1;
                smelting++;
                continue;
            }
            if (module instanceof MTEMoltenModule) {
                uniqueModuleCount[1] = 1;
                molten++;
                continue;
            }
            if (module instanceof MTEPlasmaModule) {
                uniqueModuleCount[2] = 1;
                plasma++;
                continue;
            }
            if (module instanceof MTEExoticModule) {
                if (!((MTEExoticModule) module).isMagmatterModeOn()) {
                    uniqueModuleCount[3] = 1;
                    exotic++;
                } else {
                    uniqueModuleCount[4] = 1;
                    exoticMagmatter++;
                }
            }

        }
        data.setTotalExtensionsBuilt(
            Arrays.stream(uniqueModuleCount)
                .sum() + data.getRingAmount()
                - 1);
        if (data.isInversion()) {
            float toAdd = (smelting - 1
                + (molten - 1) * 2
                + (plasma - 1) * 3
                + (exotic - 1) * 4
                + (exoticMagmatter - 1) * 5) / 5f;
            data.setTotalExtensionsBuilt(data.getTotalExtensionsBuilt() + toAdd);
        }
        data.setMilestoneProgress(3, (int) Math.floor(data.getTotalExtensionsBuilt()));
    }

    private void determineMilestoneProgress() {
        determineChargeMilestone(data);
        determineConversionMilestone(data);
        determineCatalystMilestone(data);
        determineCompositionMilestone(data);
    }

    private void determineGravitonShardAmount() {
        int sum = 0;
        for (int progress : data.getAllMilestoneProgress()) {
            if (!data.isInversion()) {
                progress = Math.min(progress, 7);
            }
            sum += progress * (progress + 1) / 2;
        }
        data.setGravitonShardsAvailable(sum - data.getGravitonShardsSpent());
    }

    private void ejectGravitonShards() {
        if (mOutputBusses.size() == 1) {
            ItemStack shard = GTOreDictUnificator.get(OrePrefixes.gem, Materials.GravitonShard, 1);

            shard.stackSize = data.getGravitonShardsAvailable();

            // VP is disabled on gorges for some reason, force it on here
            ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this.getOutputBusses(), true);
            int ejected = ejectionHelper.ejectStack(shard);
            ejectionHelper.commit();

            data.setGravitonShardsAvailable(data.getGravitonShardsAvailable() - ejected);
            data.setGravitonShardsSpent(data.getGravitonShardsSpent() + ejected);
        }
    }

    private void increaseBattery(int amount) {
        // Written to be careful of potential overflow
        long newCharge = Long.sum(data.getInternalBattery(), amount);
        if (newCharge <= data.getMaxBatteryCharge()) {
            data.setInternalBattery((int) newCharge);
        } else {
            data.setInternalBattery(data.getMaxBatteryCharge());
            data.setBatteryCharging(false);
        }
    }

    public void reduceBattery(int amount) {
        if (data.getInternalBattery() - amount <= 0) {
            data.setInternalBattery(0);
            if (!moduleHatches.isEmpty()) {
                for (MTEBaseModule module : moduleHatches) {
                    module.disconnect();
                }
            }
            destroyRenderer();
        } else {
            data.setInternalBattery(data.getInternalBattery() - amount);
            data.setTotalFuelConsumed(data.getTotalFuelConsumed() + amount);
        }
    }

    @Override
    protected void setHatchRecipeMap(MTEHatchInput hatch) {}

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes;
    }

    @Override
    public void setItemNBT(NBTTagCompound NBT) {
        data.serializeNBT(NBT, false);
        super.saveNBTData(NBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        data.serializeNBT(NBT, true);
        data.serializeRenderNBT(NBT);
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound NBT) {
        data.deserializeNBT(NBT);
        super.loadNBTData(NBT);
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().disableWorking();
    }

    @Override
    public void enableWorking() {
        super.enableWorking();
        sendLoopStart((byte) 1);
    }

    @Override
    public void disableWorking() {
        super.disableWorking();
        sendLoopEnd((byte) 1);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_GOD_FORGE_LOOP;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(SoundResource.GT_MACHINES_GOD_FORGE_LOOP, 22, 1.0F, aX, aY, aZ);
        }
    }
}
