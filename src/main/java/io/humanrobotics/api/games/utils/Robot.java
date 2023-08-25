package io.humanrobotics.api.games.utils;

import ai.humanrobotics.messaging.types.AlertDialog;
import ai.humanrobotics.messaging.types.AlertDialogOption;
import ai.humanrobotics.messaging.types.AlertDialogResult;
import ai.humanrobotics.messaging.types.FaceDisplaySettings;
import com.google.gson.Gson;
import io.humanrobotics.api.Robios;
import io.humanrobotics.api.RobiosApi;
import io.humanrobotics.api.RobiosConfig;
import io.humanrobotics.api.exception.RobiosException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Robot {
  private static final String apiKey = "...";
  private static final String robotId = "...";

  private static final String ROBOT_ADDRESS = "192.168.62.38";

  private static Robios robios;

  static {
    try {
      robios = RobiosApi.get(apiKey, RobiosConfig.forCloud(robotId));
    } catch (RobiosException e) {
      throw new RuntimeException(e);
    }
  }

  public static final long minDelay = 4500; // Minimum number of milliseconds to wait before listening to the user.
  private static String userInput = "";

  public Robot() {
    // Add the callback received the result of the recognition
    robios.addVoiceRecognitionCallback(Robot::onUserTextReceived);
    // Add a callback to receive the user click on the image
    robios.addAlertDialogCallback(Robot::onResultReceived);
  }

  public String ask(String question, String[] answers) throws Exception {
    robios.say(question);
    return listen(answers);
  }

  private static void onUserTextReceived(String userAnswer) {
    String currentDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    System.out.printf("[%s] [VoiceRecognition]: \"%s\"\n", currentDate, userAnswer);

    userInput = userAnswer;
  }

  public String listen(String[] inputOptions) throws Exception {
    userInput = "";
    while (true) {
      robios.listen();
      Thread.sleep(minDelay);

      for (String inputOption : inputOptions) {
        if (inputOption.equals(userInput)) {
          return userInput;
        }
      }
      robios.say("Não entendi");
    }
  }

  public String listen() throws Exception {
    userInput = "";
    while (userInput.isEmpty()) {
      robios.listen();
      Thread.sleep(minDelay);
    }
    return userInput;
  }

  public String displayImage(String headerText, String imageUrl, String[] btnOptions) throws Exception {
    userInput = "";

    AlertDialog dialog = new AlertDialog(headerText, "");
    dialog.setDialogType(AlertDialog.TYPE_OPTIONS);
    // image data
    dialog.setMediaType(AlertDialog.MEDIA_TYPE_IMAGE);
    dialog.setMediaUrl(imageUrl);
    // Buttons
    ArrayList<AlertDialogOption> buttonsList = new ArrayList<AlertDialogOption>();

    for (String btnOption : btnOptions) {
      buttonsList.add(new AlertDialogOption(btnOption, btnOption));
    }

    AlertDialogOption[] buttonsArr = new AlertDialogOption[buttonsList.size()];
    buttonsArr = buttonsList.toArray(buttonsArr);
    dialog.setOptions(buttonsArr);
    // how long the image is on
    dialog.setTimeout(0);
    dialog.setSpeakDescription(false);
    dialog.setSpeakTitle(true);
    dialog.setDisplayDescription(true);
    dialog.setDisplayTitle(true);

    dialog.setFaceDisplay(
        new FaceDisplaySettings(FaceDisplaySettings.MODE_NONE, FaceDisplaySettings.POSITION_BOTTOM_LEFT, 0.30f));

    robios.alertDialog(new Gson().toJson(dialog));
    while (userInput.isEmpty()) {
      Thread.sleep(minDelay);
    }
    return userInput;
  }

  public void displayImage(String imageUrl, int duration) throws Exception {
    robios.displayImage(imageUrl, duration);
  }

  private static void onResultReceived(AlertDialogResult result) {
    String currentDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    System.out.printf("[%s] [AlertDialog]: \"%s\"\n", currentDate, result.getContent());

    userInput = result.getContent();
  }

  public void playVideo(String videoUrl) {
    try {
      robios.displayVideo(videoUrl, 0, true, false); // videoUrl, displayTime, audioEnabled, loop
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void playVideo(String videoUrl, int videoDuration) {
    try {
      robios.displayVideo(videoUrl, videoDuration, true, false); // videoUrl, displayTime, audioEnabled, loop
      Thread.sleep(videoDuration * 1000 + minDelay);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void say(String msg) {
    try {
      robios.say(msg);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void close() {
    try {
      robios.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void setExpression(String expression) {
    try {
      robios.setExpression(expression);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
