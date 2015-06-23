package com.basilsystems.data;

import java.util.ArrayList;

public class IrRemote {
	
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<String> getButtons() {
		return buttons;
	}

	public void setButtons(ArrayList<String> buttons) {
		this.buttons = buttons;
	}

	private int id;
	private ArrayList<String> buttons;
	
	public IrRemote(String name, int id, ArrayList<String> buttons){
		this.name = name;
		this.id = id;
		this.buttons  = buttons;
	}

}
