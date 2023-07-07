package com.immo2n.bytelover.Objects;

import java.util.List;

public class ClassObj {
    private int id;
    private String title;
    private String class_link;
    private String video_link;
    private String start_date;
    private String start_time;
    private String delayed_date;
    private String status;
    private String course_name;
    private List<FileItem> file_list;
    public static class FileItem {
        private int class_id;
        private String name;
        private String size;
        private String link;

        public int getClass_id() {
            return class_id;
        }

        public void setClass_id(int class_id) {
            this.class_id = class_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClass_link() {
        return class_link;
    }

    public void setClass_link(String class_link) {
        this.class_link = class_link;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getDelayed_date() {
        return delayed_date;
    }

    public void setDelayed_date(String delayed_date) {
        this.delayed_date = delayed_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public List<FileItem> getFile_list() {
        return file_list;
    }

    public void setFile_list(List<FileItem> file_list) {
        this.file_list = file_list;
    }
}
