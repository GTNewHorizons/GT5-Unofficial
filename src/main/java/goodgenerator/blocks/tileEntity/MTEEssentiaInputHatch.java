package goodgenerator.blocks.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import net.minecraft.tileentity.TileEntity;

public class MTEEssentiaInputHatch extends TileThaumcraft implements IAspectContainer, IEssentiaTransport {

    public static final int CAPACITY = 512;
    public AspectList mAspects = new AspectList();
    private int count = 0;

    public MTEEssentiaInputHatch() {
        super();
    }

    public void clear() {
        this.mAspects = new AspectList();
        this.markDirty();
    }


    @Override
    public void markDirty() {
        super.markDirty();
        if (this.worldObj != null && !this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
        this.mAspects.readFromNBT(nbt);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
        this.mAspects.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.mAspects.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.mAspects.writeToNBT(nbt);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote && ++this.count % 5 == 0) {
            fillFromPipes();
        }
    }

    private void fillFromPipes() {
        if (this.mAspects.visSize() >= CAPACITY) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
            if (te instanceof IEssentiaTransport) {
                IEssentiaTransport ic = (IEssentiaTransport) te;
                if (!ic.canOutputTo(dir.getOpposite())) continue;

                Aspect ta = ic.getEssentiaType(dir.getOpposite());
                if (ta != null && this.doesContainerAccept(ta)) {
                    if (ic.getSuctionAmount(dir.getOpposite()) < this.getSuctionAmount(dir)) {
                        int taken = ic.takeEssentia(ta, 1, dir.getOpposite());
                        if (taken > 0) {
                            this.addEssentia(ta, taken, dir);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isConnectable(ForgeDirection face) { return true; }

    @Override
    public boolean canInputFrom(ForgeDirection face) { return true; }

    @Override
    public boolean canOutputTo(ForgeDirection face) { return true; }

    @Override
    public void setSuction(Aspect aspect, int amount) {}

    @Override
    public Aspect getSuctionType(ForgeDirection face) {
        return null;
    }

    @Override
    public int getSuctionAmount(ForgeDirection face) {
        return this.mAspects.visSize() < CAPACITY ? 128 : 0;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
        int has = this.mAspects.getAmount(aspect);
        int toTake = Math.min(amount, has);
        if (toTake > 0) {
            this.mAspects.remove(aspect, toTake);
            this.markDirty();
            return toTake;
        }
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
        return amount - this.addToContainer(aspect, amount);
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection face) {
        return null;
    }

    @Override
    public int getEssentiaAmount(ForgeDirection face) {
        return this.mAspects.visSize();
    }

    @Override
    public int getMinimumSuction() { return 0; }

    @Override
    public boolean renderExtendedTube() { return false; }

    @Override
    public AspectList getAspects() { return this.mAspects; }

    @Override
    public void setAspects(AspectList aspectList) {
        this.mAspects = aspectList.copy();
        this.markDirty();
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return this.mAspects.visSize() < CAPACITY;
    }

    @Override
    public int addToContainer(Aspect aspect, int amount) {
        if (amount <= 0) return 0;

        int current = this.mAspects.visSize();
        int spaceLeft = CAPACITY - current;
        if (spaceLeft <= 0) return amount;

        int toAdd = Math.min(amount, spaceLeft);
        this.mAspects.add(aspect, toAdd);
        this.markDirty();

        return amount - toAdd;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        if (this.mAspects.getAmount(aspect) >= amount) {
            this.mAspects.remove(aspect, amount);
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspects) {
        if (!this.doesContainerContain(aspects)) return false;
        for (Aspect aspect : aspects.getAspects()) {
            this.mAspects.remove(aspect, aspects.getAmount(aspect));
        }
        this.markDirty();
        return true;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return this.mAspects.getAmount(aspect) >= amount;
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        for (Aspect aspect : aspectList.getAspects()) {
            if (this.mAspects.getAmount(aspect) < aspectList.getAmount(aspect)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return this.mAspects.getAmount(aspect);
    }
}
