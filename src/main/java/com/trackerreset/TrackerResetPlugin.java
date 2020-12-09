package com.trackerreset;

import com.google.inject.Provides;
import com.trackerreset.ui.TrackerResetPanel;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
	name = "Tracker Reset"
)
public class TrackerResetPlugin extends Plugin
{
	private TrackerResetPanel panel;
	private NavigationButton navButton;

	@Inject
	private XpTrackerPlugin xpTrackerPlugin;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private Client client;

	@Inject
	private TrackerResetConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Tracker Reset started!");

		panel = new TrackerResetPanel(this);

		//final BufferedImage header = ImageUtil.getResourceStreamFromClass(getClass(), "panel_icon.png");
		//panel.loadHeaderIcon(header);
		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "panel_icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Tracker Reset")
				.icon(icon)
				.priority(5)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Tracker Reset stopped!");
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Tracker Reset says " + config.greeting(), null);
		}
	}

	@Provides
	TrackerResetConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TrackerResetConfig.class);
	}

	public void resetTrackers()
	{
		log.info("Resetting trackers!");
		//xpTrackerPlugin.resetAndInitState();
	}
}
