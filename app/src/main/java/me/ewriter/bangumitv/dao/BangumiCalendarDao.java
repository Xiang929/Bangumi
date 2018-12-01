package me.ewriter.bangumitv.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BANGUMI_CALENDAR".
*/
public class BangumiCalendarDao extends AbstractDao<BangumiCalendar, Long> {

    public static final String TABLENAME = "BANGUMI_CALENDAR";

    /**
     * Properties of entity BangumiCalendar.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name_cn = new Property(1, String.class, "name_cn", false, "NAME_CN");
        public final static Property Air_weekday = new Property(2, int.class, "air_weekday", false, "AIR_WEEKDAY");
        public final static Property Bangumi_id = new Property(3, int.class, "bangumi_id", false, "BANGUMI_ID");
        public final static Property Bangumi_total = new Property(4, Integer.class, "bangumi_total", false, "BANGUMI_TOTAL");
        public final static Property Bangumi_average = new Property(5, Float.class, "bangumi_average", false, "BANGUMI_AVERAGE");
        public final static Property Large_image = new Property(6, String.class, "large_image", false, "LARGE_IMAGE");
        public final static Property Common_image = new Property(7, String.class, "common_image", false, "COMMON_IMAGE");
        public final static Property Medium_image = new Property(8, String.class, "medium_image", false, "MEDIUM_IMAGE");
        public final static Property Small_image = new Property(9, String.class, "small_image", false, "SMALL_IMAGE");
        public final static Property Grid_image = new Property(10, String.class, "grid_image", false, "GRID_IMAGE");
        public final static Property Rank = new Property(11, Integer.class, "rank", false, "RANK");
        public final static Property Name_jp = new Property(12, String.class, "name_jp", false, "NAME_JP");
    };


    public BangumiCalendarDao(DaoConfig config) {
        super(config);
    }
    
    public BangumiCalendarDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BANGUMI_CALENDAR\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME_CN\" TEXT NOT NULL ," + // 1: name_cn
                "\"AIR_WEEKDAY\" INTEGER NOT NULL ," + // 2: air_weekday
                "\"BANGUMI_ID\" INTEGER NOT NULL ," + // 3: bangumi_id
                "\"BANGUMI_TOTAL\" INTEGER," + // 4: bangumi_total
                "\"BANGUMI_AVERAGE\" REAL," + // 5: bangumi_average
                "\"LARGE_IMAGE\" TEXT," + // 6: large_image
                "\"COMMON_IMAGE\" TEXT," + // 7: common_image
                "\"MEDIUM_IMAGE\" TEXT," + // 8: medium_image
                "\"SMALL_IMAGE\" TEXT," + // 9: small_image
                "\"GRID_IMAGE\" TEXT," + // 10: grid_image
                "\"RANK\" INTEGER," + // 11: rank
                "\"NAME_JP\" TEXT);"); // 12: name_jp
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BANGUMI_CALENDAR\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BangumiCalendar entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName_cn());
        stmt.bindLong(3, entity.getAir_weekday());
        stmt.bindLong(4, entity.getBangumi_id());
 
        Integer bangumi_total = entity.getBangumi_total();
        if (bangumi_total != null) {
            stmt.bindLong(5, bangumi_total);
        }
 
        Float bangumi_average = entity.getBangumi_average();
        if (bangumi_average != null) {
            stmt.bindDouble(6, bangumi_average);
        }
 
        String large_image = entity.getLarge_image();
        if (large_image != null) {
            stmt.bindString(7, large_image);
        }
 
        String common_image = entity.getCommon_image();
        if (common_image != null) {
            stmt.bindString(8, common_image);
        }
 
        String medium_image = entity.getMedium_image();
        if (medium_image != null) {
            stmt.bindString(9, medium_image);
        }
 
        String small_image = entity.getSmall_image();
        if (small_image != null) {
            stmt.bindString(10, small_image);
        }
 
        String grid_image = entity.getGrid_image();
        if (grid_image != null) {
            stmt.bindString(11, grid_image);
        }
 
        Integer rank = entity.getRank();
        if (rank != null) {
            stmt.bindLong(12, rank);
        }
 
        String name_jp = entity.getName_jp();
        if (name_jp != null) {
            stmt.bindString(13, name_jp);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BangumiCalendar entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName_cn());
        stmt.bindLong(3, entity.getAir_weekday());
        stmt.bindLong(4, entity.getBangumi_id());
 
        Integer bangumi_total = entity.getBangumi_total();
        if (bangumi_total != null) {
            stmt.bindLong(5, bangumi_total);
        }
 
        Float bangumi_average = entity.getBangumi_average();
        if (bangumi_average != null) {
            stmt.bindDouble(6, bangumi_average);
        }
 
        String large_image = entity.getLarge_image();
        if (large_image != null) {
            stmt.bindString(7, large_image);
        }
 
        String common_image = entity.getCommon_image();
        if (common_image != null) {
            stmt.bindString(8, common_image);
        }
 
        String medium_image = entity.getMedium_image();
        if (medium_image != null) {
            stmt.bindString(9, medium_image);
        }
 
        String small_image = entity.getSmall_image();
        if (small_image != null) {
            stmt.bindString(10, small_image);
        }
 
        String grid_image = entity.getGrid_image();
        if (grid_image != null) {
            stmt.bindString(11, grid_image);
        }
 
        Integer rank = entity.getRank();
        if (rank != null) {
            stmt.bindLong(12, rank);
        }
 
        String name_jp = entity.getName_jp();
        if (name_jp != null) {
            stmt.bindString(13, name_jp);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BangumiCalendar readEntity(Cursor cursor, int offset) {
        BangumiCalendar entity = new BangumiCalendar( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name_cn
            cursor.getInt(offset + 2), // air_weekday
            cursor.getInt(offset + 3), // bangumi_id
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // bangumi_total
            cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5), // bangumi_average
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // large_image
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // common_image
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // medium_image
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // small_image
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // grid_image
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // rank
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // name_jp
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BangumiCalendar entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName_cn(cursor.getString(offset + 1));
        entity.setAir_weekday(cursor.getInt(offset + 2));
        entity.setBangumi_id(cursor.getInt(offset + 3));
        entity.setBangumi_total(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setBangumi_average(cursor.isNull(offset + 5) ? null : cursor.getFloat(offset + 5));
        entity.setLarge_image(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCommon_image(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMedium_image(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSmall_image(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGrid_image(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRank(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setName_jp(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BangumiCalendar entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BangumiCalendar entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
