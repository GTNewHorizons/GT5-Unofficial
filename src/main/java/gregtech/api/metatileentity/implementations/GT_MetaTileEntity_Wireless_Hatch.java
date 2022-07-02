package gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static gregtech.GT_Mod.gregtechproxy;
import static gregtech.api.enums.GT_Values.*;

public class GT_MetaTileEntity_Wireless_Hatch extends GT_MetaTileEntity_Hatch_Energy implements IGlobalWirelessEnergy {

    static final long ticks_between_energy_addition = 400L;
    static final long number_of_energy_additions = 10L;
    private final long eu_transferred_per_operation = 2L * V[mTier] * ticks_between_energy_addition;
    private String owner_uuid;


    public GT_MetaTileEntity_Wireless_Hatch(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Wireless_Hatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] {""});
    }

    @Override
    public String[] getDescription() {
        String uuid = gregtechproxy.getThePlayer().getUniqueID().toString();
        return new String[] {
        "Receives " + EnumChatFormatting.RED + GT_Utility.formatNumbers(eu_transferred_per_operation/V[mTier]) + EnumChatFormatting.GRAY + " A of " + TIER_COLORS[mTier] + VN[mTier] + EnumChatFormatting.GRAY + " through trans-dimensional space every " + EnumChatFormatting.RED + GT_Utility.formatNumbers(ticks_between_energy_addition) + EnumChatFormatting.GRAY + " ticks.",
            EnumChatFormatting.GRAY + "There is currently " + EnumChatFormatting.RED + GT_Utility.formatNumbers(GlobalEnergyMap.getOrDefault(uuid, 0L)) + EnumChatFormatting.GRAY + " EU in your network."};
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[mTier]};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[mTier]};
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
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
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * number_of_energy_additions * ticks_between_energy_addition;
    }

    public long getEUCapacity() {
        return 40000L;
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {

        return new GT_MetaTileEntity_Wireless_Hatch(mName, mTier, new String[] {""}, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean ownerControl() {
        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // On first tick find the player name and attempt to add them to the map.
            if (aTick == 1) {
                owner_uuid = getBaseMetaTileEntity().getOwnerUuid().toString();

                // If the owner is not in the hash map, add them with 0 EU.
                if (!GlobalEnergyMap.containsKey(owner_uuid)) {
                    GlobalEnergyMap.put(owner_uuid, 100_000_000L);
                }
            }

            // Every ticks_between_energy_addition ticks change the energy content of the block.
            if (aTick % ticks_between_energy_addition == 0L) {

                long total_eu = GlobalEnergyMap.get(owner_uuid); // 100m

                if (total_eu > eu_transferred_per_operation) {
                    GlobalEnergyMap.put(owner_uuid, total_eu - eu_transferred_per_operation);
                    setEUVar(aBaseMetaTileEntity.getStoredEU() + eu_transferred_per_operation);
                } else {
                    GlobalEnergyMap.put(owner_uuid, 0L);
                    setEUVar(aBaseMetaTileEntity.getStoredEU() + total_eu);
                }
            }
        }
    }
}
