package com.mikorn.ifp.storm.constants;

public class CassandraContants {

    // for table file_info
    public  static  String FIELD_FILE_NAME = "file_name";
    public  static  String FIELD_WEATHER = "weather";
    public  static  String FIELD_HDFS_LOCATION_PATH = "hdfs_location_path";
    public  static  String FIELD_IMG_WIDTH = "img_width";
    public  static  String FIELD_IMG_HEIGHT = "img_height";
    public  static  String FIELD_FPS = "fps";
    public static String FIELD_FILE_EXTENSION = "file_extension";

    // for table GT_data_by_file_name
    public static String FIELD_ORG_FILES_FILE_TYPE = "org_file_file_type";
    public static String FIELD_ORG_FILES_CREATED_TIME = "org_file_created_time";
    public static String FIELD_TOTAL_FRAMES = "total_frames";
    public static String FIELD_GT_TIMESTAMP = "gt_timestamp";
    public static String FIELD_LOCATION = "location";
    public static String FIELD_CUR_VERSION = "cur_version";
    public static String FIELD_VERSIONS_READY_STATE = "version_ready_state";
    public static String FIELD_PREV_VERSION = "prev_version";
    public static String FIELD_ANNOTATED_STATUS = "annotated_status";
    public static String FIELD_VERSIONS_CREATED_TIME = "version_created_time";

    // table name
    public final static  String TABLE_GT_DATA_BY_FILE_NAME = "gt_data_by_file_name";
    public final static  String TABLE_FILE_AND_VERSION_INFO_BY_FILE_NAME = "file_and_version_info_by_file_name";

    // some status value
    public static String VALUE_STATUS_NEW = "New";
    public static String VALUE_SUB_STATUS_READY = "Ready"; // status after get file from hdfs
    public static Integer FILE_VERSION_START = 0;
}
