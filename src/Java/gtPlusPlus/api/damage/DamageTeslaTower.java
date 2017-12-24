package gtPlusPlus.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class DamageTeslaTower extends BaseCustomDamageSource{

	public DamageTeslaTower(Entity transmitter) {
		super("plasmabolt", transmitter, null);
	    this.setDamageBypassesArmor();
	    this.setDamageIsAbsolute();
	}
	
	@Override
	public IChatComponent func_151519_b(EntityLivingBase target) {
	    String s = "death.attack." + this.damageType;
	    return new ChatComponentTranslation(s, target.getCommandSenderName(), "Plasma");
	}

}
