package me.ewriter.bangumitv.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class IndexDao extends AbstractDao<Index, Long> {

    public static final String TABLENAME = "BANGUMI_INDEX";

    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name_cn = new Property(1, String.class, "name_cn", false, "NAME_CN");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Img_url = new Property(3, String.class, "img_url", false, "IMG_URL");
        public final static Property Bangumi_id = new Property(4, int.class, "bangumi_id", false, "BANGUMI_ID");
        public final static Property Info = new Property(5, String.class, "info", false, "info");
    }

    public IndexDao(DaoConfig config) {
        super(config);
    }

    public IndexDao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"BANGUMI_CALENDAR\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME_CN\" TEXT NOT NULL ," + // 1: name_cn
                "\"NAME\" TEXT NOT NULL," + // 2: name
                "\"IMG_URL\" TEXT NOT NULL," + // 3: img_url
                "\"BANGUMI_ID\" INTEGER NOT NULL," + // 4: bangumi_id
                "\"INFO\" TEXT NOT NULL,");// 5: info
    }

    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BANGUMI_CALENDAR\"";
        db.execSQL(sql);
    }

    @Override
    protected Index readEntity(Cursor cursor, int offset) {
        Index entity = new Index( //
                cursor.isNull(offset) ? null : cursor.getLong(offset + 0), // id
                cursor.getString(offset + 1), // name_cn
                cursor.getString(offset + 2), // name
                cursor.getString(offset + 3), // img_url
                cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // bangumi_id
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // info
        );
        return entity;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    protected void readEntity(Cursor cursor, Index entity, int offset) {

    }

    @Override
    protected void bindValues(DatabaseStatement stmt, Index entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        stmt.bindLong(1, id);

        String name_cn = entity.getName_cn();
        if (name_cn != null) {
            stmt.bindString(2, name_cn);
        }

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }

        String img_url = entity.getImg_url();
        if (img_url != null) {
            stmt.bindString(4, img_url);
        }

        Integer bangumi_id = entity.getBangumi_id();
        stmt.bindLong(5, bangumi_id);


        String info = entity.getInfo();
        if (info != null) {
            stmt.bindString(2, info);
        }

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Index entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        stmt.bindLong(1, id);

        String name_cn = entity.getName_cn();
        if (name_cn != null) {
            stmt.bindString(2, name_cn);
        }

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }

        String img_url = entity.getImg_url();
        if (img_url != null) {
            stmt.bindString(4, img_url);
        }

        Integer bangumi_id = entity.getBangumi_id();
        stmt.bindLong(5, bangumi_id);


        String info = entity.getInfo();
        if (info != null) {
            stmt.bindString(2, info);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(Index entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    protected Long getKey(Index entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}
