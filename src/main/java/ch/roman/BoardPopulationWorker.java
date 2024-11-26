package ch.roman;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardPopulationWorker implements Runnable
{
	private final Board board;
	private final int size;

	private final ExecutorService executor;
	private final BlockingQueue<Board> futureBoards;

	public BoardPopulationWorker ( Board board, BlockingQueue<Board> concurrentQueue, int size,
			int threads )
	{
		this.board = board;
		this.futureBoards = concurrentQueue;
		this.executor = Executors.newFixedThreadPool( threads );
		this.size = size;
	}

	@Override
	public void run ()
	{
		long startTime = System.currentTimeMillis();
		long renderCount = 0;

		// Initialize smartBoard as an array for better performance
		SmartCell[] smartBoard = new SmartCell[size * size];
		int index = 0;
		for ( int x = 0; x < size; x++ )
		{
			for ( int y = 0; y < size; y++ )
			{
				smartBoard[index++] = new SmartCell( x, y, board.isCellAlive( x, y ) );
			}
		}

		Board currentBoard = board;
		while ( true )
		{
			final boolean[][] currentState = currentBoard.getStateArray();

			// Parallel processing of smartUpdate
			Arrays.stream( smartBoard ).parallel().forEach( cell -> cell.smartUpdate( currentState, size ) );

			// Apply Game of Life rules and collect events
			List<CellPopulationEvent> events = Arrays.stream( smartBoard ).parallel()
					.map( cell -> cell.applyRules() )
					.filter( Objects::nonNull )
					.collect( Collectors.toList() );

			Board nextBoard = currentBoard.copyForNewGeneration();
			for ( CellPopulationEvent event : events )
			{
				if ( event.isAlive() )
				{
					nextBoard.makeCellAlive( event.x(), event.y() );
				}
				else
				{
					nextBoard.makeCellDie( event.x(), event.y() );
				}
			}

			try
			{
				futureBoards.put( nextBoard );
				//log.info( "Generated board with generation: " + nextBoard.getGeneration() );
			}
			catch ( InterruptedException e )
			{
				throw new RuntimeException( e );
			}
			currentBoard = nextBoard;

			renderCount++;
			if ( renderCount % 100 == 0 )
			{
				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime;
				log.info( "Calculated {} times in {} ms", renderCount, duration );
				log.info( "Calculations per second: {}", ( renderCount * 1000 ) / duration );
				log.info( "Alive cells: {}", currentBoard.aliveCells() );
				startTime = endTime;
				renderCount = 0;
			}
		}
	}
}
