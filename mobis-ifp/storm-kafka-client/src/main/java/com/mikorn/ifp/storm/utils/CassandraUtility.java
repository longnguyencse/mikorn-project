package com.mikorn.ifp.storm.utils;

import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.mikorn.ifp.storm.constants.CassandraContants;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;

public class CassandraUtility {
    public static final String keyspace = "ifp";

    // insert into GT_data_by_file_name
    public static RegularStatement buildQuInsGTData() {
        RegularStatement query = QueryBuilder
                .insertInto(keyspace, CassandraContants.TABLE_GT_DATA_BY_FILE_NAME)
                .value(CassandraContants.FIELD_FILE_NAME, bindMarker())
                .value(CassandraContants.FIELD_GT_TIMESTAMP, bindMarker())
                .value(CassandraContants.FIELD_WEATHER, bindMarker())
                .value(CassandraContants.FIELD_LOCATION, bindMarker());
        return query;
    }

    // insert into  file_and_version_info_by_file_name

    public static RegularStatement buildQuInsFileVersion() {
        RegularStatement query = QueryBuilder
                .insertInto(keyspace, CassandraContants.TABLE_FILE_AND_VERSION_INFO_BY_FILE_NAME)
                .value(CassandraContants.FIELD_FILE_NAME, bindMarker())
                .value(CassandraContants.FIELD_CUR_VERSION, bindMarker())
                .value(CassandraContants.FIELD_ANNOTATED_STATUS, bindMarker())
                .value(CassandraContants.FIELD_TOTAL_FRAMES, bindMarker())
                .value(CassandraContants.FIELD_FILE_EXTENSION, bindMarker())
                .value(CassandraContants.FIELD_ORG_FILES_FILE_TYPE, bindMarker())
                .value(CassandraContants.FIELD_ORG_FILES_CREATED_TIME, bindMarker())
                .value(CassandraContants.FIELD_VERSIONS_READY_STATE, bindMarker())
                .value(CassandraContants.FIELD_PREV_VERSION, bindMarker())
                .value(CassandraContants.FIELD_VERSIONS_CREATED_TIME, bindMarker())
                .value(CassandraContants.FIELD_HDFS_LOCATION_PATH, bindMarker())
                .value(CassandraContants.FIELD_IMG_HEIGHT, bindMarker())
                .value(CassandraContants.FIELD_IMG_WIDTH, bindMarker())
                .value(CassandraContants.FIELD_FPS, bindMarker())
//                .ifNotExists()
                ;
        return query;
    }
}
