## 0 QUERY SIMPLE
Trae todos los documentos de la coleccion
```
db.mecanico.aggregate([
    { "$match": { "idmecanico": "1" }},
    {
        $lookup: {
           from: "provincia",
           localField: "provincia.idprovincia",
           foreignField: "idprovincia",
           as: "provincia"
        }
 
    },
    {
        $unwind: "$provincia",

    }
])
```

Genera
```
{
        "_id" : ObjectId("62b9be7903230f0ef1badf83"),
        "idmecanico" : "1",
        "nombre" : "Ana",
        "provincia" : {
                "_id" : ObjectId("62b9b6e303230f0ef1badf82"),
                "idprovincia" : 7,
                "provincia" : "Los Santos",
                "pais" : {
                        "idpais" : "pa",
                        "pais" : "Panama",
                        "planeta" : {
                                "idplaneta" : "tr",
                                "planeta" : "Tierra"
                        }
                }
        }
}
```

## 1. Ejecutar lookup buscando por provincia
```
db.mecanico.aggregate([
    { "$match": { "idmecanico": "1" }},
    {
        $lookup: {
           from: "provincia",
           localField: "provincia.idprovincia",
           foreignField: "idprovincia",
           as: "provincia_doc"
        }
          
    },
    {
        $unwind: "$provincia_doc"
    }
])
```

### Genera
```
{
        "_id" : ObjectId("62b9be7903230f0ef1badf83"),
        "idmecanico" : "1",
        "nombre" : "Ana",
        "provincia" : {
                "_id" : ObjectId("62b9b6e303230f0ef1badf82"),
                "idprovincia" : 7,
                "provincia" : "Los Santos",
                "pais" : {
                        "idpais" : "pa",
                        "pais" : "Panama",
                        "planeta" : {
                                "idplaneta" : "tr",
                                "planeta" : "Tierra"
                        }
                }
        }
}
```





## 2. Ejecutar lookup buscando por provincia y pais
```
db.mecanico.aggregate([
    { "$match": { "idmecanico": "1" }},
    {
        $lookup: {
           from: "provincia",
           localField: "provincia.idprovincia",
           foreignField: "idprovincia",
           as: "provincia"
        },
          $lookup: {
           from: "pais",
           localField: "provincia.pais.idpais",
           foreignField: "idpais",
           as: "pais"
        }       
         
 
    },
    {
        $unwind: "$provincia",
       $unwind: "$pais",
    }
])
```

### Genera
```
{
        "_id" : ObjectId("62b9be7903230f0ef1badf83"),
        "idmecanico" : "1",
        "nombre" : "Ana",
        "provincia" : {
                "idprovincia" : 7,
                "provincia" : "Los Santos",
                "pais" : {
                        "idpais" : "pa",
                        "pais" : "Panama",
                        "planeta" : {
                                "idplaneta" : "tr",
                                "planeta" : "Tierra"
                        }
                }
        },
        "pais" : {
                "_id" : ObjectId("62b9b5bd03230f0ef1badf80"),
                "idpais" : "pa",
                "pais" : "Panama Canal",
                "planeta" : {
                        "idplaneta" : "tr"
                }
        }
}
```







## Genera la relacion como otro valor foraneo

{
        "_id" : ObjectId("62b9be7903230f0ef1badf83"),
        "idmecanico" : "1",
        "nombre" : "Ana",
        "provincia" : {
                "idprovincia" : 7,
                "provincia" : "Los Santos",
                "pais" : {
                        "idpais" : "pa",
                        "pais" : "Panama"
                }
        },
        "pais" : {
                "_id" : ObjectId("62b9b5bd03230f0ef1badf80"),
                "idpais" : "pa",
                "pais" : "Panama Canal"
        }
}








## Ejecutar lookup buscando por provincia , pais, planeta
```
db.mecanico.aggregate([
    { "$match": { "idmecanico": "1" }},
    {
        $lookup: {
           from: "provincia",
           localField: "provincia.idprovincia",
           foreignField: "idprovincia",
           as: "provincia"
        },
          $lookup: {
           from: "pais",
           localField: "provincia.pais.idpais",
           foreignField: "idpais",
           as: "pais"
        }       
         ,
          $lookup: {
          from: "planeta",
          localField: "provincia.pais.planeta.idplaneta",
          foreignField: "idplaneta",
          as: "planeta"
        }     
 
    },
    {
        $unwind: "$provincia",
       $unwind: "$pais",
       $unwind: "$planeta",
    }
])
```