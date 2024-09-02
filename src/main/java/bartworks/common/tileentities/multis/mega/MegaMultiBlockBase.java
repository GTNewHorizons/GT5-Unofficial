package bartworks.common.tileentities.multis.mega;

import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import bartworks.util.BWTooltipReference;
import bartworks.util.BWUtil;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.util.GTUtility;

public abstract class MegaMultiBlockBase<T extends MegaMultiBlockBase<T>> extends MTEExtendedPowerMultiBlockBase<T> {

    protected MegaMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MegaMultiBlockBase(String aName) {
        super(aName);
    }

    protected String[] getExtendedInfoData() {
        return new String[0];
    }

    protected long[] getCurrentInfoData() {
        long storedEnergy = 0, maxEnergy = 0;
        for (MTEHatch hatch : this.getExoticAndNormalEnergyHatchList()) {
            storedEnergy += hatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += hatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        return new long[] { storedEnergy, maxEnergy };
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;

        for (MTEHatchMuffler tHatch : filterValidMTEs(mMufflerHatches)) {
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        long[] ttHatches = this.getCurrentInfoData();
        long storedEnergy = ttHatches[0];
        long maxEnergy = ttHatches[1];

        for (MTEHatchEnergy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        long nominalV = this.getMaxInputEu();
        String tName = BWUtil.getTierNameFromVoltage(nominalV);
        if ("MAX+".equals(tName)) tName = EnumChatFormatting.OBFUSCATED + "MAX+";

        String[] extendedInfo = this.getExtendedInfoData();

        String[] baseInfo = {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(this.mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(this.mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(-this.lEUt)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(this.getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + GTUtility.formatNumbers(this.getMaxInputAmps())
                + "A) = "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(nominalV)
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.machines.tier") + ": "
                + EnumChatFormatting.YELLOW
                + tName
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (this.getIdealStatus() - this.getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + this.mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %" };

        String[] combinedInfo = Arrays.copyOf(baseInfo, baseInfo.length + extendedInfo.length + 1);

        System.arraycopy(extendedInfo, 0, combinedInfo, baseInfo.length, extendedInfo.length);

        combinedInfo[combinedInfo.length - 1] = BWTooltipReference.BW;

        return combinedInfo;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(this.getMaxInputEu());
        logic.setAvailableAmperage(1);
    }

    protected static class StructureElementAirNoHint<T> implements IStructureElement<T> {

        private static final StructureElementAirNoHint<?> INSTANCE = new StructureElementAirNoHint<>();

        @SuppressWarnings("unchecked")
        public static <T> IStructureElement<T> getInstance() {
            return (IStructureElement<T>) INSTANCE;
        }

        private StructureElementAirNoHint() {}

        @Override
        public boolean check(T o, World world, int x, int y, int z) {
            return world.isAirBlock(x, y, z);
        }

        @Override
        public boolean spawnHint(T o, World world, int x, int y, int z, ItemStack trigger) {
            if (world.blockExists(x, y, z) && !world.isAirBlock(x, y, z))
                // hint if this is obstructed. in case *someone* ever finish the transparent rendering
                StructureLibAPI
                    .hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), StructureLibAPI.HINT_BLOCK_META_AIR);
            return true;
        }

        @Override
        public boolean placeBlock(T o, World world, int x, int y, int z, ItemStack trigger) {
            world.setBlockToAir(x, y, z);
            return true;
        }

        @Override
        public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
            return BlocksToPlace.createEmpty();
        }

        @Override
        public PlaceResult survivalPlaceBlock(T o, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
            if (this.check(o, world, x, y, z)) return PlaceResult.SKIP;
            if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, env.getActor())) return PlaceResult.REJECT;
            world.setBlock(x, y, z, Blocks.air, 0, 2);
            return PlaceResult.ACCEPT;
        }
    }
}
