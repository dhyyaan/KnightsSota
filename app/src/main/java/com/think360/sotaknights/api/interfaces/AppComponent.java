package com.think360.sotaknights.api.interfaces;



import com.think360.sotaknights.activities.LoginActivity;
import com.think360.sotaknights.api.ApplicationModule;
import com.think360.sotaknights.api.HttpModule;
import com.think360.sotaknights.fragments.AskQuestionFragment;
import com.think360.sotaknights.fragments.CompanyFragment;
import com.think360.sotaknights.fragments.MapFragment;
import com.think360.sotaknights.fragments.ProfileFragment;
import com.think360.sotaknights.fragments.RegisterFragment;
import com.think360.sotaknights.fragments.TaskFragment;
import com.think360.sotaknights.fragments.ThreadAlarm;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {HttpModule.class, ApplicationModule.class})
public interface AppComponent {
      void inject(LoginActivity fragment);
      void inject(RegisterFragment fragment);
      void inject(MapFragment fragment);
      void inject(CompanyFragment fragment);
      void inject(AskQuestionFragment fragment);
      void inject(ThreadAlarm fragment);
      void inject(ProfileFragment fragment);
      void inject(TaskFragment fragment);


   // ApiService api();

}
