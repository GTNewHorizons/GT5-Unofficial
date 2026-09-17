package gregtech.common.items.tools;

import java.util.function.BooleanSupplier;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;
import thaumcraft.api.aspects.IEssentiaTransport;

/**
 * The essentia half of a plunger, moved out of {@code BehaviourPlungerEssentia}.
 * <p/>
 * Everything here touches Thaumcraft, so callers must check {@code Mods.Thaumcraft.isModLoaded()} before naming this
 * class -- which is what keeps it from being loaded at all when Thaumcraft is absent, exactly as the old behaviour did
 * by only registering itself in that case.
 */
public final class PlungerEssentiaActions {

    private PlungerEssentiaActions() {}

    public static boolean pullEssentia(World world, int x, int y, int z, float hitX, float hitY, float hitZ,
        BooleanSupplier pay) {
        if (world.isRemote) return false;
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IEssentiaTransport transport) || !pay.getAsBoolean()) return false;
        GTUtility.sendSoundToPlayers(world, SoundResource.GTCEU_OP_PLUNGER, 1.0F, 1.0F, hitX, hitY, hitZ);
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            transport
                .takeEssentia(transport.getEssentiaType(direction), transport.getEssentiaAmount(direction), direction);
        }
        return true;
    }
}
