package bartworks.common.tileentities.multis.mega;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import bartworks.util.BWTooltipReference;
import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchEnergyDebug;

public abstract class MegaMultiBlockBase<T extends MegaMultiBlockBase<T>> extends MTEExtendedPowerMultiBlockBase<T> {

    protected MegaMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MegaMultiBlockBase(String aName) {
        super(aName);
    }

    protected String[] getExtendedInfoData() {
        return GTValues.emptyStringArray;
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
        long[] ttHatches = this.getCurrentInfoData();
        long storedEnergy = ttHatches[0];
        long maxEnergy = ttHatches[1];

        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
            if (tHatch instanceof MTEHatchEnergyDebug debugHatch) {
                storedEnergy = debugHatch.getEUVar();
                maxEnergy = debugHatch.maxEUStore();
                break;
            }
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
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.Progress.fmt.s",
                formatNumber(this.mProgresstime / 20),
                formatNumber(this.mMaxProgresstime / 20)),
            IGregTechDeviceInformation
                .encode("GT5U.multiblock.energy.fmt", formatNumber(storedEnergy), formatNumber(maxEnergy)),
            IGregTechDeviceInformation.encode("GT5U.multiblock.usage.fmt", formatNumber(-this.lEUt)),
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.mei.fmt.mega",
                formatNumber(this.getMaxInputVoltage()),
                formatNumber(this.getMaxInputAmps()),
                formatNumber(nominalV)),
            IGregTechDeviceInformation.encode("GT5U.machines.tier.fmt", tName),
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.problems.efficiency.fmt",
                this.getIdealStatus() - this.getRepairStatus(),
                this.mEfficiency / 100.0F + " %"),
            IGregTechDeviceInformation.encode("GT5U.multiblock.pollution.fmt", getAveragePollutionPercentage()),
            IGregTechDeviceInformation.encode("GT5U.multiblock.recipesDone.fmt", formatNumber(recipesDone)) };

        String[] combinedInfo = Arrays.copyOf(baseInfo, baseInfo.length + extendedInfo.length + 1);

        System.arraycopy(extendedInfo, 0, combinedInfo, baseInfo.length, extendedInfo.length);

        combinedInfo[combinedInfo.length - 1] = BWTooltipReference.BW;

        return combinedInfo;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(this.getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
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
        public boolean couldBeValid(T o, World world, int x, int y, int z, ItemStack trigger) {
            return check(o, world, x, y, z);
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
