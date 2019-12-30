package gtPlusPlus.core.handler.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInputFromOptions;

/*
 *		Replacement for MovementInputFromOptions - Built from the source of ToggleSneak 3.0.3
 */

public class CustomMovementHandler {

	public boolean isDisabled;
	public boolean canDoubleTap;

	public boolean sprint = false;
	public boolean sprintHeldAndReleased = false;
	public boolean sprintDoubleTapped = false;

	private long lastPressed;
	private long lastSprintPressed;
	private boolean handledSneakPress;
	private boolean handledSprintPress;
	private boolean wasRiding;

	/*
	 * 		MovementInputFromOptions.updatePlayerMoveState()
	 */
	public void update(final Minecraft mc, final MovementInputFromOptions options, final EntityPlayerSP thisPlayer)
	{
		options.moveStrafe = 0.0F;
		options.moveForward = 0.0F;

		final GameSettings settings = mc.gameSettings;

		if(settings.keyBindForward.getIsKeyPressed())
		{
			++options.moveForward;
		}

		if(settings.keyBindBack.getIsKeyPressed())
		{
			--options.moveForward;
		}

		if(settings.keyBindLeft.getIsKeyPressed())
		{
			++options.moveStrafe;
		}

		if(settings.keyBindRight.getIsKeyPressed())
		{
			--options.moveStrafe;
		}

		options.jump = settings.keyBindJump.getIsKeyPressed();

		//
		// Sneak Toggle - Essentially the same as old ToggleSneak
		//

		// Check to see if Enabled - Added 6/17/14 to provide option to disable Sneak Toggle
		final boolean isSneaking = SneakManager.get(thisPlayer).Sneaking();
		//Utils.LOG_INFO("Can sneak: "+isSneaking);
		//Utils.LOG_INFO("Can sprint: "+SneakManager.Sprinting());
		if (isSneaking)
		{
			// Key Pressed
			if (settings.keyBindSneak.getIsKeyPressed() && !this.handledSneakPress)
			{
				// Descend if we are flying, note if we were riding (so we can unsneak once dismounted)
				if(thisPlayer.isRiding() || thisPlayer.capabilities.isFlying)
				{
					options.sneak = true;
					this.wasRiding = thisPlayer.isRiding();
				}
				else
				{
					options.sneak = !options.sneak;
				}

				this.lastPressed = System.currentTimeMillis();
				this.handledSneakPress = true;
			}

			// Key Released
			if (!settings.keyBindSneak.getIsKeyPressed() && this.handledSneakPress)
			{
				// If we are flying or riding, stop sneaking after descent/dismount.
				if(thisPlayer.capabilities.isFlying || this.wasRiding)
				{
					options.sneak = false;
					this.wasRiding = false;
				}
				// If the key was held down for more than 300ms, stop sneaking upon release.
				else if((System.currentTimeMillis() - this.lastPressed) > 300L)
				{
					options.sneak = false;
				}

				this.handledSneakPress = false;
			}
		}
		else
		{
			options.sneak = settings.keyBindSneak.getIsKeyPressed();
		}

		if(options.sneak || SneakManager.get(thisPlayer).Sneaking())
		{
			options.moveStrafe = (float)(options.moveStrafe * 0.3D);
			options.moveForward = (float)(options.moveForward * 0.3D);
		}

		//
		//  Sprint Toggle - Updated 6/18/2014
		//

		// Establish conditions where we don't want to start a sprint - sneaking, riding, flying, hungry
		final boolean enoughHunger = (thisPlayer.getFoodStats().getFoodLevel() > 6.0F) || thisPlayer.capabilities.isFlying;
		final boolean canSprint = !options.sneak && !thisPlayer.isRiding() && !thisPlayer.capabilities.isFlying && enoughHunger;

		this.isDisabled = !SneakManager.get(thisPlayer).Sprinting();
		this.canDoubleTap = SneakManager.get(thisPlayer).optionDoubleTap;

		// Key Pressed
		if((canSprint || this.isDisabled) && settings.keyBindSprint.getIsKeyPressed() && !this.handledSprintPress)
		{
			if(!this.isDisabled)
			{
				this.sprint = !this.sprint;
				this.lastSprintPressed = System.currentTimeMillis();
				this.handledSprintPress = true;
				this.sprintHeldAndReleased = false;
			}
		}

		// Key Released
		if((canSprint || this.isDisabled) && !settings.keyBindSprint.getIsKeyPressed() && this.handledSprintPress)
		{
			// Was key held for longer than 300ms?  If so, mark it so we can resume vanilla behavior
			if((System.currentTimeMillis() - this.lastSprintPressed) > 300L)
			{
				this.sprintHeldAndReleased = true;
			}
			this.handledSprintPress = false;
		}

	}

	public void UpdateSprint(final boolean newValue, final boolean doubleTapped, SneakManager aSneak){
		if (!aSneak.Sprinting()){
			this.sprint = false;
			this.sprintDoubleTapped = doubleTapped;
		}
		else{
			this.sprint = newValue;
			this.sprintDoubleTapped = doubleTapped;
		}
	}

}