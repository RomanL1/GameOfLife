package ch.roman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardPopulationWorker implements Runnable
{
	private final Board board;
	private final int size;
	private final int threads;

	private final ExecutorService executor;
	private final BlockingQueue<Board> futureBoards;
	private final List<Range> ranges;

	public BoardPopulationWorker ( Board board, BlockingQueue<Board> concurrentQueue, int size,
			int threads )
	{
		this.board = board;
		this.futureBoards = concurrentQueue;
		this.executor = Executors.newFixedThreadPool( threads );
		this.size = size;
		this.threads = threads;

		int listSize = size * size;
		var ranges = new ArrayList<Range>();

		if ( listSize % threads == 0 )
		{
			int rangeSize = listSize / threads;
			for ( int i = 0; i < threads; i++ )
			{
				ranges.add( new Range( i * rangeSize, ( i + 1 ) * rangeSize ) );
			}
		}
		else
		{
			int rangeSize = listSize / threads;
			for ( int i = 0; i < threads - 1; i++ )
			{
				ranges.add( new Range( i * rangeSize, ( i + 1 ) * rangeSize ) );
			}
			ranges.add( new Range( ( threads - 1 ) * rangeSize, listSize ) );
		}

		this.ranges = Collections.unmodifiableList( ranges );
	}

	@Override
	public void run ()
	{
		long starTime = System.currentTimeMillis();
		long renderCount = 0;
		var smartBoard = new ArrayList<SmartCell>();

		for ( int x = 0; x < size; x++ )
		{
			for ( int y = 0; y < size; y++ )
			{
				smartBoard.add( SmartCell.from( x, y, board ) );
			}
		}

		Board currentBoard = board;
		while ( true )
		{
			final Board board = currentBoard;
			for ( int i = 0; i < smartBoard.size(); i++ )
			{
				smartBoard.get( i ).smartUpdate( board );
			}

			var futures = new ArrayList<Future<List<CellPopulationEvent>>>();
			for ( Range range : ranges )
			{
				List<SmartCell> smartCells = smartBoard.subList( range.start, range.end );
				futures.add( executor.submit( new CellPopulationWorker( smartCells ) ) );
			}

			var result = new ArrayList<CellPopulationEvent>();
			futures.forEach( future -> {
				try
				{
					result.addAll( future.get() );
				}
				catch ( Exception e )
				{
					log.error( "Error", e );
				}
			} );

			Board nextBoard = board.copyForNewGeneration();
			result.forEach( event -> {
				if ( event.isAlive() )
				{
					nextBoard.makeCellAlive( event.x(), event.y() );
				}
				else
				{
					nextBoard.makeCellDie( event.x(), event.y() );
				}
			} );

			try
			{
				futureBoards.put( nextBoard );
			}
			catch ( InterruptedException e )
			{
				throw new RuntimeException( e );
			}
			currentBoard = nextBoard;

			//Util.sleep( 1000 );

			renderCount++;
			if ( renderCount % 100 == 0 )
			{
				long endTime = System.currentTimeMillis();
				long duration = endTime - starTime;
				log.info( "Calculated {} times in {} ms", renderCount, duration );
				//render per second
				log.info( "Calculations per second: {}", ( renderCount * 1000 ) / duration );
				log.info( "Alive cells: {}", board.aliveCells() );
				starTime = endTime;
				renderCount = 0;
			}
		}
	}

	private record Range(int start, int end)
	{
	}
}
