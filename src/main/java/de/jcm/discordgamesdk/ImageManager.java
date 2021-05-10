package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.image.ImageDimensions;
import de.jcm.discordgamesdk.image.ImageHandle;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Manager to receive information about images and the image data itself.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images">
 *     https://discordapp.com/developers/docs/game-sdk/images</a>
 */
public class ImageManager
{
	private final long pointer;
	private final Core core;

	ImageManager(long pointer, Core core)
	{
		this.pointer = pointer;
		this.core = core;
	}

	/**
	 * Prepares an image to later retrieve it.
	 * @param handle Handle identifying the image
	 * @param refresh whether to refresh the (possibly) cached image
	 * @param callback Callback to handle result and returned handle
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images#fetch">
	 *     https://discordapp.com/developers/docs/game-sdk/images#fetch</a>
	 */
	public void fetch(ImageHandle handle, boolean refresh, BiConsumer<Result, ImageHandle> callback)
	{
		core.execute(()->fetch(pointer,
		      handle.getType().ordinal(),
		      handle.getId(),
		      handle.getSize(),
		      refresh,
		      Objects.requireNonNull(callback)));
	}

	/**
	 * Fetches the dimensions (size) of an image.
	 * @param handle Handle identifying the image
	 * @return The fetches dimensions
	 * @throws GameSDKException if something went wrong fetching the image dimensions
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images#getdimensions">
	 *     https://discordapp.com/developers/docs/game-sdk/images#getdimensions</a>
	 */
	public ImageDimensions getDimensions(ImageHandle handle)
	{
		Object ret = core.execute(()->getDimensions(pointer, handle.getType().ordinal(),
		                           handle.getId(), handle.getSize()));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (ImageDimensions) ret;
		}
	}

	/**
	 * <p>Gets the image data for a given image.</p>
	 * <p>The data is automatically put in a Java byte array.</p>
	 * <p>The length of this array and thus the length of the allocated space is automatically
	 * calculated based on the dimensions.</p>
	 * @param handle Handle identifying the image
	 * @param dimensions Dimensions of the image
	 * @return The image data
	 * @throws GameSDKException if something went wrong fetching the image data
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images#getdata">
	 *     https://discordapp.com/developers/docs/game-sdk/images#getdata</a>
	 */
	public byte[] getData(ImageHandle handle, ImageDimensions dimensions)
	{
		return getData(handle, dimensions.getWidth()*dimensions.getHeight()*4);
	}

	/**
	 * <p>Gets the image data for a given image.</p>
	 * <p>The data is automatically put in a Java byte array of the given length.</p>
	 * <p>Prefer {@link #getData(ImageHandle, ImageDimensions)} and retrieve the dimensions
	 * with {@link #getDimensions(ImageHandle)}.</p>
	 * @param handle Handle identifying the image
	 * @param length Length of the image data, should be {@code width*height*4}
	 * @return The image data
	 * @throws GameSDKException if something went wrong fetching the image data
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images#getdata">
	 *     https://discordapp.com/developers/docs/game-sdk/images#getdata</a>
	 */
	public byte[] getData(ImageHandle handle, int length)
	{
		Object ret = core.execute(()->getData(pointer, handle.getType().ordinal(),
		                           handle.getId(), handle.getSize(), length));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (byte[]) ret;
		}
	}

	/**
	 * <p>Gets the image data for a give image with {@link #getData(ImageHandle, ImageDimensions)}
	 * and writes it into a {@link BufferedImage}'s raster.</p>
	 * @param handle Handle identifying the image
	 * @param dimensions Dimensions of the image
	 * @return A BufferedImage containing the image data
	 * @throws GameSDKException if something went wrong fetching the image data
	 */
	public BufferedImage getAsBufferedImage(ImageHandle handle, ImageDimensions dimensions)
	{
		byte[] data = getData(handle, dimensions);

		BufferedImage image = new BufferedImage(dimensions.getWidth(),
		                                         dimensions.getHeight(),
		                                         BufferedImage.TYPE_4BYTE_ABGR);
		image.getRaster().setDataElements(0, 0,
		                                   dimensions.getWidth(), dimensions.getHeight(),
		                                   data);

		return image;
	}

	/**
	 * <p>Obtains the image dimensions from {@link #getDimensions(ImageHandle)} and then constructs
	 * a {@link BufferedImage} using {@link #getAsBufferedImage(ImageHandle, ImageDimensions)}.
	 * @param handle Handle identifying the image
	 * @return A BufferedImage containing the image data
	 * @throws GameSDKException if something went wrong fetching the image dimensions or data
	 */
	public BufferedImage getAsBufferedImage(ImageHandle handle)
	{
		ImageDimensions dimensions = getDimensions(handle);
		return getAsBufferedImage(handle, dimensions);
	}

	private native void fetch(long pointer, int type, long id, int size,
	                          boolean refresh, BiConsumer<Result, ImageHandle> callback);
	private native Object getDimensions(long pointer, int type, long id, int size);
	private native Object getData(long pointer, int type, long id, int size, int length);
}
