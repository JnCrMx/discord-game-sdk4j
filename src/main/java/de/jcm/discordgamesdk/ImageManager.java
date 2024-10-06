package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.image.ImageDimensions;
import de.jcm.discordgamesdk.image.ImageHandle;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.commands.GetImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Manager to receive information about images and the image data itself.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images">
 *     https://discordapp.com/developers/docs/game-sdk/images</a>
 */
public class ImageManager
{
	private final Core.CorePrivate core;
	private final Map<ImageHandle, BufferedImage> imageCache = new HashMap<>();

	ImageManager(Core.CorePrivate core)
	{
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
		if(!refresh && imageCache.containsKey(handle))
		{
			callback.accept(Result.OK, handle);
		}
		else
		{
			core.sendCommand(Command.Type.GET_IMAGE, new GetImage.Args(handle), c->{
				Result r = core.checkError(c);
				if(r != Result.OK)
				{
					callback.accept(r, null);
					return;
				}
				try
				{
					GetImage.Response response = core.getGson().fromJson(c.getData(), GetImage.Response.class);
					byte[] data = response.getData();
					BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
					imageCache.put(handle, img);
					callback.accept(r, handle);
				}
				catch(IOException e)
				{
					core.log(LogLevel.ERROR, e.toString());
					callback.accept(Result.INTERNAL_ERROR, handle);
				}
			});
		}
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
		if(!imageCache.containsKey(handle))
			throw new GameSDKException(Result.NOT_FETCHED);
		BufferedImage img = imageCache.get(handle);
		return new ImageDimensions(img.getWidth(), img.getHeight());
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
		if(!imageCache.containsKey(handle))
			throw new GameSDKException(Result.NOT_FETCHED);
		BufferedImage img = imageCache.get(handle);
		byte[] data = new byte[length];
		return (byte[]) img.getRaster().getDataElements(0, 0, data);
	}

	/**
	 * <p>Gets the image data for a give image with {@link #getData(ImageHandle, ImageDimensions)}
	 * and writes it into a {@link BufferedImage}'s raster.</p>
	 * @param handle Handle identifying the image
	 * @param dimensions Dimensions of the image
	 * @return A BufferedImage containing the image data
	 * @throws GameSDKException if something went wrong fetching the image data
	 */
	@Deprecated
	public BufferedImage getAsBufferedImage(ImageHandle handle, ImageDimensions dimensions)
	{
		return getAsBufferedImage(handle);
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
		if(!imageCache.containsKey(handle))
			throw new GameSDKException(Result.NOT_FETCHED);
		return imageCache.get(handle);
	}
}
