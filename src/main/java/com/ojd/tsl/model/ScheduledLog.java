package com.ojd.tsl.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ScheduledLog implements Serializable {
    private static final long serialVersionUID = 2525367910036678105L;

    private String superScheduledName;

    private Date statrDate;

    private Date endDate;

    private Exception exception;

    private String exceptionInfo;

    private Long executionTime;

    private Boolean isSuccess;

    private ScheduledSource scheduledSource;

    private String fileName;

    public String getSuperScheduledName() {
        return superScheduledName;
    }

    public void setSuperScheduledName(String superScheduledName) {
        this.superScheduledName = superScheduledName;
    }

    public Date getStatrDate() {
        return statrDate;
    }

    public void setStatrDate(Date statrDate) {
        this.statrDate = statrDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public ScheduledSource getScheduledSource() {
        return scheduledSource;
    }

    public void setScheduledSource(ScheduledSource scheduledSource) {
        this.scheduledSource = scheduledSource;
    }

    public String getFileName() {
        return fileName;
    }

    public void generateFileName() {
        this.fileName = superScheduledName + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
    }

    public void computingTime() {
        this.executionTime = this.getEndDate().getTime() - this.statrDate.getTime();
    }
}
