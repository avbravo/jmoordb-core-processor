
# Proyectos de Referencia
Usar mongodb java driver lookup $unwind: para obtener un solo registro de una agregacion
Agregarlo al codigo java para casos de una sola coleccion


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

collection.aggregate(
Arrays.asList(match(eq("idconductor", "7")),    
lookup("auto", "auto.idauto", "idauto", "auto")
);


collection.aggregate(Arrays.asList(match(eq("idconductor", "7")),

                                   group("$customerId", sum("totalQuantity", "$quantity"),
                                                        avg("averageQuantity", "$quantity"))
                                   out("authors")));

EJEMPLO CON 
```
MongoClient mongoClient = new MongoClient("localhost");

MongoDatabase db = mongoClient.getDatabase("mydb");

Bson lookup = new Document("$lookup",
        new Document("from", "coll_two")
                .append("localField", "foreign_id")
                .append("foreignField", "_id")
                .append("as", "look_coll"));

Bson match = new Document("$match",
        new Document("look_coll.actif", true));

List<Bson> filters = new ArrayList<>();
filters.add(lookup);
filters.add(match);

AggregateIterable<Document> it = db.getCollection("coll_one").aggregate(filters);

for (Document row : it) {
    System.out.println(row.toJson());
}
```