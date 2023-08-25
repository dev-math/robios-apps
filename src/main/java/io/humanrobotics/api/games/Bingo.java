package io.humanrobotics.api.games;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

import ai.humanrobotics.messaging.types.AlertDialog;
import ai.humanrobotics.messaging.types.AlertDialogOption;
import ai.humanrobotics.messaging.types.AlertDialogResult;
import ai.humanrobotics.messaging.types.FaceDisplaySettings;
import io.humanrobotics.api.Robios;
import io.humanrobotics.api.RobiosApi;
import io.humanrobotics.api.RobiosConfig;
import io.humanrobotics.api.exception.RobiosException;
import io.humanrobotics.api.listeners.AlertDialogListener;
import io.humanrobotics.api.listeners.VoiceRecognitionListener;
import io.humanrobotics.api.models.constants.Expression;

public class Bingo {
  private static final String apiKey = "...";
  private static final String robotId = "...";
  public static Robios robios;
  private static final String errorMsg = "Não entendi.";
  private static long minDelay = 6000; // Minimum number of milliseconds to wait before listening to the user.
  public static String userInput = "";

  static {
    try {
      robios = RobiosApi.get(apiKey, RobiosConfig.forCloud(robotId));
    } catch (RobiosException e) {
      throw new RuntimeException(e);
    }
  }

