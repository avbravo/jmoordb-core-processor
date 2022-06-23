
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
