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
			result.add( smartCell.applyRules() );
		}
		return result;
	}
}
