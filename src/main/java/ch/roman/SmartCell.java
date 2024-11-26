package ch.roman;

public class SmartCell
{
	private static final int[][] NEIGHBOR_OFFSETS = {
			{ -1, -1 }, { -1, 0 }, { -1, 1 },
			{ 0, -1 }, { 0, 1 },
			{ 1, -1 }, { 1, 0 }, { 1, 1 }
	};
	private final int x;
	private final int y;
	private boolean isAlive;
	private short aliveNeighbours;

	public SmartCell ( int x, int y, boolean isAlive )
	{
		this.x = x;
		this.y = y;
		this.isAlive = isAlive;
	}

	public void smartUpdate ( boolean[][] currentState, int size )
	{
		isAlive = currentState[x][y];
		int count = 0;
		for ( int[] offset : NEIGHBOR_OFFSETS )
		{
			int nx = x + offset[0];
			int ny = y + offset[1];
			if ( nx >= 0 && ny >= 0 && nx < size && ny < size && currentState[nx][ny] )
			{
				count++;
			}
		}
		aliveNeighbours = (short) count;
	}

	public CellPopulationEvent applyRules ()
	{
		boolean nextState = isAlive;
		if ( isAlive && ( aliveNeighbours < 2 || aliveNeighbours > 3 ) )
		{
			nextState = false;
		}
		else if ( !isAlive && aliveNeighbours == 3 )
		{
			nextState = true;
		}

		if ( nextState != isAlive )
		{
			return new CellPopulationEvent( x, y, nextState );
		}
		return null;
	}
}
