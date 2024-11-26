package ch.roman;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameOfLife implements Runnable
{
	private final int size;
	private final BlockingQueue<Board> guiQueue;
	private final Board initialBoard;
	private final AtomicBoolean running = new AtomicBoolean( false );
	private final AtomicBoolean triggerNext = new AtomicBoolean( true );
	private final AtomicInteger pauseInMs = new AtomicInteger( 33 );
	private GameOfLifeGUI gui;

	public GameOfLife ( int size )
	{
		this.size = size;
		this.guiQueue = new LinkedBlockingQueue<>( 10 );
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

		BlockingQueue<Board> futureBoards = new LinkedBlockingQueue<>( 10 );
		BoardPopulationWorker boardPopulationWorker = new BoardPopulationWorker( initialBoard, futureBoards, size, 8 );
		new Thread( boardPopulationWorker ).start();

		while ( true )
		{
			try
			{
				var took = futureBoards.take();
				//log.info( "Took board with generation: " + took.getGeneration() );
				guiQueue.put( took );
			}
			catch ( InterruptedException e )
			{
				throw new RuntimeException( e );
			}

			// Repaint the GUI
			if ( gui != null )
			{
				if ( pauseInMs.get() > 0 )
				{
					Util.sleep( pauseInMs.get() );
				}
				while ( !triggerNext.get() )
				{
				}
				//triggerNext.set( false );
				//SwingUtilities.invokeLater( gui::repaint );
				SwingUtilities.invokeLater( gui::printBoard );
				//log.info( "Repaint the GUI" );
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

	public void triggerNext ()
	{
		triggerNext.set( !triggerNext.get() );
	}

	public void modifyPauseInMs ( int by )
	{
		if ( pauseInMs.get() + by > 0 )
		{
			pauseInMs.addAndGet( by );
		}
	}

	public void resetPauseInMs ()
	{
		if ( pauseInMs.get() > 0 )
		{
			pauseInMs.set( 33 );
		}
	}

	public void zeroPauseInMs ()
	{
		if ( pauseInMs.get() > 0 )
		{
			this.pauseInMs.set( 0 );
		}
	}

}
