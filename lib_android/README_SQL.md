# android SQLite 总结


## 数据库升级增加表和删除表都不涉及数据迁移，但是修改表涉及到对原有数据 进行迁移。升级的方法如下所示:

将现有表命名为临时表。
创建新表。
将临时表的数据导入新表。
删除临时表。
如果是跨版本数据库升级，可以有两种方式，如下所示: 逐级升级，确定相邻版本与现在版本的差别，V1升级到V2,V2升级到V3，依次类推。
跨级升级，确定每个版本与现在数据库的差别，为每个case编写专门升级大代码。

        
        public class DBservice extends SQLiteOpenHelper{
            private String CREATE_BOOK = "create table book(bookId integer
        primarykey,bookName text);";
            private String CREATE_TEMP_BOOK = "alter table book rename to _temp_book";
            private String INSERT_DATA = "insert into book select *,'' from _temp_book";
            private String DROP_BOOK = "drop table _temp_book";
            public DBservice(Context context, String name, CursorFactory factory,int
        version) {
            super(context, name, factory, version);
            }
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_BOOK);
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (newVersion) {
        } }