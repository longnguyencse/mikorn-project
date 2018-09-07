package com.mikorn.ifp.storm.bolt;

import com.google.gson.Gson;
import com.mikorn.ifp.storm.constants.CassandraContants;
import com.mikorn.ifp.storm.model.GTDataInfos;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ElasticSearchBolt extends BaseRichBolt
{
// ------------------------------ FIELDS ------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(BaseRichBolt.class);
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
        String postId = tuple.getString(0);
        LOG.error("ELASTICSEARCH : " + postId);

        GTDataInfos post = null;
        try {
            post = new Gson().fromJson(postId, GTDataInfos.class);

        } catch (Exception e) {
            LOG.error("PARAM WRONG FORMAT GT_DATA JSON");
            return;
        }
        HttpPost request = new HttpPost("http://192.168.1.174:9200/gt_data/gt_data/" + post.getFile_id());
        request.addHeader("Content-Type", "application/json;charset=UTF-8");
        try
        {
            StringEntity entity = new StringEntity("{\n" +
                    "    \"file_name\": \"" + post.getFile_id() + "\",\n" +
                    "    \"gt_timestamp\": \"" + post.getCreated_time() + "\",\n" +
                    "    \"weather\": \"" + post.getWeather() + "\",\n" +
                    "    \"location\": \"" + post.getLocation() + "\"\n" +
                    "}", "UTF-8");
            request.setEntity(entity);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpClientBuilder bld = HttpClientBuilder.create();
        HttpClient client = bld.build();
        try
        {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            collector.ack(tuple);
            LOG.info("Post to ElasticSearch gt_data table: " + EntityUtils.toString(entity, "UTF-8"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        request = new HttpPost("http://192.168.1.174:9200/file_and_version_info/file_and_version_info/" + post.getFile_id() + "_" + CassandraContants.FILE_VERSION_START);
        request.addHeader("Content-Type", "application/json;charset=UTF-8");
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try
        {
            StringEntity entity = new StringEntity("{\n" +
                    "    \"file_name\": \"" + post.getFile_id() + "\",\n" +
                    "    \"cur_version\": \"" + CassandraContants.FILE_VERSION_START + "\",\n" +
                    "    \"annotated_status\": \"" + CassandraContants.VALUE_STATUS_NEW + "\",\n" +
                    "    \"org_file_file_type\": \"" + post.getFile_type() + "\",\n" +
                    "    \"org_file_created_time\": \"" + sdf.format(new Date()) + "\",\n" +
                    "    \"version_ready_state\": \"" + CassandraContants.VALUE_SUB_STATUS_READY + "\",\n" +
                    "    \"prev_version\": null,\n" +
                    "    \"total_frames\": \"" + post.getTotal_frame() + "\",\n" +
                    "    \"version_created_time\": \"" +sdf.format(new Date()) + "\",\n" +
                    "    \"file_extension\": \"" + post.getFile_extension() + "\"\n" +
                    "}", "UTF-8");
            request.setEntity(entity);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        bld = HttpClientBuilder.create();
        client = bld.build();
        try
        {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            collector.ack(tuple);
            LOG.info("Post to ElasticSearch file_and_version_info table: " + EntityUtils.toString(entity, "UTF-8"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

// --------------------- Interface IComponent ---------------------


    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
    {

    }
}
