package net.runelite.client.plugins.config;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ConfigServiceImpl implements ConfigService
{
	private final ConfigPlugin plugin;

	@Inject
	ConfigServiceImpl(ConfigPlugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public void openConfig(String pluginName)
	{
		plugin.openConfig(pluginName);
	}
}
