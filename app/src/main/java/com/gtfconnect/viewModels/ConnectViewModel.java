package com.gtfconnect.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.database.repository.ConnectRepo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ConnectViewModel extends AndroidViewModel {

    private final ConnectRepo repo;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public ConnectViewModel(@NonNull Application application) {
        super(application);
        repo = new ConnectRepo(application);
    }

    public MutableLiveData<ApiResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public void getUserProfile(String api_token) {
        disposables.add(repo.getUserProfile(api_token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }

    // Todo --------------------------------------------------- YET TO BE IMPLEMENTED -------------------------------------------------------------------------------


    public void get_groupChannel_subscription(int id,String endPoint, String api_token,String device_type, String device_token) {
        disposables.add(repo.get_groupChannel_subscription(id, endPoint, api_token, device_type, device_token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }




    public void get_admin_group_channel_settings(int id, String api_token,String device_type, String device_token) {
        disposables.add(repo.get_admin_group_channel_settings(id,api_token, device_type, device_token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }




    public void update_groupChannel_profile(int id, String endPoint, String api_token, String device_type, String device_token, Map<String,Object> params) {
        disposables.add(repo.update_group_channel_profile(id, endPoint, api_token, device_type, device_token,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }





    public void update_groupChannel_permission_settings(int id, String api_token, String device_type, String device_token, Map<String,Object> params) {
        disposables.add(repo.update_group_channel_permission_settings(id, api_token, device_type, device_token,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }



    public void get_group_channel_manage_reaction_list(int id, String api_token, String device_type, String device_token, int page, int per_page) {
        disposables.add(repo.get_group_channel_manage_reaction_list(id, api_token, device_type, device_token,page,per_page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }






    public void update_groupChannel_settings(int id, String api_token, String device_type, String device_token, Map<String,Object> params) {
        disposables.add(repo.update_group_channel_permission_settings(id, api_token, device_type, device_token,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }





    public void update_groupChannel_reactions_settings(int id, String endPoint, String api_token, String device_type, String device_token, Map<String,Object> params) {
        disposables.add(repo.update_group_channel_reactions_settings(id, endPoint, api_token, device_type, device_token,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) throws Exception {
                responseLiveData.setValue(ApiResponse.success(jsonElement));

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
