package goodgenerator.blocks.tileEntity;

import static gregtech.api.enums.Textures.BlockIcons.ESSENTIA_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ESSENTIA_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ESSENTIA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public abstract class MTEHatchEssentiaBase extends MTEHatch implements IAspectContainer, IEssentiaTransport {

    protected AspectList contents = new AspectList();

    protected Aspect filter;

    protected MTEHatchEssentiaBase(int id, String name, String nameRegional, int tier, String[] description) {
        super(id, name, nameRegional, tier, 0, description);
    }

    protected MTEHatchEssentiaBase(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, 0, description, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        List<ITexture> textures = new ArrayList<>(3);

        textures.add(baseTexture);

        textures.add(TextureFactory.of(OVERLAY_ESSENTIA));

        if (isEssentiaInput()) textures.add(TextureFactory.of(ESSENTIA_IN_SIGN));
        if (isEssentiaOutput()) textures.add(TextureFactory.of(ESSENTIA_OUT_SIGN));

        return textures.toArray(new ITexture[0]);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return getTexturesActive(baseTexture);
    }

    @Override
    public abstract IMetaTileEntity newMetaEntity(IGregTechTileEntity igte);

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        contents.readFromNBT(aNBT, "contents");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        contents.writeToNBT(aNBT, "contents");
    }

    public boolean canMixEssentia() {
        return true;
    }

    public int getEssentiaCapacity() {
        return 256;
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = super.getDescriptionData();

        contents.writeToNBT(tag, "contents");

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);

        contents.readFromNBT(data, "contents");
    }

    @Override
    public AspectList getAspects() {
        return contents;
    }

    @Override
    public void setAspects(AspectList aspects) {
        contents = aspects;
        getBaseMetaTileEntity().issueTileUpdate();
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        if (canMixEssentia() && !contents.aspects.isEmpty()) return contents.getAmount(aspect) > 0;

        return true;
    }

    @Override
    public int addToContainer(Aspect aspect, int amount) {
        if (!doesContainerAccept(aspect)) return amount;

        int remainingCapacity = getEssentiaCapacity() - contents.visSize();

        int insertable = Math.min(amount, remainingCapacity);

        if (insertable > 0) {
            contents.add(aspect, insertable);

            getBaseMetaTileEntity().issueTileUpdate();
        }

        return amount - insertable;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        if (contents.reduce(aspect, amount)) {
            getBaseMetaTileEntity().issueTileUpdate();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean takeFromContainer(AspectList aspects) {
        for (var e : aspects.aspects.entrySet()) {
            if (this.contents.getAmount(e.getKey()) < e.getValue()) return false;
        }

        for (var e : aspects.aspects.entrySet()) {
            this.contents.reduce(e.getKey(), e.getValue());
        }

        getBaseMetaTileEntity().issueTileUpdate();

        return true;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return contents.getAmount(aspect) >= amount;
    }

    @Override
    public boolean doesContainerContain(AspectList aspects) {
        for (var e : aspects.aspects.entrySet()) {
            if (this.contents.getAmount(e.getKey()) < e.getValue()) return false;
        }

        return true;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return contents.getAmount(aspect);
    }

    @Override
    public boolean isConnectable(ForgeDirection dir) {
        if (getBaseMetaTileEntity() != null) {
            return dir == getBaseMetaTileEntity().getFrontFacing() && (isEssentiaInput() || isEssentiaOutput());
        }

        return false;
    }

    public abstract boolean isEssentiaOutput();

    public abstract boolean isEssentiaInput();

    protected boolean isFront(ForgeDirection dir) {
        if (getBaseMetaTileEntity() != null) {
            return dir == getBaseMetaTileEntity().getFrontFacing();
        }

        return false;
    }

    @Override
    public boolean canInputFrom(ForgeDirection dir) {
        return isFront(dir) && isEssentiaInput();
    }

    @Override
    public boolean canOutputTo(ForgeDirection dir) {
        return isFront(dir) && isEssentiaOutput();
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {
        // do nothing
    }

    @Override
    public Aspect getSuctionType(ForgeDirection dir) {
        if (!isFront(dir)) return null;

        return canMixEssentia() || !isEssentiaInput() ? null : contents.getAspects()[0];
    }

    @Override
    public int getSuctionAmount(ForgeDirection dir) {
        if (!isFront(dir)) return 0;

        return isEssentiaInput() ? 64 : 0;
    }

    public int takeEssentia(Aspect aspect, int amount) {
        int removable = Math.min(amount, contents.getAmount(aspect));

        if (removable > 0) {
            contents.remove(aspect, removable);

            getBaseMetaTileEntity().issueTileUpdate();
        }

        return removable;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, ForgeDirection dir) {
        if (!isFront(dir) || !isEssentiaOutput()) return 0;

        int removable = Math.min(amount, contents.getAmount(aspect));

        if (removable > 0) {
            contents.remove(aspect, removable);

            getBaseMetaTileEntity().issueTileUpdate();
        }

        return removable;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, ForgeDirection dir) {
        if (!isFront(dir) || !isEssentiaInput()) return amount;

        return addToContainer(aspect, amount);
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection dir) {
        if (!isFront(dir)) return null;

        return contents.getAspects()[0];
    }

    @Override
    public int getEssentiaAmount(ForgeDirection dir) {
        if (!isFront(dir)) return 0;

        return contents.visSize();
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return false;
    }

    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity igte, long tick) {
        super.onPostTick(igte, tick);

        if (tick % 5 == 0) {
            if (isEssentiaInput()) {
                ForgeDirection inputSide = igte.getFrontFacing();
                ForgeDirection outputSide = igte.getFrontFacing()
                    .getOpposite();

                TileEntity te = ThaumcraftApiHelper.getConnectableTile(
                    igte.getWorld(),
                    igte.getXCoord(),
                    igte.getYCoord(),
                    igte.getZCoord(),
                    inputSide);

                if (te != null) {
                    IEssentiaTransport ic = (IEssentiaTransport) te;

                    if (!ic.canOutputTo(outputSide)) {
                        return;
                    }

                    Aspect aspect = null;

                    if (this.filter != null) {
                        aspect = this.filter;
                    } else if (!canMixEssentia() && contents.size() > 0) {
                        aspect = this.getEssentiaType(inputSide);
                    } else if (ic.getEssentiaAmount(outputSide) > 0
                        && ic.getSuctionAmount(outputSide) < this.getSuctionAmount(inputSide)
                        && this.getSuctionAmount(inputSide) >= ic.getMinimumSuction()) {
                            aspect = ic.getEssentiaType(outputSide);
                        }

                    if (aspect != null && ic.getSuctionAmount(outputSide) < this.getSuctionAmount(inputSide)) {
                        int remaining = getEssentiaCapacity() - contents.visSize();

                        if (remaining > 0) {
                            this.addToContainer(aspect, ic.takeEssentia(aspect, remaining, outputSide));
                        }
                    }
                }
            }

            if (isEssentiaOutput()) {
                ForgeDirection outputSide = igte.getFrontFacing();
                ForgeDirection inputSide = igte.getFrontFacing()
                    .getOpposite();

                TileEntity te = ThaumcraftApiHelper.getConnectableTile(
                    igte.getWorld(),
                    igte.getXCoord(),
                    igte.getYCoord(),
                    igte.getZCoord(),
                    outputSide);

                if (te != null) {
                    IEssentiaTransport ic = (IEssentiaTransport) te;

                    if (!ic.canInputFrom(inputSide)) {
                        return;
                    }

                    for (Iterator<Entry<Aspect, Integer>> iterator = contents.aspects.entrySet()
                        .iterator(); iterator.hasNext();) {
                        var e = iterator.next();
                        int rejected = ic.addEssentia(e.getKey(), e.getValue(), inputSide);

                        if (rejected == 0) {
                            iterator.remove();
                        } else {
                            e.setValue(rejected);
                        }
                    }
                }
            }
        }
    }
}
