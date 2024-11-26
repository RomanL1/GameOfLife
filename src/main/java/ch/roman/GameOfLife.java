package ch.roman;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameOfLife implements Runnable
{
	private final int size;
	private final BlockingQueue<Board> guiQueue;
	private GameOfLifeGUI gui;
	private final Board initialBoard;
	private final AtomicBoolean running = new AtomicBoolean( false );

	public GameOfLife ( int size )
	{
		this.size = size;
		this.guiQueue = new LinkedBlockingQueue<>();
		this.initialBoard = Board.ofRandom( size );
	}

	public void setGUI ( GameOfLifeGUI gui )
	{
		this.gui = gui;
	}

	@Override
	public void run ()
	{
		running.set( true );

		BlockingQueue<Board> futureBoards = new LinkedBlockingQueue<>(10);
		BoardPopulationWorker boardPopulationWorker = new BoardPopulationWorker( initialBoard, futureBoards, size, 8 );
		new Thread( boardPopulationWorker ).start();

		while ( true )
		{
			try
			{
				var took = futureBoards.take();
				log.info( "Took board with generation: " + took.getGeneration() );
				guiQueue.offer( took );
			}
			catch ( InterruptedException e )
			{
				throw new RuntimeException( e );
			}

			// Repaint the GUI
			if ( gui != null )
			{
				Util.sleep( 5 );
				//SwingUtilities.invokeLater( gui::repaint );
				SwingUtilities.invokeLater( gui::printBoard );
				log.info( "Repaint the GUI" );
			}
		}
	}

	public Board getNextBoard () throws InterruptedException
	{
		if ( running.get() )
		{
			return guiQueue.take();
		}
		else
		{
			return initialBoard;
		}
	}

}
