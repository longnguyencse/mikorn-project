package com.mikorn.ifp.storm.bolt;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.google.gson.Gson;
import com.mikorn.ifp.storm.services.CassandraConnection;
import com.mikorn.ifp.storm.constants.CassandraContants;
import com.mikorn.ifp.storm.model.GTDataInfos;
import com.mikorn.ifp.storm.utils.CassandraUtility;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CassandraWriterBolt extends BaseRichBolt
{
// ------------------------------ FIELDS ------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(CassandraWriterBolt.class);

    private final static String USERNAME = "ifp";
    private final static String PASSWORD = "test";
    private final static String HOST = "192.168.1.152";
    private final static String KEYSPACE = "ifp";

    private Session session;
    private OutputCollector collector;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface IBolt ---------------------

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple)
    {
        try
        {
            CassandraConnection connection = new CassandraConnection(HOST, KEYSPACE, USERNAME, PASSWORD);
            session = connection.getSession();
            LOG.info("content " + tuple.getString(0));
//            UserActivity userActivity = new UserActivity(new Gson().fromJson(tuple.getString(0), Post.class));
            boundCQLStatement(tuple);
            connection.close();
        }
        catch (Throwable t)
        {
            collector.reportError(t);
            collector.fail(tuple);
            LOG.error("tuple data error " + t.toString());
        }
    }

// --------------------- Interface IComponent ---------------------


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("name", "post_id", "author_id", "content", "channel_ids", "published_time"));
    }

// -------------------------- OTHER METHODS --------------------------

    private void boundCQLStatement(Tuple input) {
        GTDataInfos infos = new GTDataInfos();
        String gtDataJson = input.getString(0);
        LOG.info("GT_DATA RECEIVE FROM KAFKA TOPIC: " + gtDataJson);
        GTDataInfos post = null;
        try {
            post = new Gson().fromJson(gtDataJson, GTDataInfos.class);

        } catch (Exception e) {
            LOG.error("PARAM WRONG FORMAT GT_DATA JSON");
            return;
        }
        infos.setFile_id(post.getFile_id());
        infos.setFile_type(post.getFile_type()); // file_type
        infos.setCreated_time(post.getCreated_time()); // created_time (format: "2018-08-23 15:11:10.545")
        infos.setWeather(post.getWeather()); // weather
        infos.setLocation(post.getLocation()); // location
        infos.setHdfs_location(post.getHdfs_location()); //hdfs_path
        infos.setFile_extension(post.getFile_extension()); // file_extension
        infos.setTotal_frame(post.getTotal_frame()); //total frame
        infos.setWidth(post.getWidth()); // width frame
        infos.setHeight(post.getHeight()); // height frame
        infos.setFps(post.getFps()); // fps
        if (!infos.isValidGTData()) {
            LOG.error("PARAM GT_DATA INVALID");
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = null;
        try {
            date = formatter.parse(infos.getCreated_time());
        } catch (ParseException e) {
            LOG.error("tuple data error " + e.getMessage());
            return;
        }
        BatchStatement batchStatement = new BatchStatement();
        PreparedStatement statement = session.prepare(CassandraUtility.buildQuInsGTData());
        batchStatement.add(statement
                .bind(
                        infos.getFile_id(),
                        date,
                        infos.getWeather(),
                        infos.getLocation()
                ));

        statement = session.prepare(CassandraUtility.buildQuInsFileVersion());
        batchStatement.add(statement.bind(
                infos.getFile_id(),
                CassandraContants.FILE_VERSION_START,
                CassandraContants.VALUE_STATUS_NEW,
                infos.getTotal_frame(),
                infos.getFile_extension(),
                infos.getFile_type(),
                new Date(),
                CassandraContants.VALUE_SUB_STATUS_READY,
                null,
                new Date(),
                infos.getHdfs_location(),
                infos.getHeight(),
                infos.getWidth(),
                infos.getFps()
        ));
        session.execute(batchStatement);

//        HttpPost request = new HttpPost("http://192.168.1.174:9200/gt_data/gt_data" + post.getFile_id());
//        request.addHeader("Content-Type", "application/json;charset=UTF-8");
//        try
//        {
//            StringEntity entity = new StringEntity("{\n" +
//                    "    \"file_name\": \"" + post.getFile_id() + "\",\n" +
//                    "    \"gt_timestamp\": \"" + post.getCreated_time() + "\",\n" +
//                    "    \"weather\": \"" + post.getWeather() + "\",\n" +
//                    "    \"location\": \"" + post.getLocation() + "\"\n" +
//                    "}", "UTF-8");
//            request.setEntity(entity);
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//        HttpClientBuilder bld = HttpClientBuilder.create();
//        HttpClient client = bld.build();
//        try
//        {
//            HttpResponse response = client.execute(request);
//            HttpEntity entity = response.getEntity();
//            collector.ack(input);
//            LOG.info("Post to ElasticSearch: " + EntityUtils.toString(entity, "UTF-8"));
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }
}