  public static void playVideo(String videoUrl, int videoDuration) {
    try {
      robios.displayVideo(videoUrl, videoDuration, true, false); // videoUrl, displayTime, audioEnabled, loop
      Thread.sleep(videoDuration + minDelay);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    // Add the callback received the result of the recognition
    robios.addVoiceRecognitionCallback(Bingo::onUserTextReceived);
    // Add a callback to receive the user click on the image
    robios.addAlertDialogCallback(Bingo::onResultReceived);

    robios.say("Olá a todas e todos!");
    robios.say("Estou feliz por poder jogar esse bingo com vocês aqui na Ondina Lobo.");

    robios.setExpression(Expression.HAPPY);
    Thread.sleep(2000);
    robios.setExpression(Expression.NORMAL);

    robios.say("Vamos às regras do jogo:");
    robios.say("Vou cantar as bolas e vocês conferem a cartela.");
    robios.say("Quem completar primeiro uma linha, coluna, diagonal, ou uma cartela cheia...");
    robios.say("grita: BINGO!");

    robios.setExpression(Expression.SCARED);
    Thread.sleep(2000);
    robios.setExpression(Expression.NORMAL);

    robios.say("Espero que tenhamos um ótimo jogo!");
    robios.say("Boa-sorte e vamos começar.");

    String playAgain;
    boolean firstPlay = true;
    do {
      bingo(new boolean[] { true, true, true, true }, firstPlay); // linha, coluna, diagonal, cartela cheia
      firstPlay = false;
      playAgain = ask("Você gostaria de começar um novo jogo?", new String[] { "sim",
          "não" });
    } while (playAgain.equals("sim"));
    robios.say("Obrigado a todos por participarem da nossa jogatina de bingo!");
    robios.setExpression(Expression.HAPPY);
    robios.say("Espero que tenham se divertido tanto quanto eu.");
    robios.say("Até mais!");
    robios.close();
  }

  public static void bingo(boolean[] metasDisponiveis, boolean firstPlay) throws Exception {
    Random random = new Random();
    boolean isGameOver = false;
    List<Integer> drawnNumbers = new ArrayList<>();

    String[] objetivos = { "linha", "coluna", "diagonal", "cartela cheia" };

    boolean alguemVenceu = false;

    if (!firstPlay) {
      alguemVenceu = true;
    }

    while (!isGameOver) {
      if (alguemVenceu) {
        String mensagemRegras = "Lembrando o que ainda está valendo ganhar é: ";

        for (int i = 0; i < objetivos.length; i++) {
          if (metasDisponiveis[i]) {
            if (i != 0 && i == objetivos.length - 1) {
              mensagemRegras += "e " + objetivos[i] + ".";
            } else if (i == objetivos.length - 1) {
              mensagemRegras += objetivos[i] + ".";
            } else if (i == objetivos.length - 2) {
              mensagemRegras += objetivos[i] + " ";
            } else {
              mensagemRegras += objetivos[i] + ", ";
            }
          }
        }

        robios.say(mensagemRegras);
        alguemVenceu = false;
      }

      int randomNumber = random.nextInt(75) + 1;
      while (drawnNumbers.contains(randomNumber)) {
        randomNumber = random.nextInt(75) + 1;
      }

      drawnNumbers.add(randomNumber);

      Thread.sleep(3500); // Delay between the spoke number and the game start

      boolean knowNumber = true;

      if (randomNumber == 1) {
        robios.say("Começou o jogo.");
      } else if (randomNumber == 9) {
        robios.say("Pingo no pé.");
      } else if (randomNumber == 10) {
        robios.say("Craque da bola.");
      } else if (randomNumber == 11) {
        robios.say("Um atrás do outro.");
      } else if (randomNumber == 13) {
        robios.say("Deu azar.");
      } else if (randomNumber == 22) {
        robios.say("Dois patinhos na lagoa.");
      } else if (randomNumber == 31) {
        robios.say("Preparem os fogos.");
      } else if (randomNumber == 45) {
        robios.say("Fim do Primeiro Tempo.");
      } else if (randomNumber == 51) {
        robios.say("Uma boa ideia.");
      } else if (randomNumber == 66) {
        robios.say("Só falta o sapato.");
      } else if (randomNumber == 69) {
        robios.say("Um pra cima e outro pra baixo.");
      } else if (randomNumber == 75) {
        robios.say("Terminou o jogo.");
      } else {
        robios.say(String.valueOf(randomNumber));
        knowNumber = false;
      }

      if (knowNumber) {
        robios.say("É o " + randomNumber);
      }

      Thread.sleep(3000); // delay for the number speak

      for (int i = 0; i < 2; i++) {
        String[] quotes = { "Repetindo, ", "Mais uma vez, ", "Anota aí, " };
        robios.say(quotes[random.nextInt(quotes.length)] + randomNumber);
        Thread.sleep(3500); // delay between the answers
      }

      if (drawnNumbers.size() >= 15 && drawnNumbers.size() % 5 == 0) {
        String[] jokeQuotes = { "Tá dificil esse jogo ein?", "Pelo jeito vocês estão sem sorte hoje!" };
        robios.say(jokeQuotes[random.nextInt(jokeQuotes.length)]);
      }

      if (drawnNumbers.size() > 3) {
        String[] quotes = { "Continuando... ", "Tudo bem! Vamos lá... " };
        String respostaVencedor;
        if (random.nextInt(2) == 0) {
          respostaVencedor = ask("Alguém venceu?", new String[] { "sim", "não", "bingo" });
        } else {
          String valendo = "Alguém completou ";
          for (int i = 0; i < objetivos.length; i++) {
            if (metasDisponiveis[i]) {
              if (i != 0 && i == objetivos.length - 1) {
                valendo += "ou " + objetivos[i];
              } else if (i == objetivos.length - 1) {
                valendo += objetivos[i];
              } else if (i == objetivos.length - 2) {
                valendo += objetivos[i] + " ";
              } else {
                valendo += objetivos[i] + ", ";
              }
            }
          }

          valendo += "?";

          Thread.sleep(500);
          respostaVencedor = displayImage(valendo, null, new String[] { "sim", "não" });
        }

        if (respostaVencedor.equals("sim") || respostaVencedor.equals("bingo")) {
          robios.setExpression("scared");
          Thread.sleep(2000);
          robios.setExpression("normal");

          // print the numbers already choosen
          String urlNumeros = "https://placehold.jp/50px/ffffff/000000/1920x1080.png?text=N%C3%BAmeros%20sorteados%3A";
          for (Integer num : drawnNumbers) {
            urlNumeros = urlNumeros + "%20%3E%3E%20" + String.valueOf(num);
          }

          String aindaValendo = "";
          for (int i = 0; i < objetivos.length; i++) {
            if (metasDisponiveis[i]) {
              aindaValendo += objetivos[i] + ";";
            }
          }

          aindaValendo += "ninguém ganhou";

          String btnClicked = displayImage("Confirme a opção em que houve um ganhador: ", urlNumeros,
              aindaValendo.split(";"));

          if (!btnClicked.equals("ninguém ganhou")) {
            alguemVenceu = true;

            if (btnClicked.equals("linha")) {
              metasDisponiveis[0] = false;
            } else if (btnClicked.equals("coluna")) {
              metasDisponiveis[1] = false;
            } else if (btnClicked.equals("diagonal")) {
              metasDisponiveis[2] = false;
            } else if (btnClicked.equals("cartela cheia")) {
              metasDisponiveis[3] = false;
              isGameOver = true;
            }

            robios.say("Parabéns para quem venceu na " + btnClicked + "!");

            // check if someone has already won the whole card
            if (metasDisponiveis[3]) {
              String continuarJogo = ask("Você gostaria de continuar o jogo nessa cartela?",
                  new String[] { "sim", "não" });
              if (continuarJogo.equals("não")) {
                return;
              }
            }
          } else {
            String alertQuotes[] = { "Vocês estão comendo bola?", "Tão dormindo aí?" };
            robios.say(alertQuotes[random.nextInt(alertQuotes.length)]);
          }
        }

        if (!isGameOver) {
          robios.say(quotes[random.nextInt(quotes.length)]);
        }
      }
    }
  }

  public static String ask(String question, String[] answers) throws Exception {
    robios.say(question);
    return listen(answers);
  }

  public static void onUserTextReceived(String userAnswer) {
    String currentDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    System.out.printf("[%s] [VoiceRecognition]: \"%s\"\n", currentDate, userAnswer);

    userInput = userAnswer;
  }

  public static String listen(String[] inputOptions) throws Exception {
    userInput = "";
    while (true) {
      robios.listen();
      Thread.sleep(minDelay);

      for (String inputOption : inputOptions) {
        if (inputOption.equals(userInput)) {
          return userInput;
        }
      }
      robios.say(errorMsg);
    }
  }

  public static String listen() throws Exception {
    userInput = "";
    while (userInput.isEmpty()) {
      robios.listen();
      Thread.sleep(minDelay);
    }
    return userInput;
  }

  public static String displayImage(String headerText, String imageUrl, String[] btnOptions) throws Exception {
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

  public static void onResultReceived(AlertDialogResult result) {
    String currentDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    System.out.printf("[%s] [AlertDialog]: \"%s\"\n", currentDate, result.getContent());

    userInput = result.getContent();
  }
}
