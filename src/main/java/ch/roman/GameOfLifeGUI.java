package ch.roman;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameOfLifeGUI extends JPanel
{

	private final GameOfLife gameOfLife;
	private final int size;
	private final int cellSize; // Adjust as needed
	private BufferedImage bufferedImage;

	public GameOfLifeGUI ( GameOfLife gameOfLife, int size, int scale )
	{
		this.gameOfLife = gameOfLife;
		this.size = size;
		this.cellSize = scale;
		int width = size * cellSize;
		int height = size * cellSize;
		setPreferredSize( new Dimension( width, height ) );
	}

	@Override
	protected void paintComponent ( Graphics g )
	{
		super.paintComponent( g );

		// Initialize or update the buffered image
		if ( bufferedImage == null || bufferedImage.getWidth() != getWidth()
				|| bufferedImage.getHeight() != getHeight() )
		{
			Board board = null;
			try
			{
				board = gameOfLife.getNextBoard();
				log.info( "Start board with generation: " + board.getGeneration() );
			}
			catch ( InterruptedException e )
			{
				throw new RuntimeException( e );
			}
			bufferedImage = new BufferedImage( getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB );
			renderBufferedImage( board );
		}

		// Draw the buffered image
		g.drawImage( bufferedImage, 0, 0, null );
	}

	private void renderBufferedImage ( Board board )
	{
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED );

		for ( int x = 0; x < size; x++ )
		{
			for ( int y = 0; y < size; y++ )
			{
				g2d.setColor( board.isCellAlive( x, y ) ? Color.WHITE : Color.BLACK );
				int x1 = x * cellSize;
				int y1 = y * cellSize;
				g2d.fillRect( x1, y1, cellSize, cellSize );
			}
		}

		g2d.dispose(); // Dispose of the graphics context
	}

	// Call this method to update the buffered image when the board state changes
	public void printBoard ()
	{
		Board board = null;
		try
		{
			board = gameOfLife.getNextBoard();
			log.info( "Updating buffered image with generation: " + board.getGeneration() );
		}
		catch ( InterruptedException e )
		{
			throw new RuntimeException( e );
		}

		renderBufferedImage( board );
		repaint();
	}

}

