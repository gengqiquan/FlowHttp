package com.gengqiquan.flowhttp;

public class Modell {

    /**
     * result : SUCCESS
     * code : 200
     * data : {"id":"526307800","name":"123我爱你","singer":"新乐尘符","pic":"http://p1.music.126.net/_LNk7rEEBSdAcnyHL8zi6Q==/109951163093399018.jpg?param=400y400","lrc":"https://api.bzqll.com/music/netease/lrc?id=526307800&key=579621905","url":"https://api.bzqll.com/music/netease/url?id=526307800&key=579621905","time":199}
     */

    private String result;
    private int code;
    private String msg;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Modell{" +
                "result='" + result + '\'' +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
