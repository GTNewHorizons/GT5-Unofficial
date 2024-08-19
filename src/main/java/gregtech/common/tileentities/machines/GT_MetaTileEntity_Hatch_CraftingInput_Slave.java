package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_CRAFTING_INPUT_SLAVE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Hatch_CraftingInput_Slave extends GT_MetaTileEntity_Hatch_InputBus
    implements IDualInputHatch {

    private GT_MetaTileEntity_Hatch_CraftingInput_ME master; // use getMaster() to access
    private int masterX, masterY, masterZ;
    private boolean masterSet = false; // indicate if values of masterX, masterY, masterZ are valid

    public GT_MetaTileEntity_Hatch_CraftingInput_Slave(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            6,
            0,
            new String[] { "Proxy for Crafting Input Buffer/Bus",
                "Link with Crafting Input Buffer/Bus using Data Stick to share inventory",
                "Left click on the Crafting Input Buffer/Bus, then right click on this block to link them", });
        disableSort = true;
    }

    public GT_MetaTileEntity_Hatch_CraftingInput_Slave(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        disableSort = true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_CraftingInput_Slave(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return getTexturesInactive(aBaseTexture);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_CRAFTING_INPUT_SLAVE) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        if (aTimer % 100 == 0 && masterSet && getMaster() == null) {
            trySetMasterFromCoord(masterX, masterY, masterZ);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        if (aNBT.hasKey("master")) {
            NBTTagCompound masterNBT = aNBT.getCompoundTag("master");
            masterX = masterNBT.getInteger("x");
            masterY = masterNBT.getInteger("y");
            masterZ = masterNBT.getInteger("z");
            masterSet = true;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (masterSet) {
            NBTTagCompound masterNBT = new NBTTagCompound();
            masterNBT.setInteger("x", masterX);
            masterNBT.setInteger("y", masterY);
            masterNBT.setInteger("z", masterZ);
            aNBT.setTag("master", masterNBT);
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        var ret = new ArrayList<String>();
        if (getMaster() != null) {
            ret.add(
                "This bus is linked to the Crafting Input Buffer at " + masterX
                    + ", "
                    + masterY
                    + ", "
                    + masterZ
                    + ".");
            ret.addAll(Arrays.asList(getMaster().getInfoData()));
        } else ret.add("This bus is not linked to any Crafting Input Buffer.");
        return ret.toArray(new String[0]);
    }

    public GT_MetaTileEntity_Hatch_CraftingInput_ME getMaster() {
        if (master == null) return null;
        if (master.getBaseMetaTileEntity() == null) { // master disappeared
            master = null;
        }
        return master;
    }

    @Override
    public byte getTierForStructure() {
        return getMaster() == null ? super.getTierForStructure() : getMaster().getTierForStructure();
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
    public Iterator<GT_MetaTileEntity_Hatch_CraftingInput_ME.PatternSlot> inventories() {
        return getMaster() != null ? getMaster().inventories() : Collections.emptyIterator();
    }

    @Override
    public Optional<IDualInputInventory> getFirstNonEmptyInventory() {
        return getMaster() != null ? getMaster().getFirstNonEmptyInventory() : Optional.empty();
    }

    @Override
    public boolean supportsFluids() {
        return getMaster() != null && getMaster().supportsFluids();
    }

    @Override
    public boolean justUpdated() {
        return getMaster() != null && getMaster().justUpdated();
    }

    public GT_MetaTileEntity_Hatch_CraftingInput_ME trySetMasterFromCoord(int x, int y, int z) {
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return null;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity)) return null;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof GT_MetaTileEntity_Hatch_CraftingInput_ME)) return null;
        masterX = x;
        masterY = y;
        masterZ = z;
        masterSet = true;
        master = (GT_MetaTileEntity_Hatch_CraftingInput_ME) metaTileEntity;
        return master;
    }

    private boolean tryLinkDataStick(EntityPlayer aPlayer) {
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();

        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            return false;
        }
        if (!dataStick.hasTagCompound() || !dataStick.stackTagCompound.getString("type")
            .equals("CraftingInputBuffer")) {
            return false;
        }

        NBTTagCompound nbt = dataStick.stackTagCompound;
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");
        if (trySetMasterFromCoord(x, y, z) != null) {
            aPlayer.addChatMessage(new ChatComponentText("Link successful"));
            return true;
        }
        aPlayer.addChatMessage(new ChatComponentText("Link failed"));
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) {
            return false;
        }
        if (tryLinkDataStick(aPlayer)) {
            return true;
        }
        var master = getMaster();
        if (master != null) {
            return master.onRightclick(master.getBaseMetaTileEntity(), aPlayer);
        }
        return false;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add((tag.getBoolean("linked") ? "Linked" : "Not linked"));

        if (tag.hasKey("masterX")) {
            currenttip.add(
                "Bound to " + tag
                    .getInteger("masterX") + ", " + tag.getInteger("masterY") + ", " + tag.getInteger("masterZ"));
        }

        if (tag.hasKey("masterName")) {
            currenttip.add(EnumChatFormatting.GOLD + tag.getString("masterName") + EnumChatFormatting.RESET);
        }

        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {

        tag.setBoolean("linked", getMaster() != null);
        if (masterSet) {
            tag.setInteger("masterX", masterX);
            tag.setInteger("masterY", masterY);
            tag.setInteger("masterZ", masterZ);
        }
        if (getMaster() != null) tag.setString("masterName", getMaster().getName());

        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public List<ItemStack> getItemsForHoloGlasses() {
        return getMaster() != null ? getMaster().getItemsForHoloGlasses() : null;
    }
}
