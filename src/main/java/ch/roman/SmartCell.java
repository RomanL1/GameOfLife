package ch.roman;

import java.util.ArrayList;
import java.util.List;

public class SmartCell
{
	private final int x;
	private final int y;
	private final List<Cell> neighbours;
	private boolean isAlive;
	private short aliveNeighbours;

	private SmartCell ( int x, int y, boolean isAlive )
	{
		this.x = x;
		this.y = y;
		this.isAlive = isAlive;
		this.neighbours = new ArrayList<>();
	}

	public static SmartCell from ( int _x, int _y, Board board )
	{
		SmartCell smartCell = new SmartCell( _x, _y, board.isCellAlive( _x, _y ) );
		for ( int x = _x - 1; x <= _x + 1; x++ )
		{
			for ( int y = _y - 1; y <= _y + 1; y++ )
			{
				if ( x == _x && y == _y )
				{
					continue;
				}
				if ( x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize() )
				{
					continue;
				}
				smartCell.neighbours.add( Cell.of( x, y, board.isCellAlive( x, y ) ) );
			}
		}
		smartCell.aliveNeighbours = (short) smartCell.neighbours.stream().filter( Cell::isAlive ).count();
		return smartCell;
	}

	//Any live cell with fewer than two live neighbours dies, as if by underpopulation.
	public boolean isLiveAndHasLessThanTwoLiveNeighbours ()
	{
		return isAlive && aliveNeighbours < 2;
	}

	//Any live cell with two or three live neighbours lives on to the next generation.
	public boolean isLiveAndHasTwoOrThreeLiveNeighbours ()
	{
		return isAlive && ( aliveNeighbours == 2 || aliveNeighbours == 3 );
	}

	//Any live cell with more than three live neighbours dies, as if by overpopulation.
	public boolean isLiveAndHasMoreThanThreeLiveNeighbours ()
	{
		return isAlive && aliveNeighbours > 3;
	}

	//Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	public boolean isDeadAndHasExactlyThreeLiveNeighbours ()
	{
		return !isAlive && aliveNeighbours == 3;
	}

	public int x ()
	{
		return x;
	}

	public int y ()
	{
		return y;
	}

	public void smartUpdate ( Board board )
	{
		for ( Cell cell : neighbours )
		{
			cell.setAlive( board.isCellAlive( cell.x(), cell.y() ) );
		}
		this.isAlive = board.isCellAlive( x, y );
		this.aliveNeighbours = (short) this.neighbours.stream().filter( Cell::isAlive ).count();
	}

}
