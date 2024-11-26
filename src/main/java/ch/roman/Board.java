package ch.roman;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

public class Board
{
	@Getter
	private final int size;
	private final Set<Coordinate> aliveCells;
	@Getter
	private final int generation;

	private Board ( int size )
	{
		this.size = size;
		aliveCells = ConcurrentHashMap.newKeySet();
		Random random = new Random( 69420 );
		for ( int i = 0; i < size; i++ )
		{
			for ( int j = 0; j < size; j++ )
			{
				if ( random.nextDouble() > 0.5 )
				{
					aliveCells.add( Coordinate.of( i, j ) );
				}
			}
		}
		generation = 0;
	}

	private Board ( int size, int generation )
	{
		this.size = size;
		this.aliveCells = ConcurrentHashMap.newKeySet();
		this.generation = generation;
	}

	public static Board ofRandom ( int size )
	{
		return new Board( size );
	}

	public static Board ofGeneration ( int size, int generation )
	{
		return new Board( size, generation );
	}

	public boolean isCellAlive ( int x, int y )
	{
		return aliveCells.contains( Coordinate.of( x, y ) );
	}

	public void makeCellAlive ( int x, int y )
	{
		aliveCells.add( Coordinate.of( x, y ) );
	}

	public void makeCellDie ( int x, int y )
	{
		aliveCells.remove( Coordinate.of( x, y ) );
	}

	public int aliveCells ()
	{
		return aliveCells.size();
	}

	public Board copyForNewGeneration ()
	{
		Board board = new Board( size, generation + 1 );
		board.aliveCells.addAll( aliveCells );
		return board;
	}

}
