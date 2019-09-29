package com.tool.rss.ui.NetWork;


/**
 * JKX2018
 * Created by xyrzx on 2018/5/7.
 */
public class HttpResult<T> {

    private int code;
    private String msg;
    private String stime;

    //业务关注的Data
    private T data;

    private T list;

    public int getCode() {

        if (code == -1){
            //AppManager.getInstance().mainLoginOut();
        }

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

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("code=").append(code);
        if (null != data) {
            sb.append(" data:").append(data.toString());
        }
        sb.append(" message=").append(msg).append(" start =").append(stime);
        return sb.toString();
    }
}
