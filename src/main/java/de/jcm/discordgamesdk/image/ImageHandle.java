package de.jcm.discordgamesdk.image;

import de.jcm.discordgamesdk.ImageManager;

import java.util.Objects;

/**
 * <p>Handle identifying a Discord image.</p>
 * <p>Mainly used for {@link ImageManager}.</p>
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images#data-models-imagehandle-struct">
 *     https://discordapp.com/developers/docs/game-sdk/images#data-models-imagehandle-struct</a>
 */
public class ImageHandle
{
	private ImageType type;
	private long id;
	private int size;

	/**
	 * Constructs a new ImageHandle.
	 * @param type Type or source of the image
	 * @param id ID of the user to get the avatar image of
	 * @param size Desired resolution of the image, a power of 2 between 16 and 256
	 * @throws IllegalArgumentException if the desired size is not allowed (s. a.)
	 */
	public ImageHandle(ImageType type, long id, int size)
	{
		this.type = Objects.requireNonNull(type);
		this.id = id;
		if(size < 16)
			throw new IllegalArgumentException("size is smaller than 16: "+size);
		if(size > 256)
			throw new IllegalArgumentException("size is greater than 2048: "+size);
		/*
		see & thanks to
		https://codereview.stackexchange.com/questions/172849/checking-if-a-number-is-power-of-2-or-not
		 */
		if((size & (size - 1)) != 0)
			throw new IllegalArgumentException("size is not a power of 2: "+size);
		this.size = size;
	}

	ImageHandle(int type, long id, int size)
	{
		this(ImageType.values()[type], id ,size);
	}

	/**
	 * Gets the type or source of the ImageHandle.
	 * @return The type/source.
	 */
	public ImageType getType()
	{
		return type;
	}

	/**
	 * Sets the type or source of the ImageHandle.
	 * @param type New type
	 */
	public void setType(ImageType type)
	{
		this.type = Objects.requireNonNull(type);
	}

	/**
	 * Gets the ID of the user to whose avatar this handle points to.
	 * @return A user ID
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * Sets the ID of the user to whose avatar this handle points to.
	 * @param id A user ID
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * Gets the desired resolution of the image.
	 * @return The desired resolution
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Sets the desired resolution of the image.
	 * @param size The desired resolution
	 */
	public void setSize(int size)
	{
		if((size & (size - 1)) != 0)
			throw new IllegalArgumentException("size is not a power of 2: "+size);
		this.size = size;
	}

	/**
	 * <p>Generates a string representation of the handle containing all its attributes.</p>
	 * <p>This is just one of <i>IntelliJ IDEA</i>'s default {@code toString()}-Methods,
	 * so don't expect anything special.</p>
	 * @return A string representation of the handle
	 */
	@Override
	public String toString()
	{
		return "ImageHandle{" +
				"type=" + type +
				", id=" + id +
				", size=" + size +
				'}';
	}
}
