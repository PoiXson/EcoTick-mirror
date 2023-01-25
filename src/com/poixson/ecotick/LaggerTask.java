package com.poixson.ecotick;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.utils.ThreadUtils;


public class LaggerTask extends BukkitRunnable {
	protected static final Logger log = EcoTickPlugin.log;
	protected static final String LOG_PREFIX = EcoTickPlugin.LOG_PREFIX;

	protected final EcoTickPlugin plugin;

	protected AtomicLong state = new AtomicLong(0L);



	public LaggerTask(final EcoTickPlugin plugin) {
		this.plugin = plugin;
	}



	public void start() {
		this.runTaskTimer(this.plugin, 200L, 20L);
	}
	public void stop() {
		try {
			this.cancel();
		} catch (IllegalStateException ignore) {}
	}



	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().size() == 0) {
			final long state = this.state.getAndIncrement();
			if (state == 0L)
				log.info(LOG_PREFIX + "Slowing the server..");
			if (state % 300L == 0)
				UnloadChunks();
			ThreadUtils.Sleep(1000L);
		} else {
			final long state = this.state.getAndSet(0L);
			if (state != 0L)
				log.info(LOG_PREFIX + "Resuming normal ticks..");
		}
	}



	public static void UnloadChunks() {
		final long mem = Runtime.getRuntime().freeMemory();
		int count = 0;
		for (final World world : Bukkit.getWorlds()) {
			final Collection<Chunk> forced = world.getForceLoadedChunks();
			final Chunk[] chunks = world.getLoadedChunks();
			for (final Chunk chunk : chunks) {
				if (forced.contains(chunk))
					continue;
				if (chunk.unload(true))
					count++;
			}
		}
		System.gc();
		if (count > 0)
			log.info(String.format("%sUnloaded %d chunks", LOG_PREFIX, Integer.valueOf(count)));
		final long freed = mem - Runtime.getRuntime().freeMemory();
		if (freed > 10485760L) // 10MB
			log.info(String.format("%sFreed memory: %dMB", LOG_PREFIX, Long.valueOf(freed/1024/1024)));
	}



}
