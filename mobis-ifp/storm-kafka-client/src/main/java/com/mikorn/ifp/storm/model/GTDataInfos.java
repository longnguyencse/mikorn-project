package com.mikorn.ifp.storm.model;

import org.apache.commons.lang3.StringUtils;

public class GTDataInfos {

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public int getNumber_pedestrian() {
        return number_pedestrian;
    }

    public void setNumber_pedestrian(int number_pedestrian) {
        this.number_pedestrian = number_pedestrian;
    }

    public int getNumber_car() {
        return number_car;
    }

    public void setNumber_car(int number_car) {
        this.number_car = number_car;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    String file_id;
    String created_time;
    int number_pedestrian;
    int number_car;
    String weather;
    String location;
    String time;
    String file_type;
    int total_frame;
    int img_width;

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getHdfs_location() {
        return hdfs_location_path;
    }

    public void setHdfs_location(String hdfs_location) {
        this.hdfs_location_path = hdfs_location;
    }

    String file_extension;
    String hdfs_location_path;

    public int getTotal_frame() {
        return total_frame;
    }

    public void setTotal_frame(int total_frame) {
        this.total_frame = total_frame;
    }

    public int getWidth() {
        return img_width;
    }

    public void setWidth(int width) {
        this.img_width = width;
    }

    public int getHeight() {
        return img_height;
    }

    public void setHeight(int height) {
        this.img_height = height;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    int img_height;
    int fps;

    public boolean isValidGTData() {
        if (StringUtils.isBlank(file_id) || StringUtils.isBlank(file_type) || StringUtils.isBlank(created_time)
                || StringUtils.isBlank(file_extension)) {
            return false;
        }
        return true;
    }
}
