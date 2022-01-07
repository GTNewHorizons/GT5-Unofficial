/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package gtPlusPlus.xmod.forestry.bees.tileentities;

import java.util.*;

import cpw.mods.fml.common.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import buildcraft.api.statements.ITriggerExternal;
import forestry.api.apiculture.*;
import forestry.apiculture.ApiaryBeeListener;
import forestry.apiculture.ApiaryBeeModifier;
import forestry.apiculture.IApiary;
import forestry.apiculture.inventory.IApiaryInventory;
import forestry.apiculture.inventory.InventoryApiary;
import forestry.apiculture.tiles.TileBeeHousingBase;
import forestry.apiculture.trigger.ApicultureTriggers;
import gtPlusPlus.xmod.forestry.bees.gui.ContainerBeeHouse;
import gtPlusPlus.xmod.forestry.bees.gui.GuiBeeHouse;
import net.minecraftforge.common.util.ForgeDirection;

public class TileDenseBeeHouse extends TileBeeHousingBase implements IApiary {
	private final IBeeModifier beeModifier = new ApiaryBeeModifier();
	private final IBeeListener beeListener = new ApiaryBeeListener(this);
	private final InventoryApiary inventory = new InventoryApiary(getAccessHandler());

	public TileDenseBeeHouse() {
		super("apiary2");
		setInternalInventory(inventory);
	}

	@Override
	public IBeeHousingInventory getBeeInventory() {
		return inventory;
	}

	@Override
	public IApiaryInventory getApiaryInventory() {
		return inventory;
	}

	@Override
	public Collection<IBeeModifier> getBeeModifiers() {
		List<IBeeModifier> beeModifiers = new ArrayList<>();

		beeModifiers.add(beeModifier);

		for (IHiveFrame frame : inventory.getFrames()) {
			beeModifiers.add(frame.getBeeModifier());
		}

		return beeModifiers;
	}

	@Override
	public Iterable<IBeeListener> getBeeListeners() {
		return Collections.singleton(beeListener);
	}

	/* ITRIGGERPROVIDER */
	@Optional.Method(modid = "BuildCraftAPI|statements")
	@Override
	public Collection<ITriggerExternal> getExternalTriggers(ForgeDirection side, TileEntity tile) {
		LinkedList<ITriggerExternal> res = new LinkedList<>();
		res.add(ApicultureTriggers.missingQueen);
		res.add(ApicultureTriggers.missingDrone);
		res.add(ApicultureTriggers.noFrames);
		return res;
	}

	@Override
	public Object getGui(EntityPlayer player, int data) {
		ContainerBeeHouse container = new ContainerBeeHouse(player.inventory, this, true);
		return new GuiBeeHouse<>(this, container, GuiBeeHouse.Icon.APIARY);
	}

	@Override
	public Object getContainer(EntityPlayer player, int data) {
		return new ContainerBeeHouse(player.inventory, this, true);
	}
}
