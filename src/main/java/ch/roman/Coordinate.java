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
		final int prime1 = 0x9e3779b9; // Large prime (Golden ratio)
		final int prime2 = 0x85ebca6b; // Another large prime

		int h = x;
		h ^= y + prime1 + (h << 6) + (h >> 2); // Mix x and y
		h *= prime2; // Spread values with prime multiplication

		return h;
	}

	@Override
	public String toString ()
	{
		return "Coordinate [x=" + x + ", y=" + y + "]";
	}
}
