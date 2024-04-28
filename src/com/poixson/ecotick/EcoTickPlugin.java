package com.poixson.ecotick;

import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.configuration.file.FileConfiguration;

import com.poixson.tools.xJavaPlugin;


public class EcoTickPlugin extends xJavaPlugin {
	@Override public int getSpigotPluginID() { return 107938; }
	@Override public int getBStatsID() {       return 17533;  }

	protected static final long DEFAULT_LAG_DELAY = 30L;

	protected final AtomicReference<LaggerTask> lagger = new AtomicReference<LaggerTask>(null);



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
		this.saveConfigs();
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
		super.loadConfigs();
		// config.yml
		{
			final FileConfiguration cfg = this.getConfig();
			this.config.set(cfg);
			this.configDefaults(cfg);
			cfg.options().copyDefaults(true);
		}
	}
	@Override
	protected void saveConfigs() {
		super.saveConfig();
	}
	@Override
	protected void configDefaults(final FileConfiguration cfg) {
		super.configDefaults(cfg);
		cfg.addDefault("Lag Delay Seconds", Long.valueOf(DEFAULT_LAG_DELAY));
	}



	public long getLagDelay() {
		return this.config.get().getLong("Lag Delay Seconds");
	}



}
