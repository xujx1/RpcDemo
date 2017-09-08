package com.xujx.rpc.dto.response;

/**
 * Created by xujinxin on 2017/9/6.
 * 封装远程调用返回对象
 */
public class RpcResponse {

    /**
     * 请求的序号
     */
    private Long requestId;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 返回结果
     */
    private Object result;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId=" + requestId +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }
}
