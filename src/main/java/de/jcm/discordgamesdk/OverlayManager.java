package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.ActivityActionType;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.commands.OpenOverlayActivityInvite;
import de.jcm.discordgamesdk.impl.commands.OpenOverlayGuildInvite;
import de.jcm.discordgamesdk.impl.commands.OpenOverlayVoiceSettings;
import de.jcm.discordgamesdk.impl.commands.SetOverlayLocked;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Manager to control Discord's game overlay.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay">
 *     https://discordapp.com/developers/docs/game-sdk/overlay</a>
 */
public class OverlayManager
{
	private final Core.CorePrivate core;

	OverlayManager(Core.CorePrivate core)
	{
		this.core = core;
	}

	/**
	 * <p>Checks if the overlay is enabled by the user.</p>
	 * @return {@code true} if the overlay is enabled
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#isenabled">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#isenabled</a>
	 */
	public boolean isEnabled()
	{
		return core.overlayData.isEnabled();
	}

	/**
	 * <p>Checks if the overlay is locked.</p>
	 * @return {@code true} if the overlay is locked
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#islocked">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#islocked</a>
	 */
	public boolean isLocked()
	{
		return core.overlayData.isLocked();
	}

	/**
	 * <p>Changes the locked status of the overlay.</p>
	 * <p>The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.</p>
	 * @param locked New locked status
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#setlocked">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#setlocked</a>
	 */
	public void setLocked(boolean locked)
	{
		setLocked(locked, Core.DEFAULT_CALLBACK);
	}

	/**
	 * <p>Changes the locked status of the overlay.</p>
	 * <p>A custom callback is used to handle the returned {@link Result}.</p>
	 * @param locked New locked status
	 * @param callback Callback to process the returned {@link Result}.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#setlocked">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#setlocked</a>
	 */
	public void setLocked(boolean locked, Consumer<Result> callback)
	{
		core.sendCommand(Command.Type.SET_OVERLAY_LOCKED, new SetOverlayLocked.Args(locked, core.pid), c->{
			callback.accept(core.checkError(c));
		});
	}

	/**
	 * <p>Opens the overlay and prepares a join/spectate invitation to send.</p>
	 * <p>The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.</p>
	 * @param type Type of the invitation
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#openactivityinvite">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#openactivityinvite</a>
	 */
	public void openActivityInvite(ActivityActionType type)
	{
		openActivityInvite(type, Core.DEFAULT_CALLBACK);
	}

	/**
	 * <p>Opens the overlay and prepares a join/spectate invitation to send.</p>
	 * <p>A custom callback is used to handle the returned {@link Result}.</p>
	 * @param type Type of the invitation
	 * @param callback Callback to process the returned {@link Result}.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#openactivityinvite">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#openactivityinvite</a>
	 */
	public void openActivityInvite(ActivityActionType type, Consumer<Result> callback)
	{
		core.sendCommand(Command.Type.OPEN_OVERLAY_ACTIVITY_INVITE, new OpenOverlayActivityInvite.Args(type.nativeValue(), core.pid), c->{
			callback.accept(core.checkError(c));
		});
	}

	/**
	 * <p>Opens the overlay and attempts to join a guild with an invite code.</p>
	 * <p>The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.</p>
	 * @param code Invite code for a guild
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#openguildinvite">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#openguildinvite</a>
	 */
	public void openGuildInvite(String code)
	{
		openGuildInvite(code, Core.DEFAULT_CALLBACK);
	}

	/**
	 * <p>Opens the overlay and attempts to join a guild with an invite code.</p>
	 * <p>A custom callback is used to handle the returned {@link Result}.</p>
	 * @param code Invite code for a guild
	 * @param callback Callback to process the returned {@link Result}.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#openguildinvite">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#openguildinvite</a>
	 */
	public void openGuildInvite(String code, Consumer<Result> callback)
	{
		core.sendCommand(Command.Type.OPEN_OVERLAY_GUILD_INVITE, new OpenOverlayGuildInvite.Args(code, core.pid), c->{
			callback.accept(core.checkError(c));
		});
	}

	/**
	 * <p>Opens the overlay and shows a widget to adjust the user's application specific voice settings.</p>
	 * <p>The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.</p>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#openvoicesettings">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#openvoicesettings</a>
	 */
	public void openVoiceSettings()
	{
		openVoiceSettings(Core.DEFAULT_CALLBACK);
	}

	/**
	 * <p>Opens the overlay and shows a widget to adjust the user's application specific voice settings.</p>
	 * <p>A custom callback is used to handle the returned {@link Result}.</p>
	 * @param callback Callback to process the returned {@link Result}.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#openvoicesettings">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#openvoicesettings</a>
	 */
	public void openVoiceSettings(Consumer<Result> callback)
	{
		core.sendCommand(Command.Type.OPEN_OVERLAY_VOICE_SETTINGS, new OpenOverlayVoiceSettings.Args(core.pid), c->{
			callback.accept(core.checkError(c));
		});
	}
}
