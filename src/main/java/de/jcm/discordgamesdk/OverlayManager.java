package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.ActivityActionType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Manager to control Discord's game overlay.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay">
 *     https://discordapp.com/developers/docs/game-sdk/overlay</a>
 */
public class OverlayManager
{
	private long pointer;

	OverlayManager(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * <p>Checks if the overlay is enabled by the user.</p>
	 * @return {@code true} if the overlay is enabled
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#isenabled">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#isenabled</a>
	 */
	public boolean isEnabled()
	{
		return isEnabled(pointer);
	}

	/**
	 * <p>Checks if the overlay is locked.</p>
	 * @return {@code true} if the overlay is locked
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#islocked>
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#islocked</a>
	 */
	public boolean isLocked()
	{
		return isLocked(pointer);
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
		setLocked(pointer, locked, Core.DEFAULT_CALLBACK);
	}

	/**
	 * <p>Changes the locked status of the overlay.</p>
	 * <p>A custom callback is used to handle the returned {@link Result}.</p>
	 * @param locked New locked status
	 * @param callback Callback to process the returned {@link Result}.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#setlocked">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#setlocked</a>
	 */
	public void setLocked(boolean locked, @NotNull Consumer<Result> callback)
	{
		setLocked(pointer, locked, callback);
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
	public void openActivityInvite(ActivityActionType type, @NotNull Consumer<Result> callback)
	{
		openActivityInvite(pointer, type.ordinal(), callback);
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
	public void openGuildInvite(String code, @NotNull Consumer<Result> callback)
	{
		openGuildInvite(pointer, code, callback);
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
	public void openVoiceSettings(@NotNull Consumer<Result> callback)
	{
		openVoiceSettings(pointer, callback);
	}

	private native boolean isEnabled(long pointer);
	private native boolean isLocked(long pointer);
	private native void setLocked(long pointer, boolean locked, Consumer<Result> callback);
	private native void openActivityInvite(long pointer, int type, Consumer<Result> callback);
	private native void openGuildInvite(long pointer, String code, Consumer<Result> callback);
	private native void openVoiceSettings(long pointer, Consumer<Result> callback);
}
