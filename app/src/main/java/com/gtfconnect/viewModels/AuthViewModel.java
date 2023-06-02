package com.gtfconnect.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.database.repository.AuthRepo;

import java.io.File;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;



public class AuthViewModel extends AndroidViewModel {

    private final AuthRepo repo;
    MultipartBody.Part bodyfornt;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repo = new AuthRepo(application);
    }

    public MutableLiveData<ApiResponse> getResponseLiveData() {
        return responseLiveData;
    }




    public void Login(String device_type,String device_token,Map<String, Object> map) {
        disposables.add(repo.login(device_type,device_token,map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(jsonElement -> responseLiveData.setValue(ApiResponse.success(jsonElement)), new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }

    public void Registration(String deviceToken,Map<String, Object> map) {
        disposables.add(repo.registration(deviceToken,map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(jsonElement -> responseLiveData.setValue(ApiResponse.success(jsonElement)), new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }

    public void VerifyOTP(Map<String, Object> map) {
        disposables.add(repo.verifyOTP(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(jsonElement -> responseLiveData.setValue(ApiResponse.success(jsonElement)), new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }



    public void resendOTP(String email) {
        disposables.add(repo.resendOTP(email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(jsonElement -> responseLiveData.setValue(ApiResponse.success(jsonElement)), new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }


    public void forgetPassword(String email) {
        disposables.add(repo.forgetPassword(email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                responseLiveData.setValue(ApiResponse.loading());
            }
        }).subscribe(jsonElement -> responseLiveData.setValue(ApiResponse.success(jsonElement)), new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                responseLiveData.setValue(ApiResponse.error(throwable));
            }
        }));
    }











    public void updatePassword(String api_token,String device_type,String device_token,Map<String,Object> params) {
        disposables.add(repo.updatePassword(api_token, device_type, device_token, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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












    public void getCountryDataList() {
        disposables.add(repo.getCountryList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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


    public void getStateList(int countryCode) {
        disposables.add(repo.getStateList(countryCode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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


    public void getCityList(int stateCode) {
        disposables.add(repo.getCityList(stateCode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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









    public void updateUserProfile(String api_token,String device_type,String device_token,String fromGtfConnect,Map<String,Object> params) {
        disposables.add(repo.updateUserProfile(api_token, device_type, device_token, fromGtfConnect, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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




    public void updateProfilePic(String api_token,int userID,File image){
        RequestBody part =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        image
                );
        MultipartBody.Part image_part = MultipartBody.Part.createFormData("ProfilePic", image.getName(), part);




        disposables.add(repo.update_profile_pic(api_token,userID,image_part).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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






    public void get_cms_data(String type){

        disposables.add(repo.get_cms_data("android","test",type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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







    public void report_user(String api_token,String device_type,String device_token,Map<String,Object> params){

        disposables.add(repo.report_user(api_token,device_type,device_token,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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
