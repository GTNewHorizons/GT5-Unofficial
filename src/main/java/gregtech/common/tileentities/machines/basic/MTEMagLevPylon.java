package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.loaders.misc.GTPotions;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;

@EventBusSubscriber
public class MTEMagLevPylon extends MTETieredMachineBlock {

    public int mRange = 16;
    private final static Int2BooleanOpenHashMap playerBuffMap = new Int2BooleanOpenHashMap();

    private AxisAlignedBB checkArea;

    public MTEMagLevPylon(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Grants flight with the power of magnets. Range: ");
    }

    public MTEMagLevPylon(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {
        checkArea = AxisAlignedBB
            .getBoundingBox(
                baseMetaTileEntity.getXCoord(),
                baseMetaTileEntity.getYCoord(),
                baseMetaTileEntity.getZCoord(),
                baseMetaTileEntity.getXCoord() + 1,
                baseMetaTileEntity.getYCoord() + 1,
                baseMetaTileEntity.getZCoord() + 1)
            .expand(mRange, mRange, mRange);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (baseMetaTileEntity.isServerSide() && baseMetaTileEntity.isAllowedToWork()) {
            if (tick % 100 == 0 && checkArea != null) {
                List<EntityPlayer> players = getBaseMetaTileEntity().getWorld()
                    .getEntitiesWithinAABB(EntityPlayer.class, checkArea);
                for (int i = 0; i < players.size(); i++) {
                    EntityPlayer player = players.get(i);
                    if (player instanceof FakePlayer) continue;

                    player.addPotionEffect(new PotionEffect(GTPotions.potionMagLev.id, TickTime.SECOND * 6));
                }
            }
        }
    }

    @Override
    public void onRemoval() {
        checkArea = null;
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
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof FakePlayer) return;
        if (!(event.entityLiving instanceof EntityPlayer player)) return;

        int ownerUUID = player.getUniqueID()
            .hashCode();
        if (player.isPotionActive(GTPotions.potionMagLev)) {
            if (!playerBuffMap.containsKey(ownerUUID)) {
                playerBuffMap.put(ownerUUID, true);
            } else playerBuffMap.replace(ownerUUID, true);
            player.capabilities.allowFlying = true;
        } else {
            if (playerBuffMap.replace(ownerUUID, false)) {
                if (!player.capabilities.isCreativeMode) {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                    player.sendPlayerAbilities();
                }
            }
        }
    }
}
