package itc.ink.hhxrf.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangwenjiang on 2018/10/15.
 */

public class SQLiteDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_FILE_NAME="local_db.db3";
    public static final int DATABASE_VERSION=1;
    private final String TB_CREATE_LOGIN_INFO="create table tb_rank_info(item_id,rank_num)";

    public SQLiteDBHelper(Context context, String name, int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TB_CREATE_LOGIN_INFO);
        initRankTb(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void initRankTb(SQLiteDatabase sqLiteDatabase){
        String resultRankSqlStr1="insert into tb_rank_info(item_id,rank_num) values ('11','1')";
        String resultRankSqlStr2="insert into tb_rank_info(item_id,rank_num) values ('12','2')";
        String resultRankSqlStr3="insert into tb_rank_info(item_id,rank_num) values ('13','3')";
        String resultRankSqlStr4="insert into tb_rank_info(item_id,rank_num) values ('14','4')";
        String resultRankSqlStr5="insert into tb_rank_info(item_id,rank_num) values ('15','5')";
        String resultRankSqlStr6="insert into tb_rank_info(item_id,rank_num) values ('16','6')";
        String resultRankSqlStr7="insert into tb_rank_info(item_id,rank_num) values ('17','7')";
        String resultRankSqlStr8="insert into tb_rank_info(item_id,rank_num) values ('18','8')";
        sqLiteDatabase.execSQL(resultRankSqlStr1);
        sqLiteDatabase.execSQL(resultRankSqlStr2);
        sqLiteDatabase.execSQL(resultRankSqlStr3);
        sqLiteDatabase.execSQL(resultRankSqlStr4);
        sqLiteDatabase.execSQL(resultRankSqlStr5);
        sqLiteDatabase.execSQL(resultRankSqlStr6);
        sqLiteDatabase.execSQL(resultRankSqlStr7);
        sqLiteDatabase.execSQL(resultRankSqlStr8);

        String operateRankSqlStr1="insert into tb_rank_info(item_id,rank_num) values ('21','1')";
        String operateRankSqlStr2="insert into tb_rank_info(item_id,rank_num) values ('22','2')";
        String operateRankSqlStr3="insert into tb_rank_info(item_id,rank_num) values ('23','3')";
        sqLiteDatabase.execSQL(operateRankSqlStr1);
        sqLiteDatabase.execSQL(operateRankSqlStr2);
        sqLiteDatabase.execSQL(operateRankSqlStr3);

        String systemRankSqlStr1="insert into tb_rank_info(item_id,rank_num) values ('31','1')";
        String systemRankSqlStr2="insert into tb_rank_info(item_id,rank_num) values ('32','2')";
        String systemRankSqlStr3="insert into tb_rank_info(item_id,rank_num) values ('33','3')";
        String systemRankSqlStr4="insert into tb_rank_info(item_id,rank_num) values ('34','4')";
        String systemRankSqlStr5="insert into tb_rank_info(item_id,rank_num) values ('35','5')";
        sqLiteDatabase.execSQL(systemRankSqlStr1);
        sqLiteDatabase.execSQL(systemRankSqlStr2);
        sqLiteDatabase.execSQL(systemRankSqlStr3);
        sqLiteDatabase.execSQL(systemRankSqlStr4);
        sqLiteDatabase.execSQL(systemRankSqlStr5);
    }
}
