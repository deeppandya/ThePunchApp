package logicielsystems.thepunchapp.Schema;

import com.orm.SugarRecord;

/**
 * Created by Deep on 18/06/2016.
 */
public class Employees extends SugarRecord {

    private long companyId;
    /*private String empId;
    private String Name;*/
    private String empId;
    private String Name;

    public Employees() {
    }

    public Employees(long companyId, String empId, String Name) {
        this.companyId = companyId;
        this.empId = empId;
        this.Name = Name;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}


