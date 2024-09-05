package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.AuthorColen;
import static gregtech.api.enums.GTValues.V;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEWirelessDynamo extends MTEHatchDynamo implements IWirelessEnergyHatchInformation {

    private UUID owner_uuid;

    public MTEWirelessDynamo(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public MTEWirelessDynamo(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] { "" });
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[mTier] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[mTier] };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return false;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 2 * V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return totalStorage(V[mTier]);
    }

    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Stores energy globally in a network, up to 2^(2^31) EU.",
            EnumChatFormatting.GRAY + "Does not connect to wires. This block accepts EU into the network.",
            AuthorColen };
    }

    @Override
    public long maxAmperesOut() {
        return 2;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.WIRELESS;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWirelessDynamo(mName, mTier, new String[] { "" }, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // On first tick find the player name and attempt to add them to the map.
            if (aTick == 1) {

                // UUID and username of the owner.
                owner_uuid = aBaseMetaTileEntity.getOwnerUuid();

                strongCheckOrAddUser(owner_uuid);
            }

            // Every ticks_between_energy_addition ticks change the energy content of the machine.
            if (aTick % ticks_between_energy_addition == 0L) {
                addEUToGlobalEnergyMap(owner_uuid, getEUVar());
                setEUVar(0L);
            }
        }
    }
}
