package utils;

import collection.CollectionManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;

public class JsonConvertor {
    static public String toJson(Serializable o){
        Gson gson = new Gson();
        return gson.toJson(o);
    }
    static public CollectionManager fromJson(String json){
        if (json == null)
            return null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<CollectionManager>() {}.getType();
            return gson.fromJson(json, type);
        }
        catch (JsonSyntaxException ex)
        {
            return null;
        }
    }
}
