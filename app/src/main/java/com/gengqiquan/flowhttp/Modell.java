package com.gengqiquan.flowhttp;

public class Modell {

    /**
     * result : SUCCESS
     * code : 200
     * data : {"id":"526307800","name":"123我爱你","singer":"新乐尘符","pic":"http://p1.music.126.net/_LNk7rEEBSdAcnyHL8zi6Q==/109951163093399018.jpg?param=400y400","lrc":"https://api.bzqll.com/music/netease/lrc?id=526307800&key=579621905","url":"https://api.bzqll.com/music/netease/url?id=526307800&key=579621905","time":199}
     */

    private String result;
    private int code;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Modell{" +
                "result='" + result + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", singer='" + singer + '\'' +
                    ", pic='" + pic + '\'' +
                    ", lrc='" + lrc + '\'' +
                    ", url='" + url + '\'' +
                    ", time=" + time +
                    '}';
        }

        /**
         * id : 526307800
         * name : 123我爱你
         * singer : 新乐尘符
         * pic : http://p1.music.126.net/_LNk7rEEBSdAcnyHL8zi6Q==/109951163093399018.jpg?param=400y400
         * lrc : https://api.bzqll.com/music/netease/lrc?id=526307800&key=579621905
         * url : https://api.bzqll.com/music/netease/url?id=526307800&key=579621905
         * time : 199
         */

        private String id;
        private String name;
        private String singer;
        private String pic;
        private String lrc;
        private String url;
        private int time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSinger() {
            return singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
