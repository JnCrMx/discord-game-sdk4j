package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.ActivityActionType;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Manager to control Discord's game overlay.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay">
 *     https://discordapp.com/developers/docs/game-sdk/overlay</a>
 */
public class OverlayManager
{
	private final long pointer;
	private final Core core;

	OverlayManager(long pointer, Core core)
	{
		this.pointer = pointer;
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
		return core.execute(()->isEnabled(pointer));
	}

	/**
	 * <p>Checks if the overlay is locked.</p>
	 * @return {@code true} if the overlay is locked
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#islocked">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#islocked</a>
	 */
	public boolean isLocked()
	{
		return core.execute(()->isLocked(pointer));
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
		core.execute(()->setLocked(pointer, locked, Objects.requireNonNull(callback)));
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
		core.execute(()->openActivityInvite(pointer, type.ordinal(), Objects.requireNonNull(callback)));
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
		core.execute(()->openGuildInvite(pointer, code, Objects.requireNonNull(callback)));
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
		core.execute(()->openVoiceSettings(pointer, Objects.requireNonNull(callback)));
	}

	private native boolean isEnabled(long pointer);
	private native boolean isLocked(long pointer);
	private native void setLocked(long pointer, boolean locked, Consumer<Result> callback);
	private native void openActivityInvite(long pointer, int type, Consumer<Result> callback);
	private native void openGuildInvite(long pointer, String code, Consumer<Result> callback);
	private native void openVoiceSettings(long pointer, Consumer<Result> callback);
}
