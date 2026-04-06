package com.whh.hloveyq.web.vm;

public class LevelCount {
	private final int level;
	private final int count;
	private final int percent;

	public LevelCount(int level, int count, int percent) {
		this.level = level;
		this.count = count;
		this.percent = percent;
	}

	public int getLevel() {
		return level;
	}

	public int getCount() {
		return count;
	}

	public int getPercent() {
		return percent;
	}
}

