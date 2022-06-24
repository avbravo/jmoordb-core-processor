
# Proyectos de Referencia


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