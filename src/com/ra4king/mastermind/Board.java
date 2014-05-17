package com.ra4king.mastermind;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JOptionPane;

import com.ra4king.gameutils.BasicScreen;

public class Board extends BasicScreen {
	private final int GREEN = 1, YELLOW = 2, RED = 3, BLUE = 4, WHITE = 5, BLACK = 6;
	private final int SIMILAR = 7, EXACT = 8, CORRECT = 9;
	
	private final int OFFSET = 90;
	
	private int hidden[];
	private int tries[][];
	private int results[][];
	private int currentRow, colorSelected;
	private Ellipse2D.Double triesPegs[][];
	private Ellipse2D.Double colors[];
	private Ellipse2D.Double go;
	private RoundRectangle2D.Double clear, giveUp;
	private boolean isGoDown, isClearDown, isGiveUpDown;
	private int gameState;
	
	public Board() {
		triesPegs = new Ellipse2D.Double[10][4];
		colors = new Ellipse2D.Double[6];
		clear = new RoundRectangle2D.Double(150,15,60,20,10,10);
		giveUp = new RoundRectangle2D.Double(230,15,80,20,10,10);
		
		for(int a = 0; a < triesPegs.length; a++)
			for(int b = 0; b < triesPegs[a].length; b++)
				triesPegs[a][b] = new Ellipse2D.Double(OFFSET+b*50, 60+a*50, 30, 30);
		
		reset();
	}
	
	private void reset() {
		hidden = new int[4];
		tries = new int[10][4];
		results = new int[10][4];
		
		gameState = currentRow = colorSelected = 0;
		
		go = new Ellipse2D.Double(OFFSET+195,60,30,30);
		
		for(int a = 0; a < colors.length; a++)
			colors[a] = new Ellipse2D.Double(10+(a%3)*25,57+(a/3)*25,20,20);
		
		randomize();
	}
	
	private void randomize() {
		for(int a = 0; a < hidden.length; a++) {
			hidden[a] = 0;
			
random:		while(hidden[a] == 0) {
				int c = (int)(Math.random()*6+1);
				
				for(int b = 0; b < hidden.length; b++)
					if(b != a && c == hidden[b])
						continue random;
				hidden[a] = c;
			}
		}
	}
	
