package userInterface.testInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double[] graphData;
	double min;
	double max;
	
	int iterations;
	int underflow;
	
	final int marginLeft = 40, marginRight = TestRequest.sizeX - 40;
	final int marginBottom = TestRequest.sizeY - 40, marginTop = 40;
	final int graphLeft = 120, graphRight = TestRequest.sizeX - 120;
	final int graphBottom = TestRequest.sizeY - 80, graphTop = 80;
	
	public GraphPanel(double[] graphData, int iterations, int underflow, double min, double max) {
		this.graphData = graphData;
		
		this.min = min;
		this.max = max;
		
		this.iterations = iterations;
		this.underflow = underflow;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		setupGraph(g2d);
		drawGraph(g2d);
		
	}
	
	public void drawGraph(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(2));
		
		double dX = (double)(graphRight - graphLeft) / graphData.length;
		
		for(int index = 1; index < graphData.length; index++) {
			double inverse = 1 - graphData[index];
			double inversePast = 1 - graphData[index - 1];
			
			double y1 = (graphBottom - graphTop) * inversePast + graphTop;
			double y2 = (graphBottom - graphTop) * inverse + graphTop;
			
			double x1 = dX * (index - 1) + graphLeft;
			double x2 = dX * (index) + graphLeft;
			
			int x1i = (int) Math.round(x1);
			int y1i = (int) Math.round(y1);
			int x2i = (int) Math.round(x2);
			int y2i = (int) Math.round(y2);
			
			g2d.drawLine(x1i, y1i, x2i, y2i);
		}
	}
	
	public void setupGraph(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(Color.black);
		g2d.drawLine(graphLeft, graphTop, graphLeft, graphBottom);
		g2d.drawLine(graphLeft, graphBottom, graphRight, graphBottom);
		g2d.drawLine(graphRight, graphTop, graphRight, graphBottom);
		g2d.drawLine(graphLeft, graphTop, graphRight, graphTop);
		
		int AxisLabelHeight = graphTop + ((graphBottom - graphTop) / 2);
		
		g2d.drawString("Price", marginLeft, AxisLabelHeight);
		g2d.drawString("" + max, marginLeft, graphTop);
		g2d.drawString("" + min, marginLeft, graphBottom);
		
		final int plotPoints = 5;
		
		int[] points = prepXAxis(plotPoints);
		
		int baseX = (graphRight - graphLeft) / plotPoints;
		
		for(int index = 0; index < points.length; index++) {
			g2d.drawString("" + points[index], graphLeft + (baseX * index), marginBottom - 20);
		}
		
		g2d.drawString("Iteration", (graphLeft + (graphRight - graphLeft) / 2) - 20, marginBottom - 10);
	
	}
	
	
	public int[] prepXAxis(int plotPoints) {
		int[] ret = new int[plotPoints + 1];
		ret[1] = 0;
		
		double base = (double) iterations / (plotPoints - 1);
		
		for(int index = 0; index < ret.length - 1; index++) {
			ret[index] = (int)((base * index) - underflow);
		}
		
		ret[ret.length - 1] = iterations;
		return ret;
	}
}
