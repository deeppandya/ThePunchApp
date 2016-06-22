package logicielsystems.thepunchapp.Listners;

/**
 * Created by Deep on 22/06/2016.
 */
public interface AddTaskListner {
    void beforeAddTask();
    void afterAddTask(boolean isSuccess);
}
