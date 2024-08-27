package me.kisters.ciweda.collector;

import me.kisters.ciweda.util.CollectorStatus;

public interface Collector {
    public String getName();
    public CollectorStatus getStatus();
}
