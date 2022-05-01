package it.fktcod.ktykshrk.utils.hook;

import java.util.List;

import it.fktcod.ktykshrk.module.mods.Reach;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Profiler.Result;

public class ProfilerHook extends Profiler {
    public Profiler profiler;

    public String lastSection = "";

    public ProfilerHook(Profiler profiler) {
        super();
        this.profiler = profiler;
        this.profilingEnabled = profiler.profilingEnabled;
    }

    @Override
    public void clearProfiling() {
        profiler.clearProfiling();
    }

  

	/**
     * Start section
     */
    @Override
    public void startSection(String name) {
        this.lastSection = name;
     //  Reach.onStartSection(this.lastSection);
        profiler.startSection(name);
    }

    @Override
    public void endSection() {
      //  Reach.onEndSection(this.lastSection);
        profiler.endSection();
    }

    @Override
    public void endStartSection(String name) {
     // Reach.onEndStartSection(this.lastSection);
        this.lastSection = name;
        profiler.endStartSection(name);
    }

    @Override
    public String getNameOfLastSection() {
        return profiler.getNameOfLastSection();
    }
    @Override
    public List<Result> getProfilingData(String p_76321_1_)
    {
        return this.getProfilingData(p_76321_1_);
    }
}

