package com.example.Inmopro.v1.Controller.MonitorCon;

public class Response {
    private String message;
    private Object data;  // Puedes ajustar el tipo según lo que devuelvas
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Response() {
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static class ResponseBuilder {
        private String message;
        private Object data;
        private boolean success;

        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.message = this.message;
            response.data = this.data;
            response.success = this.success;
            return response;
        }
    }
}
