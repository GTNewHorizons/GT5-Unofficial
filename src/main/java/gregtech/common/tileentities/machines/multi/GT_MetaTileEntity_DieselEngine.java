package gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.input.Keyboard;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
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

public class GT_MetaTileEntity_DieselEngine extends GT_MetaTileEntity_MultiBlockBase {
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

    public String[] getDescription() {
    	final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType("Combustion Generator")
		.addInfo("Controller block for the Large Combustion Engine")
		.addInfo("Supply Diesel Fuels and 1000L of Lubricant per hour to run")
		.addInfo("Supply 40L/s of Oxygen to boost output (optional)")
		.addInfo("Default: Produces 2048EU/t at 100% fuel efficiency")
		.addInfo("Boosted: Produces 6144EU/t at 150% fuel efficiency")
		.addInfo("You need to wait for it to reach 300% to output full power")
		.addPollutionAmount(20 * getPollutionPerTick(null))
		.addSeparator()
		.beginStructureBlock(3, 3, 4, false)
		.addController("Front center")
		.addCasingInfo("Stable Titanium Machine Casing", 16)
		.addOtherStructurePart("Titanium Gear Box Machine Casing", "Inner 2 blocks")
		.addOtherStructurePart("Engine Intake Machine Casing", "8x, ring around controller")
		.addStructureInfo("Engine Intake Casings must not be obstructed in front (only air blocks)")
		.addDynamoHatch("Back center")
		.addMaintenanceHatch("One of the casings next to a Gear Box")
		.addMufflerHatch("Top middle back, above the rear Gear Box")
		.addInputHatch("Diesel Fuel, next to a Gear Box")
		.addInputHatch("Lubricant, next to a Gear Box")
		.addInputHatch("Oxygen, optional, next to a Gear Box")
		.toolTipFinisher("Gregtech");
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return tt.getInformation();
		} else {
			return tt.getStructureInformation();
		}
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][50], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][50]};
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

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
        Collection<GT_Recipe> tRecipeList = getFuelMap().mRecipeList;

        if(tFluids.size() > 0 && tRecipeList != null) { //Does input hatch have a diesel fuel?
            for (FluidStack hatchFluid1 : tFluids) { //Loops through hatches
                for(GT_Recipe aFuel : tRecipeList) { //Loops through diesel fuel recipes
                    FluidStack tLiquid;
                    if ((tLiquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null) { //Create fluidstack from current recipe
                        if (hatchFluid1.isFluidEqual(tLiquid)) { //Has a diesel fluid
                            fuelConsumption = tLiquid.amount = boostEu ? (getBoostFactor() * getNominalOutput() / aFuel.mSpecialValue) : (getNominalOutput() / aFuel.mSpecialValue); //Calc fuel consumption
                            if(depleteInput(tLiquid)) { //Deplete that amount
                                boostEu = depleteInput(getBooster().getGas(2L * getAdditiveFactor()));

                                if(tFluids.contains(Materials.Lubricant.getFluid(1L))) { //Has lubricant?
                                    //Deplete Lubricant. 1000L should = 1 hour of runtime (if baseEU = 2048)
                                    if(mRuntime % 72 == 0 || mRuntime == 0) depleteInput(Materials.Lubricant.getFluid((boostEu ? 2L : 1L) * getAdditiveFactor()));
                                } else return false;

                                fuelValue = aFuel.mSpecialValue;
                                fuelRemaining = hatchFluid1.amount; //Record available fuel
                                this.mEUt = mEfficiency < 2000 ? 0 : getNominalOutput(); //Output 0 if startup is less than 20%
                                this.mProgresstime = 1;
                                this.mMaxProgresstime = 1;
                                this.mEfficiencyIncrease = getEfficiencyIncrease();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        this.mEUt = 0;
        this.mEfficiency = 0;
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        byte tSide = getBaseMetaTileEntity().getBackFacing();
        int tX = getBaseMetaTileEntity().getXCoord();
        int tY = getBaseMetaTileEntity().getYCoord();
        int tZ = getBaseMetaTileEntity().getZCoord();

        if(getBaseMetaTileEntity().getBlockAtSideAndDistance(tSide, 1) != getGearboxBlock() && getBaseMetaTileEntity().getBlockAtSideAndDistance(tSide, 2) != getGearboxBlock()) {
            return false;
        }
        if(getBaseMetaTileEntity().getMetaIDAtSideAndDistance(tSide, 1) != getGearboxMeta() && getBaseMetaTileEntity().getMetaIDAtSideAndDistance(tSide, 2) != getGearboxMeta()) {
            return false;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i != 0) || (j != 0)) {
                    for (int k = 0; k < 4; k++) {

                        final int fX = tX - (tSide == 5 ? 1 : tSide == 4 ? -1 : i),
                                  fZ = tZ - (tSide == 2 ? -1 : tSide == 3 ? 1 : i),
                                  aY = tY + j,
                                  aX = tX + (tSide == 5 ? k : tSide == 4 ? -k : i),
                                  aZ = tZ + (tSide == 2 ? -k : tSide == 3 ? k : i);

                        final Block frontAir = getBaseMetaTileEntity().getBlock(fX, aY, fZ);
                        final String frontAirName = frontAir.getUnlocalizedName();
                        if(!(getBaseMetaTileEntity().getAir(fX, aY, fZ) || frontAirName.equalsIgnoreCase("tile.air") || frontAirName.equalsIgnoreCase("tile.railcraft.residual.heat"))) {
                            return false; //Fail if vent blocks are obstructed
                        }

                        if (((i == 0) || (j == 0)) && ((k == 1) || (k == 2))) {
                            if (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta()) {
                                // Do nothing
                            } else if (!addMufflerToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? 2 : tSide == 4 ? -2 : 0), tY + 1, tZ + (tSide == 3 ? 2 : tSide == 2 ? -2 : 0)), getCasingTextureIndex())) {
                                return false; //Fail if no muffler top middle back
                            } else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ))) {
                                return false;
                            }
                        } else if (k == 0) {
                          if(!(getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getIntakeBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getIntakeMeta())) {
                              return false;
                          }
                        } else if (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta()) {
                            // Do nothing
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        this.mDynamoHatches.clear();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 3);
        if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
            if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Dynamo)) {
                this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) tTileEntity.getMetaTileEntity());
                ((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).updateTexture(getCasingTextureIndex());
            } else {
                return false;
            }
        }
        return true;
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

    public int getMaxEfficiency(ItemStack aStack) {
        return boostEu ? 30000 : 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 24;
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
                EnumChatFormatting.BLUE+"Diesel Engine"+EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.energy")+": " +
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+
                EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU",
                getIdealStatus() == getRepairStatus() ?
                EnumChatFormatting.GREEN+StatCollector.translateToLocal("GT5U.turbine.maintenance.false")+EnumChatFormatting.RESET :
                EnumChatFormatting.RED+StatCollector.translateToLocal("GT5U.turbine.maintenance.true")+EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.engine.output")+": " +EnumChatFormatting.RED+(-mEUt*mEfficiency/10000)+EnumChatFormatting.RESET+" EU/t",
                StatCollector.translateToLocal("GT5U.engine.consumption")+": " +EnumChatFormatting.YELLOW+fuelConsumption+EnumChatFormatting.RESET+" L/t",
                StatCollector.translateToLocal("GT5U.engine.value")+": " +EnumChatFormatting.YELLOW+fuelValue+EnumChatFormatting.RESET+" EU/L",
                StatCollector.translateToLocal("GT5U.turbine.fuel")+": " +EnumChatFormatting.GOLD+fuelRemaining+EnumChatFormatting.RESET+" L",
                StatCollector.translateToLocal("GT5U.engine.efficiency")+": " +EnumChatFormatting.YELLOW+(mEfficiency/100F)+EnumChatFormatting.YELLOW+" %",
                StatCollector.translateToLocal("GT5U.multiblock.pollution")+": " + EnumChatFormatting.GREEN + mPollutionReduction+ EnumChatFormatting.RESET+" %"

        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }
}
