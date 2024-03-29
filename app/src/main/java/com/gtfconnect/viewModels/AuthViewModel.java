package com.gtfconnect.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.database.repository.AuthRepo;
import com.gtfconnect.models.CountryData;

import java.io.File;
import java.util.List;
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
/*


    // user access
    public void insertUser(ProfileData loginData) {
        repo.insertUser(loginData);
    }

    public void UpdateImage(String url,int id) {
        repo.UpdateImage(url,id);
    }

    public void UpdateEmail(String email,int id) {
        repo.UpdateEmail(email,id);
    }


    public LiveData<ProfileData> getUSer() {
        return repo.getUser();
    }

    public LiveData<List<CountryData>> GetCountry() {
        return repo.GetCountry();
    }

    public void logout() {
        repo.logout();
    }
*/

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
