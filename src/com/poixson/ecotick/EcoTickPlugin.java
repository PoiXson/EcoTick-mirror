package com.poixson.ecotick;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;


public class EcoTickPlugin extends xJavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");
	public static final String LOG_PREFIX  = "[EcoTick] ";

	protected static final long DEFAULT_LAG_DELAY = 30L;

	protected static final AtomicReference<EcoTickPlugin> instance = new AtomicReference<EcoTickPlugin>(null);

	protected final AtomicReference<LaggerTask> lagger = new AtomicReference<LaggerTask>(null);

	@Override public int getSpigotPluginID() { return 107938; }
	@Override public int getBStatsID() {       return 17533;  }



	public EcoTickPlugin() {
		super(EcoTickPlugin.class);
	}



	@Override
	public void onEnable() {
		super.onEnable();
		// lagger task
		{
			final long delay = this.getLagDelay();
			final LaggerTask task = new LaggerTask(this, delay);
			final LaggerTask previous = this.lagger.getAndSet(task);
			if (previous != null)
				previous.stop();
			task.start();
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		// lagger task
		{
			final LaggerTask task = this.lagger.getAndSet(null);
			if (task != null)
				task.stop();
		}
	}



	// -------------------------------------------------------------------------------
	// configs



	@Override
	protected void loadConfigs() {
		this.mkPluginDir();
		// config.yml
		{
			final FileConfiguration cfg = this.getConfig();
			this.config.set(cfg);
			this.configDefaults(cfg);
			cfg.options().copyDefaults(true);
			super.saveConfig();
		}
	}
	@Override
	protected void saveConfigs() {
		// config.yml
		super.saveConfig();
	}
	@Override
	protected void configDefaults(final FileConfiguration cfg) {
		cfg.addDefault("Lag Delay Seconds", Long.valueOf(DEFAULT_LAG_DELAY));
	}



	public long getLagDelay() {
		return this.config.get().getLong("Lag Delay Seconds");
	}



}