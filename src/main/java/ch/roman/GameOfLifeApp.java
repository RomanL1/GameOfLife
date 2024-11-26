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
		GameOfLifeGUI panel = new GameOfLifeGUI( gameOfLife, size, scale );

		// Set the GUI in GameOfLife
		gameOfLife.setGUI( panel );

		JFrame frame = new JFrame( "Game of Life" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.add( panel );
		frame.pack();
		frame.setLocationRelativeTo( null ); // Center the frame
		frame.setVisible( true );
		frame.setResizable( false );

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

					}
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
