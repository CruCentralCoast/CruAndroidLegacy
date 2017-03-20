package com.crucentralcoast.app;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JUnitJacocoTestRunner extends AndroidJUnitRunner
{

    @Override
    public void onCreate(Bundle arguments)
    {
        //RxJavaPlugins.getInstance().registerObservableExecutionHook(RxIdlingResource.get());
        super.onCreate(arguments);
    }

    @Override
    public void onStart() {
        System.setProperty("jacoco-agent.destfile", "/data/data/"+ BuildConfig.APPLICATION_ID+"/coverage.ec");
        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        createCoverageReport();
        super.finish(resultCode, results);
    }

    @Override
    public boolean onException(Object obj, Throwable e)
    {
        createCoverageReport();
        return super.onException(obj, e);
    }

    private void createCoverageReport()
    {
        try {
            Class rt = Class.forName("org.jacoco.agent.rt.RT");
            Method getAgent = rt.getMethod("getAgent");
            Method dump = getAgent.getReturnType().getMethod("dump", boolean.class);
            Object agent = getAgent.invoke(null);
            dump.invoke(agent, false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
