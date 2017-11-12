import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by binh on 11/4/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
