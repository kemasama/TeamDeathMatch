package net.devras.tdm;

import java.util.ArrayList;

public class Rank {
	public static ArrayList<Rank> Ranks = new ArrayList<>();
	public static Rank getRank(int point) {
		Rank rank = Ranks.get(0);
		for (Rank ran : Ranks) {
			if (point > ran.getNeedLevel() && rank.getNeedLevel() < ran.getNeedLevel()) {
				rank = ran;
			}
		}

		return rank;
	}

	private String name;
	private int needLevel;

	public Rank(String name, int need) {
		this.name = name;
		this.needLevel = need;
		Ranks.add(this);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
}
