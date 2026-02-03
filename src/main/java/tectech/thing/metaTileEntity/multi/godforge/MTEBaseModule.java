package tectech.thing.metaTileEntity.multi.godforge;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.processInitialSettings;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public abstract class MTEBaseModule extends TTMultiblockBase implements IConstructable, ISurvivalConstructable {

    protected final int tier = getTier();
    protected boolean isConnected = false;
    protected double overclockTimeFactor = 2d;
    protected boolean isUpgrade83Unlocked = false;
    protected boolean isMultiStepPlasmaCapable = false;
    protected boolean isMagmatterCapable = false;
    private boolean isVoltageConfigUnlocked = false;
    private boolean isInversionUnlocked = false;
    protected UUID userUUID;
    protected int machineHeat = 0;
    protected int overclockHeat = 0;
    protected int calculatedMaxParallel = 0;
    protected int plasmaTier = 0;
    protected double processingSpeedBonus = 0;
    protected double energyDiscount = 0;
    protected long processingVoltage = 2_000_000_000;
    protected BigInteger powerTally = BigInteger.ZERO;
    protected long recipeTally = 0;
    private long currentRecipeHeat = 0;
    private static IIconContainer ScreenON;
    private static IIconContainer ScreenOFF;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int TEXTURE_INDEX = 960;

    public MTEBaseModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBaseModule(String aName) {
        super(aName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && isConnected) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
            if (mEfficiency < 0) mEfficiency = 0;
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    public boolean drainEnergyInput(long EUtEffective, long Amperes) {
        long EU_drain = EUtEffective * Amperes;
        if (EU_drain == 0L) {
            return true;
        } else {
            if (EU_drain > 0L) {
                EU_drain = -EU_drain;
            }
            return addEUToGlobalEnergyMap(userUUID, EU_drain);
        }
    }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean val) {
        isConnected = val;
    }

    public void setHeat(int heat) {
        machineHeat = heat;
    }

    public int getHeat() {
        return machineHeat;
    }

    public void setHeatForOC(int heat) {
        overclockHeat = heat;
    }

    public int getHeatForOC() {
        return overclockHeat;
    }

    public void setCalculatedMaxParallel(int parallel) {
        calculatedMaxParallel = parallel;
    }

    public int getCalculatedMaxParallel() {
        return calculatedMaxParallel;
    }

    public int getActualParallel() {
        return Math.max(
            1,
            alwaysMaxParallel ? getCalculatedMaxParallel()
                : Math.min(getCalculatedMaxParallel(), powerPanelMaxParallel));
    }

    public void setSpeedBonus(double bonus) {
        processingSpeedBonus = bonus;
    }

    public double getSpeedBonus() {
        return processingSpeedBonus;
    }

    public void setEnergyDiscount(double discount) {
        energyDiscount = discount;
    }

    public double getEnergyDiscount() {
        return energyDiscount;
    }

    public void setUpgrade83(boolean unlocked) {
        isUpgrade83Unlocked = unlocked;
    }

    public void setOverclockTimeFactor(double factor) {
        overclockTimeFactor = factor;
    }

    public double getOverclockTimeFactor() {
        return overclockTimeFactor;
    }

    public void setMultiStepPlasma(boolean isCapable) {
        isMultiStepPlasmaCapable = isCapable;
    }

    public boolean isMultiStepPlasma() {
        return isMultiStepPlasmaCapable;
    }

    public void setProcessingVoltage(long Voltage) {
        processingVoltage = Voltage;
    }

    public long getProcessingVoltage() {
        return processingVoltage;
    }

    public void setMagmatterCapable(boolean isCapable) {
        isMagmatterCapable = isCapable;
    }

    public boolean isMagmatterCapable() {
        return isMagmatterCapable;
    }

    public double getHeatEnergyDiscount() {
        return isUpgrade83Unlocked ? 0.92 : 0.95;
    }

    public void setPlasmaTier(int tier) {
        plasmaTier = tier;
    }

    public int getPlasmaTier() {
        return plasmaTier;
    }

    public void setVoltageConfig(boolean unlocked) {
        isVoltageConfigUnlocked = unlocked;
    }

    public boolean getVoltageConfig() {
        return isVoltageConfigUnlocked;
    }

    public boolean getInversionConfig() {
        return isInversionUnlocked;
    }

    public void setInversionConfig(boolean inversion) {
        isInversionUnlocked = inversion;
    }

    public void setPowerTally(BigInteger amount) {
        powerTally = amount;
    }

    public BigInteger getPowerTally() {
        return powerTally;
    }

    public void addToPowerTally(BigInteger amount) {
        powerTally = powerTally.add(amount);
    }

    public void setRecipeTally(long amount) {
        recipeTally = amount;
    }

    public long getRecipeTally() {
        return recipeTally;
    }

    public void addToRecipeTally(long amount) {
        recipeTally += amount;
    }

    public int getTier() {
        return tier;
    }

    public void setCurrentRecipeHeat(long heat) {
        currentRecipeHeat = heat;
    }

    public long getCurrentRecipeHeat() {
        return currentRecipeHeat;
    }

    @Override
    public long getMaxInputVoltage() {
        return GTValues.V[tier];
    }

    // This prevents processingLogic from overflowing on energy, can be changed if/when it can handle > max long
    protected long getSafeProcessingVoltage() {
        return Math.min(getProcessingVoltage(), Long.MAX_VALUE / getActualParallel());
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        if (this instanceof MTESmeltingModule) {
            return getStructureDefinition(GregTechAPI.sBlockCasings5, 12);
        }
        return getStructureDefinition(GodforgeCasings, 8);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 3, 3, 0, stackSize, hintsOnly);
    }

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(1000, elementBudget * 5);
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, realBudget, env, false, true);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        if (!structureCheck_EM(STRUCTURE_PIECE_MAIN, 3, 3, 0)) {
            return false;
        }

        if (this instanceof MTEExoticModule) {
            if (mOutputHatches.isEmpty()) {
                return false;
            }
            return !mOutputBusses.isEmpty();
        }

        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide() && (aTick == 1)) {
            userUUID = processInitialSettings(aBaseMetaTileEntity);
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected abstract @NotNull MTEMultiBlockBaseGui<?> getGui();

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean supportsTerminalLeftCornerColumn() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
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
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    private static IStructureDefinition<MTEBaseModule> getStructureDefinition(Block coilBlock, int meta) {
        return StructureDefinition.<MTEBaseModule>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                new String[][] { { "       ", "  BBB  ", " BBBBB ", " BB~BB ", " BBBBB ", "  BBB  ", "       " },
                    { "  CCC  ", " CFFFC ", "CFFFFFC", "CFFFFFC", "CFFFFFC", " CFFFC ", "  CCC  " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   G   ", "       ", "       ", "       " } })
            .addElement('A', ofBlock(coilBlock, meta))
            .addElement(
                'B',
                GTStructureUtility
                    .ofHatchAdderOptional(MTEBaseModule::addClassicToMachineList, TEXTURE_INDEX, 1, GodforgeCasings, 0))
            .addElement('C', ofBlock(GodforgeCasings, 0))
            .addElement('D', ofBlock(GodforgeCasings, 1))
            .addElement('E', ofBlock(GodforgeCasings, 2))
            .addElement('F', ofBlock(GodforgeCasings, 3))
            .addElement('G', ofBlock(GodforgeCasings, 4))
            .build();
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        NBT.setBoolean("isConnected", isConnected);
        NBT.setBoolean("isVoltageConfigUnlocked", isVoltageConfigUnlocked);
        NBT.setLong("processingVoltage", processingVoltage);
        NBT.setLong("recipeTally", recipeTally);
        NBT.setLong("currentRecipeHeat", currentRecipeHeat);
        NBT.setByteArray("powerTally", powerTally.toByteArray());
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound NBT) {
        isConnected = NBT.getBoolean("isConnected");
        isVoltageConfigUnlocked = NBT.getBoolean("isVoltageConfigUnlocked");
        processingVoltage = NBT.getLong("processingVoltage");
        recipeTally = NBT.getLong("recipeTally");
        currentRecipeHeat = NBT.getLong("currentRecipeHeat");
        powerTally = new BigInteger(NBT.getByteArray("powerTally"));
        super.loadNBTData(NBT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenON = Textures.BlockIcons.CustomIcon.create("iconsets/GODFORGE_MODULE_ACTIVE");
        ScreenOFF = Textures.BlockIcons.CustomIcon.create("iconsets/SCREEN_OFF");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX), TextureFactory.builder()
                .addIcon(ScreenOFF)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX) };
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
