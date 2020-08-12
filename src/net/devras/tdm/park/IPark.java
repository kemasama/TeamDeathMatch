package net.devras.tdm.park;

import net.devras.tdm.Game;

public interface IPark {
	public boolean isActive();
	public void onEnable();
	public void onDisable();

	public Game getInstance();

	public String getPermission();
	public String getName();
	public String getVersion();
	public String getDescription();
	public int getNeedCoin();
}
