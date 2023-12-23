package com.poixson.ecotick;

import static com.poixson.ecotick.EcoTickPlugin.LOG_PREFIX;
import static com.poixson.tools.xJavaPlugin.Log;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.poixson.utils.ThreadUtils;


public class LaggerTask extends BukkitRunnable {

	protected final EcoTickPlugin plugin;

	protected AtomicLong state = new AtomicLong(0L);
	protected final long delay;



	public LaggerTask(final EcoTickPlugin plugin, final long delay) {
		this.plugin = plugin;
		this.delay  = delay;
	}



	public void start() {
		this.runTaskTimer(this.plugin, 200L, 19L);
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
			if (state == this.delay) {
				this.log().info(LOG_PREFIX + "Slowing the server..");
				UnloadChunks();
			} else
			if (state > this.delay) {
				// once every 5 minutes
				if (state % 300L == 0)
					UnloadChunks();
				ThreadUtils.Sleep(1000L);
			}
		} else {
			if (this.state.getAndSet(0L) != 0L)
				this.log().info(LOG_PREFIX + "Resuming normal ticks..");
		}
	}



	public static void UnloadChunks() {
		final long mem = Runtime.getRuntime().freeMemory();
		int count = 0;
		for (final World world : Bukkit.getWorlds()) {
			final Chunk[] chunks = world.getLoadedChunks();
			for (final Chunk chunk : chunks) {
				if (chunk.isForceLoaded())
					continue;
				if (chunk.unload(true))
					count++;
			}
		}
		System.gc();
		if (count > 0)
			Log().info(String.format("%sUnloaded %d chunks", LOG_PREFIX, Integer.valueOf(count)));
		final long freed = mem - Runtime.getRuntime().freeMemory();
		if (freed > 10485760L) // 10MB
			Log().info(String.format("%sFreed memory: %dMB", LOG_PREFIX, Long.valueOf(freed/1024/1024)));
	}



	public Logger log() {
		return this.plugin.getLogger();
	}



}
