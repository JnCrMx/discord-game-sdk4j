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
	private final Core.CorePrivate core;

	VoiceManager(Core.CorePrivate core)
	{
		this.core = core;
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
		return core.voiceData.getInputMode();
	}

	/**
	 * Sets a new voice input mode for the current user.
	 * <p>
	 * Discord checks the validity of the {@link VoiceInputMode#getShortcut()} and
	 * e.g. removes illegal keys (no error is thrown).
	 * However, the plausibility is not checked, allowing shortcuts to
	 * contain the same key multiple times and to be composed of as many
	 * keys as you can fit into the string (max length is 255 bytes).
	 * @param inputMode The new voice input mode
	 * @param callback Callback to process the returned {@link Result}
	 * @see #setInputMode(VoiceInputMode)
	 * @see #getInputMode()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#setinputmode">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#setinputmode</a>
	 */
	public void setInputMode(VoiceInputMode inputMode, Consumer<Result> callback)
	{
		throw new RuntimeException("not implemented");
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
		return core.voiceData.isSelfMute();
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
		throw new RuntimeException("not implemented");
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
		return core.voiceData.isSelfDeaf();
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
		throw new RuntimeException("not implemented");
	}

	/**
	 * Checks if a user with a given ID is locally muted by the current user.
	 * @param userId ID of the user to check
	 * @return {@code true} if the user is locally muted
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #setLocalMute(long, boolean)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#islocalmute">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#islocalmute</a>
	 */
	public boolean isLocalMute(long userId)
	{
		return core.voiceData.getLocalMutes().contains(Long.toString(userId));
	}

	/**
	 * Locally mutes or unmutes the user with the given ID.
	 * @param userId ID of the user to (un)mute
	 * @param mute {@code true} to mute the user, {@code false} to unmute the user
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #isLocalMute(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#setlocalmute">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#setlocalmute</a>
	 */
	public void setLocalMute(long userId, boolean mute)
	{
		throw new RuntimeException("not implemented");
	}

	/**
	 * Gets the local volume adjustment for the user with the given ID.
	 * <p>
	 * A volume of {@code 100} is default.
	 * A volume lower than that means that the volume for the given user
	 * is reduced (a volume of {@code 0} means no sound at all).
	 * If the volume is higher than the default, it is boosted
	 * (up to {@code 200} which is the maximal boost).
	 * @param userId ID of the user to get the volume adjustment for
	 * @return The volume adjustment in percent, an integer in percent between 0 and 200
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #setLocalVolume(long, int)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#getlocalvolume">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#getlocalvolume</a>
	 */
	public int getLocalVolume(long userId)
	{
		return core.voiceData.getLocalVolumes().get(Long.toString(userId));
	}

	/**
	 * Adjust the volume for a given user id locally.
	 * <p>
	 * A volume of {@code 100} is default.
	 * A volume lower than that means that the volume for the given user
	 * is reduced (a volume of {@code 0} means no sound at all).
	 * If the volume is higher than the default, it is boosted
	 * (up to {@code 200} which is the maximal boost).
	 * @param userId ID of the user to adjust the volume for
	 * @param volume New volume adjustment in percent, an integer from 0 to 200
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLocalVolume(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#setlocalvolume">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#setlocalvolume</a>
	 */
	public void setLocalVolume(long userId, int volume)
	{
		if(volume < 0 || volume > 200)
			throw new IllegalArgumentException("volume out of range: "+volume);

		throw new RuntimeException("not implemented");
	}
}
