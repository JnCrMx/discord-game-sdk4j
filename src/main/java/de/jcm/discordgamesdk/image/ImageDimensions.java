package de.jcm.discordgamesdk.image;

import de.jcm.discordgamesdk.ImageManager;

/**
 * <p>Structure providing information about the size/dimensions of a Discord image.</p>
 * <p>This structure is only returned from
 * {@link ImageManager#getDimensions(ImageHandle)} and thus has
 * neither a public constructor nor any setter methods.</p>
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/images#data-models-imagedimensions-struct">
 *     https://discordapp.com/developers/docs/game-sdk/images#data-models-imagedimensions-struct</a>
 */
public class ImageDimensions
{
	private final int width;
	private final int height;

	ImageDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	/**
	 * Gets the width (horizontal dimension) of the image.
	 * @return The width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets the height (vertical dimension) of the image.
	 * @return The height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * <p>Generates a string representation of the image dimensions containing all its attributes.</p>
	 * <p>This is just one of <i>IntelliJ IDEA</i>'s default {@code toString()}-Methods,
	 * so don't expect anything special.</p>
	 * @return A string representation of the image dimensions
	 */
	@Override
	public String toString()
	{
		return "ImageDimensions{" +
				"width=" + width +
				", height=" + height +
				'}';
	}
}
