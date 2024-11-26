package ch.roman;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CellPopulationWorker implements Callable<List<CellPopulationEvent>>
{
	private final List<SmartCell> list;
	//	private final Board board;

	public CellPopulationWorker ( List<SmartCell> list )
	{
		this.list = list;
		//		this.board = board;
	}

	@Override
	public List<CellPopulationEvent> call () throws Exception
	{
		var result = new ArrayList<CellPopulationEvent>();
		for ( SmartCell smartCell : list )
		{
			if ( smartCell.isLiveAndHasLessThanTwoLiveNeighbours() )
			{
				//				board.setCell( smartCell.x(), smartCell.y(), false );
				result.add( new CellPopulationEvent( smartCell.x(), smartCell.y(), false ) );
				//					log.info( "isLiveAndHasLessThanTwoLiveNeighbours" );
			}
			else if ( smartCell.isLiveAndHasTwoOrThreeLiveNeighbours() )
			{
				// Cell remains alive
				//					log.info( "isLiveAndHasTwoOrThreeLiveNeighbours" );
			}
			else if ( smartCell.isLiveAndHasMoreThanThreeLiveNeighbours() )
			{
				//				board.setCell( smartCell.x(), smartCell.y(), false );
				result.add( new CellPopulationEvent( smartCell.x(), smartCell.y(), false ) );
				//					log.info( "isLiveAndHasMoreThanThreeLiveNeighbours" );
			}
			else if ( smartCell.isDeadAndHasExactlyThreeLiveNeighbours() )
			{
				//				board.setCell( smartCell.x(), smartCell.y(), true );
				result.add( new CellPopulationEvent( smartCell.x(), smartCell.y(), true ) );
				//					log.info( "isDeadAndHasExactlyThreeLiveNeighbours" );
			}
		}
		return result;
	}
}
