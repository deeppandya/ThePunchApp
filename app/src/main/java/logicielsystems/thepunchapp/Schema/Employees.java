package logicielsystems.thepunchapp.Schema;

import com.orm.SugarRecord;

/**
 * Created by Deep on 18/06/2016.
 */
public class Employees extends SugarRecord {

    private long companyId;
    private String employeeId;
    private String employeeName;

    public Employees() {
    }

    public Employees(long companyId, String employeeId, String employeeName) {
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}


