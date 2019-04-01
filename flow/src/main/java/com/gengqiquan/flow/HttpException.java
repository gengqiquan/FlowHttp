

package com.gengqiquan.flow;

import retrofit2.Response;

public
class HttpException extends Exception {
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
