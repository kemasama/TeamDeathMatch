package net.devras.tdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.devras.tdm.listener.BoosterListener;
import net.devras.tdm.listener.ChatEvent;
import net.devras.tdm.listener.CommonListener;
import net.devras.tdm.listener.DamageEvent;
import net.devras.tdm.listener.DamageListener;
import net.devras.tdm.listener.JoinListener;
import net.devras.tdm.listener.Spectate;
import net.devras.tdm.park.Park;
import net.devras.tdm.park.ParkManager;
import net.devras.tdm.park.common.DiaChestPlate;
import net.devras.tdm.park.common.GapplePark;
import net.devras.tdm.park.common.Knowledge;
import net.devras.tdm.park.common.PlayerZombie;
import net.devras.tdm.park.common.Regeneration;
import net.devras.tdm.park.common.SilverPark;
import net.devras.tdm.park.common.SpeedPark;
import net.devras.tdm.park.common.StartResistancePark;
import net.devras.tdm.park.common.StrengthPark;
import net.devras.tdm.park.common.UpgradeArmor;
import net.devras.tdm.park.common.UpgradeSword;
import net.devras.tdm.util.CustomConfig;
import net.devras.tdm.util.TeamHelper;


public class Game extends JavaPlugin {
	public static Game Instance;
	public static int boostPer = 1;

	public static final boolean STATIC_RESPAWN = true;
	public static HashMap<UUID, Integer> Point = new HashMap<>();
	public static HashMap<UUID, Integer> Money = new HashMap<>();
	public CustomConfig cConfig;
	public FileConfiguration config;

