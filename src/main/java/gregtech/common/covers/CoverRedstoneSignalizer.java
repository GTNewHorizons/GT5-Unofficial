package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;

@SuppressWarnings("unused") // TODO: Consider re-registering this
public class CoverRedstoneSignalizer extends CoverBehavior {

    CoverRedstoneSignalizer(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int coverDataValue = coverData.get();
        coverDataValue = (coverDataValue + 1) % 48;
        switch (coverDataValue / 16) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("078", "Signal = ") + (coverDataValue & 0xF));
            case 1 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("079", "Conditional Signal = ") + (coverDataValue & 0xF));
            case 2 -> GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.trans("080", "Inverted Conditional Signal = ") + (coverDataValue & 0xF));
        }
        coverData.set(coverDataValue);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public byte getRedstoneInput(byte aInputRedstone) {
        int coverDataValue = coverData.get();
        if (coverDataValue < 16) {
            return (byte) (coverDataValue & 0xF);
        }
        if ((coveredTile.get() instanceof IMachineProgress machine)) {
            if (machine.isAllowedToWork()) {
                if (coverDataValue / 16 == 1) {
                    return (byte) (coverDataValue & 0xF);
                }
            } else if (coverDataValue / 16 == 2) {
                return (byte) (coverDataValue & 0xF);
            }
            return 0;
        }
        return (byte) (coverDataValue & 0xF);
    }
}
