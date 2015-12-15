package com.elena;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.FileInputStream;
import java.io.IOException;
//play background music
public class Sound {

    public Sound(){
    //music method to play sound file from. https://www.youtube.com/watch?v=VMSTTg5EEnY
    //objects to play music continuously
    AudioPlayer MGP = AudioPlayer.player;
    AudioStream BGM;//background music
    AudioData MD;//my data
    ContinuousAudioDataStream loop = null;
    //new BackGroundMusic audio stream
    try {
        BGM = new AudioStream(new FileInputStream("hello.wma"));//random file I had in Sample music
        MD = BGM.getData(); //get data from file and place into player
        loop = new ContinuousAudioDataStream(MD);

    }catch (IOException error){
        System.out.println("Error with music file.");
        System.out.println(error);
    }

    MGP.start(loop);

}}
