package com.github.sarhatabaot.tcmobarena;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import media.xen.tradingcards.CardUtil;
import media.xen.tradingcards.TradingCards;
import media.xen.tradingcards.api.addons.AddonListener;
import media.xen.tradingcards.api.addons.TradingCardsAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public class MobArenaListener extends AddonListener {
	private final TradingCards tradingCards;

	public MobArenaListener(final TradingCardsAddon tradingCardsAddon, final TradingCards tradingCards) {
		super(tradingCardsAddon);
		this.tradingCards = tradingCards;
	}

	/*public MobArenaListener(final TradingCards tradingCards, final JavaPlugin plugin) {
		this.tradingCards = tradingCards;
		this.plugin = plugin;
	}*/

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		MobArena maPlugin = (MobArena) Bukkit.getServer().getPluginManager().getPlugin("MobArena");
		if (maPlugin == null || !maPlugin.isEnabled() || !tradingCardsAddon.getJavaPlugin().getConfig().getBoolean("disable-in-arena")) {
			return;
		}

		boolean drop = false;
		String worldName = "";
		List<String> worldBlackList = new ArrayList<>();

		if (event.getEntity().getKiller() != null) {
			Player p = event.getEntity().getKiller();
			drop = (!tradingCards.isOnList(p) || tradingCards.blacklistMode() != 'b') && (!tradingCards.isOnList(p) && tradingCards.blacklistMode() == 'b' || tradingCards.isOnList(p) && tradingCards.blacklistMode() == 'w');
			worldName = p.getWorld().getName();
			worldBlackList = tradingCards.getConfig().getStringList("World-Blacklist");
		}

		if (drop && !worldBlackList.contains(worldName)) {
			String rare = CardUtil.calculateRarity(event.getEntityType(), false);
			boolean cancelled = false;
			if (!rare.equalsIgnoreCase("None")) {
				if (tradingCards.getConfig().getBoolean("General.Spawner-Block") && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals(tradingCards.getConfig().getString("General.Spawner-Mob-Name"))) {
					tradingCards.debug("Mob came from spawner, not dropping card.");
					cancelled = true;
				}

				if (!cancelled) {
					event.getDrops().add(CardUtil.getRandomCard(rare, false));
				}
			}
		}

	}
}
