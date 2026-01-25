package bartworks.common.tileentities.multis.mega;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.validMTEList;

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
import gregtech.api.enums.GTValues;
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
