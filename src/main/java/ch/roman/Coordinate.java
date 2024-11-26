package ch.roman;

import java.util.Objects;

import lombok.Getter;

@Getter
public class Coordinate
{
	private final int x;
	private final int y;

	private Coordinate ( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	public static Coordinate of ( int x, int y )
	{
		return new Coordinate( x, y );
	}

	@Override
	public boolean equals ( Object o )
	{
		if ( !( o instanceof Coordinate that ) )
			return false;
		return x == that.x && y == that.y;
	}

	@Override
	public int hashCode ()
	{
		return Objects.hash( x, y );
	}

	@Override
	public String toString ()
	{
		return "Coordinate [x=" + x + ", y=" + y + "]";
	}
}
