package com.crucentralcoast.app.presentation.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.R;


/**
 * Created by main on 4/26/2016.
 */
public class AlertDialogCreator {

    private AlertDialog alertDialog;

    public AlertDialogCreator(Context context, String title, String msg,
                              DialogInterface.OnClickListener posAction, String posMsg,
                              DialogInterface.OnClickListener negAction, String negMsg) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, posMsg, posAction);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negMsg, negAction);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
    }

    //used for more common yes/no alert dialogs
    public AlertDialogCreator(Context context, String title, String msg,
                              DialogInterface.OnClickListener posAction) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, CruApplication.getContext().getString(R.string.alert_dialog_yes), posAction);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, CruApplication.getContext().getString(R.string.alert_dialog_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.hide();
                    }
                });
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
    }

    public void setMessage(String newMsg) {
        alertDialog.setMessage(newMsg);
    }

    public void show() {
        alertDialog.show();
    }

    public void hide() {
        alertDialog.hide();
    }

    public void setView(View view) {
        alertDialog.setView(view);
    }
}
