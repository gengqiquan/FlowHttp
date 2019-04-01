

package com.gengqiquan.flow;


import java.io.IOException;

import okhttp3.Response;

public
class HttpException extends IOException {
    public int getHttpCode() {
        return response.code();
    }

//    public Response getResponse() {
//        return response;
//    }

    private Response response;

    public HttpException(Response response) {
        super(response.message());
        this.response = response;
    }
    public HttpException(Throwable cause) {
        super(cause);
    }
}
