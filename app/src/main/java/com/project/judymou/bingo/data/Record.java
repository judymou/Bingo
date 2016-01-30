package com.project.judymou.bingo.data;

public class Record {
	private String imageContent;
	private String imageText;

	public Record() {}

	public Record(String imageContent, String imageText) {
		this.imageContent = imageContent;
		this.imageText = imageText;
	}

	public String getImageContent() {
		return imageContent;
	}

	public String getImageText() {
		return imageText;
	}
}
