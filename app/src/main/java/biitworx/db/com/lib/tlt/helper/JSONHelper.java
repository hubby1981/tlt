package biitworx.db.com.lib.tlt.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by marcel.weissgerber on 26.10.2016.
 */

public class JSONHelper {


    public static JSONObject mapFromObject(Object data) {

        HashMap<String, String> fields = ObjectHelper.getFieldsJsonIgnore(data);
        HashMap<String, DbReference> refs = ObjectHelper.getReferencesEx(data.getClass());
        JSONObject result = new JSONObject();
        for (Map.Entry<String, String> field : fields.entrySet()) {

            try {
                result.put(field.getKey(), field.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, DbReference> ref : refs.entrySet()) {
            JSONArray array = new JSONArray();
            try {


                List<Object> items = (List<Object>) ObjectHelper.getDeclaredFieldByName(data.getClass(), ref.getKey()).get(data);
                if (items != null) {
                    for (Object item : items) {
                        array.put(mapFromObject(item));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            try {
                result.put(ref.getKey(), array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static <T> T mapToObject(Class<T> tClass, JSONObject json) {
        try {
            T result = tClass.newInstance();
            HashMap<String, String> fields = ObjectHelper.getFieldsJsonIgnore(result);
            HashMap<String, DbReference> refs = ObjectHelper.getReferencesEx(tClass);

            for (Map.Entry<String, String> field : fields.entrySet()) {
                Field invert = ObjectHelper.getDeclaredFieldByName(tClass, field.getKey());
                if (invert != null) {
                    String value = null;
                    try {
                        value = json.getString(field.getKey());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Object insert = null;
                    if (invert.getType() == Boolean.class || invert.getType() == boolean.class) {
                        insert = value != null ? Boolean.parseBoolean(value) : false;
                    } else if (invert.getType() == Integer.class || invert.getType() == int.class) {

                        insert = value != null ? Integer.parseInt(value) : 0;
                    } else if (invert.getType() == Float.class || invert.getType() == float.class) {
                        insert = value != null ? Float.parseFloat(value) : 0f;
                    } else if (invert.getType() == UUID.class) {
                        insert = value != null ? UUID.fromString(value) : null;
                    } else {
                        insert = value;
                    }
                    if (insert != null)
                        invert.set(result, insert);
                }
            }
            for (Map.Entry<String, DbReference> ref : refs.entrySet()) {
                try {
                    JSONArray array = json.getJSONArray(ref.getKey());
                    if (array != null) {
                        List<Object> items = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject id = array.getJSONObject(i);
                            if (id != null) {
                                Object o = mapToObject(ref.getValue().items(), id);
                                if (o != null) {
                                    items.add(o);
                                }
                            }
                        }
                        if (items.size() > 0) {
                            ObjectHelper.getDeclaredFieldByName(tClass, ref.getKey()).set(result, items);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return result;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
