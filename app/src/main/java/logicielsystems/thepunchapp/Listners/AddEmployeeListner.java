package logicielsystems.thepunchapp.Listners;

/**
 * Created by Deep on 21/06/2016.
 */
public interface AddEmployeeListner {
    void beforeAddEmployee();
    void afterAddEmployee(boolean isSuccess);
}
