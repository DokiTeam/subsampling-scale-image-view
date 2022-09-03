package com.davemorrissey.labs.subscaleview;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public final class TasksDisposer {

    private final ArrayList<WeakReference<AsyncTask<?, ?, ?>>> tasks = new ArrayList<>();

    public synchronized void dispose() {
        for (final WeakReference<AsyncTask<?, ?, ?>> taskRef : tasks) {
            final AsyncTask<?, ?, ?> task = taskRef.get();
            if (task != null) {
                task.cancel(false);
            }
        }
        tasks.clear();
    }

    public void add(final AsyncTask<?, ?, ?> task) {
        tasks.add(new WeakReference<>(task));
    }
}
