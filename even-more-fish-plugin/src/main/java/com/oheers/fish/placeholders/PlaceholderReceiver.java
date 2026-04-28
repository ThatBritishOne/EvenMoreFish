package com.oheers.fish.placeholders;

import com.oheers.fish.EvenMoreFish;
import com.oheers.fish.FishUtils;
import com.oheers.fish.competition.Competition;
import com.oheers.fish.database.model.user.UserReport;
import com.oheers.fish.messages.ConfigMessage;
import com.oheers.fish.messages.abstracted.EMFMessage;
import com.oheers.fish.placeholders.abstracted.EMFPlaceholder;
import com.oheers.fish.placeholders.impl.competition.CompetitionActivePlaceholder;
import com.oheers.fish.placeholders.impl.competition.CompetitionPlaceFishPlaceholder;
import com.oheers.fish.placeholders.impl.competition.CompetitionPlacePlayerPlaceholder;
import com.oheers.fish.placeholders.impl.competition.CompetitionPlaceSizePlaceholder;
import com.oheers.fish.placeholders.impl.competition.CompetitionTimeLeftPlaceholder;
import com.oheers.fish.placeholders.impl.competition.CompetitionTypeFormatPlaceholder;
import com.oheers.fish.placeholders.impl.competition.CompetitionTypePlaceholder;
import com.oheers.fish.placeholders.impl.database.player.TotalCompetitionsJoinedPlaceholder;
import com.oheers.fish.placeholders.impl.database.player.TotalCompetitionsWonPlaceholder;
import com.oheers.fish.placeholders.impl.database.player.TotalFishCaughtPlaceholder;
import com.oheers.fish.placeholders.impl.database.player.TotalFishSoldPlaceholder;
import com.oheers.fish.placeholders.impl.database.player.TotalMoneyEarnedPlaceholder;
import com.oheers.fish.placeholders.impl.player.CustomFishingBooleanPlaceholder;
import com.oheers.fish.placeholders.impl.player.CustomFishingStatusPlaceholder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Handles all PlaceholderAPI expansions for EvenMoreFish.
 */
public class PlaceholderReceiver extends PlaceholderExpansion {

    private static final Map<CompetitionType, ConfigMessage> COMPETITION_TYPE_MESSAGES = Map.of(
            CompetitionType.LARGEST_FISH, ConfigMessage.COMPETITION_TYPE_LARGEST,
            CompetitionType.LARGEST_TOTAL, ConfigMessage.COMPETITION_TYPE_LARGEST_TOTAL,
            CompetitionType.MOST_FISH, ConfigMessage.COMPETITION_TYPE_MOST,
            CompetitionType.SPECIFIC_FISH, ConfigMessage.COMPETITION_TYPE_SPECIFIC,
            CompetitionType.SPECIFIC_RARITY, ConfigMessage.COMPETITION_TYPE_SPECIFIC_RARITY,
            CompetitionType.SHORTEST_FISH, ConfigMessage.COMPETITION_TYPE_SHORTEST,
            CompetitionType.SHORTEST_TOTAL, ConfigMessage.COMPETITION_TYPE_SHORTEST_TOTAL,
            CompetitionType.RAMPAGE, ConfigMessage.COMPETITION_TYPE_RAMPAGE
    );

    private final EvenMoreFish plugin;
    private final List<EMFPlaceholder> handlers;

    public PlaceholderReceiver(@NotNull EvenMoreFish plugin) {
        this.plugin = plugin;
        this.handlers = createHandlers();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "emf";
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    private List<EMFPlaceholder> createHandlers() {
        return Stream.of(
            // Competition
            new CompetitionActivePlaceholder(),
            new CompetitionPlaceFishPlaceholder(),
            new CompetitionPlacePlayerPlaceholder(),
            new CompetitionPlaceSizePlaceholder(),
            new CompetitionTimeLeftPlaceholder(),
            new CompetitionTypeFormatPlaceholder(),
            new CompetitionTypePlaceholder(),

            // Database - Player
            new TotalCompetitionsJoinedPlaceholder(),
            new TotalCompetitionsWonPlaceholder(),
            new TotalFishCaughtPlaceholder(),
            new TotalFishSoldPlaceholder(),
            new TotalMoneyEarnedPlaceholder(),

            // Player
            new CustomFishingBooleanPlaceholder(),
            new CustomFishingStatusPlaceholder()
        ).toList();
    }

    @Override
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String identifier) {
        for (EMFPlaceholder handler : handlers) {
            if (handler.shouldProcess(identifier)) {
                return handler.parsePAPI(player, identifier);
            }
        }
        return null;
    }

}