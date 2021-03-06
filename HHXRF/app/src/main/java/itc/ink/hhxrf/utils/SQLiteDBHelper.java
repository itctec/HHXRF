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
    private final String TB_CREATE_COMPOUND_LIB_INFO="create table tb_compound_lib_info(compound_id,compound_element,compound_name,show_state)";
    private final String TB_CREATE_HISTORY_DATA="create table tb_history_data(sample_name PRIMARY KEY,mark_id,mark_name,mark_suit_value,test_datetime DATETIME,test_way,calibration_type)";
    private final String TB_CREATE_HISTORY_DATA_CONTENT="create table tb_history_data_content(element_name,element_concentration float,element_range,element_average float,sample_name,group_name,FOREIGN KEY(sample_name) REFERENCES tb_history_data(sample_name) on update cascade on delete cascade)";
    private final String TB_CREATE_TYPE_CALIBRATION="create table tb_type_calibration(type_name PRIMARY KEY,enable_state)";
    private final String TB_CREATE_TYPE_CALIBRATION_CONTENT="create table tb_type_calibration_content(element_id,element_name,value_multiplication,value_plus,value_unit,type_name,FOREIGN KEY(type_name) REFERENCES tb_type_calibration(type_name))";
    private final String TB_CREATE_MARK_DB="create table tb_mark_db(mark_db_id PRIMARY KEY,mark_db_name,mark_db_selected)";
    private final String TB_CREATE_MARK="create table tb_mark(mark_id PRIMARY KEY,mark_name,mark_num,mark_db_id,mark_rank_num,FOREIGN KEY(mark_db_id) REFERENCES tb_mark_db(mark_db_id) on delete cascade)";
    private final String TB_CREATE_MARK_ELEMENT="create table tb_mark_element(element_id,element_name,element_min_value,element_max_value,element_tol_value,mark_id,FOREIGN KEY(mark_id) REFERENCES tb_mark(mark_id) on delete cascade)";

    public SQLiteDBHelper(Context context, String name, int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TB_CREATE_FRAGMENT_RANK_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_ELEMENT_LIB_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_ELEMENT_SHOW_RANK_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_COMPOUND_LIB_INFO);
        sqLiteDatabase.execSQL(TB_CREATE_HISTORY_DATA);
        sqLiteDatabase.execSQL(TB_CREATE_HISTORY_DATA_CONTENT);
        sqLiteDatabase.execSQL(TB_CREATE_TYPE_CALIBRATION);
        sqLiteDatabase.execSQL(TB_CREATE_TYPE_CALIBRATION_CONTENT);
        sqLiteDatabase.execSQL(TB_CREATE_MARK_DB);
        sqLiteDatabase.execSQL(TB_CREATE_MARK);
        sqLiteDatabase.execSQL(TB_CREATE_MARK_ELEMENT);
        initFragmentRankTb(sqLiteDatabase);
        initElementLibTb(sqLiteDatabase);
        initCompoundLibTb(sqLiteDatabase);
        //initHistoryTb(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void initFragmentRankTb(SQLiteDatabase sqLiteDatabase){
        String resultRankSqlStr="insert into tb_fragment_rank_info(item_id,rank_num) values " +
                "('11','1'),('12','2'),('13','3'),('14','4'),('15','5'),('16','6'),('17','7')";
        sqLiteDatabase.execSQL(resultRankSqlStr);

        String operateRankSqlStr="insert into tb_fragment_rank_info(item_id,rank_num) values ('21','1'),('22','2'),('23','3')";
        sqLiteDatabase.execSQL(operateRankSqlStr);

        String systemRankSqlStr="insert into tb_fragment_rank_info(item_id,rank_num) values ('31','1'),('32','2'),('33','3'),('34','4'),('35','5'),('36','6'),('37','7')";
        sqLiteDatabase.execSQL(systemRankSqlStr);
    }

    public void initElementLibTb(SQLiteDatabase sqLiteDatabase) {
        String elementInsertSqlStr = "insert into tb_element_lib_info(element_id,element_name,element_ordinal) values " +
                "('-1','A','-1'),('1','Ac','89'),('2','Ag','47'),('3','Al','13'),('4','Ar','18'),('5','As','33'),('6','At','85'),('7','Au','79'),('-1','B','-1'),('8','B','5')," +
                "('9','Ba','56'),('10','Be','4'),('11','Bi','83'),('12','Br','35'),('-1','C','-1'),('13','C','6'),('14','Ca','20'),('15','Cd','48'),('16','Ce','58'),('17','Cl','17')," +
                "('18','Co','27'),('19','Cr','24'),('20','Cs','55'),('21','Cu','29'),('-1','D','-1'),('22','Dy','66'),('-1','E','-1'),('23','Er','68'),('24','Eu','63'),('-1','F','-1')," +
                "('25','F','9'),('26','Fe','26'),('27','Fr','87'),('-1','G','-1'),('28','Ga','31'),('29','Gd','64'),('30','Ge','32'),('-1','H','-1'),('31','Hf','72'),('32','Hg','80')," +
                "('33','Ho','67'),('-1','I','-1'),('34','I','53'),('35','In','49'),('36','Ir','77'),('-1','K','-1'),('37','K','19'),('38','Kr','36'),('-1','L','-1'),('39','La','57')," +
                "('40','Li','3'),('41','Lu','71'),('-1','M','-1'),('42','Mg','12'),('43','Mn','25'),('44','Mo','42'),('-1','N','-1'),('45','N','7'),('46','Na','11'),('47','Nb','41')," +
                "('48','Nd','60'),('49','Ne','10'),('50','Ni','28'),('-1','O','-1'),('51','O','8'),('52','Os','76'),('-1','P','-1'),('53','P','15'),('54','Pa','91'),('55','Pb','82')," +
                "('56','Pd','46'),('57','Pm','61'),('58','Po','84'),('59','Pr','59'),('60','Pt','78'),('-1','R','-1'),('61','Ra','88'),('62','Rb','37'),('63','Re','75'),('64','Rh','45')," +
                "('65','Rn','86'),('66','Ru','44'),('-1','S','-1'),('67','S','16'),('68','Sb','51'),('69','Sc','21'),('70','Se','34'),('71','Si','14'),('72','Sm','62'),('73','Sn','50')," +
                "('74','Sr','38'),('-1','T','-1'),('75','Ta','73'),('76','Tb','65'),('77','Tc','43'),('78','Te','52'),('79','Th','90'),('80','Ti','22'),('81','Tl','81'),('82','Tm','69')," +
                "('-1','U','-1'),('83','U','92'),('-1','V','-1'),('84','V','23'),('-1','W','-1'),('85','W','74'),('-1','X','-1'),('86','Xe','54'),('-1','Y','-1'),('87','Y','39')," +
                "('88','Yb','70'),('-1','Z','-1'),('89','Zn','30'),('90','Zr','40')" ;
        sqLiteDatabase.execSQL(elementInsertSqlStr);
    }

    public void initCompoundLibTb(SQLiteDatabase sqLiteDatabase) {
        String compoundInsertSqlStr = "insert into tb_compound_lib_info(compound_id,compound_element,compound_name,show_state) values " +
                "('-1','Fe','-1','-1'),('1','Fe','Fe3O4','true'),('2','Fe','Fe2O3','false'),('-1','Al','-1','-1'),('3','Al','AL2O3','true')";
        sqLiteDatabase.execSQL(compoundInsertSqlStr);
    }


}
