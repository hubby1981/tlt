package biitworx.db.com.lib.tlt.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by marcel.weissgerber on 09.05.2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    private final static String DBNAME = "games.bittworx.losttale";
    private final static int version = 1;
    private static final String SELECT_FROM = "SELECT * FROM ";
    private static final String SELECT_ROWID = "SELECT last_insert_rowid() AS rowid FROM ";
    private static final String LIMIT_1 = " LIMIT 1";
    private static final String DELETE_FROM = "DELETE FROM ";
    private static final String WHERE_PARENT = " WHERE parent=";
    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String PARENT_CHILD_VALUES = " (parent,child) VALUES ";
    private static final String SELECT_PARENT_CHILD_FROM = "SELECT parent,child FROM ";

    public DbHelper(Context context) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createDb(db);

    }

    private void createDb(SQLiteDatabase db) {


        Setup s = new Setup();

        for (String st : s.getCreateTables()) {
            db.execSQL(st);
            //System.out.println(st);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Setup s = new Setup();

        HashMap<String, List<Object>> all = s.secure().getAll(db);

        for (String st : s.getDropTables()) {
            db.execSQL(st);
            //System.out.println(st);

        }

        createDb(db);

        if (all != null && all.size() > 0)
            s.secure().reInsertData(all, db);
    }


    public SQLiteDatabase get() {
        return getWritableDatabase();
    }

    public void readLastIdFor(Object object, SQLiteDatabase db) {
        String table = ObjectHelper.getTableNameEx(object.getClass());
        if (table != null) {
            String st = SELECT_ROWID + table + LIMIT_1;
            Cursor c = db.rawQuery(st, null);
            if (c.moveToNext())
                ((BaseDataObject) object).createdEx(c.getInt(0));
            c.close();
        }
    }

    public String insert(Object object, boolean forceInsert, SQLiteDatabase dbEx) {
        if (object == null) return "";
        SQLiteDatabase db = getSqLiteDatabase(dbEx);

        if (db != null && db.isOpen()) {
            int pid = ((BaseDataObject) object).getPid();
            String oldSt = ObjectHelper.createInsertStatement(object);
            String st = pid == -1 ? oldSt : ObjectHelper.createUpdateStatement(object, pid);
            if (forceInsert)
                st = oldSt;
            if (st != null) {

                db.execSQL(st);
                if (pid == -1)
                    readLastIdFor(object, db);
                HashMap<String, DbReference> ref = ObjectHelper.getReferencesEx(object.getClass());
                if (ref.size() > 0) {
                    String deleteId = ((BaseDataObject) object).getUID().toString();
                    for (Map.Entry<String, DbReference> e : ref.entrySet()) {
                        deleteExisting(db, deleteId, e);
                        Field f = ObjectHelper.getDeclaredFieldByName(object.getClass(), e.getKey());
                        if (f != null) {
                            f.setAccessible(true);
                            try {
                                List<Object> items = (List<Object>) f.get(object);
                                if (items != null) {
                                    for (Object bo : items) {
                                        String id = insert(bo, forceInsert, dbEx);
                                        String st2 = INSERT_INTO + e.getKey() + PARENT_CHILD_VALUES + "('" + deleteId + "','" +
                                                id + "')";
                                        db.execSQL(st2);
                                    }
                                }
                            } catch (IllegalAccessException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return ((BaseDataObject) object).getUID().toString();
    }

    private void deleteExisting(SQLiteDatabase db, String deleteId, Map.Entry<String, DbReference> e) {
        db.execSQL(DELETE_FROM + e.getKey() + WHERE_PARENT + "'" + deleteId + "'");
    }


    public <T> List<T> getData(Class<T> clazz, SQLiteDatabase db2, boolean lazy) {
        List<T> result = new ArrayList<>();

        try
        {
            SQLiteDatabase db = getSqLiteDatabase(db2);

            List<String> fields = ObjectHelper.getFieldsEx(clazz);
            String table = ObjectHelper.getTableNameEx(clazz);
            if (isInit(db, fields, table)) {

                String st = SELECT_FROM + table;


                Cursor cursor = db.rawQuery(st, null);
                while (cursor.moveToNext()) {
                    T obj = createObjInst(clazz, result);
                    int pid = -1;
                    if (obj != null) {
                        for (String fd : cursor.getColumnNames()) {
                            pid = extractValues(clazz, cursor, obj, pid, fd);
                        }

                        if (lazy)
                            loadReferences(clazz, db, obj);

                        BaseDataObject parsed = safeParseObject(obj);
                        if (parsed != null)
                            parsed.importedEx(pid);
                    }
                }

                cursor.close();

            }

        }catch(Exception e){

        }

        return result;
    }

    private SQLiteDatabase getSqLiteDatabase(SQLiteDatabase db2) {
        return db2 != null ? db2 : get();
    }

    private <T> int extractValues(Class<T> clazz, Cursor cursor, T obj, int pid, String fd) {
        String value = cursor.getString(cursor.getColumnIndex(fd));

        if (fd.equals("pid")) {
            pid = cursor.getInt(cursor.getColumnIndex(fd));
        } else {
            Field fg = ObjectHelper.getDeclaredFieldByName(clazz, fd);
            Object val = getObject(value, fg);

            if (fg != null) {
                fg.setAccessible(true);
                try {
                    fg.set(obj, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }


        }
        return pid;
    }

    @Nullable
    private <T> T createObjInst(Class<T> clazz, List<T> result) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            result.add(obj);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private Object getObject(String value, Field fg) {

        if (fg.getGenericType().equals(int.class)) {
            return Integer.parseInt(value);
        } else if (fg.getGenericType().equals(long.class)) {
            return  Long.parseLong(value);
        } else if (fg.getGenericType().equals(float.class)) {
            return  Float.parseFloat(value);
        } else if (fg.getGenericType().equals(boolean.class)) {
            return  Boolean.parseBoolean(value);
        } else if (fg.getGenericType().equals(UUID.class)) {
            return  UUID.fromString(value);
        } else {
            return value;
        }

    }

    public <T> T getRefs(Class<T> clazz, T obj) {
        loadReferences(clazz, get(), obj);
        ((BaseDataObject) obj).importedEx2();
        return obj;
    }

    private <T> void loadReferences(Class<T> clazz, SQLiteDatabase db, T obj) {
        HashMap<String, DbReference> ref = ObjectHelper.getReferencesEx(clazz);
        if (ref.size() > 0) {
            String refUid = ((BaseDataObject) obj).getUID().toString();
            for (Map.Entry<String, DbReference> e : ref.entrySet()) {
                String st2 = SELECT_PARENT_CHILD_FROM + e.getKey() + WHERE_PARENT + "'" + refUid + "'";

                List<Object> objectList = new ArrayList<>();
                parseLine(db, e, st2, objectList);
                saveValues(clazz, obj, e, objectList);
            }
        }
    }

    private <T> void saveValues(Class<T> clazz, T obj, Map.Entry<String, DbReference> e, List<Object> objectList) {
        if (objectList.size() > 0) {
            Field fg2 = ObjectHelper.getDeclaredFieldByName(clazz, e.getKey());
            if (fg2 != null) {
                fg2.setAccessible(true);
                try {
                    fg2.set(obj, objectList);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void parseLine(SQLiteDatabase db, Map.Entry<String, DbReference> e, String st2, List<Object> sub2) {
        Cursor cursor = db.rawQuery(st2, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("child"));
            sub2.add(getData(e.getValue().items(), db, id, true));
        }
        cursor.close();
    }

    public <T> T getData(Class<T> clazz, SQLiteDatabase db2, String id, boolean lazy) {
        SQLiteDatabase db = getSqLiteDatabase(db2);
        List<String> fields = ObjectHelper.getFieldsEx(clazz);
        T obj = createSingleInstance(clazz);
        String table = ObjectHelper.getTableNameEx(clazz);
        if (isInit(db, fields, table)) {

            String st = SELECT_FROM + table + " WHERE uid='" + id + "'";

            int pid = -1;
            Cursor cursor = db.rawQuery(st, null);
            while (cursor.moveToNext()) {

                if (obj != null) {
                    for (String fd : cursor.getColumnNames()) {
                        pid = extractValues(clazz, cursor, obj, pid, fd);
                    }
                    checkLazy(clazz, lazy, db, obj);


                }
            }
            cursor.close();

            BaseDataObject parsed = safeParseObject(obj);
            if (parsed != null)
                parsed.importedEx(pid);
        }

        return obj;
    }

    private boolean isInit(SQLiteDatabase db, List<String> fields, String table) {
        return fields != null && table != null && db != null && db.isOpen();
    }

    @Nullable
    private <T> T createSingleInstance(Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public BaseDataObject safeParseObject(Object obj) {
        if (obj.getClass().getSuperclass().equals(BaseDataObject.class)) {
            return ((BaseDataObject) obj);
        }
        return null;
    }

    private <T> void checkLazy(Class<T> clazz, boolean lazy, SQLiteDatabase db, T obj) {
        if (lazy)
            loadReferences(clazz, db, obj);
    }
}
