package de.jcm.discordgamesdk.impl.commands;

import de.jcm.discordgamesdk.image.ImageHandle;

import java.util.Base64;

public class GetImage
{
	private GetImage() {}

	public static class Args
	{
		private final String type;
		private final String id;
		private final String format;
		private final int size;

		public Args(ImageHandle handle)
		{
			this(handle.getType().name().toLowerCase(), Long.toString(handle.getId()), "png", handle.getSize());
		}

		public Args(String type, String id, String format, int size)
		{
			this.type = type;
			this.id = id;
			this.format = format;
			this.size = size;
		}
	}

	public static class Response
	{
		private String data_url;

		public String getDataURL()
		{
			return data_url;
		}

		public byte[] getData()
		{
			if(!data_url.startsWith("data:image/png;base64,"))
				throw new IllegalArgumentException("not a data url");
			return Base64.getDecoder().decode(data_url.substring("data:image/png;base64,".length()));
		}
	}
}
