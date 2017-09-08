package com.xujx.rpc.dto.request;

import java.util.Arrays;

/**
 * Created by xujinxin on 2017/9/6.
 * 封装远程调用请求对象
 */
public class RpcRequest {

    /**
     * 请求的序号
     */
    private Long requestId;

    /**
     * 请求的类名 : 全类名
     */
    private String className;

    /**
     * 请求类的方法名
     */
    private String methodName;

    /**
     * 参数的属性
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数
     */
    private Object[] parameters;


    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId=" + requestId +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
