package com.github.sarhatabaot.tcmobarena;

import net.tinetwork.tradingcards.api.TradingCardsPlugin;
import net.tinetwork.tradingcards.api.addons.AddonListener;
import net.tinetwork.tradingcards.api.addons.TradingCardsAddon;
import net.tinetwork.tradingcards.api.model.DropType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;


public class MobArenaListener extends AddonListener {
    private final TradingCardsPlugin tradingCards;
    private CommentedConfigurationNode configurationNode;

    public MobArenaListener(final TradingCardsAddon tradingCardsAddon, final TradingCardsPlugin tradingCards) {
        super(tradingCardsAddon);
        this.tradingCards = tradingCards;
        try {
            this.configurationNode = YamlConfigurationLoader.builder()
                    .path(Path.of("config.yml"))
                    .build().load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!configurationNode.node("disable-in-arena").getBoolean()) {
            return;
        }

        if(event.getEntity().getKiller() == null) {
            return;
        }

        final Player player = event.getEntity().getKiller();
        if (!canDropPlayer(player) || !canDropWorld(player.getWorld())) {
            return;
        }

        DropType dropType = tradingCards.getDropTypeManager().getMobType(event.getEntityType());
        String rare = tradingCards.getCardManager().getRandomRarity(dropType, false);
        if ("none".equalsIgnoreCase(rare))
            return;

        event.getDrops().add(getRandomDrop(rare));
    }

    private ItemStack getRandomDrop(final String rarity) {
        if (configurationNode.node("use-active-series").getBoolean())
            return tradingCards.getCardManager().getRandomActiveCard(rarity).build(false);
        return tradingCards.getCardManager().getRandomCard(rarity).build(false);
    }

    private boolean canDropPlayer(Player player) {
        return tradingCards.getPlayerBlacklist().isAllowed(player);
    }

    private boolean canDropWorld(World world) {
        return tradingCards.getWorldBlacklist().isAllowed(world);
    }
}
