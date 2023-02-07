package com.poixson.ecotick;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import com.poixson.commonmc.tools.plugin.xJavaPlugin;


public class EcoTickPlugin extends xJavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");
	public static final String LOG_PREFIX  = "[EcoTick] ";
	protected static final int SPIGOT_PLUGIN_ID = 0;
	protected static final int BSTATS_PLUGIN_ID = 17533;

	protected static final AtomicReference<EcoTickPlugin> instance = new AtomicReference<EcoTickPlugin>(null);

	protected final AtomicReference<LaggerTask> lagger = new AtomicReference<LaggerTask>(null);



	public EcoTickPlugin() {
		super(EcoTickPlugin.class);
	}



	@Override
	public void onEnable() {
		super.onEnable();
		// lagger task
		{
			final LaggerTask task = new LaggerTask(this);
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



	@Override
	protected int getSpigotPluginID() {
		return SPIGOT_PLUGIN_ID;
	}
	@Override
	protected int getBStatsID() {
		return BSTATS_PLUGIN_ID;
	}



}