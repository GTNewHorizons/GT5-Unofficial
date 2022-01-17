package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

public class GT_MetaTileEntity_DieselEngine extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_DieselEngine> {
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<GT_MetaTileEntity_DieselEngine>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<GT_MetaTileEntity_DieselEngine>>() {
        @Override
        protected IStructureDefinition<GT_MetaTileEntity_DieselEngine> computeValue(Class<?> type) {
            return StructureDefinition.<GT_MetaTileEntity_DieselEngine>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                            {"---", "iii", "chc", "chc", "ccc", },
                            {"---", "i~i", "hgh", "hgh", "cdc", },
                            {"---", "iii", "chc", "chc", "ccc", },
                    }))
                    .addElement('i', lazy(t -> ofBlock(t.getIntakeBlock(), t.getIntakeMeta())))
                    .addElement('c', lazy(t -> ofBlock(t.getCasingBlock(), t.getCasingMeta())))
                    .addElement('g', lazy(t -> ofBlock(t.getGearboxBlock(), t.getGearboxMeta())))
                    .addElement('d', lazy(t -> ofHatchAdder(GT_MetaTileEntity_DieselEngine::addDynamoToMachineList, t.getCasingTextureIndex(), 2)))
                    .addElement('h', lazy(t -> ofHatchAdderOptional(GT_MetaTileEntity_DieselEngine::addToMachineList, t.getCasingTextureIndex(), 1, t.getCasingBlock(), t.getCasingMeta())))
                    .build();
        }
    };
    protected int fuelConsumption = 0;
    protected int fuelValue = 0;
    protected int fuelRemaining = 0;
    protected boolean boostEu = false;

    public GT_MetaTileEntity_DieselEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_DieselEngine(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Combustion Generator")
                .addInfo("Controller block for the Large Combustion Engine")
                .addInfo("Supply Diesel Fuels and 1000L of Lubricant per hour to run")
                .addInfo("Supply 40L/s of Oxygen to boost output (optional)")
                .addInfo("Default: Produces 2048EU/t at 100% fuel efficiency")
                .addInfo("Boosted: Produces 6144EU/t at 150% fuel efficiency")
                .addInfo("You need to wait for it to reach 300% to output full power")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 3, 4, false)
                .addController("Front center")
                .addCasingInfo("Stable Titanium Machine Casing", 16)
                .addOtherStructurePart("Titanium Gear Box Machine Casing", "Inner 2 blocks")
                .addOtherStructurePart("Engine Intake Machine Casing", "8x, ring around controller")
                .addStructureInfo("Engine Intake Casings must not be obstructed in front (only air blocks)")
                .addDynamoHatch("Back center", 2)
                .addMaintenanceHatch("One of the casings next to a Gear Box", 1)
                .addMufflerHatch("Top middle back, above the rear Gear Box", 1)
                .addInputHatch("Diesel Fuel, next to a Gear Box", 1)
                .addInputHatch("Lubricant, next to a Gear Box", 1)
                .addInputHatch("Oxygen, optional, next to a Gear Box", 1)
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[]{
                    casingTexturePages[0][50],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                    casingTexturePages[0][50],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{casingTexturePages[0][50]};
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeDieselEngine.png");
    }

    // can't use getRecipeMap() or else the fluid hatch will reject oxygen
    protected GT_Recipe.GT_Recipe_Map_Fuel getFuelMap() {
        return GT_Recipe.GT_Recipe_Map.sDieselFuels;
    }

    /**
     * The nominal energy output
     * This can be further multiplied by {@link #getMaxEfficiency(ItemStack)} when boosted
     */
    protected int getNominalOutput() {
        return 2048;
    }

    protected Materials getBooster() {
        return Materials.Oxygen;
    }

    /**
     * x times fuel will be consumed when boosted
     * This will however NOT increase power output
     * Go tweak {@link #getMaxEfficiency(ItemStack)} and {@link #getNominalOutput()} instead
     */
    protected int getBoostFactor() {
        return 2;
    }

    /**
     * x times of additive will be consumed when boosted
     */
    protected int getAdditiveFactor() {
        return 1;
    }

    /**
     * Efficiency will increase by this amount every tick
     */
    protected int getEfficiencyIncrease() {
        return 15;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<FluidStack> tFluids = getStoredFluids();

        // fast track lookup
        if (!tFluids.isEmpty()) {
            for (FluidStack tFluid : tFluids) {
                GT_Recipe tRecipe = getFuelMap().findFuel(tFluid);
                if (tRecipe == null) continue;

                FluidStack tLiquid = tFluid.copy();
                fuelConsumption = tLiquid.amount = boostEu ? (getBoostFactor() * getNominalOutput() / tRecipe.mSpecialValue) : (getNominalOutput() / tRecipe.mSpecialValue); //Calc fuel consumption
                //Deplete that amount
                if (!depleteInput(tLiquid)) return false;
                boostEu = depleteInput(getBooster().getGas(2L * getAdditiveFactor()));

                //Deplete Lubricant. 1000L should = 1 hour of runtime (if baseEU = 2048)
                if ((mRuntime % 72 == 0 || mRuntime == 0) && !depleteInput(Materials.Lubricant.getFluid((boostEu ? 2L : 1L) * getAdditiveFactor())))
                    return false;

                fuelValue = tRecipe.mSpecialValue;
                //Check to prevent burning HOG without consuming it, if not boosted
                if (!boostEu && fuelValue > getNominalOutput()) { return false; }
                fuelRemaining = tFluid.amount; //Record available fuel
                this.mEUt = mEfficiency < 2000 ? 0 : getNominalOutput(); //Output 0 if startup is less than 20%
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                this.mEfficiencyIncrease = getEfficiencyIncrease();
                return true;
            }
        }
        this.mEUt = 0;
        this.mEfficiency = 0;
        return false;
    }


    @Override
    public IStructureDefinition<GT_MetaTileEntity_DieselEngine> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 1) && !mMufflerHatches.isEmpty() && mMaintenanceHatches.size() == 1;
    }

    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    public byte getCasingMeta() {
        return 2;
    }

    public Block getIntakeBlock() {
        return GregTech_API.sBlockCasings4;
    }

    public byte getIntakeMeta() {
        return 13;
    }

    public Block getGearboxBlock() {
        return GregTech_API.sBlockCasings2;
    }

    public byte getGearboxMeta() {
        return 4;
    }

    public byte getCasingTextureIndex() {
        return 50;
    }

    private boolean addToMachineList(IGregTechTileEntity tTileEntity) {
        return ((addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())) || (addInputToMachineList(tTileEntity, getCasingTextureIndex())) || (addOutputToMachineList(tTileEntity, getCasingTextureIndex())) || (addMufflerToMachineList(tTileEntity, getCasingTextureIndex())));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DieselEngine(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return boostEu ? 30000 : 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionLargeCombustionEnginePerSecond;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction=0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction=Math.max(tHatch.calculatePollutionReduction(100),mPollutionReduction);
            }
        }

        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }


        return new String[]{
                EnumChatFormatting.BLUE + "Diesel Engine" + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " +
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(maxEnergy) + EnumChatFormatting.RESET + " EU",
                getIdealStatus() == getRepairStatus() ?
                        EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false") + EnumChatFormatting.RESET :
                        EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true") + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.engine.output") + ": " +
                        EnumChatFormatting.RED + GT_Utility.formatNumbers((mEUt*mEfficiency/10000)) + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.engine.consumption") + ": "  +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(fuelConsumption) + EnumChatFormatting.RESET + " L/t",
                StatCollector.translateToLocal("GT5U.engine.value") + ": " +
                        EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(fuelValue) + EnumChatFormatting.RESET + " EU/L",
                StatCollector.translateToLocal("GT5U.turbine.fuel") + ": " +
                        EnumChatFormatting.GOLD + GT_Utility.formatNumbers(fuelRemaining) + EnumChatFormatting.RESET + " L",
                StatCollector.translateToLocal("GT5U.engine.efficiency") + ": " +
                        EnumChatFormatting.YELLOW + (mEfficiency/100F) + EnumChatFormatting.YELLOW + " %",
                StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " +
                        EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"

        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 1);
    }
}
