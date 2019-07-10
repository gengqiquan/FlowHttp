package com.gengqiquan.flowhttp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modell<T> {


    /**
     * code : 200
     * message : 成功!
     * result : [{"sid":"29641934","text":"天呐，这变化也太大了吧\u2026","type":"video","thumbnail":"http://wimg.spriteapp.cn/picture/2019/0709/59ead0dea21511e9b797842b2b4c75ab_wpd.jpg","video":"http://wvideo.spriteapp.cn/video/2019/0709/59ead0dea21511e9b797842b2b4c75ab_wpd.mp4","images":null,"up":"80","down":"8","forward":"0","comment":"5","uid":"20746665","name":"樱花丶葬礼","header":"http://wimg.spriteapp.cn/profile/large/2019/03/26/5c99f6da60550_mini.jpg","top_comments_content":null,"top_comments_voiceuri":null,"top_comments_uid":null,"top_comments_name":null,"top_comments_header":null,"passtime":"2019-07-10 02:50:02"},{"sid":"29640936","text":"喜欢给人挖坑的那帮家伙原来都是这种态啊","type":"video","thumbnail":"http://wimg.spriteapp.cn/picture/2019/0708/5d2359085f68a__b.jpg","video":"http://wvideo.spriteapp.cn/video/2019/0708/5d23590882fb3_wpd.mp4","images":null,"up":"85","down":"6","forward":"1","comment":"10","uid":"11996791","name":"Pescado","header":"http://wimg.spriteapp.cn/profile/large/2019/02/10/5c6015142adc7_mini.jpg","top_comments_content":null,"top_comments_voiceuri":null,"top_comments_uid":null,"top_comments_name":null,"top_comments_header":null,"passtime":"2019-07-10 02:10:01"}]
     */

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Modell{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result.toString() +
                '}';
    }
}
