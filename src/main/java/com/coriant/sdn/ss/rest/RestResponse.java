package com.coriant.sdn.ss.rest;

/**
 * Created by manusant on 4/11/2017.
 */
public class RestResponse {

    private int statusCode;
    private String body;

    public RestResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public RestResponse(spark.Response response) {
        this.statusCode = response.status();
        this.body = response.body();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static RestResponse ok(spark.Response response) {
        response.status(204);
        response.body("Request Successfully processed");
        return new RestResponse(response);
    }

    public static <R> R ok(spark.Response response, R responseData) {
        response.status(200);
        return responseData;
    }

    public static RestResponse error(spark.Response response) {
        response.status(500);
        response.body("Something goes wrong wile processing request");
        return new RestResponse(response);
    }

    public static RestResponse error(spark.Response response, String error) {
        response.status(400);
        response.body(error);
        return new RestResponse(response);
    }

    public static RestResponse badRequest(spark.Response response, String error) {
        response.status(500);
        response.body(error);
        return new RestResponse(response);
    }

    public static RestResponse notImplemented(spark.Response response) {
        response.status(501);
        response.body("Request service not implemented yet");
        return new RestResponse(response);
    }
}
