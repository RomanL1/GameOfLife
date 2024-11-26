package ch.roman;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Cell
{
	private final Coordinate coordinate;
	@Setter
	private boolean isAlive;

	private Cell ( Coordinate coordinate, boolean isAlive )
	{
		this.coordinate = coordinate;
		this.isAlive = isAlive;
	}

	private Cell ( Cell cell )
	{
		this.coordinate = cell.coordinate;
		this.isAlive = cell.isAlive;
	}

	public static Cell of ( int x, int y )
	{
		return Cell.of( x, y, false );
	}

	public static Cell of ( int x, int y, boolean isAlive )
	{
		return new Cell( Coordinate.of( x, y ), isAlive );
	}

	public int x ()
	{
		return coordinate.getX();
	}

	public int y ()
	{
		return coordinate.getY();
	}

}
