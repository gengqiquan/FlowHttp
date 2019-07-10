package com.gengqiquan.flowhttp;

import com.google.gson.annotations.SerializedName;

public  class Detail {
        /**
         * sid : 29641934
         * text : 天呐，这变化也太大了吧…
         * type : video
         * thumbnail : http://wimg.spriteapp.cn/picture/2019/0709/59ead0dea21511e9b797842b2b4c75ab_wpd.jpg
         * video : http://wvideo.spriteapp.cn/video/2019/0709/59ead0dea21511e9b797842b2b4c75ab_wpd.mp4
         * images : null
         * up : 80
         * down : 8
         * forward : 0
         * comment : 5
         * uid : 20746665
         * name : 樱花丶葬礼
         * header : http://wimg.spriteapp.cn/profile/large/2019/03/26/5c99f6da60550_mini.jpg
         * top_comments_content : null
         * top_comments_voiceuri : null
         * top_comments_uid : null
         * top_comments_name : null
         * top_comments_header : null
         * passtime : 2019-07-10 02:50:02
         */

        @SerializedName("sid")
        private String sid;
        @SerializedName("text")
        private String text;
        @SerializedName("type")
        private String type;
        @SerializedName("thumbnail")
        private String thumbnail;
        @SerializedName("video")
        private String video;
        @SerializedName("images")
        private Object images;
        @SerializedName("up")
        private String up;
        @SerializedName("down")
        private String down;
        @SerializedName("forward")
        private String forward;
        @SerializedName("comment")
        private String comment;
        @SerializedName("uid")
        private String uid;
        @SerializedName("name")
        private String name;
        @SerializedName("header")
        private String header;
        @SerializedName("top_comments_content")
        private Object topCommentsContent;
        @SerializedName("top_comments_voiceuri")
        private Object topCommentsVoiceuri;
        @SerializedName("top_comments_uid")
        private Object topCommentsUid;
        @SerializedName("top_comments_name")
        private Object topCommentsName;
        @SerializedName("top_comments_header")
        private Object topCommentsHeader;
        @SerializedName("passtime")
        private String passtime;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public Object getImages() {
            return images;
        }

        public void setImages(Object images) {
            this.images = images;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getDown() {
            return down;
        }

        public void setDown(String down) {
            this.down = down;
        }

        public String getForward() {
            return forward;
        }

        public void setForward(String forward) {
            this.forward = forward;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public Object getTopCommentsContent() {
            return topCommentsContent;
        }

        public void setTopCommentsContent(Object topCommentsContent) {
            this.topCommentsContent = topCommentsContent;
        }

        public Object getTopCommentsVoiceuri() {
            return topCommentsVoiceuri;
        }

        public void setTopCommentsVoiceuri(Object topCommentsVoiceuri) {
            this.topCommentsVoiceuri = topCommentsVoiceuri;
        }

        public Object getTopCommentsUid() {
            return topCommentsUid;
        }

        public void setTopCommentsUid(Object topCommentsUid) {
            this.topCommentsUid = topCommentsUid;
        }

        public Object getTopCommentsName() {
            return topCommentsName;
        }

        public void setTopCommentsName(Object topCommentsName) {
            this.topCommentsName = topCommentsName;
        }

        public Object getTopCommentsHeader() {
            return topCommentsHeader;
        }

        public void setTopCommentsHeader(Object topCommentsHeader) {
            this.topCommentsHeader = topCommentsHeader;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

    @Override
    public String toString() {
        return "Detail{" +
                "text='" + text + '\'' +
                ", comment='" + comment + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}