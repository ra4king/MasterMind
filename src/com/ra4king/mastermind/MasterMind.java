package com.ra4king.mastermind;

import com.ra4king.gameutils.Game;

public class MasterMind extends Game {
	private static final long serialVersionUID = -2301134907282976467L;
	
	public static void main(String args[]) {
		MasterMind game = new MasterMind();
		game.setupFrame("MasterMind", true);
		game.start();
	}
	
	public MasterMind() {
		super(400,600);
	}
	
	protected void initGame() {
		showFPS(false);
		setScreen("Board",new Board());
	}
	
	protected boolean stopGame() {
		return true;
	}
}