	public void update(long deltaTime) {
		if(gameState > 0) {
			if(gameState < 4) {
				gameState += 3;
			}
			else {
				if(gameState == 4)
					JOptionPane.showMessageDialog(getGame(), "You lost!!","Game Over",JOptionPane.OK_OPTION);
				else if(gameState == 5)
					JOptionPane.showMessageDialog(getGame(), "You won!!","Game Over",JOptionPane.OK_OPTION);
				else
					JOptionPane.showMessageDialog(getGame(), "Game Over!!","Game Over",JOptionPane.OK_OPTION);
				
				reset();
			}
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(150,90,20));
		g.fillRect(0,0,getGame().getWidth(),getGame().getHeight());
		
		g.setColor(new Color(0,0,0,50));
		g.fillRect(0, 55+currentRow*50, getGame().getWidth(), 50);
		
		for(int a = 0; a < tries.length; a++) {
			for(int b = 0; b < tries[a].length; b++) {
				switch(tries[a][b]) {
					case GREEN: g.setColor(Color.green); break;
					case YELLOW: g.setColor(Color.yellow); break;
					case RED: g.setColor(Color.red); break;
					case BLUE: g.setColor(Color.blue); break;
					case WHITE: g.setColor(Color.white); break;
					case BLACK: g.setColor(Color.black); break;
					default: g.setColor(new Color(0,0,0,0));
				}
				
				g.fill(triesPegs[a][b]);
				
				g.setColor(Color.black);
				g.draw(triesPegs[a][b]);
			}
		}
		
		if(isGoDown)
			g.setColor(Color.white);
		else
			g.setColor(Color.black);
		g.fill(go);
		
		if(isGoDown)
			g.setColor(Color.black);
		else
			g.setColor(Color.white);
		
		int x[] = {(int)go.x+10,(int)go.x+10,(int)go.x+25};
		int y[] = {(int)go.y+5,(int)go.y+25,(int)go.y+15};
		g.fill(new Polygon(x,y,3));
		
		for(int a = 0; a < results.length; a++) {
			for(int b = 0; b < results[a].length; b++) {
				switch(results[a][b]) {
					case SIMILAR: g.setColor(Color.white); break;
					case EXACT: g.setColor(Color.black); break;
					case CORRECT: g.setColor(Color.blue); break;
					default: g.setColor(new Color(0,0,0,0));
				}
				
				g.fillOval(OFFSET+240+(b%2)*20,60+a*50+(b/2)*20,10,10);
				
				g.setColor(Color.black);
				g.drawOval(OFFSET+240+(b%2)*20,60+a*50+(b/2)*20,10,10);
			}
		}
		
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,25));
		
		for(int a = 0; a < 4; a++) {
			if(gameState == 0) {
				g.setColor(Color.gray);
				g.fillOval(OFFSET+a*50, 555, 30, 30);
				g.setColor(Color.white);
				g.drawString("?",OFFSET+a*50+8, 580);
			}
			else {
				switch(hidden[a]) {
					case GREEN: g.setColor(Color.green); break;
					case YELLOW: g.setColor(Color.yellow); break;
					case RED: g.setColor(Color.red); break;
					case BLUE: g.setColor(Color.blue); break;
					default:
					case WHITE: g.setColor(Color.white); break;
					case BLACK: g.setColor(Color.black); break;
				}
				g.fillOval(OFFSET+a*50, 555, 30, 30);
				
				g.setColor(Color.black);
				g.drawOval(OFFSET+a*50, 555, 30, 30);
			}
		}
		
		g.setColor(Color.green);
		g.fill(colors[0]);
		g.setColor(Color.yellow);
		g.fill(colors[1]);
		g.setColor(Color.red);
		g.fill(colors[2]);
		g.setColor(Color.blue);
		g.fill(colors[3]);
		g.setColor(Color.white);
		g.fill(colors[4]);
		g.setColor(Color.black);
		g.fill(colors[5]);
		
		if(colorSelected == 5)
			g.setColor(Color.white);
		else
			g.setColor(Color.black);
		Ellipse2D.Double e = colors[colorSelected];
		e.setFrame(e.x-1,e.y-1,e.width+2,e.height+2);
		g.setStroke(new BasicStroke(2));
		g.draw(e);
		e.setFrame(e.x+1,e.y+1,e.width-2,e.height-2);
		
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
		
		RoundRectangle2D.Double r = clear;
		
		if(isClearDown)
			g.setColor(Color.white);
		else
			g.setColor(Color.black);
		
		g.fill(r);
		
		if(isClearDown)
			g.setColor(Color.black);
		else
			g.setColor(Color.white);
		
		String s = "Clear";
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(s);
		int height = fm.getHeight();
		g.drawString(s, Math.round(r.x + (r.width-width)/2), Math.round(r.y + (r.height+height)/2 - fm.getDescent()));
		
		r = giveUp;
		
		if(isGiveUpDown)
			g.setColor(Color.white);
		else
			g.setColor(Color.black);
		
		g.fill(r);
		
		if(isGiveUpDown)
			g.setColor(Color.black);
		else
			g.setColor(Color.white);
		
		s = "I give up :(";
		fm = g.getFontMetrics();
		width = fm.stringWidth(s);
		height = fm.getHeight();
		g.drawString(s, Math.round(r.x + (r.width-width)/2), Math.round(r.y + (r.height+height)/2 - fm.getDescent()));
	}
	
	public void mousePressed(MouseEvent me) {
		if(me.getButton() == MouseEvent.BUTTON1) { 
			if(gameState > 0)
				return;
			
			for(int a = 0; a < colors.length; a++)
				if(colors[a].contains(me.getPoint())) {
					colorSelected = a;
					return;
				}
			
			for(int a = 0; a < triesPegs[currentRow].length; a++)
				if(triesPegs[currentRow][a].contains(me.getPoint())) {
					for(int b = 0; b < tries[currentRow].length; b++)
						if(a != b && tries[currentRow][b] == colorSelected+1)
							tries[currentRow][b] = 0;
					
					tries[currentRow][a] = colorSelected+1;
					return;
				}
			
			if(clear.contains(me.getPoint()))
				isClearDown = true;
			
			if(giveUp.contains(me.getPoint()))
				isGiveUpDown = true;
			
			if(go.contains(me.getPoint()))
				isGoDown = true;
		}
		else if(me.getButton() == MouseEvent.BUTTON3)
			colorSelected = (colorSelected+1)%colors.length;
	}
	
	public void mouseReleased(MouseEvent me) {
		isClearDown = false;
		isGiveUpDown = false;
		isGoDown = false;
		
		if(clear.contains(me.getPoint())) {
			for(int a = 0; a < tries[currentRow].length; a++)
				tries[currentRow][a] = 0;
		}
		else if(giveUp.contains(me.getPoint()))
			gameState = 3;
		else if(go.contains(me.getPoint())) {
			int exact = 0, similar = 0;
			
			for(int a = 0; a < tries[currentRow].length; a++) {
				int c = tries[currentRow][a];
				
				if(c == 0) {
					JOptionPane.showMessageDialog(getGame(), "Incomplete row!");
					return;
				}
				
				if(c == hidden[a])
					exact++;
				else {
					for(int b = 0; b < hidden.length; b++)
						if(b != a && c == hidden[b]) {
							similar++;
							break;
						}
				}
			}
			
			if(exact == 4) {
				gameState = 2;
				results[currentRow][0] = CORRECT;
				currentRow++;
			}
			else {
				int count = 0;
				while(exact-- > 0)
					results[currentRow][count++] = EXACT;
				while(similar-- > 0)
					results[currentRow][count++] = SIMILAR;
				
				currentRow++;
				
				if(currentRow == tries.length) {
					currentRow--;
					gameState = 1;
				}
				
				go.y += 50;
				
				for(Ellipse2D.Double e : colors)
					e.y += 50;
			}
		}
	}
}
