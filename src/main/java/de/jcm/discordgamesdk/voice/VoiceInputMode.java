package de.jcm.discordgamesdk.voice;

import de.jcm.discordgamesdk.VoiceManager;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Structure representing an input configuration for Discord voice chat.
 * @see VoiceManager#getInputMode()
 * @see VoiceManager#setInputMode(VoiceInputMode, Consumer)
 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#data-models-inputmode-struct">
 *     https://discord.com/developers/docs/game-sdk/discord-voice#data-models-inputmode-struct</a>
 */
public class VoiceInputMode
{
	/**
	 * The type of an input mode specifying how voice transmission is activated.
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#data-models-inputmodetype-enum">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#data-models-inputmodetype-enum</a>
	 */
	public enum InputModeType
	{
		/**
		 * Discord tired to automatically detect when the user is talking.
		 */
		VOICE_ACTIVITY,
		/**
		 * The user need to hold a button while talking.
		 */
		PUSH_TO_TALK
		;

		private static final int OFFSET = 0;

		private int nativeValue()
		{
			return ordinal() + OFFSET;
		}

		private static InputModeType javaValue(int nativeValue)
		{
			return values()[nativeValue-OFFSET];
		}
	}

	private InputModeType type;
	private String shortcut;

	VoiceInputMode(int type, String shortcut)
	{
		this(InputModeType.javaValue(type), shortcut);
	}

	/**
	 * Constructs a new input mode with a type (VAD or PTT) and a hotkey for PTT.
	 * @param type The type of the input mode
	 * @param shortcut The shortcut for {@link InputModeType#PUSH_TO_TALK}
	 */
	public VoiceInputMode(InputModeType type, String shortcut)
	{
		this.type = type;
		this.shortcut = shortcut;
	}

	/**
	 * Gets the type of this input mode.
	 * @return An {@link InputModeType}
	 * @see #setType(InputModeType)
	 */
	public InputModeType getType()
	{
		return type;
	}

	/**
	 * Sets the type of this input mode.
	 * <p>
	 * This does not directly affect the input mode currently set in Discord.
	 * You need to set it using {@link VoiceManager#setInputMode(VoiceInputMode, Consumer)}.
	 * @param type The new {@link InputModeType}
	 * @see #getType()
	 */
	public void setType(InputModeType type)
	{
		this.type = type;
	}

	/**
	 * Gets the shortcut for {@link InputModeType#PUSH_TO_TALK}.
	 * <p>
	 * Key combinations can be expressed by concatenating two or more key names
	 * with a {@code '+'}, e.g. {@code "shift + p"} or {@code "ctrl + 5"}.
	 * @return A shortcut string in the format further described in the documentation,
	 *         max. 255 bytes
	 * @see #setShortcut(String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#data-models-shortcut-keys">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#data-models-shortcut-keys</a>
	 */
	public String getShortcut()
	{
		return shortcut;
	}

	/**
	 * Sets the shortcut for {@link InputModeType#PUSH_TO_TALK}.
	 * <p>
	 * Key combinations can be expressed by concatenating two or more key names
	 * with a {@code '+'}, e.g. {@code "shift + p"} or {@code "ctrl + 5"}.
	 * <p>
	 * This does not directly affect the input mode currently set in Discord.
	 * You need to set it using {@link VoiceManager#setInputMode(VoiceInputMode, Consumer)}.
	 * @param shortcut A new shortcut string in the format further described in the documentation,
	 *                 max. 255 bytes
	 * @see #getShortcut()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord-voice#data-models-shortcut-keys">
	 *     https://discord.com/developers/docs/game-sdk/discord-voice#data-models-shortcut-keys</a>
	 */
	public void setShortcut(String shortcut)
	{
		if(shortcut.getBytes(StandardCharsets.UTF_8).length >= 256)
			throw new IllegalArgumentException("max shortcut length is 255");
		this.shortcut = shortcut;
	}

	/**
	 * Creates a simple string representation of this input mode containing:
	 * <ul>
	 *     <li>the type ({@link #getType()})
	 *     <li>the shortcut ({@link #getShortcut()})
	 * </ul>
	 * <p>
	 * It is <b>not</b> recommended to use this method for serialization.
	 * It is only intended for debugging.
	 * <p>
	 * The methods uses the plain fields rather than the named getter methods.
	 * Overriding those will hence not change to return value of this method.
	 * @return A string representation of this input mode.
	 */
	@Override
	public String toString()
	{
		return "VoiceInputMode{" +
				"type=" + type +
				", shortcut='" + shortcut + '\'' +
				'}';
	}

	/**
	 * {@inheritDoc}
	 * @see Objects#equals(Object, Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		VoiceInputMode that = (VoiceInputMode) o;
		return type == that.type && Objects.equals(shortcut, that.shortcut);
	}

	/**
	 * {@inheritDoc}
	 * @see Objects#hash(Object...)
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(type, shortcut);
	}

	int getNativeType()
	{
		return getType().nativeValue();
	}
}
