package io.humanrobotics.api.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.humanrobotics.api.Robios;
import io.humanrobotics.api.games.utils.Robot;

public class Puzzle {
  private static final Robot robot = new Robot();

  public static void confirmacao() throws Exception {
    String resposta = robot.ask("Deu tudo certo?", new String[] { "sim", "não" });
    while (resposta.equals("não")) {
      robot.say("Vou esperar um pouco mais por você. Se precisar chame alguém da nossa equipe para te ajudar!");
      robot.setExpression("happy");
      Thread.sleep(5000);
      robot.setExpression("normal");
      resposta = robot.ask("Deu tudo certo?", new String[] { "sim", "não" });
    }
    robot.say("OK. Então vamos conitnuar!");
    Thread.sleep(2000);
  }

  public static void main(String[] args) throws Exception {
    robot.say("Olá, hoje vamos jogar um jogo de quebra-cabeça!");
    robot.say("Vou te ensinar a abrir o jogo.");
    robot.say("Primeiramente, vamos ligar nosso tablet!");
    robot.say("Dê dois toques na tela e ele ligará");
    Thread.sleep(2000);

    for (int i = 0; i < 3; i++) {
      if (i == 0) {
        robot.say("Agora, vamos encontrar o aplicativo que vamos usar.");
        Thread.sleep(1000);
        robot.say("Procure por esse ícone.");
        robot.say("Toque apenas uma vez!");
        robot.displayImage(
            "https://robots.humanrobotics.ai/cdn/get?fileName=cdn/9e2b3d3a-4702-4a70-b6d1-f67124f7c52c/1cbbd722-2163-45ce-a3e3-9b3eaa2a1426/df35ef1f-097b-474d-8142-91c8a253bed2_23c6b754-51ae-43b8-a54f-e841c080062e.jpg",
            10);
        Thread.sleep(13000);
        robot.say("O aplicativo foi aberto corretamente?");
      } else {
        robot.say("Vamos tentar mais uma vez!");
        Thread.sleep(1500);
        robot.say("Procure por esse ícone e toque!");
        robot.displayImage(
            "https://robots.humanrobotics.ai/cdn/get?fileName=cdn/9e2b3d3a-4702-4a70-b6d1-f67124f7c52c/1cbbd722-2163-45ce-a3e3-9b3eaa2a1426/df35ef1f-097b-474d-8142-91c8a253bed2_23c6b754-51ae-43b8-a54f-e841c080062e.jpg",
            14);
        Thread.sleep(18000);
        robot.say("Se conseguiu abrir o aplicativo diga: SIM!");
      }
      String resp = robot.listen();
      if (resp.equals("não")) {
        break;
      }
    }

    Thread.sleep(2000);
    confirmacao();

    robot.say("Toque no coraçãozinho que há na tela.");
    robot.displayImage(
        "https://robots.humanrobotics.ai/cdn/get?fileName=cdn/9e2b3d3a-4702-4a70-b6d1-f67124f7c52c/1cbbd722-2163-45ce-a3e3-9b3eaa2a1426/f7588100-63c8-4613-b9ab-f6d481f1fb98_58a7713f-25e7-4dfa-b008-c86cd05e97c1.jpg",
        12);
    Thread.sleep(15000);
    robot.say("Se tudo der certo você estará aqui!");
    robot.displayImage(
        "https://robots.humanrobotics.ai/cdn/get?fileName=cdn/e4605dd5-5241-4030-a034-2c6fb798289f/476d9557-a143-4e6a-b6b1-a358572b149c/4e6c01ed-65c3-492c-ae16-01537bd0be07_1.jpg",
        9);
    Thread.sleep(11000);
    confirmacao();

    robot.say("Agora é só selecionar a imagem do quebra-cabeça!");
    Thread.sleep(1000);
    robot.displayImage(
        "https://robots.humanrobotics.ai/cdn/get?fileName=cdn/e4605dd5-5241-4030-a034-2c6fb798289f/476d9557-a143-4e6a-b6b1-a358572b149c/cee4f109-3e4f-4f16-bc8e-ec574d29579d_foto1.png",
        12);
    Thread.sleep(14000);
    robot.say("Agora, se você está vendo essa tela para começar o quebra-cabeça, estaremos no lugar certo!");
    robot.displayImage("https://robots.humanrobotics.ai/cdn/get?fileName=cdn/e4605dd5-5241-4030-a034-2c6fb798289f/476d9557-a143-4e6a-b6b1-a358572b149c/0fcfe8f5-00ab-4e01-97db-b52feacad48f_1.jpg", 7);
    Thread.sleep(8500);
    confirmacao();

    robot.say("Toque no botão Abrir experimento");
    robot.displayImage("https://robots.humanrobotics.ai/cdn/get?fileName=cdn/e4605dd5-5241-4030-a034-2c6fb798289f/476d9557-a143-4e6a-b6b1-a358572b149c/288de113-87c0-4056-a590-fbc07265a4f6_foto1.png", 10);

    robot.say("Agora nessa tela você escolher uma obra, toque em qualquer uma");
    robot.displayImage("https://robots.humanrobotics.ai/cdn/get?fileName=cdn/e4605dd5-5241-4030-a034-2c6fb798289f/476d9557-a143-4e6a-b6b1-a358572b149c/31c0a718-45fa-4cf6-aeb5-11dbebadaed3_1.jpg", 10);
    confirmacao();

    robot.say("Para começar o jogo clique em Individual");
    robot.displayImage("https://robots.humanrobotics.ai/cdn/get?fileName=cdn/e4605dd5-5241-4030-a034-2c6fb798289f/476d9557-a143-4e6a-b6b1-a358572b149c/95c5d586-7729-4a92-a250-fe2d07fb0872_foto1.png", 10);

    confirmacao();
    robot.say("Agora é só jogar, toque e segura em uma peça para arrastá-la e montar o quebra-cabeça.");
    robot.say("Quando você terminar siga as instruções para jogar novamente ou sair do jogo.");
    robot.say("Se divirta!");
    robot.setExpression("happy");

    robot.close();
  }
}
