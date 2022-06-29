
# Proyectos de Referencia

```mermaid
graph TD;
    A-->B;
    A-->C;
    B-->D;
    C-->D;
```


```
@Repository
public interface Country{

@Query(value="select * from")
public List<Country> findAll();
}
```


# Tareas

## MongoDB 4.4

## Generar vistas para las referenciac mediante $lookup


### Tablas referenciadas @Referenced

```
Persona{
idpersona:1, name:"aris"}

Visitante { idvisitante:1, persona:{idpersona:1}}
```

Person.java
```
  @Entity(name="Person")
  public class Person{
  @Id
  @Column
  private String idperson;
  
  @Column
  private String name;
}
```


Visitante.java
```
  @Entity(name="Visitante")
  public class Visitante{
  @Id
  @Column
  private String idvisitante;
  @Column
  @Referenced(foreingkey=idperson) 
  private Person person;
```

El @Referenced generara un loockup, para las consultas
https://hevodata.com/learn/mongodb-join-two-collections/
98db.userInfo.aggregate([
    { $lookup:
        {
           from: "address",
           localField: "contact_name",
           foreignField: "name",
           as: "address"
        }
    }
]).pretty();

Usar agegate java driver

asi para los query solo se usaria un json y no hay que invocar del otro repository
en los @Repository serian basicos





## Repository

- Crear anotaciones para los metodos del reposotory
- Analizar el entity para ver las referencias si son necesarias

@Repositort(entity=Persona.class)
public interface PersonRepository{
@Query
public List<Person> findAll()

@Query
public Optional<Person> findById(String id);

}

## Controller
- Nombres de metodos deben ser iguales al repository para que el genere los enpoinst
invocandolos,
- El generar los enpoinst.

@Controller()
public interface PersonController{
@ControllerEndPoint(path="",
public List<Person> findAll();

}



## JSonObject
https://cloudogu.com/en/blog/Java-Annotation-Processors_1-Intro

https://github.com/cloudogu/annotation-processors/tree/master/part-3

```
package com.cloudogu.blog;

import org.junit.Test;

import static org.junit.Assert.*;

public class PersonTest {

    @Test
    public void testJsonWriter() {
        Person person = new Person("tricia", "tricia.mcmillian@hitchhicker.com");
        String json = PersonJsonWriter.toJson(person);
        assertEquals("{\"class\": \"class com.cloudogu.blog.Person\",\"username\": \"tricia\",\"email\": \"tricia.mcmillian@hitchhicker.com\"}", json);
    }

}
```


Vaidaciones
https://hannesdorfmann.com/annotation-processing/annotationprocessing101/
Class<?> clazz = annotation.type();
      qualifiedSuperClassName = clazz.getCanonicalName();
      simpleTypeName = clazz.getSimpleName();


@Override

    public <T> Map<String, Object> toMap(T entity) {

        Objects.requireNonNull(entity, "entity is required");

        Map<String, Object> map = new HashMap<>();

        final Class<?> type = entity.getClass();

        final Entity annotation = Optional.ofNullable(
                type.getAnnotation(Entity.class))

                .orElseThrow(() -> new RuntimeException("The class must have Entity annotation"));

Otavio Santana
https://dzone.com/articles/introduction-to-reflectionless-know-what-the-new-t


Creacion y lectura de clases Class.forName()
https://www.tabnine.com/code/java/methods/java.lang.Class/forName


 public static void main(String[] args)
        throws ClassNotFoundException
    {
 
        // returns the Class object for this class
        Class myClass = Class.forName("Test");
 
        System.out.println("Class represented by myClass: "
                           + myClass.toString());
 
        // Get the fields of myClass
        // using getFields() method
        System.out.println("Fields of myClass: "
                           + Arrays.toString(
                                 myClass.getFields()));
    }


NONGODB VIEWS

https://www.mongodb.com/docs/manual/core/views/



# VISTAS JOINS DE TRES TABLAS
# $lockup
Java Driver
https://mongodb.github.io/mongo-java-driver/3.2/builders/aggregation/

Agregation en Jmoordb
https://avbravo-2.gitbook.io/jmoordb/capitulo-7-aggregation/capitulo-6-aggregation

```
{
$lookup:
{
From:<join collections>,
localField: <input documents’ field>,
foreignField:<documents’ field of the ‘from’ collection>,
as: <result array field>
}
}
```

Referencia:
https://stackoverflow.com/questions/35813854/how-to-join-multiple-collections-with-lookup-in-mongodb

Objectivo:
  Relacionar sivaUser, sivaUserInfo, sivaUserRole

$lookup

## Insert datos en coleccion
```
db.sivaUser.insertMany( [
       { 
       "email" : "admin@gmail.com",
       "userId" : "AD",
       "userName" : "admin"
       },
       {
        "email" : "avbravo@gmail.com",
        "userId" : "AVBRAVO",
        "userName" : "aris"
        }
       ] )
```

### "userinfo"
```
db.sivaUserInfo.insert([
        {
         "userId" : "AD",
         "phone" : "0000000000"
        },
        {
        "userId" : "AVBRAVO",
        "phone" : "65277389"
        }
])
```

### "userrole"
```
db.sivaUserRole.insert([
        {
            "userId" : "AD",
            "role" : "admin"
        },
        {
            "userId" : "AVBRAVO",
            "role" : "testing"
        }
])
```


## Crear el agregate
### Con el match aplicamos el where
### Lookup hacemos el join

```
db.sivaUser.aggregate([
    { "$match": { "userId": "AVBRAVO" }},
    {
        $lookup: {
           from: "sivaUserRole",
           localField: "userId",
           foreignField: "userId",
           as: "userRole"
        }
    },
    {
        $unwind: "$userRole"
    },
    {
        $lookup: {
            from: "sivaUserInfo",
            localField: "userId",
            foreignField: "userId",
            as: "userInfo"
        }
    },
    {
        $unwind: "$userInfo"
    }
])
```

## Resultado
```
{
	"_id" : ObjectId("56d82612b63f1c31cf906003"),
	"userId" : "AD",
	"phone" : "0000000000",
	"userRole" : {
		"_id" : ObjectId("56d82612b63f1c31cf906003"),
		"userId" : "AD",
		"role" : "admin"
	},
	"userInfo" : {
		"_id" : ObjectId("56d82612b63f1c31cf906003"),
		"userId" : "AD",
		"phone" : "0000000000"
	}
}
```




LEE ANOTACIONES
https://github.com/MorphiaOrg/morphia/blob/master/core/src/main/java/dev/morphia/mapping/codec/pojo/EntityModel.java
