package net.devras.tdm.util;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author crisdev333
 *
 */
public class TeamHelper {
    private static Scoreboard scoreboard;
    private static Objective sidebar;
    private static Objective playerList;
    private static Team team1, team2, team3, team4;

	@SuppressWarnings("deprecation")
	public static void Init() {

		if (scoreboard != null) {
			for (OfflinePlayer p : scoreboard.getPlayers()) {
				if (p != null && p.isOnline()) {
					p.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				}
			}
		}

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        playerList = scoreboard.registerNewObjective("player", "dummy");
        playerList.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        team1 = scoreboard.registerNewTeam("Team1");
        team1.setDisplayName("Red");
        team1.setPrefix("§c[RED]§r ");
        team1.setAllowFriendlyFire(false);
        team1.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        team2 = scoreboard.registerNewTeam("Team2");
        team2.setDisplayName("Blue");
        team2.setPrefix("§b[BLUE]§r ");
        team2.setAllowFriendlyFire(false);
        team2.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        team3 = scoreboard.registerNewTeam("Team3");
        team3.setDisplayName("Green");
        team3.setPrefix("§a[GREEN]§r ");
        team3.setAllowFriendlyFire(false);
        team3.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        team4 = scoreboard.registerNewTeam("Team4");
        team4.setDisplayName("Yellow");
        team4.setPrefix("§e[YELLOW]§r ");
        team4.setAllowFriendlyFire(false);
        team4.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

        // Create Teams
        for(int i=1; i<=15; i++) {
            Team team2 = scoreboard.registerNewTeam("SLOT_" + i);
            team2.addEntry(genEntry(i));
        }
	}

	@SuppressWarnings("deprecation")
	public static void addPlayer(Player player, int id) {
		switch (id) {
		case 1:
			team1.addPlayer(player);
			break;
		case 2:
			team2.addPlayer(player);
			break;
		case 3:
			team3.addPlayer(player);
			break;
		case 4:
			team4.addPlayer(player);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public static void removePlayer(Player player) {
		if (team1.hasPlayer(player)) {
			team1.removePlayer(player);
		}
		if (team2.hasPlayer(player)) {
			team2.removePlayer(player);
		}
		if (team3.hasPlayer(player)) {
			team3.removePlayer(player);
		}
		if (team4.hasPlayer(player)) {
			team4.removePlayer(player);
		}
	}

	@SuppressWarnings("deprecation")
	public static int getEntry(Player player) {
		int id = 0;

		if (team1.hasPlayer(player)) {
			id = 1;
		}
		if (team2.hasPlayer(player)) {
			id = 2;
		}
		if (team3.hasPlayer(player)) {
			id = 3;
		}
		if (team4.hasPlayer(player)) {
			id = 4;
		}

		return id;
	}

	public static Team getLittleTeam() {
		Team t = team1;
		int little = 0;

		for (Team team : scoreboard.getTeams()) {
			if (team.getName().startsWith("SLOT_")) {
				continue;
			}
			int cnt = team.getEntries().size();
			if (cnt > little) {
				t = team;
			}
		}

		return t;
	}

	@SuppressWarnings("deprecation")
	public static void setHealth(Player player, int amount) {
		playerList.getScore(player).setScore(amount);



		player.setScoreboard(scoreboard);
	}

    public static void setTitle(String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);
        sidebar.setDisplayName(title.length()>32 ? title.substring(0, 32) : title);
    }

    public static void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(slot);
        }

        text = ChatColor.translateAlternateColorCodes('&', text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public static void removeSlot(int slot) {
        String entry = genEntry(slot);
        if(scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    public static void setSlotsFromList(List<String> list) {
        while(list.size()>15) {
            list.remove(list.size()-1);
        }

        int slot = list.size();

        if(slot<15) {
            for(int i=(slot +1); i<=15; i++) {
                removeSlot(i);
            }
        }

        for(String line : list) {
            setSlot(slot, line);
            slot--;
        }
    }

    private static String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private static String getFirstSplit(String s) {
        return s.length()>16 ? s.substring(0, 16) : s;
    }

    private static String getSecondSplit(String s) {
        if(s.length()>32) {
            s = s.substring(0, 32);
        }
        return s.length()>16 ? s.substring(16) : "";
    }

}