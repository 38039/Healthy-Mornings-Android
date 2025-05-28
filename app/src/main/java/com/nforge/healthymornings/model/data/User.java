// Lokalna instancja modelu danych użytkownika w oparciu o encję "users"
package com.nforge.healthymornings.model.data;

public class User {
    // Użytkownik / Program nie powinien być w stanie nadpisywać danych użytkownika
    private final int             id_user;
    private final String          name;
    private final String          surname;
    private final String          gender;
    private final String          username;
    private final String          email;
    private final String          bio;
    private final java.util.Date  date_of_birth;
    private final double          height;
    private final double          weight;
    private final boolean         isAdmin;


    // Konstruktor
    // Poprzez niego dane są przekazywane z zdalnej do lokalnej instancji
    public User(
            int             id_user,
            String          name,
            String          surname,
            String          gender,
            String          username,
            String          email,
            String          bio,
            java.util.Date  date_of_birth,
            double          height,
            double          weight,
            boolean         isAdmin
    ) {
        this.id_user        = id_user;
        this.name           = name;
        this.surname        = surname;
        this.gender         = gender;
        this.username       = username;
        this.email          = email;
        this.bio            = bio;
        this.date_of_birth  = date_of_birth;
        this.height         = height;
        this.weight         = weight;
        this.isAdmin        = isAdmin;
    }


    // Gettery
    // Użyteczne gdy trzeba uzyskać szczegóły o użytkowniku
    public String           getName()           { return name;      }
    public int              getIdUser()         { return id_user;   }
    public String           getSurname()        { return surname;   }
    public String           getGender()         { return gender;    }
    public String           getUsername()       { return username;  }
    public String           getEmail()          { return email;     }
    public String           getBio()            { return bio;       }
    public java.util.Date   getDate_of_birth()  { return date_of_birth; }
    public double           getHeight()         { return height;    }
    public double           getWeight()         { return weight;    }
    public boolean          getIsAdmin()        { return isAdmin;   }
}