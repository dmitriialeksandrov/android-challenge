package com.dmitriialeksandrov.githubapp.screen.general;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.dmitriialeksandrov.githubapp.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingDialog extends DialogFragment {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    @NonNull
    public static LoadingView view(@NonNull FragmentManager fm) {
        return new LoadingDialogView(fm);
    }

    @NonNull
    public static LoadingView view(@NonNull Fragment fragment) {
        return view(fragment.getFragmentManager());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(View.inflate(getActivity(), R.layout.dialog_loading, null))
                .create();
    }

    private static class LoadingDialogView implements LoadingView {

        private final FragmentManager fragmentManager;

        private final AtomicBoolean waitToHide;

        private LoadingDialogView(@NonNull FragmentManager fm) {
            fragmentManager = fm;
            boolean shown = fm.findFragmentByTag(LoadingDialog.class.getName()) != null;
            waitToHide = new AtomicBoolean(shown);
        }

        @Override
        public void showLoading() {
            if (waitToHide.compareAndSet(false, true)) {
                if (fragmentManager.findFragmentByTag(LoadingDialog.class.getName()) == null) {
                    DialogFragment dialog = new LoadingDialog();
                    dialog.show(fragmentManager, LoadingDialog.class.getName());
                }
            }
        }

        @Override
        public void hideLoading() {
            if (waitToHide.compareAndSet(true, false)) {
                HANDLER.post(new HideTask(fragmentManager));
            }
        }
    }

    private static class HideTask implements Runnable {

        private final WeakReference<FragmentManager> fragmentManagerReference;

        private int attempts = 10;

        public HideTask(@NonNull FragmentManager fm) {
            fragmentManagerReference = new WeakReference<>(fm);
        }

        @Override
        public void run() {
            HANDLER.removeCallbacks(this);
            final FragmentManager fm = fragmentManagerReference.get();
            if (fm != null) {
                final LoadingDialog dialog = (LoadingDialog) fm.findFragmentByTag(LoadingDialog.class.getName());
                if (dialog != null) {
                    dialog.dismissAllowingStateLoss();
                } else if (--attempts >= 0) {
                    HANDLER.postDelayed(this, 300);
                }
            }
        }
    }

}
