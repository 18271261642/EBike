package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.net.CommNetView;

/**
 * Created by Admin
 * Date 2022/3/29
 */
public interface LoginView<T> extends CommNetView {


    void not200CodeMsg(String msg);
}
