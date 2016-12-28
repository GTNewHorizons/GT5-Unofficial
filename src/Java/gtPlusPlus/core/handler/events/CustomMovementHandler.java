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
	public void update(Minecraft mc, MovementInputFromOptions options, EntityPlayerSP thisPlayer)
	{
		options.moveStrafe = 0.0F;
		options.moveForward = 0.0F;

		GameSettings settings = mc.gameSettings;

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
		if (SneakManager.Sneaking())
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
				else if(System.currentTimeMillis() - this.lastPressed > 300L)
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

		if(options.sneak || SneakManager.Sneaking())
		{
			options.moveStrafe = (float)((double)options.moveStrafe * 0.3D);
			options.moveForward = (float)((double)options.moveForward * 0.3D);
		}

		//
		//  Sprint Toggle - Updated 6/18/2014
		//

		// Establish conditions where we don't want to start a sprint - sneaking, riding, flying, hungry
		boolean enoughHunger = (float)thisPlayer.getFoodStats().getFoodLevel() > 6.0F || thisPlayer.capabilities.isFlying;
		boolean canSprint = !options.sneak && !thisPlayer.isRiding() && !thisPlayer.capabilities.isFlying && enoughHunger;

		isDisabled = !SneakManager.canSprint;
		canDoubleTap = SneakManager.optionDoubleTap;

		// Key Pressed
		if((canSprint || isDisabled) && settings.keyBindSprint.getIsKeyPressed() && !this.handledSprintPress)
		{
			if(!isDisabled)
			{
				this.sprint = !this.sprint;
				this.lastSprintPressed = System.currentTimeMillis();
				this.handledSprintPress = true;
				this.sprintHeldAndReleased = false;
			}
		}

		// Key Released
		if((canSprint || isDisabled) && !settings.keyBindSprint.getIsKeyPressed() && this.handledSprintPress)
		{
			// Was key held for longer than 300ms?  If so, mark it so we can resume vanilla behavior
			if(System.currentTimeMillis() - this.lastSprintPressed > 300L)
			{
				this.sprintHeldAndReleased = true;
			}
			this.handledSprintPress = false;
		}

	}

	public void UpdateSprint(boolean newValue, boolean doubleTapped){
		if (SneakManager.SprintingDisabled()){
			this.sprint = false;
			this.sprintDoubleTapped = doubleTapped;
		}
		else{
			this.sprint = newValue;
			this.sprintDoubleTapped = doubleTapped;
		}		
	}
	
}