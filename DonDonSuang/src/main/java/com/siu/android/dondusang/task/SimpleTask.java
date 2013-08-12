package com.siu.android.dondusang.task;

import android.os.AsyncTask;

/**
 * Created by lukas on 6/24/13.
 */
public abstract class SimpleTask<T_Owner, Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected T_Owner mOwner;

    protected SimpleTask(T_Owner owner) {
        mOwner = owner;
    }

    public void setOwner(T_Owner owner) {
        mOwner = null;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (mOwner == null) {
            return;
        }

        onResult(result);
    }

    public boolean stopTaskIfRunning() {
        boolean running = getStatus() != Status.FINISHED;

        if (running) {
            cancel(true);
        }

        mOwner = null;
        return running;
    }

    public static final <T extends SimpleTask> T stopTaskAndGetNullReference(T task) {
        if (task != null) {
            task.stopTaskIfRunning();
        }

        return null;
    }

    public abstract void onResult(Result result);
}
