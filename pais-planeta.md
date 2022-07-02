
## Pais-Planeta

db.pais.aggregate(
[
  
   {
    $lookup:{
            from:"planeta",
            localField:"planeta.idplaneta",
            foreignField:"idplaneta",
            as:"planeta"
            }
    }
]
).pretty()

## Pais-Planeta 
   Pais-Oceano

db.pais.aggregate(
[
  
   {
    $lookup:{
            from:"planeta",
            localField:"planeta.idplaneta",
            foreignField:"idplaneta",
            as:"planeta"
            }
    }
,
{ 
  $lookup:{
            from:"oceano",
            localField:"oceano.idoceano",
            foreignField:"idoceano",
            as:"oceano"
            }
  }

]
).pretty()