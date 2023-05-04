package com.gtfconnect.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.database.repository.ConnectRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.reflect.TypeOfKt;
import okhttp3.MultipartBody;

public class ConnectViewModel extends AndroidViewModel {

    private final ConnectRepo repo;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();




    private final String DEVICE_TYPE = "android";

    private final String DEVICE_TOKEN = "test";


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


    public void get_groupChannel_subscription(int id,String endPoint, String api_token) {
        disposables.add(repo.get_groupChannel_subscription(id, endPoint, api_token, DEVICE_TYPE, DEVICE_TOKEN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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


    public void update_groupChannel_profile(int id,String api_token,  Map<String,Object> params) {
        disposables.add(repo.update_group_channel_profile(id, api_token, DEVICE_TYPE, DEVICE_TOKEN,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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






    public void get_group_channel_info(int id,String api_token) {
        disposables.add(repo.get_group_channel_info(id, api_token, DEVICE_TYPE, DEVICE_TOKEN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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





    public void update_groupChannel_permission_settings(int id, String api_token,  Map<String,Object> params) {
        disposables.add(repo.update_group_channel_permission_settings(id, api_token, DEVICE_TYPE, DEVICE_TOKEN,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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



    public void get_group_channel_manage_reaction_list(int id, String api_token, int page, int per_page,int isActive) {
        disposables.add(repo.get_group_channel_manage_reaction_list(id, api_token, DEVICE_TYPE, DEVICE_TOKEN, page,per_page,isActive).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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





    public void get_group_channel_manage_subscriber_list(int id, String api_token,  int page, int per_page) {
        disposables.add(repo.get_group_channel_manage_subscriber_list(id, api_token, DEVICE_TYPE, DEVICE_TOKEN,page,per_page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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






    public void update_group_channel_reaction_list(int id, String api_token,  Map<String,Object> params) {
        disposables.add(repo.update_group_channel_reaction_list(id, api_token, DEVICE_TYPE, DEVICE_TOKEN,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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







    public void get_dummy_user_list(int id, String api_token, String device_type, String device_token) {
        disposables.add(repo.get_dummy_user_list(id, api_token, DEVICE_TYPE, DEVICE_TOKEN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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







    public void update_dummy_user_list(int id, String api_token, Map<String,Object> params) {
        disposables.add(repo.update_dummy_user_list(id, api_token, DEVICE_TYPE, DEVICE_TOKEN,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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






    public void update_groupChannel_settings(int id, String api_token,  Map<String,Object> params) {
        disposables.add(repo.update_group_channel_settings(id, api_token, DEVICE_TYPE, DEVICE_TOKEN,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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





    public void update_groupChannel_reactions_settings(int id, String endPoint, String api_token,  Map<String,Object> params) {
        disposables.add(repo.update_group_channel_reactions_settings(id, endPoint, api_token, DEVICE_TYPE, DEVICE_TOKEN,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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








    public void get_group_channel_member_media(int id, String api_token,String memberID) {
        disposables.add(repo.get_group_channel_member_media(id, api_token, DEVICE_TYPE, DEVICE_TOKEN,memberID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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













    public void get_saved_messages(String api_token,int per_page,int page) {
        disposables.add(repo.get_saved_messages(api_token, DEVICE_TYPE, DEVICE_TOKEN,per_page,page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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







    public void save_group_channel_message(String api_token,int channelID,Map<String,Object> params) {

        final String action = "WithGcChat";

        disposables.add(repo.save_group_channel_message(api_token, DEVICE_TYPE, DEVICE_TOKEN,channelID,action,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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




    public void save_personal_message(String api_token,  String message, List<MultipartBody.Part> files) {

        final String action = "Manually";

        disposables.add(repo.save_personal_message(api_token, DEVICE_TYPE, DEVICE_TOKEN,message,action,files).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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












    public void get_exclusive_offers(String api_token, String search, int page) {

        final int per_page = 10;
        disposables.add(repo.get_exclusive_offers(api_token,DEVICE_TYPE, DEVICE_TOKEN,search,page,per_page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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





    public void delete_saved_message(int id,String api_token) {
        disposables.add(repo.delete_saved_message(id,api_token, DEVICE_TYPE, DEVICE_TOKEN).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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
