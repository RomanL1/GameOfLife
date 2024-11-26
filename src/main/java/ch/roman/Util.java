package ch.roman;

public class Util
{
	public static void sleep ( int millis )
	{
		try
		{
			Thread.sleep( millis );
		}
		catch ( InterruptedException e )
		{
			Thread.currentThread().interrupt();
			throw new RuntimeException( e );
		}
	}
}
