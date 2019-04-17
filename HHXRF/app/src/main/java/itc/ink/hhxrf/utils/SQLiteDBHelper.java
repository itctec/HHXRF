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
    private final String TB_CREATE_FRAGMENT_RANK_INFO="create table tb_fragment_rank_info(item_id,rank_num)";
    private final String TB_CREATE_ELEMENT_LIB_INFO="create table tb_element_lib_info(element_id,element_name,element_ordinal)";
    private final String TB_CREATE_ELEMENT_SHOW_RANK_INFO="create table tb_element_show_rank_info(element_id,element_name,element_ordinal,element_rank_num)";

    public SQLiteDBHelper(Context context, String name, int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TB_CREATE_FRAGMENT_RANK_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_ELEMENT_LIB_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_ELEMENT_SHOW_RANK_INFO);
        initFragmentRankTb(sqLiteDatabase);
        initElementShowTb(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void initFragmentRankTb(SQLiteDatabase sqLiteDatabase){
        String resultRankSqlStr1="insert into tb_fragment_rank_info(item_id,rank_num) values ('11','1')";
        String resultRankSqlStr2="insert into tb_fragment_rank_info(item_id,rank_num) values ('12','2')";
        String resultRankSqlStr3="insert into tb_fragment_rank_info(item_id,rank_num) values ('13','3')";
        String resultRankSqlStr4="insert into tb_fragment_rank_info(item_id,rank_num) values ('14','4')";
        String resultRankSqlStr5="insert into tb_fragment_rank_info(item_id,rank_num) values ('15','5')";
        String resultRankSqlStr6="insert into tb_fragment_rank_info(item_id,rank_num) values ('16','6')";
        String resultRankSqlStr7="insert into tb_fragment_rank_info(item_id,rank_num) values ('17','7')";
        String resultRankSqlStr8="insert into tb_fragment_rank_info(item_id,rank_num) values ('18','8')";
        sqLiteDatabase.execSQL(resultRankSqlStr1);
        sqLiteDatabase.execSQL(resultRankSqlStr2);
        sqLiteDatabase.execSQL(resultRankSqlStr3);
        sqLiteDatabase.execSQL(resultRankSqlStr4);
        sqLiteDatabase.execSQL(resultRankSqlStr5);
        sqLiteDatabase.execSQL(resultRankSqlStr6);
        sqLiteDatabase.execSQL(resultRankSqlStr7);
        sqLiteDatabase.execSQL(resultRankSqlStr8);

        String operateRankSqlStr1="insert into tb_fragment_rank_info(item_id,rank_num) values ('21','1')";
        String operateRankSqlStr2="insert into tb_fragment_rank_info(item_id,rank_num) values ('22','2')";
        String operateRankSqlStr3="insert into tb_fragment_rank_info(item_id,rank_num) values ('23','3')";
        sqLiteDatabase.execSQL(operateRankSqlStr1);
        sqLiteDatabase.execSQL(operateRankSqlStr2);
        sqLiteDatabase.execSQL(operateRankSqlStr3);

        String systemRankSqlStr1="insert into tb_fragment_rank_info(item_id,rank_num) values ('31','1')";
        String systemRankSqlStr2="insert into tb_fragment_rank_info(item_id,rank_num) values ('32','2')";
        String systemRankSqlStr3="insert into tb_fragment_rank_info(item_id,rank_num) values ('33','3')";
        String systemRankSqlStr4="insert into tb_fragment_rank_info(item_id,rank_num) values ('34','4')";
        String systemRankSqlStr5="insert into tb_fragment_rank_info(item_id,rank_num) values ('35','5')";
        sqLiteDatabase.execSQL(systemRankSqlStr1);
        sqLiteDatabase.execSQL(systemRankSqlStr2);
        sqLiteDatabase.execSQL(systemRankSqlStr3);
        sqLiteDatabase.execSQL(systemRankSqlStr4);
        sqLiteDatabase.execSQL(systemRankSqlStr5);
    }

    public void initElementShowTb(SQLiteDatabase sqLiteDatabase) {
        String elementInsertSqlStr = "insert into tb_element_lib_info(element_id,element_name,element_ordinal) values " +
                "('1','Li','3'),('2','Be','4'),('3','B','5'),('4','C','6'),('5','N','7'),('6','O','8'),('7','F','9'),('8','Ne','10'),('9','Na','11'),('10','Mg','12')," +
                "('11','Al','13'),('12','Si','14'),('13','P','15'),('14','S','16'),('15','Cl','17'),('16','Ar','18'),('17','K','19'),('18','Ca','20'),('19','Sc','21'),('20','Ti','22')," +
                "('21','V','23'),('22','Cr','24'),('23','Mn','25'),('24','Fe','26'),('25','Co','27'),('26','Ni','28'),('27','Cu','29'),('28','Zn','30'),('29','Ga','31'),('30','Ge','32')," +
                "('31','As','33'),('32','Se','34'),('33','Br','35'),('34','Kr','36'),('35','Rb','37'),('36','Sr','38'),('37','Y','39'),('38','Zr','40'),('39','Nb','41'),('40','Mo','42')," +
                "('41','Tc','43'),('42','Ru','44'),('43','Rh','45'),('44','Pd','46'),('45','Ag','47'),('46','Cd','48'),('47','In','49'),('48','Sn','50'),('49','Sb','51'),('50','Te','52')," +
                "('51','I','53'),('52','Xe','54'),('53','Cs','55'),('54','Ba','56'),('55','Hf','72'),('56','Ta','73'),('57','W','74'),('58','Re','75'),('59','Os','76'),('60','Ir','77')," +
                "('61','Pt','78'),('62','Au','79'),('63','Hg','80'),('64','Tl','81'),('65','Pb','82'),('66','Bi','83'),('67','Po','84'),('68','At','85'),('69','Rn','86'),('70','Fr','87')," +
                "('71','Ra','88'),('72','La','57'),('73','Ce','58'),('74','Pr','59'),('75','Nd','60'),('76','Pm','61'),('77','Sm','62'),('78','Eu','63'),('79','Gd','64'),('80','Tb','65')," +
                "('81','Dy','66'),('82','Ho','67'),('83','Er','68'),('84','Tm','69'),('85','Yb','70'),('86','Lu','71'),('87','Ac','89'),('88','Th','90'),('89','Pa','91'),('90','U','92')";
        sqLiteDatabase.execSQL(elementInsertSqlStr);
    }

}
