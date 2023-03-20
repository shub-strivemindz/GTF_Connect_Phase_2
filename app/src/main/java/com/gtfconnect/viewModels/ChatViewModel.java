package com.gtfconnect.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.gtfconnect.controller.ApiResponse;
import com.gtfconnect.database.repository.AuthRepo;
import com.gtfconnect.database.repository.ChatRepo;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.PartMap;

public class ChatViewModel extends AndroidViewModel {

    private final ChatRepo repo;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repo = new ChatRepo(application);
    }

    public MutableLiveData<ApiResponse> getResponseLiveData() {
        return responseLiveData;
    }


    public void pinMessage(Map<String, Object> param) {
        disposables.add(repo.pinMessage(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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



    public void getPinnedMessages(Map<String, Object> param) {
        disposables.add(repo.getPinnedMessages(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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



    public void removePinnedMessage(Map<String, Object> param) {
        disposables.add(repo.removePinnedMessage(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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


    public void removeAllPinnedMessage(Map<String, Object> param) {
        disposables.add(repo.removeAllPinnedMessage(param).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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



    public void getEmojiList() {
        disposables.add(repo.getEmojiList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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


    public void attachFile(Map<String, Object> params,
                           List<MultipartBody.Part> files) {

        disposables.add(repo.attachFile(params,files).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
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
