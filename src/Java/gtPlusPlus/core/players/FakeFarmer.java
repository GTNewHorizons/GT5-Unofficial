package gtPlusPlus.core.players;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class FakeFarmer extends EntityPlayerMP {
	private static final UUID uuid = UUID.fromString("c1ddfd7f-120a-4437-8b64-38660d3ec62d");

	private static GameProfile FAKE_PROFILE = new GameProfile(uuid, "[GT_Farm_Manager]");

	public FakeFarmer(final WorldServer world) {
		super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, FAKE_PROFILE, new ItemInWorldManager(world));
	}

	@Override
	public boolean canCommandSenderUseCommand(final int i, final String s) {
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(0, 0, 0);
	}

	@Override
	public void addChatComponentMessage(final IChatComponent chatmessagecomponent) {
	}

	@Override
	public void addChatMessage(final IChatComponent p_145747_1_) {
	}

	@Override
	public void addStat(final StatBase par1StatBase, final int par2) {
	}

	@Override
	public void openGui(final Object mod, final int modGuiId, final World world, final int x, final int y, final int z) {
	}

	@Override
	public boolean isEntityInvulnerable() {
		return true;
	}

	@Override
	public boolean canAttackPlayer(final EntityPlayer player) {
		return false;
	}

	@Override
	public void onDeath(final DamageSource source) {
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void travelToDimension(final int dim) {
	}

	@Override
	public void func_147100_a(final C15PacketClientSettings pkt) {
	}

	@Override
	public boolean canPlayerEdit(final int par1, final int par2, final int par3, final int par4, final ItemStack par5ItemStack) {
		return true;
	}
}