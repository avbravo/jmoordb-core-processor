
// Uncorrelated Subqueries
// (supported as of MongoDB 3.6)
// {
//    from: "<collection to join>",
//    let: { <var_1>: <expression>, …, <var_n>: <expression> },
//    pipeline: [ <pipeline to execute on the collection to join> ],
//    as: "<output array field>"
// }



db.orders.aggregate(
  [
    {
      $lookup: {
        from: "catalog",
        let: { "order_sku": "$item" },
        pipeline: [
          {
            $match: {
              $expr: {
                $eq: ["$sku", "$$order_sku"]
              }
            }
          },
          {
            $lookup: {
              from: "warehouses",
              pipeline: [
                {
                  $match: {
                    $expr:{
                      $eq : ["$stock_item", "$$order_sku"]
                    }
                  }
                },
                {
                  $project : { "instock": 1, "_id": 0}
                }
              ],
              as: "wh"
            }
          },
          { "$unwind": "$wh" },
          {
            $project : { "description": 1, "instock": "$wh.instock", "_id": 0}
          }
        ],
        as: "inventory"
      },
    },
  ]
)


## Java


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