
# Cursos
Fuente:
https://www.youtube.com/watch?v=N3ny7bvS_IM

Nota: Recuerde que cada {} genera un flujo para sel siguente

```
db.tutor.insertMany(
[
{
idtutor:"1",
tutor: "aris"
},
 {
idtutor:"2",
tutor: "ana"
}
]
)

```

```
db.curso.insertMany(
[
    {
     idcurso : "c1",
     curso : "Java",
     tutor :
         { 
         idtutor : "1"
         }
    },
     { 
     idcurso: "c2",
     curso  : "C",
     tutor  : 
            { 
             idtutor:"2"
           }
    },
    {
    idcurso : "c3",
     curso : "Docker",
     tutor :
         { 
         idtutor : "1"
         }
    },
    {
    idcurso : "c4",
     curso : "MongoDB",
     tutor :
         { 
         idtutor : "2"
         }
    }
]
)

``` 


```
db.video.insertMany(
[
    {
     idvideo : "v1",
     video : "Java One",
     curso :
         { 
         idcurso : "c1"
         }
    },
     { 
     idvideo: "v2",
     video  : "C Benning",
     curso  : 
            { 
             idcurso:"c2"
           }
    },
    {
         idvideo : "v3",
         video : "Java JL",
         curso :
             { 
             idcurso : "c1"
             }
        },
        {
         idvideo : "v4",
         video : "Docker native",
         curso :
             { 
             idcurso : "c3"
             }
        },

        {
         idvideo : "v5",
         video : "Mongo Started",
         curso :
             { 
             idcurso : "c4"
             }
        }

]
)

```

#consulta basica
# Lookup
## consulta del tutor idtutor 1
```
db.tutor.aggregate(
[
  {
   $match:
          {
           idtutor:"1"
           }
   }
]
).pretty()
```

### Salida
```
{                                                                                                    
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),                                                
        "idtutor" : "1",                                                                             
        "tutor" : "aris"                                                                             
}    
```
--------------------------------------------------
## Consultar los cursos del tutor
Lo devolvera como una list
```
db.tutor.aggregate(
[
  {
   $match:
          {
           idtutor:"1"
           }
   },
   {
    $lookup:{
            from:"curso",
            localField:"idtutor",
            foreignField:"tutor.idtutor",
            as:"curso"
            }
    }
]
).pretty()
```

### Nos devuelve una lista de cursos
```
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : [
                {
                        "_id" : ObjectId("62ba0fc9e60c2531407fec4f"),
                        "idcurso" : "c1",
                        "curso" : "Java",
                        "tutor" : {
                                "idtutor" : "1"
                        }
                },
                {
                        "_id" : ObjectId("62ba0fc9e60c2531407fec51"),
                        "idcurso" : "c3",
                        "curso" : "Docker",
                        "tutor" : {
                                "idtutor" : "1"
                        }
                }
        ]
}
```
-----------------------------------------------------
## Si deseamos que genere un registro por cada curso que tenga el autor
util para referencias simple
agregamos $unwind:
```
db.tutor.aggregate(
[
  {
   $match:
          {
           idtutor:"1"
           }
   },
   {
    $lookup:{
            from:"curso",
            localField:"idtutor",
            foreignField:"tutor.idtutor",
            as:"curso"
            }
    }
   ,
  {
   $unwind:"$curso"
  }
]
).pretty()
```

### Observe la salida hay un registro de tutorpor cada curso
```
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec4f"),
                "idcurso" : "c1",
                "curso" : "Java",
                "tutor" : {
                        "idtutor" : "1"
                }
        }
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec51"),
                "idcurso" : "c3",
                "curso" : "Docker",
                "tutor" : {
                        "idtutor" : "1"
                }
        }
}
```
--------------------------------------------
## Ahora unirlo con la coleccion video
Observe que el localField que se usara es el que se genero como salida
del paso anterior es decir curso.idcurso del generado $ que sera la base para concatenarlo con el siguiente
de la tabla video.
```
db.tutor.aggregate(
[
  {
   $match:
          {
           idtutor:"1"
           }
   },
   {
    $lookup:{
            from:"curso",
            localField:"idtutor",
            foreignField:"tutor.idtutor",
            as:"curso"
            }
    }
   ,
  {
   $unwind:"$curso"
  }
  ,{
   $lookup:{
            from:"video",
            localField:"curso.idcurso",
            foreignField:"curso.idcurso",
            as:"video"
           }
  }
]
).pretty()
```

### Salida
Puede observar que tendremos un registro generado para cada curso
si deseamos generar uno para cada video usamos $unwind:
```
{                                                                                                                   
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),                                                               
        "idtutor" : "1",                                                                                            
        "tutor" : "aris",                                                                                           
        "curso" : {                                                                                                 
                "_id" : ObjectId("62ba0fc9e60c2531407fec4f"),                                                       
                "idcurso" : "c1",                                                                                   
                "curso" : "Java",                                                                                   
                "tutor" : {                                                                                         
                        "idtutor" : "1"                                                                             
                }                                                                                                   
        },                                                                                                          
        "video" : [                                                                                                 
                {                                                                                                   
                        "_id" : ObjectId("62ba14d487d3c1efe5ede7bc"),                                               
                        "idvideo" : "v1",                                                                           
                        "video" : "Java One",
                        "curso" : {
                                "idcurso" : "c1"
                        }
                },
                {
                        "_id" : ObjectId("62ba14d487d3c1efe5ede7be"),
                        "idvideo" : "v3",
                        "video" : "Java JL",
                        "curso" : {
                                "idcurso" : "c1"
                        }
                }
        ]
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec51"),
                "idcurso" : "c3",
                "curso" : "Docker",
                "tutor" : {
                        "idtutor" : "1"
                }
        },
        "video" : [
                {
                        "_id" : ObjectId("62ba14d487d3c1efe5ede7bf"),
                        "idvideo" : "v4",
                        "video" : "Docker native",
                        "curso" : {
                                "idcurso" : "c3"
                        }
                }
        ]
}
```

