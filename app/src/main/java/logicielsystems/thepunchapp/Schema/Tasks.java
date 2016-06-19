package logicielsystems.thepunchapp.Schema;

import com.orm.SugarRecord;

/**
 * Created by Deep on 18/06/2016.
 */
public class Tasks extends SugarRecord {

    private long companyId;
    private String taskId;
    private String taskName;

    public Tasks() {
    }

    public Tasks(long companyId, String taskId, String taskName) {
        this.companyId = companyId;
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
