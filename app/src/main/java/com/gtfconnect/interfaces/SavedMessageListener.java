package com.gtfconnect.interfaces;

import com.example.audiowaveform.WaveformSeekBar;

public interface SavedMessageListener {

    void deleteSavedPost(int messageID);

    void playAudio(String path, WaveformSeekBar seekBar);

    void pauseAudio(String path, WaveformSeekBar seekBar);
}
