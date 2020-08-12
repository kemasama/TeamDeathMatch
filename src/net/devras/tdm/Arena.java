package net.devras.tdm;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.devras.tdm.event.ArenaStartEvent;
import net.devras.tdm.event.ArenaStopEvent;
import net.devras.tdm.util.TeamHelper;
import net.devras.tdm.util.Title;

public class Arena {

	/**
	 * Options
	 */

	private static Arena Instance;
	public static Arena getInstance() {
		return Instance;
	}

	/**
	 * Configuration
	 */
	public static int INT_GAME_TIME = 300;
	public static int INT_LOBBY_TIME = 60;
	public static int KILL_POINT = 80;
	public static int KILL_MONEY = 100;

	public Arena() {
		Instance = this;

		Kills.put("Red", 0);
		Kills.put("Blue", 0);
		Kills.put("Green", 0);
		Kills.put("Yellow", 0);

		task = new MainTask(this);
		armor = new Armor();

		Bukkit.getScheduler().runTaskTimer(Game.Instance, task, 0L, 20L);
	}

	public MainTask task;

	public Location lobby;
	public Location team1, team2, team3, team4;

	public HashMap<UUID, Boolean> Players = new HashMap<>();
	public HashMap<String, Integer> Kills = new HashMap<>();

	public HashMap<UUID, Boolean> Protection = new HashMap<>();

	public boolean isInGame = false;

	public int CRT_GAME_TIME = INT_GAME_TIME;
	public int CRT_LOBBY_TIME = INT_LOBBY_TIME;

	public int CurrentTeam = 0;

	public Armor armor;

	/**
	 * Override Method
	 */

	public void Start() {
		CRT_LOBBY_TIME = INT_LOBBY_TIME;
		CRT_GAME_TIME = INT_GAME_TIME;

		if (Players.size() < 2) {
			return;
		}

		isInGame = true;

		Title t = new Title("§eGame Start");
		for (UUID key : Players.keySet()) {
			Player p = Bukkit.getPlayer(key);
			if (p != null) {
				t.send(p);
				armor.equip(p);
				int id = TeamHelper.getEntry(p);
				switch (id) {
				case 1:
					p.teleport(team1);
					break;
				case 2:
					p.teleport(team2);
					break;
				case 3:
					p.teleport(team3);
					break;
				case 4:
					p.teleport(team4);
					break;
				default:
					break;
				}

				p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
			}
		}

		ArenaStartEvent event = new ArenaStartEvent(Players.keySet());
		Bukkit.getPluginManager().callEvent(event);
	}

	public void Stop() {
		isInGame = false;
		CRT_LOBBY_TIME = INT_LOBBY_TIME;
		CRT_GAME_TIME = INT_GAME_TIME;

		String team = getMostKillTeam();
		String tName = team;
		String member = "";
		if (team == null) {
			team = "Drow";
			tName = "Drow";
			member = " - ";
		}else {
			for (UUID key : Players.keySet()) {
				Player p = Bukkit.getPlayer(key);
				if (p != null) {
					int id = TeamHelper.getEntry(p);
					String tname = "";
					switch (id) {
					case 1:
						tname = "Red";
						break;
					case 2:
						tname = "Blue";
						break;
					case 3:
						tname = "Green";
						break;
					case 4:
						tname = "Yellow";
						break;
					default:
						break;
					}

					if (!tname.isEmpty() && team.equals(tname)) {
						if (member.isEmpty()) {
							member = p.getName();
						}else {
							member += ", " + p.getName();
						}
						int point = Game.Point.containsKey(key) ? Game.Point.get(key) : 0;
						point += 40;
						Game.Point.put(key, point);
					}
				}
			}

			team = team + " won the game";
		}

		Title t = new Title("§cGame Over", team);
		for (UUID key : Players.keySet()) {
			Player p = Bukkit.getPlayer(key);
			if (p != null) {
				int point = Game.Point.containsKey(key) ? Game.Point.get(key) : 0;
				point += 1;
				Game.Point.put(key, point);
				int money = Game.Money.containsKey(key) ? Game.Money.get(key) : 0;

				int id = TeamHelper.getEntry(p);
				String tname = "";
				switch (id) {
				case 1:
					tname = "Red";
					break;
				case 2:
					tname = "Blue";
					break;
				case 3:
					tname = "Green";
					break;
				case 4:
					tname = "Yellow";
					break;
				default:
					break;
				}

				t.send(p);
				p.sendMessage("§7---------------------------");
				p.sendMessage("    §bThank for playing!");
				p.sendMessage("  §6Win§7: " + tName);
				p.sendMessage("  §6Member§7: §c" + member);
				p.sendMessage("  §eYou earn total point ");
				p.sendMessage("     §a" + point + " §ePoint");
				p.sendMessage("     §a" + money + " §eCoins");
				p.sendMessage("     §c" + (Kills.containsKey(tname) ? Kills.get(tname) : 0) + " §eKills");
				p.sendMessage("§7---------------------------");
				Game.sendLobby(p);
			}
		}

		Kills.put("Red", 0);
		Kills.put("Blue", 0);
		Kills.put("Green", 0);
		Kills.put("Yellow", 0);

		ArenaStopEvent event = new ArenaStopEvent(Players.keySet());
		Bukkit.getPluginManager().callEvent(event);

		TeamHelper.Init();
	}

	public String getMostKillTeam() {
		String team = null;

		int current = 0;
		for (String key : Kills.keySet()) {
			if (Kills.get(key) > current) {
				team = key;
				current = Kills.get(key);
			}
		}

		return team;
	}

	public void addPlayer(Player p) {
		CurrentTeam++;
		if (CurrentTeam > 4) {
			CurrentTeam = 1;
		}

		TeamHelper.addPlayer(p, CurrentTeam);
		//Protection.put(p.getUniqueId(), false);
	}
	public void removePlayer(Player p) {
		TeamHelper.removePlayer(p);
		if (Protection.containsKey(p.getUniqueId())) {
			Protection.remove(p.getUniqueId());
		}
	}

}
