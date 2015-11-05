package com.androidcru.helloworld;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MainPresenterTest
{
    @Test
    public void testGetSnackbarMessage() throws Exception
    {
        MainPresenter presenter = new MainPresenter();
        assertThat(presenter.getSnackbarMessage(), is("Replace with your own action"));
    }
}