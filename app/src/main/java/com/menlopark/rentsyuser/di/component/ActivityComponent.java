package com.menlopark.rentsyuser.di.component;



import com.menlopark.rentsyuser.di.scope.ActivityScope;
import com.menlopark.rentsyuser.ui.activitys.MainActivity;
import com.menlopark.rentsyuser.ui.activitys.SplashActivity;
import com.menlopark.rentsyuser.widget.dialog.VerifyDialog;

import dagger.Component;


@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {

    void inject(MainActivity activity);
    void inject(SplashActivity activity);
    void inject(VerifyDialog Dialog);
}
