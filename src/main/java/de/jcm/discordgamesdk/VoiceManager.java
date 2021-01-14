package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.voice.VoiceInputMode;

import java.util.function.Consumer;

/**
 * Manager to control voice settings for Lobby voice channels.
 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice">
 *     https://discord.com/developers/docs/game-sdk/discord-voice</a>
 */
public class VoiceManager
{
	private final long pointer;

	VoiceManager(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * Gets the current voice input mode of the user.
	 * <p>
	 * This method only returns a <i>copy</i> of the input mode.
	 * Modifying it will not affect the current voice input mode
	 * unless you use {@link #setInputMode(VoiceInputMode, Consumer)}.
	 * @return The current {@link VoiceInputMode}
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #setInputMode(VoiceInputMode, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#getinputmode">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#getinputmode</a>
	 */
	public VoiceInputMode getInputMode()
	{
		Object ret = getInputMode(pointer);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (VoiceInputMode) ret;
		}
	}

	/**
	 * Sets a new voice input mode for the current user.
	 * @param inputMode The new voice input mode
	 * @param callback Callback to process the returned {@link Result}
	 * @see #setInputMode(VoiceInputMode)
	 * @see #getInputMode()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#setinputmode">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#setinputmode</a>
	 */
	public void setInputMode(VoiceInputMode inputMode, Consumer<Result> callback)
	{
		setInputMode(pointer, inputMode, callback);
	}

	/**
	 * Sets a new voice input mode for the current user.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param inputMode The new voice input mode
	 * @see #setInputMode(VoiceInputMode, Consumer)
	 * @see #getInputMode()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#setinputmode">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#setinputmode</a>
	 */
	public void setInputMode(VoiceInputMode inputMode)
	{
		setInputMode(inputMode, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Gets whether the current user has muted themselves.
	 * @return {@code true} if the current user is currently muted
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #setSelfMute(boolean)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#isselfmute">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#isselfmute</a>
	 */
	public boolean isSelfMute()
	{
		Object ret = isSelfMute(pointer);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Boolean) ret;
		}
	}

	/**
	 * Mutes or unmutes the current user (self mute).
	 * @param selfMute {@code true} to mute, {@code false} to unmute
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #isSelfMute()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#setselfmute">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#setselfmute</a>
	 */
	public void setSelfMute(boolean selfMute)
	{
		Result result = setSelfMute(pointer, selfMute);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	/**
	 * Gets whether the current user has deafened themselves.
	 * @return {@code true} if the current user is currently deafened
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #setSelfDeaf(boolean)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#isselfdeaf">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#isselfdeaf</a>
	 */
	public boolean isSelfDeaf()
	{
		Object ret = isSelfDeaf(pointer);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Boolean) ret;
		}
	}

	/**
	 * Deafens or undeafens the current user (self deaf).
	 * @param selfDeaf {@code true} to deafen, {@code false} to undeafen
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #isSelfDeaf()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#setselfdeaf">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#setselfdeaf</a>
	 */
	public void setSelfDeaf(boolean selfDeaf)
	{
		Result result = setSelfDeaf(pointer, selfDeaf);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	private native Object getInputMode(long pointer);
	private native void setInputMode(long pointer, VoiceInputMode inputMode, Consumer<Result> callback);

	private native Object isSelfMute(long pointer);
	private native Result setSelfMute(long pointer, boolean mute);
	private native Object isSelfDeaf(long pointer);
	private native Result setSelfDeaf(long pointer, boolean deaf);

	private native Object isLocalMute(long pointer, long userId);
	private native Result setLocalMute(long pointer, long userId, boolean mute);
	private native Object getLocalVolume(long pointer, long userId);
	private native Result setLocalVolume(long pointer, long userId, byte volume);
}
