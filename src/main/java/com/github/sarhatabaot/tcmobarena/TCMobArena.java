package com.github.sarhatabaot.tcmobarena;

import media.xen.tradingcards.TradingCards;
import org.bukkit.plugin.java.JavaPlugin;

public final class TCMobArena extends JavaPlugin {
	@Override
	public void onEnable() {
		TradingCards tradingCards = (TradingCards) getServer().getPluginManager().getPlugin("TradingCards");
		getServer().getPluginManager().registerEvents(new MobArenaListener(tradingCards,this),this);
	}
}
