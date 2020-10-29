package com.neteru.ankh.classes.utils.async;

public abstract class BaseTask<R> implements CustomCallable<R> {
    @Override
    public void setUiForLoading() {

    }

    @Override
    public void setDataAfterLoading(R result) {

    }

    @Override
    public R call() {
        return null;
    }
}


