/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jmoordb.core.util;

import com.jmoordb.core.annotation.Referenced;
import com.jmoordb.core.annotation.enumerations.TypePK;
import static com.jmoordb.core.util.JmoordbCoreUtil.setHourToDate;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.glassfish.jersey.uri.UriComponent;

/**
 *
 * @author avbravo
 */
public class DocumentUtil {
    // <editor-fold defaultstate="collapsed" desc="String getIdValue(Document document, Referenced referenced)">

    /**
     *
     * @param document
     * @param referenced
     * @return Obtiene el valor del id referenciado
     */
    public static String getIdValue(Document document, Referenced referenced) {
        String result = "";
        try {

            String data = "";
            if (document.get(referenced.from()) != null) {
                data = document.get(referenced.from()).toString();
                data = data.replace("Document{{", "");
                data = data.replace("}}", "");
                data = data.replace(referenced.foreignField(), "");
                data = data.replace("=", "");
                result = data.trim();
            } else {
                // Cuando se pasa desde un List<Document> de llaves primarias
                data = document.toJson();
                data = data.replace("{", "");
                data = data.replace("}", "");
                data = data.replace("\"", "");
                data = data.replace(referenced.foreignField(), "");
                data = data.replace(":", "");
                result = data.trim();

            }
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String getListValue(Document document, Referenced referenced)">

    /**
     *
     * @param document
     * @param referenced
     * @return List<Document> con las referencias de un @Referenced List<>
     */
    public static List<Document> getListValue(Document document, Referenced referenced) {
        List<Document> result = new ArrayList<>();
        try {

            String data = document.get(referenced.from()).toString();
            data = data.replace("Document{{", "");

            data = data.replace("}}", "");

            data = data.replace(referenced.foreignField(), "");

            data = data.replace("=", "");

            data = data.replace("[", "");

            data = data.trim();
            data = data.replace("[", "");

            data = data.replace("]", "");

            data = data.trim();
            StringTokenizer st = new StringTokenizer(data, ",");
            while (st.hasMoreTokens()) {
                Document doc = new Document();
                if (referenced.typePK().equals(TypePK.STRING)) {
                    doc.append(referenced.foreignField(), st.nextToken().trim());
                } else {
                    Integer value = Integer.parseInt(st.nextToken().trim());
                    doc.append(referenced.foreignField(), value);
                }
                result.add(doc);

            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }
        return result;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Document jsonToDocument(String json)">
    /**
     * Convierte un Json a Document
     *
     * @param json
     * @return
     */
    public static Document jsonToDocument(String json) {
        return Document.parse(json.toString());
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String bsonToJson(Bson filter)">
    public static String bsonToJson(Bson filter) {
        String json = "";
        try {
//            BsonDocument asBsonDocument =filter.toBsonDocument(BsonDocument.class,  MongoClient.getDefaultCodecRegistry());

            BsonDocument asBsonDocument = filter.toBsonDocument(BsonDocument.class, getDefaultCodecRegistry());
            json = asBsonDocument.toJson();
        } catch (Exception e) {

        }
        return json;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Document sortBuilder(HashMap<String,String>hashmap )">
    /**
     * crea un document sort en base a un hashmap
     *
     * @param hashmap
     * @return
     */
    public static Document sortBuilder(HashMap<String, String> map) {
        Document sort = new Document();
        try {

            sort.toJson();

            if (map == null || map.isEmpty()) {

            } else {

                map.entrySet().forEach(m -> {
                    sort.append(m.getKey().toString(), createOrder(m.getValue().toString()));
                });

            }
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }
        return sort;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Integer createOrder(String sorter)">
    /**
     * devuelve el indice de ordenacion
     *
     * @param sorter
     * @return
     */
    private static Integer createOrder(String sorter) {
        Integer ordernumber = 1;
        try {
            sorter = sorter.trim().toLowerCase();
            switch (sorter) {
                case "asc":
                    ordernumber = 1;
                    break;
                case "desc":
                    ordernumber = -1;
                    break;
                default:
                    ordernumber = 1;
            }
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }
        return ordernumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="public Document sortBuilder(String sortfield, String order  )">
    /**
     * crea un documento para ordenar
     *
     * @param sortfield
     * @param order: asc/desc
     * @return
     */
    public static Bson filterEQBuilder(String fieldname, String value, String fieldtype) {

        Bson filter;
        try {
            fieldtype = fieldtype.toLowerCase();
            switch (fieldtype) {
                case "integer":
                    filter = Filters.eq(fieldname, Integer.parseInt(value));
                    break;
                case "double":
                    filter = Filters.eq(fieldname, Double.parseDouble(value));
                    break;
                case "string":
                    filter = Filters.eq(fieldname, value);
                    break;
                case "date":
                    filter = Filters.eq(fieldname, JmoordbCoreDateUtil.stringToISODate(value));
                case "boolean":
                    Boolean valueBoolean = false;
                    if (value.equals("true")) {
                        valueBoolean = true;
                    }
                    filter = Filters.eq(fieldname, valueBoolean);
                    break;
                default:
                    filter = Filters.eq(fieldname, value);

            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }
        return null;

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Bson createBsonBetweenDateWithoutHours(String fieldnamestart, Date datestartvalue, String fieldlimitname, Date datelimitvalue) {">
    /**
     * crea un filtro Bson entre fechas tomando en cuenta la hora
     *
     * @param fieldnamestart
     * @param datestartvalue
     * @param fieldlimitname
     * @param datelimitvalue
     * @param docSort
     * @return
     */
    public static Bson createBsonBetweenDateWithoutHours(String fieldnamestart, Date datestartvalue, String fieldlimitname, Date datelimitvalue) {
        Bson filter = new Document();
        try {

            Date dateStart = setHourToDate(datestartvalue, 0, 0);
            Date dateEnd = setHourToDate(datelimitvalue, 23, 59);
            filter = Filters.and(Filters.gte(fieldnamestart, dateStart), Filters.lte(fieldlimitname, dateEnd));
            return filter;
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }

        return filter;
    }
    // </editor-fold>
    
    
     // <editor-fold defaultstate="collapsed" desc="Bson createBsonBetweenDateUsingHours(String fieldnamestart, Date datestartvalue, String fieldlimitname, Date datelimitvalue)">
    /**
     * crea un filtro Bson entre fechas sin tomar en cuenta la hora
     * @param fieldnamestart
     * @param datestartvalue
     * @param fieldlimitname
     * @param datelimitvalue
     * @return 
     */
    public static Bson createBsonBetweenDateUsingHours(String fieldnamestart, Date datestartvalue, String fieldlimitname, Date datelimitvalue) {
   Bson filter = new Document();
        try {
      
              Date dateStart = setHourToDate(datestartvalue, 0, 0);
            Date dateEnd = setHourToDate(datelimitvalue, 23, 59);
            filter = Filters.and(Filters.gte(fieldnamestart, datestartvalue), Filters.lte(fieldlimitname,  datelimitvalue));
            

return filter;
        } catch (Exception e) {
          Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }

        return filter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="String encodeJson(String query)" >

    public static String encodeJson(String query) {
        try {

            return UriComponent.encode(query, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED);
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + "error: " + e.getLocalizedMessage());
        }
        return query;

    }
    // </editor-fold>
}
