package cn.bos.monitor.entity;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    int id;
    int mid;
    String type;
    String name;
    String span;
    String path;
    List<String> jobList = null;
    String whereClause;
    String endKeyword;
    String[] receivers;
    String querySql;

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers.replace(" ", "").split(",");
    }

    public void setEndKeyword(String endKeyword) {
        this.endKeyword = endKeyword;
    }

    public String getEndKeyword() {
        return endKeyword;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String where_clause) {
        this.whereClause = where_clause;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public List<String> getJobList() {
        return jobList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJobList(String jobListString) {
//        System.out.println("setJobList: jobListNum -> " + jobListString.length());
        if (jobListString.length() != 0) {
            List<String> jobList = new ArrayList<>();
            for (String s : jobListString.split(",")) {
                jobList.add(s.replace("'", "").trim());
            }
            this.jobList = jobList;
        }
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
}

