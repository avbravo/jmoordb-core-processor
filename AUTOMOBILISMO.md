
# Proyectos de Referencia
Usar mongodb java driver lookup $unwind: para obtener un solo registro de una agregacion
Agregarlo al codigo java para casos de una sola coleccion

Perform Multiple Joins and a Correlated Subquery with $lookup

Perform an Uncorrelated Subquery with $lookup

https://www.mongodb.com/docs/manual/reference/operator/aggregation/unwind/

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

## Ejecutar lookup
```
db.conductor.aggregate([
    { "$match": { "idconductor": "7" }},
    {
        $lookup: {
           from: "auto",
           localField: "auto.idauto",
           foreignField: "idauto",
           as: "auto"
        }
    },
    {
        $unwind: "$auto"
    }
])
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



