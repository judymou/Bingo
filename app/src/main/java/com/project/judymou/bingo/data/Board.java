package com.project.judymou.bingo.data;

import java.util.List;

public class Board {
	String name;
	List<String> items;

	public Board() {}

	public Board(String name, List<String> items) {
		this.name = name;
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public List<String> getItems() {
		return items;
	}
}
