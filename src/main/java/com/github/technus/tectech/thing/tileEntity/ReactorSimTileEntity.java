package com.github.technus.tectech.thing.tileEntity;

import com.github.technus.tectech.Reference;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.core.IC2;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by danie_000 on 30.09.2017.
 */
public class ReactorSimTileEntity extends TileEntityNuclearReactorElectric {
    private boolean hadRedstone =true;

    @Override
    public void onLoaded() {
        super.onLoaded();
        if(IC2.platform.isSimulating() && addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            //this.addedToEnergyNet = false;
        }
    }

    @Override
    public void onUnloaded() {
        addedToEnergyNet=false;
        super.onUnloaded();
    }

    @Override
    public String getInventoryName() {
        return "Nuclear Reactor Simulator";
    }

    //public int gaugeHeatScaled(int i) {
    //    return i * this.heat / (this.maxHeat / 100 * 85);
    //}

    //public void readFromNBT(NBTTagCompound nbttagcompound) {
    //    super.readFromNBT(nbttagcompound);
    //    //this.heat = nbttagcompound.getInteger("heat");
    //    //this.inputTank.readFromNBT(nbttagcompound.getCompoundTag("inputTank"));
    //    //this.outputTank.readFromNBT(nbttagcompound.getCompoundTag("outputTank"));
    //    //this.output = (float)nbttagcompound.getShort("output");
    //}

    //public void writeToNBT(NBTTagCompound nbttagcompound) {
    //    super.writeToNBT(nbttagcompound);
    //    //NBTTagCompound inputTankTag = new NBTTagCompound();
    //    //this.inputTank.writeToNBT(inputTankTag);
    //    //nbttagcompound.setTag("inputTank", inputTankTag);
    //    //NBTTagCompound outputTankTag = new NBTTagCompound();
    //    //this.outputTank.writeToNBT(outputTankTag);
    //    //nbttagcompound.setTag("outputTank", outputTankTag);
    //    //nbttagcompound.setInteger("heat", this.heat);
    //    //nbttagcompound.setShort("output", (short)((int)this.getReactorEnergyOutput()));
    //}

    //public void setRedstoneSignal(boolean redstone) {
    //    this.redstone = redstone;
    //}

    //public void drawEnergy(double amount) {
    //}

