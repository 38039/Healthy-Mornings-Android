// Lokalna instancja modelu danych zadania w oparciu o encję "Tasks"
package com.nforge.healthymornings.model.data;

public class Task {
    // Użytkownik / Program nie powinien być w stanie nadpisywać zadań
    private final int     id_task;
    private final String  category;
    private final String  name;
    private final String  description;
    private final int     points_reward;


    // Konstruktor
    // Poprzez niego dane są przekazywane z zdalnej do lokalnej instancji
    public Task (
            int     ID          ,
            String  category    ,
            String  name        ,
            String  description ,
            int     points
    ) {
        this.id_task        = ID;
        this.category       = category;
        this.name           = name;
        this.description    = description;
        this.points_reward  = points;
    }


    // Gettery
    // Użyteczne gdy trzeba uzyskać szczegóły zadania
    public String  getName()           { return name;          }
    public int     getID()             { return id_task;       }
    public String  getCategory()       { return category;      }
    public String  getDescription()    { return description;   }
    public int     getReward()         { return points_reward; }
}
