package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;

@EventBusSubscriber
public class MTEMagLevPylon extends MTETieredMachineBlock {

    public int mRange = 16;

    private static final WeakHashMap<EntityPlayer, PlayerFlightData> playerMap = new WeakHashMap<>();
    private static final WeakHashMap<MTEMagLevPylon, Integer> machineMap = new WeakHashMap<>();

    public MTEMagLevPylon(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Grants flight with the power of magnets. Range: ");
    }

    public MTEMagLevPylon(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {
        machineMap.put(this, mRange);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (baseMetaTileEntity.isAllowedToWork() && baseMetaTileEntity.isServerSide()) {
            if (tick % 100 == 0 && !machineMap.containsKey(this)) {
                machineMap.put(this, mRange);
            }
        }
    }

    @Override
    public void onRemoval() {
        machineMap.remove(this);
    }

    // TODO: New Texture
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (facing != ForgeDirection.UP) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        if (active) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_TELEPORTER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_ACTIVE_GLOW)
                .glow()
                .build() };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_TELEPORTER),
            TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_GLOW)
                .glow()
                .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMagLevPylon(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 512L;
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 50;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 2;
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
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }
        if (event.player instanceof FakePlayer) return;

    }

    private static class PlayerFlightData {

        private final WeakReference<EntityPlayer> player;
        private int checkTick = 0;

        public PlayerFlightData(EntityPlayer player) {
            this.player = new WeakReference<>(player);
        }
    }
}
