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
		Random random = new Random( /*69420*/ );
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
//		// Center offsets
//		int xOffset = size / 2;
//		int yOffset = size / 2;
//
//		// Define the pattern (relative to the center of the grid)
//		//print from lexicon 119P4H1V0
//		int[][] pattern = {
//				{ 0, 3 },{ 1, 3 },{ 0, 4 },{ 1, 4 },
//				{ 0, 7 },{ 1, 7 },{ 0, 8 },{ 1, 8 },
//
//				{ 3, 1 },{ 3, 2 },{ 3, 3 },{ 3, 4 },{ 3, 5 },{ 3, 6 },{ 3, 7 },{ 3, 8},{ 3, 9 },{ 3, 10},
//				{ 4, 0 },{ 5, 0 },{ 5, 1 },
//				{ 4, 11 },{ 5, 11 },{ 5, 10 },
//
//				{ 5, 4 },{ 5, 5 },{ 5, 6 },{ 5, 7 },
//
//				{ 7, 5 },{ 7, 6 },{ 10, 5 },{ 10, 6 },{ 8, 4 },{ 9, 4 },{ 8, 7 },{ 9, 7 },
//
//				{ 12, 4 },{ 12, 5 },{ 12, 6 },{ 12, 7 },
//
//				{ 14, 1 },{ 14, 2 },{ 14, 3 },{ 14, 4 },{ 14, 5 },{ 14, 6 },{ 14, 7 },{ 14, 8},{ 14, 9 },{ 14, 10},
//				{ 12, 0 },{ 12, 1 },{ 13, 0 },
//				{ 12, 11 },{ 12, 10 },{ 13, 11 },
//
//				{ 16, 3 },{ 17, 3 },{ 16, 4 },{ 17, 4 },
//				{ 16, 7 },{ 17, 7 },{ 16, 8 },{ 17, 8 },
//		};
//
//		// Add each live cell to the aliveCells set
//		for (int[] coordinate : pattern) {
//			int x = xOffset + coordinate[0];
//			int y = yOffset + coordinate[1];
//			aliveCells.add(Coordinate.of(x, y));
//		}
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

	public boolean[][] getStateArray ()
	{
		boolean[][] state = new boolean[size][size];
		for ( Coordinate coordinate : aliveCells )
		{
			state[coordinate.getX()][coordinate.getY()] = true;
		}
		return state;
	}

}
