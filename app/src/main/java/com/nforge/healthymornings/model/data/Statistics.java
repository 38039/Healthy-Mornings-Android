package com.nforge.healthymornings.model.data;

public class Statistics {
    private final long id_statistics;
    private final long id_user;
    private final short tasks_active;
    private final short tasks_completed;

    public Statistics(
            long id_statistics,
            long id_user,
            short tasks_active,
            short tasks_completed
    ) {
        this.id_statistics = id_statistics;
        this.id_user = id_user;
        this.tasks_active = tasks_active;
        this.tasks_completed = tasks_completed;
    }

    public long getIdStatistics()      { return id_statistics;     }
    public long getIdUser()           { return id_user;           }
    public short getTasksActive()     { return tasks_active;      }
    public short getTasksCompleted()  { return tasks_completed;   }
}
