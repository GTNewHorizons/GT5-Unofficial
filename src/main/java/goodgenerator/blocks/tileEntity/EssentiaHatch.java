package goodgenerator.blocks.tileEntity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import goodgenerator.crossmod.thaumcraft.LargeEssentiaEnergyData;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class EssentiaHatch extends TileEntity implements IAspectContainer, IEssentiaTransport {

    private Aspect mLocked;
    private AspectList current = new AspectList();
    public int mState = 0;

    public void setLockedAspect(Aspect aAspect) {
        this.mLocked = aAspect;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        this.mLocked = Aspect.getAspect(tagCompound.getString("mLocked"));
        this.mState = tagCompound.getInteger("mState");
        current = new AspectList();
        NBTTagList tlist = tagCompound.getTagList("Aspects", 10);
        for (int j = 0; j < tlist.tagCount(); ++j) {
            NBTTagCompound rs = tlist.getCompoundTagAt(j);
            if (rs.hasKey("key")) {
                current.add(Aspect.getAspect(rs.getString("key")), rs.getInteger("amount"));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setString("mLocked", this.mLocked == null ? "" : this.mLocked.getTag());
        tagCompound.setInteger("mState", mState);
        NBTTagList tlist = new NBTTagList();
        Aspect[] aspectA = current.getAspects();
        for (Aspect aspect : aspectA) {
            if (aspect != null) {
                NBTTagCompound f = new NBTTagCompound();
                f.setString("key", aspect.getTag());
                f.setInteger("amount", current.getAmount(aspect));
                tlist.appendTag(f);
            }
        }
        tagCompound.setTag("Aspects", tlist);
    }

    public final Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();
        readFromNBT(nbt);
    }

    public void markDirty() {
        super.markDirty();
        if (this.worldObj.isRemote) {
            return;
        }
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void updateEntity() {
        fillfrompipe();
    }

    public void fillfrompipe() {
        if (getEssentiaAmount(null) >= 1000) return;
        TileEntity[] te = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            te[i] = ThaumcraftApiHelper.getConnectableTile(
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    ForgeDirection.VALID_DIRECTIONS[i]);
            if (te[i] != null) {
                IEssentiaTransport pipe = (IEssentiaTransport) te[i];
                if (!pipe.canOutputTo(ForgeDirection.VALID_DIRECTIONS[i])) {
                    return;
                }
                if ((pipe.getEssentiaType(ForgeDirection.VALID_DIRECTIONS[i].getOpposite()) != null)
                        && (pipe.getSuctionAmount(ForgeDirection.VALID_DIRECTIONS[i])
                                < getSuctionAmount(ForgeDirection.VALID_DIRECTIONS[i]))) {
                    Aspect readyInput = pipe.getEssentiaType(ForgeDirection.VALID_DIRECTIONS[i].getOpposite());
                    int type = LargeEssentiaEnergyData.getAspectTypeIndex(readyInput);
                    if (type != -1 && (mState & (1 << type)) == 0) continue;
                    if (readyInput.equals(mLocked)) {
                        addToContainer(mLocked, pipe.takeEssentia(mLocked, 1, ForgeDirection.VALID_DIRECTIONS[i]));
                    }
                    if (mLocked == null) addToContainer(
                            pipe.getEssentiaType(ForgeDirection.VALID_DIRECTIONS[i]),
                            pipe.takeEssentia(
                                    pipe.getEssentiaType(ForgeDirection.VALID_DIRECTIONS[i]),
                                    1,
                                    ForgeDirection.VALID_DIRECTIONS[i]));
                }
            }
        }
    }

    @Override
    public AspectList getAspects() {
        return current;
    }

    @Override
    public void setAspects(AspectList aspectList) {
        this.current.add(aspectList);
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        int type = LargeEssentiaEnergyData.getAspectTypeIndex(aspect);
        if (type != -1 && (mState & (1 << type)) == 0) return false;
        return (mLocked == null || mLocked.equals(aspect)) && getEssentiaAmount(null) <= 1000;
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        int type = LargeEssentiaEnergyData.getAspectTypeIndex(aspect);
        if (type != -1 && (mState & (1 << type)) == 0) return i;
        int ready = Math.min(1000 - getEssentiaAmount(null), i);
        if ((mLocked == null || mLocked.equals(aspect)) && ready > 0) {
            current.add(aspect, ready);
            this.markDirty();
            return i - ready;
        }
        this.markDirty();
        return i;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return current.aspects.containsKey(aspect) && i <= current.getAmount(aspect);
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        ArrayList<Boolean> ret = new ArrayList<Boolean>();
        for (Aspect a : aspectList.aspects.keySet()) ret.add(current.aspects.containsKey(a));
        return !ret.contains(false);
    }

    @Override
    public int containerContains(Aspect aspect) {
        return current.aspects.containsKey(aspect) ? current.getAmount(aspect) : 0;
    }

    @Override
    public boolean isConnectable(ForgeDirection forgeDirection) {
        return true;
    }

    @Override
    public boolean canInputFrom(ForgeDirection forgeDirection) {
        return true;
    }

    @Override
    public boolean canOutputTo(ForgeDirection forgeDirection) {
        return false;
    }

    @Override
    public void setSuction(Aspect aspect, int i) {}

    @Override
    public Aspect getSuctionType(ForgeDirection forgeDirection) {
        return this.mLocked;
    }

    @Override
    public int getSuctionAmount(ForgeDirection forgeDirection) {
        return 256;
    }

    @Override
    public int takeEssentia(Aspect aspect, int i, ForgeDirection forgeDirection) {
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int i, ForgeDirection forgeDirection) {
        return i - addToContainer(aspect, i);
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection forgeDirection) {
        if (current == null || current.size() < 1) return null;
        return current.getAspects()[0];
    }

    @Override
    public int getEssentiaAmount(ForgeDirection forgeDirection) {
        int ret = 0;
        for (final Aspect A : current.aspects.keySet()) {
            ret += current.getAmount(A);
        }
        return ret;
    }

    @Override
    public int getMinimumSuction() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean renderExtendedTube() {
        return true;
    }
}
