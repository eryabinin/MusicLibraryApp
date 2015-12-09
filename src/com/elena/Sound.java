package com.elena;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.FileInputStream;
import java.io.IOException;
//play background music
public class Sound {
    public static void sound(){
    //music method to play sound file from. https://www.youtube.com/watch?v=VMSTTg5EEnY
    //objects to play music continuously
    AudioPlayer MGP = AudioPlayer.player;
    AudioStream BGM;
    AudioData MD;//my data
    ContinuousAudioDataStream loop = null;
    //new BackGroundMusic audio stream
    try {
        BGM = new AudioStream(new FileInputStream("Kalimba.mp3"));//random file I had in Sample music, TODO find a favorite one
        MD = BGM.getData(); //get data from file and place into player
        loop = new ContinuousAudioDataStream(MD);

    }catch (IOException error){
        System.out.println("Error with music file.");
        System.out.println(error);
    }

    MGP.start(loop);

}}