	@Override
	public void onDisable() {

		try {
			config.set("lobby", Arena.getInstance().lobby);
			config.set("team1", Arena.getInstance().team1);
			config.set("team2", Arena.getInstance().team2);
			config.set("team3", Arena.getInstance().team3);
			config.set("team4", Arena.getInstance().team4);
			config.set("INT_GAME_TIME", Arena.INT_GAME_TIME);
			config.set("INT_LOBBY_TIME", Arena.INT_LOBBY_TIME);
			config.set("KILL_POINT", Arena.KILL_POINT);
			config.set("KILL_MONEY", Arena.KILL_MONEY);

			cConfig.saveConfig();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (MySQL.isConnected()) {
			MySQL.disconnect();
		}

		ArrayList<String> Keys = new ArrayList<>();
		for (String park : ParkManager.getParks().keySet()) {
			Keys.add(park);
		}

		for (String park : Keys) {
			ParkManager.unReg(park);
		}

		super.onDisable();
	}

	@Override
	public void onEnable() {

		Instance = this;

		TeamHelper.Init();
		Arena arena = new Arena();

		cConfig = new CustomConfig("config.yml");
		cConfig.saveDefaultConfig();
		config = cConfig.getConfig();

		try {
			Arena.INT_GAME_TIME = config.getInt("INT_GAME_TIME", 500);
			Arena.INT_LOBBY_TIME = config.getInt("INT_LOBBY_TIME", 60);
			Arena.KILL_POINT = config.getInt("KILL_POINT", 80);
			Arena.KILL_MONEY = config.getInt("KILL_MONEY", 100);

			if (config.get("lobby") instanceof Location) {
				arena.lobby = (Location) config.get("lobby");
			}

			if (config.get("team1") instanceof Location) {
				arena.team1 = (Location) config.get("team1");
			}
			if (config.get("team2") instanceof Location) {
				arena.team2 = (Location) config.get("team2");
			}
			if (config.get("team3") instanceof Location) {
				arena.team3 = (Location) config.get("team3");
			}
			if (config.get("team4") instanceof Location) {
				arena.team4 = (Location) config.get("team4");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// MySQL
		try {
			MySQL.Config(config.getString("mysql.host"), config.getString("mysql.port"), config.getString("mysql.name"), config.getString("mysql.user"), config.getString("mysql.pass"));
			//SQL.Connect();

			for (int i = 0; i < 5; i++) {
				MySQL.Connect();
				if (MySQL.isConnected()) {
					break;
				}
			}

			if (MySQL.isConnected()) {
				if (!MySQL.tableExists("point")) {
					MySQL.createTable("point", "uuid varchar(36) not null primary key, point int not null default 0");
				}
				if (!MySQL.tableExists("money")) {
					MySQL.createTable("money", "uuid varchar(36) not null primary key, point int not null default 0");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		KillLog.Init();

		Rank.Ranks.clear();
		Rank.Ranks.add(new Rank("§7First Noon", 0));
		Rank.Ranks.add(new Rank("§dKemo", 250));
		Rank.Ranks.add(new Rank("§cFoolish Warrior", 760));
		Rank.Ranks.add(new Rank("§6Brain Muscle", 1210));
		Rank.Ranks.add(new Rank("§bAnt", 1810));
		Rank.Ranks.add(new Rank("§eSwordsman", 2260));
		Rank.Ranks.add(new Rank("§5Assalt", 2910));
		Rank.Ranks.add(new Rank("§aCombatant", 3260));
		Rank.Ranks.add(new Rank("§2Viper", 3910));
		Rank.Ranks.add(new Rank("§4Warrior", 4460));
		Rank.Ranks.add(new Rank("§9Killer", 5010));
		Rank.Ranks.add(new Rank("§bSoldier", 5510));
		Rank.Ranks.add(new Rank("§6Strong", 5960));
		Rank.Ranks.add(new Rank("§cPowerful Kemo", 6460));

		// bold
		Rank.Ranks.add(new Rank("§d§lNoon Master", 7010));
		Rank.Ranks.add(new Rank("§3§lCrocodile", 8010));
		Rank.Ranks.add(new Rank("§5§lAssasin", 9010));
		Rank.Ranks.add(new Rank("§9§lMidnight Moon", 10010));
		Rank.Ranks.add(new Rank("§b§lInvisible Soldier", 12510));
		Rank.Ranks.add(new Rank("§6§lLegendary Sword", 15010));
		Rank.Ranks.add(new Rank("§2§lGreat Warrior", 17510));
		Rank.Ranks.add(new Rank("§e§lKemostar", 20010));
		Rank.Ranks.add(new Rank("§4§lKemamon", 25010));
		Rank.Ranks.add(new Rank("§a§lGiraffe", 30010));
		Rank.Ranks.add(new Rank("§2§l§oDragon", 37510));
		Rank.Ranks.add(new Rank("§5§l§oWarLoad", 45010));
		Rank.Ranks.add(new Rank("§8§l§oGhost", 52510));
		Rank.Ranks.add(new Rank("§1§l§oChaos", 60010));
		Rank.Ranks.add(new Rank("§7§l§oAssasination Tactical Special Force", 67510));
		Rank.Ranks.add(new Rank("§c§l§oHero", 75010));
		Rank.Ranks.add(new Rank("§c§l§oT§6§l§oh§e§l§oe§a§l§oH§b§l§oe§9§l§or§5§l§oo", 100000));


		//Rank.Ranks.add(new Rank("§7§l§oEternal Noon", 1000000));
		//Rank.Ranks.add(new Rank("§c§lASTF", 10010));

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		ParkManager.reg(new Park());
		ParkManager.reg(new PlayerZombie());
		ParkManager.reg(new GapplePark());
		ParkManager.reg(new SilverPark());
		ParkManager.reg(new StrengthPark());
		ParkManager.reg(new Regeneration());
		ParkManager.reg(new SpeedPark());
		ParkManager.reg(new Knowledge());
		ParkManager.reg(new StartResistancePark());
		ParkManager.reg(new UpgradeSword());
		ParkManager.reg(new UpgradeArmor());
		ParkManager.reg(new DiaChestPlate());

		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new DamageEvent(), this);
		Bukkit.getPluginManager().registerEvents(new CommonListener(), this);
		Bukkit.getPluginManager().registerEvents(new Spectate(), this);
		Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SpectatePlayer(), this);
		Bukkit.getPluginManager().registerEvents(new ParkMenu(), this);

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "NL");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "NL", new BoosterListener());

		super.onEnable();
	}

	public static void addExp(Player p, int amount) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(p.getName());
			out.writeUTF(p.getUniqueId().toString());
			out.writeUTF(String.valueOf(amount));
			p.sendPluginMessage(Game.Instance, "NL", out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("tdm")) {
			if (args.length > 0) {
				if (!(sender instanceof Player)) {
					return true;
				}

				Player p = (Player) sender;

				if (args[0].equalsIgnoreCase("lobby")) {
					Arena.getInstance().lobby = p.getLocation();
					p.sendMessage("§eLobby Set Success!");
				}
				if (args[0].equalsIgnoreCase("team1")) {
					Arena.getInstance().team1 = p.getLocation();
					p.sendMessage("§eSet Success!");
				}
				if (args[0].equalsIgnoreCase("team2")) {
					Arena.getInstance().team2 = p.getLocation();
					p.sendMessage("§eSet Success!");
				}
				if (args[0].equalsIgnoreCase("team3")) {
					Arena.getInstance().team3 = p.getLocation();
					p.sendMessage("§eSet Success!");
				}
				if (args[0].equalsIgnoreCase("team4")) {
					Arena.getInstance().team4 = p.getLocation();
					p.sendMessage("§eSet Success!");
				}

				if (args[0].equalsIgnoreCase("point")) {
					if (args.length > 1) {
						Player pl = Bukkit.getPlayer(args[1]);
						if (pl != null) {
							int point = Game.Point.get(pl.getUniqueId());

							if (args.length > 2) {
								point += Integer.parseInt(args[2]);
							}else {
								point += 100;
							}

							Game.Point.put(pl.getUniqueId(), point);
							p.sendMessage("§aSuccess");
						}
					}
				}
				if (args[0].equalsIgnoreCase("start")) {
					Arena.getInstance().CRT_LOBBY_TIME = 0;
				}
				if (args[0].equalsIgnoreCase("end")) {
					Arena.getInstance().CRT_GAME_TIME = 0;
				}

				return true;

			}
			sender.sendMessage("TeamDeathMatch is running!");
			return true;
		}

		return super.onCommand(sender, command, label, args);
	}

	public static void sendLobby(Player p) {
		try {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF("lobby");
			p.sendPluginMessage(Game.Instance, "BungeeCord", out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showPlayer(Player target) {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.showPlayer(target);
			target.showPlayer(pl);
		}
	}


	public static void hidePlayer(Player target) {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.hidePlayer(target);
		}
	}


}
