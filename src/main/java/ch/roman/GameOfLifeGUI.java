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
	private final int cellSize;
	private BufferedImage bufferedImage;

	public GameOfLifeGUI ( GameOfLife gameOfLife, int size, int scale )
	{
		this.gameOfLife = gameOfLife;
		this.size = size;
		this.cellSize = scale;
		int width = size * cellSize;
		int height = size * cellSize;
		setPreferredSize( new Dimension( width, height ) );

		// Initialize the buffered image
		bufferedImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
	}

	@Override
	protected void paintComponent ( Graphics g )
	{
		super.paintComponent( g );

		// Draw the buffered image
		g.drawImage( bufferedImage, 0, 0, null );
	}

	public void printBoard ()
	{
		Board board = null;
		try
		{
			board = gameOfLife.getNextBoard();
			//log.info( "Updating buffered image with generation: " + board.getGeneration() );
		}
		catch ( InterruptedException e )
		{
			throw new RuntimeException( e );
		}

		renderBufferedImage( board );
		repaint();
	}

	private void renderBufferedImage ( Board board )
	{
		int width = size * cellSize;
		int height = size * cellSize;
		int[] pixels = new int[width * height];

		int aliveColor = Color.WHITE.getRGB();
		int deadColor = Color.BLACK.getRGB();

		boolean[][] state = board.getStateArray();

		for ( int y = 0; y < size; y++ )
		{
			int yOffset = y * cellSize;
			for ( int x = 0; x < size; x++ )
			{
				int color = state[x][y] ? aliveColor : deadColor;
				int xOffset = x * cellSize;
				// Fill the cell area in the pixels array
				for ( int dy = 0; dy < cellSize; dy++ )
				{
					int pixelIndex = ( yOffset + dy ) * width + xOffset;
					for ( int dx = 0; dx < cellSize; dx++ )
					{
						pixels[pixelIndex + dx] = color;
					}
				}
			}
		}

		bufferedImage.setRGB( 0, 0, width, height, pixels, 0, width );
	}
}

