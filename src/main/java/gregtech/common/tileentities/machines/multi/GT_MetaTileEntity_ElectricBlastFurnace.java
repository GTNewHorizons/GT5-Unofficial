package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;

public class GT_MetaTileEntity_ElectricBlastFurnace extends GT_MetaTileEntity_AbstractMultiFurnace<GT_MetaTileEntity_ElectricBlastFurnace> implements IConstructable {
    private int mHeatingCapacity = 0;
    private final ArrayList<GT_MetaTileEntity_Hatch_Output> mPollutionOutputHatches = new ArrayList<>();
    private final FluidStack[] pollutionFluidStacks = {Materials.CarbonDioxide.getGas(1000),
            Materials.CarbonMonoxide.getGas(1000), Materials.SulfurDioxide.getGas(1000)};

    private static final int CASING_INDEX = 11;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_ElectricBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_ElectricBlastFurnace>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][]{
                    {"ttt", "tmt", "ttt"},
                    {"CCC", "C-C", "CCC",},
                    {"CCC", "C-C", "CCC",},
                    {"b~b", "bbb", "bbb"}
            }))
            .addElement('t', ofHatchAdderOptional(GT_MetaTileEntity_ElectricBlastFurnace::addOutputHatchToTopList, CASING_INDEX, 1, GregTech_API.sBlockCasings1, CASING_INDEX))
            .addElement('m', ofHatchAdder(GT_MetaTileEntity_ElectricBlastFurnace::addMufflerToMachineList, CASING_INDEX, 2))
            .addElement('C', ofBlockAdder(GT_MetaTileEntity_ElectricBlastFurnace::addCoil, GregTech_API.sBlockCasings5, 0))
            .addElement('b', ofHatchAdderOptional(GT_MetaTileEntity_ElectricBlastFurnace::addBottomHatch, CASING_INDEX, 3, GregTech_API.sBlockCasings1, CASING_INDEX))
            .build();

    public GT_MetaTileEntity_ElectricBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ElectricBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public GT_MetaTileEntity_ElectricBlastFurnace newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ElectricBlastFurnace(this.mName);
    }

    @Override
    public String[] getDescription() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Blast Furnace")
                .addInfo("Controller block for the Electric Blast Furnace")
                .addInfo("You can use some fluids to reduce recipe time. Place the circuit in the Input Bus")
                .addInfo("Each 900K over the min. Heat required multiplies EU/t by 0.95")
                .addInfo("Each 1800K over the min. Heat required allows for one upgraded overclock instead of normal")
                .addInfo("Upgraded overclocks reduce recipe time to 25% (instead of 50%) and increase EU/t to 400%")
                .addInfo("Additionally gives +100K for every tier past MV")
                .addPollutionAmount(20 * getPollutionPerTick(null))
                .addSeparator()
                .beginStructureBlock(3, 4, 3, true)
                .addController("Front bottom")
                .addCasingInfo("Heat Proof Machine Casing", 0)
                .addOtherStructurePart("Heating Coils", "Two middle Layers")
                .addEnergyHatch("Any bottom layer casing")
                .addMaintenanceHatch("Any bottom layer casing")
                .addMufflerHatch("Top middle")
                .addInputBus("Any bottom layer casing")
                .addInputHatch("Any bottom layer casing")
                .addOutputBus("Any bottom layer casing")
                .addOutputHatch("Gasses, Any top layer casing")
                .addStructureInfo("Recovery amount scales with Muffler Hatch tier")
                .addOutputHatch("Platline fluids, Any bottom layer casing")
                .toolTipFinisher("Gregtech");
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return tt.getStructureInformation();
        } else {
            return tt.getInformation();
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                        casingTexturePages[0][CASING_INDEX],
                        TextureFactory.of(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE),
                        TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW).glow().build()};
            return new ITexture[]{
                    casingTexturePages[0][CASING_INDEX],
                    TextureFactory.of(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW).glow().build()};
        }
        return new ITexture[]{casingTexturePages[0][CASING_INDEX]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ElectricBlastFurnace.png");
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_ElectricBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ItemStack[] tInputs = getCompactedInputs();
        FluidStack[] tFluids = getCompactedFluids();

        if (tInputs.length <= 0)
            return false;

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(
                getBaseMetaTileEntity(),
                false,
                V[tTier],
                tFluids,
                tInputs
        );

        if (tRecipe == null)
            return false;
        if (this.mHeatingCapacity < tRecipe.mSpecialValue)
            return false;
        if (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs))
            return false;

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        int tHeatCapacityDivTiers = (mHeatingCapacity - tRecipe.mSpecialValue) / 900;
        byte overclockCount = calculateOverclockednessEBF(tRecipe.mEUt, tRecipe.mDuration, tVoltage);
        //In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return false;
        if (this.mEUt > 0) {
            this.mEUt = (-this.mEUt);
        }
        if (tHeatCapacityDivTiers > 0) {
            this.mEUt = (int) (this.mEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
            this.mMaxProgresstime >>= Math.min(tHeatCapacityDivTiers / 2, overclockCount);//extra free overclocking if possible
            if (this.mMaxProgresstime < 1)
                this.mMaxProgresstime = 1;//no eu efficiency correction
        }
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        this.mOutputItems = new ItemStack[]{
                tRecipe.getOutput(0),
                tRecipe.getOutput(1)
        };
        this.mOutputFluids = new FluidStack[]{
                tRecipe.getFluidOutput(0)
        };
        updateSlots();
        return true;
    }

    /**
     * Calcualtes overclocked ness using long integers
     *
     * @param aEUt      - recipe EUt
     * @param aDuration - recipe Duration
     */
    protected byte calculateOverclockednessEBF(int aEUt, int aDuration, long maxInputVoltage) {
        byte mTier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage)), timesOverclocked = 0;
        if (mTier == 0) {
            //Long time calculation
            long xMaxProgresstime = ((long) aDuration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                //make impossible if too long
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = aEUt >> 2;
                mMaxProgresstime = (int) xMaxProgresstime;
            }
            //return 0;
        } else {
            //Long EUt calculation
            long xEUt = aEUt;
            //Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            mMaxProgresstime = aDuration;

            while (tempEUt <= V[mTier - 1]) {
                tempEUt <<= 2;//this actually controls overclocking
                //xEUt *= 4;//this is effect of everclocking
                mMaxProgresstime >>= 1;//this is effect of overclocking
                xEUt = mMaxProgresstime == 0 ? xEUt >> 1 : xEUt << 2;//U know, if the time is less than 1 tick make the machine use less power
                timesOverclocked++;
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = (int) xEUt;
                if (mEUt == 0)
                    mEUt = 1;
                if (mMaxProgresstime == 0)
                    mMaxProgresstime = 1;//set time to 1 tick
            }
        }
        return timesOverclocked;
    }

    public boolean addOutputHatchToTopList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mPollutionOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mHeatingCapacity = 0;

        replaceDeprecatedCoils(aBaseMetaTileEntity);

        mCoilLevel = null;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 3, 0))
            return false;

        if (mCoilLevel == null)
            return false;

        this.mHeatingCapacity = (int) mCoilLevel.getHeat() + 100 * (GT_Utility.getTier(getMaxInputVoltage()) - 2);
        return true;
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int tX = aBaseMetaTileEntity.getXCoord() + xDir;
        int tY = aBaseMetaTileEntity.getYCoord();
        int tZ = aBaseMetaTileEntity.getZCoord() + zDir;
        int tUsedMeta;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos++) {
            for (int zPos = tZ - 1; zPos <= tZ + 1; zPos++) {
                if ((xPos == tX) && (zPos == tZ))
                    continue;
                for (int yPos = tY + 1; yPos <= tY + 2; yPos++) {
                    tUsedMeta = aBaseMetaTileEntity.getMetaID(xPos, yPos, zPos);
                    if (tUsedMeta < 12)
                        continue;
                    if (tUsedMeta > 14)
                        continue;
                    if (aBaseMetaTileEntity.getBlock(xPos, yPos, zPos) != GregTech_API.sBlockCasings1)
                        continue;

                    aBaseMetaTileEntity.getWorld().setBlock(
                            xPos,
                            yPos,
                            zPos,
                            GregTech_API.sBlockCasings5,
                            tUsedMeta - 12,
                            3
                    );
                }
            }
        }
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null)
            return false;
        FluidStack tLiquid = aLiquid.copy();
        boolean isOutputPollution = false;
        for (FluidStack pollutionFluidStack : pollutionFluidStacks) {
            if (!tLiquid.isFluidEqual(pollutionFluidStack))
                continue;

            isOutputPollution = true;
            break;
        }
        ArrayList<GT_MetaTileEntity_Hatch_Output> tOutputHatches;
        if (isOutputPollution) {
            tOutputHatches = this.mPollutionOutputHatches;
            int pollutionReduction = 0;
            for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
                if (!isValidMetaTileEntity(tHatch))
                    continue;
                pollutionReduction = 100 - tHatch.calculatePollutionReduction(100);
                break;
            }
            tLiquid.amount = tLiquid.amount * (pollutionReduction + 5) / 100;
        } else {
            tOutputHatches = this.mOutputHatches;
        }
        return dumpFluid(tOutputHatches, tLiquid, true) ||
                dumpFluid(tOutputHatches, tLiquid, false);
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (!isValidMetaTileEntity(tHatch))
                continue;
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (!isValidMetaTileEntity(tHatch))
                continue;
            storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
        }

        return new String[]{
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " + EnumChatFormatting.GREEN + mProgresstime / 20 + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + mMaxProgresstime / 20 + EnumChatFormatting.RESET + " s",
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " + EnumChatFormatting.GREEN + storedEnergy + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET + " EU",
                StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " + EnumChatFormatting.RED + -mEUt + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " + EnumChatFormatting.YELLOW + getMaxInputVoltage() + EnumChatFormatting.RESET + " EU/t(*2A) " + StatCollector.translateToLocal("GT5U.machines.tier") + ": " +
                        EnumChatFormatting.YELLOW + VN[GT_Utility.getTier(getMaxInputVoltage())] + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " +
                        EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " " + StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " +
                        EnumChatFormatting.YELLOW + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.EBF.heat") + ": " +
                        EnumChatFormatting.GREEN + mHeatingCapacity + EnumChatFormatting.RESET + " K",
                StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " + EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1,3,0);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        // TODO implement me
        return new String[0];
    }
}
