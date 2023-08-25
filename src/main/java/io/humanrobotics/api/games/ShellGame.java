package io.humanrobotics.api.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.humanrobotics.api.Robios;
import io.humanrobotics.api.games.utils.Robot;

class Level {
  private final int levelNumber;
  private final String videoUrl;
  private final int videoDuration;
  private final int correctAnswer;
  private final String videoResult;
  private final int videoResultDuration;

  public Level(int levelNumber, String videoUrl, int videoDuration, String videoResult, int videoResultDuration, int correctAnswer) {
    this.levelNumber = levelNumber;
    this.videoUrl = videoUrl;
    this.videoDuration = videoDuration;
    this.correctAnswer = correctAnswer;
    this.videoResult = videoResult;
    this.videoResultDuration = videoResultDuration;
  }

  public int getLevelNumber() {
    return levelNumber;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public int getVideoDuration() {
    return videoDuration;
  }

  public int getCorrectAnswer() {
    return correctAnswer;
  }

  public String getVideoResult() {
    return videoResult;
  }

  public int getVideoResultDuration() {
    return videoResultDuration;
  }
}

public class ShellGame {
  private final List<Level> levels;
  private final Random random;
  private static final Robot robot = new Robot();

  public ShellGame() {
    // add more easy levels as needed
    levels = new ArrayList<>();
    levels.add(new Level(1, "http://www.each.usp.br/petsi/wp-content/uploads/2023/07/ic-sarajane-11.mp4", 16, "http://www.each.usp.br/petsi/wp-content/uploads/2023/07/ic-sarajane-12.mp4", 4, 3));
    levels.add(new Level(2, "http://www.each.usp.br/petsi/wp-content/uploads/2023/07/ic-sarajane-21.mp4", 15, "http://www.each.usp.br/petsi/wp-content/uploads/2023/07/ic-sarajane-22.mp4", 6, 3));
    levels.add(new Level(3, "http://www.each.usp.br/petsi/wp-content/uploads/2023/07/ic-sarajane-31.mp4", 15, "http://www.each.usp.br/petsi/wp-content/uploads/2023/07/ic-sarajane-32.mp4", 6, 2));

    this.random = new Random();
  }

  public void play() throws Exception {
    robot.say("Olá! Bem-vindo ao jogo do copo!");

    // shuffle each level list
    Collections.shuffle(levels);

    int currentLevelIndex = 0;
    boolean completedAllLevels = false;

    while (!completedAllLevels) {
      Level currentLevel = levels.get(currentLevelIndex);
      boolean isGuessCorrect = playLevel(currentLevel);
      int tries = 1;
      while (!isGuessCorrect) {
        String isUserTrying = robot.ask("Gostaria de tentar de novo?", new String[] { "sim", "não" });
        if (tries == 5 || isUserTrying.equals("não")) {
          robot.say("Esse jogo é difícil mesmo, não desanime e tente de novo depois!");
          return;
        }

        tries++;
        isGuessCorrect = playLevel(currentLevel);
      }

      robot.playVideo(currentLevel.getVideoResult(), currentLevel.getVideoResultDuration());
      if (currentLevelIndex == levels.size() - 1) {
        completedAllLevels = true;
      }
      currentLevelIndex++;
    }

    robot.say("Parabéns, você completou todos os níveis! Obrigado por jogar!");

    robot.say("Fim de jogo!");
  }

  public int askCup() throws Exception {
    while (true) {
      String answer = robot.listen();
      Thread.sleep(Robot.minDelay);

      if (answer.contains("primeiro") || answer.contains("um")) {
        return 1;
      } else if (answer.contains("segundo") || answer.contains("dois")) {
        return 2;
      } else if (answer.contains("terceiro") || answer.contains("três")) {
        return 3;
      }

      robot.say("Não entendi");
    }
  }

  public boolean playLevel(Level currentLevel) throws Exception {
    robot.say("Nível " + currentLevel.getLevelNumber());
    robot.say("Assista ao vídeo com atenção: ");
    robot.playVideo(currentLevel.getVideoUrl(), currentLevel.getVideoDuration());
    String[] questions = { "Em qual copo a bola está:", "Qual copo é o certo?",
        "Você consegue adivinhar em qual copo está a bola?", "Qual copo contém a bola?",
        "Em qual copo você acha que a bola está?" };
    robot.say(questions[random.nextInt(questions.length)]);
    robot.say("O primeiro, o segundo ou o terceiro?");
    int guess = askCup();
    if (guess == currentLevel.getCorrectAnswer()) {
      String[] congratulationsMsgs = { "Incrível, você acertou!", "Muito bem, você acertou!",
          "Fantástico, você acertou!", "Maravilhoso, você acertou!",
          "Excelente, você acertou!", "Sensacional, você acertou!" };
      robot.say(congratulationsMsgs[random.nextInt(congratulationsMsgs.length)]);
      return true;
    } else {
      robot.say("Que pena, você errou...");
      return false;
    }
  }

  public static void main(String[] args) throws Exception {
    ShellGame game = new ShellGame();
    game.play();
    robot.close();
  }
}
