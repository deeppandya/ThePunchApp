package logicielsystems.thepunchapp.Listners;

/**
 * Created by Deep on 12/06/2016.
 */
public interface LoginListner {
    void beforeLogin();
    void afterLogin(boolean isSuccess);
}
