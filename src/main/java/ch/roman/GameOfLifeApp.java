package ch.roman;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

public class GameOfLifeApp
{

	public static void main ( String[] args )
	{
		int size = 1000; // Adjust size as needed
		int scale = 1; // Adjust scale as needed

		// Create the game instance
		GameOfLife gameOfLife = new GameOfLife( size );

		// Create the GUI with the game board
		GameOfLifeGUI gui = new GameOfLifeGUI( gameOfLife, size, scale );

		// Set the GUI in GameOfLife
		gameOfLife.setGUI( gui );

		JFrame frame = new JFrame( "Game of Life" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.add( gui );
		frame.pack();
		frame.setLocationRelativeTo( null ); // Center the frame
		frame.setVisible( true );
		frame.setResizable( false );
		//print first board
		SwingUtilities.invokeLater( gui::printBoard );

		AtomicBoolean started = new AtomicBoolean( false );
		//		Util.sleep( 1000 );
		//		new Thread( gameOfLife ).start();
		frame.addKeyListener( new KeyListener()
		{
			@Override
			public void keyTyped ( KeyEvent e )
			{

				if ( e.getKeyChar() == ' ' )
				{
					if ( !started.get() )
					{
						started.set( true );
						new Thread( gameOfLife ).start();
						return;
					}
					gameOfLife.triggerNext();
				}
				else if ( e.getKeyChar() == '1' )
				{
					gameOfLife.modifyPauseInMs( -5 );
				}
				else if ( e.getKeyChar() == '2' )
				{
					gameOfLife.modifyPauseInMs( 5 );
				}
				else if ( e.getKeyChar() == '3' )
				{
					gameOfLife.resetPauseInMs();
				}
				else if ( e.getKeyChar() == '0' )
				{
					gameOfLife.zeroPauseInMs();
				}
				else if ( e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE )
				{
					System.exit( 0 );
				}
			}

			@Override
			public void keyPressed ( KeyEvent e )
			{

			}

			@Override
			public void keyReleased ( KeyEvent e )
			{

			}
		} );
	}
}