    //public float sendEnergy(float send) {
    //    return 0.0F;
    //}

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return false;
    }

    @Override
    public double getOfferedEnergy() {
        return 0;
    }

    //public int getSourceTier() {
    //    return 4;
    //}

    @Override
    public double getReactorEUEnergyOutput() {
        return (double)(getReactorEnergyOutput() * 5.0F * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear"));
    }

    //public List<TileEntity> getSubTiles() {
    //    if(this.subTiles == null) {
    //        this.subTiles = new ArrayList();
    //        this.subTiles.add(this);
    //        Direction[] arr$ = Direction.directions;
    //        int len$ = arr$.length;
    //
    //        for(int i$ = 0; i$ < len$; ++i$) {
    //            Direction dir = arr$[i$];
    //            TileEntity te = dir.applyToTileEntity(this);
    //            if(te instanceof TileEntityReactorChamberElectric && !te.isInvalid()) {
    //                this.subTiles.add(te);
    //            }
    //        }
    //    }
    //
    //    return this.subTiles;
    //}

    //private void processfluidsSlots() {
    //    RecipeOutput outputinputSlot = this.processInputSlot(true);
    //    if(outputinputSlot != null) {
    //        this.processInputSlot(false);
    //        List<ItemStack> outputoutputSlot = outputinputSlot.items;
    //        this.coolantoutputSlot.add(outputoutputSlot);
    //    }
    //
    //    RecipeOutput outputoutputSlot1 = this.processOutputSlot(true);
    //    if(outputoutputSlot1 != null) {
    //        this.processOutputSlot(false);
    //        List<ItemStack> processResult = outputoutputSlot1.items;
    //        this.hotcoolantoutputSlot.add(processResult);
    //    }
    //
    //}

    //public void refreshChambers() {
    //    if(this.addedToEnergyNet) {
    //        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    //    }
    //
    //    this.subTiles = null;
    //    if(this.addedToEnergyNet) {
    //        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
    //    }
    //
    //}

    @Override
    protected void updateEntityServer() {
        if(updateTicker++ % getTickRate() == 0) {
            if (!worldObj.isRemote && worldObj.doChunksNearChunkExist(xCoord, yCoord, zCoord, 2)) {
                if(hadRedstone && !receiveredstone()) {
                    hadRedstone = false;
                } else if(!hadRedstone && receiveredstone()){
                    doUpdates();
                    hadRedstone=true;
                }
                markDirty();
            }
        }
    }

    //public void dropAllUnfittingStuff() {
    //    int i;
    //    ItemStack stack;
    //    for(i = 0; i < this.reactorSlot.size(); ++i) {
    //        stack = this.reactorSlot.get(i);
    //        if(stack != null && !this.isUsefulItem(stack, false)) {
    //            this.reactorSlot.put(i, null);
    //            this.eject(stack);
    //        }
    //    }
    //
    //    for(i = this.reactorSlot.size(); i < this.reactorSlot.rawSize(); ++i) {
    //        stack = this.reactorSlot.get(i);
    //        this.reactorSlot.put(i, null);
    //        this.eject(stack);
    //    }
    //
    //}

    //public boolean isUsefulItem(ItemStack stack, boolean forInsertion) {
    //    Item item = stack.getItem();
    //    return (item instanceof IReactorComponent || (item == Ic2Items.TritiumCell.getItem() || item == Ic2Items.reactorDepletedUraniumSimple.getItem() || item == Ic2Items.reactorDepletedUraniumDual.getItem() || item == Ic2Items.reactorDepletedUraniumQuad.getItem() || item == Ic2Items.reactorDepletedMOXSimple.getItem() || item == Ic2Items.reactorDepletedMOXDual.getItem() || item == Ic2Items.reactorDepletedMOXQuad.getItem()));
    //}

    //public void eject(ItemStack drop) {
    //    if(IC2.platform.isSimulating() && drop != null) {
    //        float f = 0.7F;
    //        double d = (double)(this.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
    //        double d1 = (double)(this.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
    //        double d2 = (double)(this.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
    //        EntityItem entityitem = new EntityItem(this.worldObj, (double)this.xCoord + d, (double)this.yCoord + d1, (double)this.zCoord + d2, drop);
    //        entityitem.delayBeforeCanPickup = 10;
    //        this.worldObj.spawnEntityInWorld(entityitem);
    //    }
    //}

    @Override
    public boolean calculateHeatEffects() {
        if(heat >= 4000 && IC2.platform.isSimulating()) {
            float power = (float) heat / (float) maxHeat;
            if(power >= 1.0F) {
                explode();//ding
                return true;
            } else {
                //int[] coord;
                //Block block;
                //Material mat;
                //if(power >= 0.85F && this.worldObj.rand.nextFloat() <= 0.2F * this.hem) {
                //    coord = this.getRandCoord(2);
                //    if(coord != null) {
                //        block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
                //        if(block.isAir(this.worldObj, coord[0], coord[1], coord[2])) {
                //            this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
                //        } else if(block.getBlockHardness(this.worldObj, coord[0], coord[1], coord[2]) >= 0.0F && this.worldObj.getTileEntity(coord[0], coord[1], coord[2]) == null) {
                //            mat = block.getMaterial();
                //            if(mat != Material.rock && mat != Material.iron && mat != Material.lava && mat != Material.ground && mat != Material.clay) {
                //                this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
                //            } else {
                //                this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.flowing_lava, 15, 7);
                //            }
                //        }
                //    }
                //}
                //
                //if(power >= 0.7F) {
                //    List var5 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox((double)(this.xCoord - 3), (double)(this.yCoord - 3), (double)(this.zCoord - 3), (double)(this.xCoord + 4), (double)(this.yCoord + 4), (double)(this.zCoord + 4)));
                //
                //    for(int var6 = 0; var6 < var5.size(); ++var6) {
                //        Entity var7 = (Entity)var5.get(var6);
                //        var7.attackEntityFrom(IC2DamageSource.radiation, (float)((int)((float)this.worldObj.rand.nextInt(4) * this.hem)));
                //    }
                //}
                //
                //if(power >= 0.5F && this.worldObj.rand.nextFloat() <= this.hem) {
                //    coord = this.getRandCoord(2);
                //    if(coord != null) {
                //        block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
                //        if(block.getMaterial() == Material.water) {
                //            this.worldObj.setBlockToAir(coord[0], coord[1], coord[2]);
                //        }
                //    }
                //}
                //
                //if(power >= 0.4F && this.worldObj.rand.nextFloat() <= this.hem) {
                //    coord = this.getRandCoord(2);
                //    if(coord != null && this.worldObj.getTileEntity(coord[0], coord[1], coord[2]) == null) {
                //        block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
                //        mat = block.getMaterial();
                //        if(mat == Material.wood || mat == Material.leaves || mat == Material.cloth) {
                //            this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
                //        }
                //    }
                //}
                return false;
            }
        }
        return false;
    }

    //public int[] getRandCoord(int radius) {
    //    if(radius <= 0) {
    //        return null;
    //    } else {
    //        int[] c = new int[]{this.xCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius, this.yCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius, this.zCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius};
    //        return c[0] == this.xCoord && c[1] == this.yCoord && c[2] == this.zCoord?null:c;
    //    }
    //}

    //public void processChambers() {
    //    short size = this.getReactorSize();
    //
    //    for(int pass = 0; pass < 2; ++pass) {
    //        for(int y = 0; y < 6; ++y) {
    //            for(int x = 0; x < size; ++x) {
    //                ItemStack stack = this.reactorSlot.get(x, y);
    //                if(stack != null && stack.getItem() instanceof IReactorComponent) {
    //                    IReactorComponent comp = (IReactorComponent)stack.getItem();
    //                    comp.processChamber(this, stack, x, y, pass == 0);
    //                }
    //            }
    //        }
    //    }
    //
    //}

    //public boolean produceEnergy() {
    //    return this.receiveredstone() && ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/generator") > 0.0F;
    //}

    //NO need
    //public boolean receiveredstone() {
    //    return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.redstone;
    //}

    //public short getReactorSize() {
    //    if(this.worldObj == null) {
    //        return 9;
    //    } else {
    //        short cols = 3;
    //        Direction[] arr$ = Direction.directions;
    //        int len$ = arr$.length;
    //
    //        for(int i$ = 0; i$ < len$; ++i$) {
    //            Direction direction = arr$[i$];
    //            TileEntity target = direction.applyToTileEntity(this);
    //            if(target instanceof TileEntityReactorChamberElectric) {
    //                ++cols;
    //            }
    //        }
    //
    //        return cols;
    //    }
    //}

    //public int getTickRate() {
    //    return 20;
    //}

    //public ContainerBase<TileEntityNuclearReactorElectric> getGuiContainer(EntityPlayer entityPlayer) {
    //    return new ContainerNuclearReactor(entityPlayer, this);
    //}

    //@SideOnly(Side.CLIENT)
    //public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
    //    return new GuiNuclearReactor(new ContainerNuclearReactor(entityPlayer, this));
    //}

    //public void onGuiClosed(EntityPlayer entityPlayer) {}

    //new method
    private void doUpdates(){
        heat=0;
        do{
            dropAllUnfittingStuff();
            output = 0.0F;
            maxHeat = 10000;
            hem = 1.0F;
            processChambers();
        }while(!calculateHeatEffects() && output>0);
    }

    //region no need to change
    //public void onNetworkUpdate(String field) {
    //    if(field.equals("output")) {
    //        if(this.output > 0.0F) {
    //            if(this.lastOutput <= 0.0F) {
    //                if(this.audioSourceMain == null) {
    //                    this.audioSourceMain = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/NuclearReactorLoop.ogg", true, false, IC2.audioManager.getDefaultVolume());
    //                }
    //
    //                if(this.audioSourceMain != null) {
    //                    this.audioSourceMain.play();
    //                }
    //            }
    //
    //            if(this.output < 40.0F) {
    //                if(this.lastOutput <= 0.0F || this.lastOutput >= 40.0F) {
    //                    if(this.audioSourceGeiger != null) {
    //                        this.audioSourceGeiger.remove();
    //                    }
    //
    //                    this.audioSourceGeiger = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/GeigerLowEU.ogg", true, false, IC2.audioManager.getDefaultVolume());
    //                    if(this.audioSourceGeiger != null) {
    //                        this.audioSourceGeiger.play();
    //                    }
    //                }
    //            } else if(this.output < 80.0F) {
    //                if(this.lastOutput < 40.0F || this.lastOutput >= 80.0F) {
    //                    if(this.audioSourceGeiger != null) {
    //                        this.audioSourceGeiger.remove();
    //                    }
    //
    //                    this.audioSourceGeiger = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/GeigerMedEU.ogg", true, false, IC2.audioManager.getDefaultVolume());
    //                    if(this.audioSourceGeiger != null) {
    //                        this.audioSourceGeiger.play();
    //                    }
    //                }
    //            } else if(this.output >= 80.0F && this.lastOutput < 80.0F) {
    //                if(this.audioSourceGeiger != null) {
    //                    this.audioSourceGeiger.remove();
    //                }
    //
    //                this.audioSourceGeiger = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/GeigerHighEU.ogg", true, false, IC2.audioManager.getDefaultVolume());
    //                if(this.audioSourceGeiger != null) {
    //                    this.audioSourceGeiger.play();
    //                }
    //            }
    //        } else if(this.lastOutput > 0.0F) {
    //            if(this.audioSourceMain != null) {
    //                this.audioSourceMain.stop();
    //            }
    //
    //            if(this.audioSourceGeiger != null) {
    //                this.audioSourceGeiger.stop();
    //            }
    //        }
    //
    //        this.lastOutput = this.output;
    //    }
    //
    //    super.onNetworkUpdate(field);
    //}

    //public float getWrenchDropRate() {
    //    return 1F;
    //}

    //public ChunkCoordinates getPosition() {
    //    return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
    //}

    //public World getWorld() {
    //    return this.worldObj;
    //}

    //public int getHeat() {
    //    return this.heat;
    //}

    //public void setHeat(int heat1) {
    //    this.heat = heat1;
    //}

    //public int addHeat(int amount) {
    //    this.heat += amount;
    //    return this.heat;
    //}

    //public ItemStack getItemAt(int x, int y) {
    //    return x >= 0 && x < this.getReactorSize() && y >= 0 && y < 6?this.reactorSlot.get(x, y):null;
    //}

    //public void setItemAt(int x, int y, ItemStack item) {
    //    if(x >= 0 && x < this.getReactorSize() && y >= 0 && y < 6) {
    //        this.reactorSlot.put(x, y, item);
    //    }
    //}

    @Override
    public void explode() {
        getWorld().playSoundEffect(xCoord,yCoord,zCoord, Reference.MODID+":microwave_ding", 1, 1);
    }

    @Override
    public void addEmitHeat(int heat) {}

    //region no need
    //public int getMaxHeat() {
    //    return this.maxHeat;
    //}

    //public void setMaxHeat(int newMaxHeat) {
    //    this.maxHeat = newMaxHeat;
    //}

    //public float getHeatEffectModifier() {
    //    return this.hem;
    //}

    //public void setHeatEffectModifier(float newHEM) {
    //    this.hem = newHEM;
    //}

    //public float getReactorEnergyOutput() {
    //    return this.output;
    //}

    //public float addOutput(float energy) {
    //    return this.output += energy;
    //}

    //PRIVATE not used
    //private RecipeOutput processInputSlot(boolean simulate) {
    //    if(!this.coolantinputSlot.isEmpty()) {
    //        MutableObject output = new MutableObject();
    //        if(this.coolantinputSlot.transferToTank(this.inputTank, output, simulate) && (output.getValue() == null || this.coolantoutputSlot.canAdd((ItemStack)output.getValue()))) {
    //            if(output.getValue() == null) {
    //                return new RecipeOutput(null);
    //            }
    //
    //            return new RecipeOutput(null, (ItemStack)output.getValue());
    //        }
    //    }
    //
    //    return null;
    //}

    //private RecipeOutput processOutputSlot(boolean simulate) {
    //    if(!this.hotcoolinputSlot.isEmpty()) {
    //        MutableObject output = new MutableObject();
    //        if(this.hotcoolinputSlot.transferFromTank(this.outputTank, output, simulate) && (output.getValue() == null || this.hotcoolantoutputSlot.canAdd((ItemStack)output.getValue()))) {
    //            if(output.getValue() == null) {
    //                return new RecipeOutput(null);
    //            }
    //
    //            return new RecipeOutput(null, (ItemStack)output.getValue());
    //        }
    //    }
    //
    //    return null;
    //}

    @Override
    public boolean isFluidCooled() {
        return false;
    }

    //!!!Private - removed use cases
    //private void movefluidinWorld(boolean out) {
    //    if(out) {
    //        if(this.inputTank.getFluidAmount() < 1000 && this.outputTank.getFluidAmount() < 1000) {
    //            this.inputTank.setFluid(null);
    //            this.outputTank.setFluid(null);
    //        } else {
    //            for(int coolantFluid = 1; coolantFluid < 4; ++coolantFluid) {
    //                for(int coolantBlock = 1; coolantBlock < 4; ++coolantBlock) {
    //                    for(int hotCoolantFluid = 1; hotCoolantFluid < 4; ++hotCoolantFluid) {
    //                        if(this.surroundings[coolantBlock][coolantFluid][hotCoolantFluid] instanceof BlockAir) {
    //                            if(this.inputTank.getFluidAmount() >= 1000) {
    //                                this.worldObj.setBlock(coolantBlock + this.xCoord - 2, coolantFluid + this.yCoord - 2, hotCoolantFluid + this.zCoord - 2, this.inputTank.getFluid().getFluid().getBlock());
    //                                this.inputTank.drain(1000, true);
    //                            } else if(this.outputTank.getFluidAmount() >= 1000) {
    //                                this.worldObj.setBlock(coolantBlock + this.xCoord - 2, coolantFluid + this.yCoord - 2, hotCoolantFluid + this.zCoord - 2, this.outputTank.getFluid().getFluid().getBlock());
    //                                this.outputTank.drain(1000, true);
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //
    //            if(this.inputTank.getFluidAmount() < 1000) {
    //                this.inputTank.setFluid(null);
    //            }
    //
    //            if(this.outputTank.getFluidAmount() < 1000) {
    //                this.outputTank.setFluid(null);
    //            }
    //        }
    //    } else {
    //        Fluid var9 = BlocksItems.getFluid(InternalName.fluidCoolant);
    //        Block var10 = BlocksItems.getFluidBlock(InternalName.fluidCoolant);
    //        Fluid var11 = BlocksItems.getFluid(InternalName.fluidHotCoolant);
    //        Block hotCoolantBlock = BlocksItems.getFluidBlock(InternalName.fluidHotCoolant);
    //
    //        for(int yoffset = 1; yoffset < 4; ++yoffset) {
    //            for(int xoffset = 1; xoffset < 4; ++xoffset) {
    //                for(int zoffset = 1; zoffset < 4; ++zoffset) {
    //                    if(this.surroundings[xoffset][yoffset][zoffset] == var10) {
    //                        this.worldObj.setBlock(xoffset + this.xCoord - 2, yoffset + this.yCoord - 2, zoffset + this.zCoord - 2, Blocks.air);
    //                        this.inputTank.fill(new FluidStack(var9, 1000), true);
    //                    } else if(this.surroundings[xoffset][yoffset][zoffset] == hotCoolantBlock) {
    //                        this.worldObj.setBlock(xoffset + this.xCoord - 2, yoffset + this.yCoord - 2, zoffset + this.zCoord - 2, Blocks.air);
    //                        this.outputTank.fill(new FluidStack(var11, 1000), true);
    //                    }
    //                }
    //            }
    //        }
    //    }
    //
    //}

    //!!!! PRIVATE - removed use cases
    //private boolean readyforpressurizedreactor() {
    //    Block coolantBlock = BlocksItems.getFluidBlock(InternalName.fluidCoolant);
    //    Block hotCoolantBlock = BlocksItems.getFluidBlock(InternalName.fluidHotCoolant);
    //
    //    int xoffset;
    //    int yoffset;
    //    int zoffset;
    //    for(xoffset = -2; xoffset < 3; ++xoffset) {
    //        for(yoffset = -2; yoffset < 3; ++yoffset) {
    //            for(zoffset = -2; zoffset < 3; ++zoffset) {
    //                if(this.worldObj.isAirBlock(xoffset + this.xCoord, yoffset + this.yCoord, zoffset + this.zCoord)) {
    //                    this.surroundings[xoffset + 2][yoffset + 2][zoffset + 2] = Blocks.air;
    //                } else {
    //                    Block block = this.worldObj.getBlock(xoffset + this.xCoord, yoffset + this.yCoord, zoffset + this.zCoord);
    //                    if((block == coolantBlock || block == hotCoolantBlock) && this.worldObj.getBlockMetadata(xoffset + this.xCoord, yoffset + this.yCoord, zoffset + this.zCoord) != 0) {
    //                        this.surroundings[xoffset + 2][yoffset + 2][zoffset + 2] = Blocks.air;
    //                    } else {
    //                        this.surroundings[xoffset + 2][yoffset + 2][zoffset + 2] = block;
    //                    }
    //                }
    //            }
    //        }
    //    }
    //
    //    for(xoffset = 1; xoffset < 4; ++xoffset) {
    //        for(yoffset = 1; yoffset < 4; ++yoffset) {
    //            for(zoffset = 1; zoffset < 4; ++zoffset) {
    //                if(!(this.surroundings[xoffset][yoffset][zoffset] instanceof BlockGenerator) && !(this.surroundings[xoffset][yoffset][zoffset] instanceof BlockReactorChamber) && this.surroundings[xoffset][yoffset][zoffset] != coolantBlock && this.surroundings[xoffset][yoffset][zoffset] != hotCoolantBlock && !(this.surroundings[xoffset][yoffset][zoffset] instanceof BlockAir)) {
    //                    return false;
    //                }
    //            }
    //        }
    //    }
    //
    //    for(xoffset = 0; xoffset < 5; ++xoffset) {
    //        for(yoffset = 0; yoffset < 5; ++yoffset) {
    //            if(!(this.surroundings[xoffset][4][yoffset] instanceof BlockReactorVessel) && !(this.surroundings[xoffset][4][yoffset] instanceof BlockReactorAccessHatch) && !(this.surroundings[xoffset][4][yoffset] instanceof BlockReactorRedstonePort) && !(this.surroundings[xoffset][4][yoffset] instanceof BlockReactorFluidPort)) {
    //                return false;
    //            }
    //
    //            if(!(this.surroundings[xoffset][0][yoffset] instanceof BlockReactorVessel) && !(this.surroundings[xoffset][0][yoffset] instanceof BlockReactorAccessHatch) && !(this.surroundings[xoffset][0][yoffset] instanceof BlockReactorRedstonePort) && !(this.surroundings[xoffset][0][yoffset] instanceof BlockReactorFluidPort)) {
    //                return false;
    //            }
    //
    //            if(!(this.surroundings[0][xoffset][yoffset] instanceof BlockReactorVessel) && !(this.surroundings[0][xoffset][yoffset] instanceof BlockReactorAccessHatch) && !(this.surroundings[0][xoffset][yoffset] instanceof BlockReactorRedstonePort) && !(this.surroundings[0][xoffset][yoffset] instanceof BlockReactorFluidPort)) {
    //                return false;
    //            }
    //
    //            if(!(this.surroundings[4][xoffset][yoffset] instanceof BlockReactorVessel) && !(this.surroundings[4][xoffset][yoffset] instanceof BlockReactorAccessHatch) && !(this.surroundings[4][xoffset][yoffset] instanceof BlockReactorRedstonePort) && !(this.surroundings[4][xoffset][yoffset] instanceof BlockReactorFluidPort)) {
    //                return false;
    //            }
    //
    //            if(!(this.surroundings[yoffset][xoffset][0] instanceof BlockReactorVessel) && !(this.surroundings[yoffset][xoffset][0] instanceof BlockReactorAccessHatch) && !(this.surroundings[yoffset][xoffset][0] instanceof BlockReactorRedstonePort) && !(this.surroundings[yoffset][xoffset][0] instanceof BlockReactorFluidPort)) {
    //                return false;
    //            }
    //
    //            if(!(this.surroundings[yoffset][xoffset][4] instanceof BlockReactorVessel) && !(this.surroundings[yoffset][xoffset][4] instanceof BlockReactorAccessHatch) && !(this.surroundings[yoffset][xoffset][4] instanceof BlockReactorRedstonePort) && !(this.surroundings[yoffset][xoffset][4] instanceof BlockReactorFluidPort)) {
    //                return false;
    //            }
    //        }
    //    }
    //
    //    return true;
    //}

    //public int gaugeLiquidScaled(int i, int tank) {
    //    switch(tank) {
    //        case 0:
    //            if(this.inputTank.getFluidAmount() <= 0) {
    //                return 0;
    //            }
    //
    //            return this.inputTank.getFluidAmount() * i / this.inputTank.getCapacity();
    //        case 1:
    //            if(this.outputTank.getFluidAmount() <= 0) {
    //                return 0;
    //            }
    //
    //            return this.outputTank.getFluidAmount() * i / this.outputTank.getCapacity();
    //        default:
    //            return 0;
    //    }
    //}

    //public FluidTank getinputtank() {
    //    return this.inputTank;
    //}

    //public FluidTank getoutputtank() {
    //    return this.outputTank;
    //}

    //public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    //    return new FluidTankInfo[]{this.inputTank.getInfo(), this.outputTank.getInfo()};
    //}

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}
