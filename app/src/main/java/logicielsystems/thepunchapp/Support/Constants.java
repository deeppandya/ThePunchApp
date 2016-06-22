package logicielsystems.thepunchapp.Support;

/**
 * Created by Deep on 01/05/2016.
 */
public class Constants {

    public static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    public class ServerUrl {
        public static final String LOGIN_URL="http://159.203.42.206:8080/ThePunchApp/api/auth/login";
        public static final String ADD_EMPLOYEE_URL="http://159.203.42.206:8080/ThePunchApp/api/company/addEmployee";
        public static final String ADD_TASK_URL="http://159.203.42.206:8080/ThePunchApp/api/company/addTask";
    }

    public class AsyncParams{

        //Login params
        public static final String EMAIL="email";
        public static final String PASSWORD="password";

        //AddEmployee params
        public static final String EMPLOYEES="employees";

        //AddTask params
        public static final String TASKS="tasks";

    }
}