--------------------------------------------
## Ahora generaremos una salida por cada video de cada curso
usamos $unwind:
```
db.tutor.aggregate(
[
  {
   $match:
          {
           idtutor:"1"
           }
   },
   {
    $lookup:{
            from:"curso",
            localField:"idtutor",
            foreignField:"tutor.idtutor",
            as:"curso"
            }
    }
   ,
  {
   $unwind:"$curso"
  }
  ,{
   $lookup:{
            from:"video",
            localField:"curso.idcurso",
            foreignField:"curso.idcurso",
            as:"video"
           }
  }
, 
  {
   $unwind:"$video"
  }
]
).pretty()
```

### Salida
Observe que ahora tenemos un video por cada curso y tutor
Generamos mas registros individuales.
```
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec4f"),
                "idcurso" : "c1",
                "curso" : "Java",
                "tutor" : {
                        "idtutor" : "1"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7bc"),
                "idvideo" : "v1",
                "video" : "Java One",
                "curso" : {
                        "idcurso" : "c1"
                }
        }
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec4f"),
                "idcurso" : "c1",
                "curso" : "Java",
                "tutor" : {
                        "idtutor" : "1"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7be"),
                "idvideo" : "v3",
                "video" : "Java JL",
                "curso" : {
                        "idcurso" : "c1"
                }
        }
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec51"),
                "idcurso" : "c3",
                "curso" : "Docker",
                "tutor" : {
                        "idtutor" : "1"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7bf"),
                "idvideo" : "v4",
                "video" : "Docker native",
                "curso" : {
                        "idcurso" : "c3"
                }
        }
}
```

--------------------------------------------
## Ahora podemos indicar los campos en la salida 
mediante $project

```
db.tutor.aggregate(
[
  {
   $match:
          {
           idtutor:"1"
           }
   },
   {
    $lookup:{
            from:"curso",
            localField:"idtutor",
            foreignField:"tutor.idtutor",
            as:"curso"
            }
    }
   ,
  {
   $unwind:"$curso"
  }
  ,{
   $lookup:{
            from:"video",
            localField:"curso.idcurso",
            foreignField:"curso.idcurso",
            as:"video"
           }
  }
, 
  {
   $unwind:"$video"
  }
,
{
 $project:{
           idtutor : "idtutor",
           tutor : "tutor", 
           idcurso: "$curso.idcurso", 
           titulo : "$video.video"

          } 
}
]
).pretty()
```

### Salida
```
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "idtutor",
        "tutor" : "tutor",
        "idcurso" : "c1",
        "titulo" : "Java One"
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "idtutor",
        "tutor" : "tutor",
        "idcurso" : "c1",
        "titulo" : "Java JL"
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "idtutor",
        "tutor" : "tutor",
        "idcurso" : "c3",
        "titulo" : "Docker native"
}

```

---------------------------------------------------------
## Query de todos los autores
```
db.tutor.aggregate(
[
  
   {
    $lookup:{
            from:"curso",
            localField:"idtutor",
            foreignField:"tutor.idtutor",
            as:"curso"
            }
    }
   ,
  {
   $unwind:"$curso"
  }
  ,{
   $lookup:{
            from:"video",
            localField:"curso.idcurso",
            foreignField:"curso.idcurso",
            as:"video"
           }
  }
, 
  {
   $unwind:"$video"
  }
]
).pretty()
```

## Salida
Observe que hay un registro por cada tutor cada curso y cada video
para que pueda ser procesado.

```
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec4f"),
                "idcurso" : "c1",
                "curso" : "Java",
                "tutor" : {
                        "idtutor" : "1"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7bc"),
                "idvideo" : "v1",
                "video" : "Java One",
                "curso" : {
                        "idcurso" : "c1"
                }
        }
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec4f"),
                "idcurso" : "c1",
                "curso" : "Java",
                "tutor" : {
                        "idtutor" : "1"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7be"),
                "idvideo" : "v3",
                "video" : "Java JL",
                "curso" : {
                        "idcurso" : "c1"
                }
        }
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec40"),
        "idtutor" : "1",
        "tutor" : "aris",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec51"),
                "idcurso" : "c3",
                "curso" : "Docker",
                "tutor" : {
                        "idtutor" : "1"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7bf"),
                "idvideo" : "v4",
                "video" : "Docker native",
                "curso" : {
                        "idcurso" : "c3"
                }
        }
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec41"),
        "idtutor" : "2",
        "tutor" : "ana",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec50"),
                "idcurso" : "c2",
                "curso" : "C",
                "tutor" : {
                        "idtutor" : "2"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7bd"),
                "idvideo" : "v2",
                "video" : "C Benning",
                "curso" : {
                        "idcurso" : "c2"
                }
        }
}
{
        "_id" : ObjectId("62ba0eb4e60c2531407fec41"),
        "idtutor" : "2",
        "tutor" : "ana",
        "curso" : {
                "_id" : ObjectId("62ba0fc9e60c2531407fec52"),
                "idcurso" : "c4",
                "curso" : "MongoDB",
                "tutor" : {
                        "idtutor" : "2"
                }
        },
        "video" : {
                "_id" : ObjectId("62ba14d487d3c1efe5ede7c0"),
                "idvideo" : "v5",
                "video" : "Mongo Started",
                "curso" : {
                        "idcurso" : "c4"
                }
        }
}
```