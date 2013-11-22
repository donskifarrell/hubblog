package com.donskifarrell.Hubblog;

import com.donskifarrell.Hubblog.Interfaces.DataProvider;
import com.donskifarrell.Hubblog.Providers.HubblogDataProvider;
import com.google.inject.AbstractModule;

/**
 * Created with IntelliJ IDEA.
 * User: donski
 * Date: 22/11/13
 * Time: 17:00
 */
public class BindingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataProvider.class).to(HubblogDataProvider.class);
    }
}
