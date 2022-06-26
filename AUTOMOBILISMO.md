
# Proyectos de Referencia


## Clases
## Auto.java
```
@Entity
public class Auto {
    @Id
    @Column
    private String idauto;
    @Column
    private String marca;
..
}
```

## Conductor.java
```
@Entity
public class Conductor {
    @Id
    @Column
    private String idconductor;
    @Column
   private String nombre;
    @Referenced(collection = "auto",field = "idauto",repository = "")
    private Auto auto;
}
```



# Database
```
use automovilismo
```


## Auto
```
db.auto.insertMany( [
       { 
       "idauto" : "hiunday",
       "marca" : "GRAND I10"
       },
       {
      "idauto" : "mazda",
       "marca" : "323"
        }
   ] )
```
## Conductor
```
db.conductor.insertMany( [
       { 
       "idconductor" : "7",
       "conductor" : "Aristides",
       "auto":
            { 
               "idauto" : "hiunday",
               "marca" : "GRAND I10"
           }
       },
       { 
       "idconductor" : "8",
       "conductor" : "Bailey",
       "auto":
            { 
               "idauto" : "mazda",
               "marca" : "323"
           }
       }
   ] )
```


