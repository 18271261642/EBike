package com.bonlala.ebike.dialog;

import android.content.Context;

import com.bonlala.base.BaseDialog;
import com.bonlala.ebike.R;

import androidx.annotation.NonNull;

/**
 * Created by Admin
 * Date 2022/3/25
 */
public class PrivacyDialogView {

    public static class Builder extends CommonDialog.Builder<Builder>{


        @Override
        public Builder setThemeStyle(int id) {
            return super.setThemeStyle(id);
        }

        public Builder(Context context) {
            super(context);
            setThemeStyle(R.style.edit_AlertDialog_style);
            setCustomView(R.layout.dialog_privacy_view);
        }


    }
}
