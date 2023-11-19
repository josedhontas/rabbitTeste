package com.microservico.estoquepreco;

import com.microservico.estoquepreco.conections.RabbitMQConection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class EstoqueprecoApplication {

  public static void main(String[] args) {
    // Inicie a aplicação Spring Boot
    ConfigurableApplicationContext context = SpringApplication.run(EstoqueprecoApplication.class, args);

    // Obtenha a instância de RabbitMQConection do contexto
    RabbitMQConection rabbitMQConnection = context.getBean(RabbitMQConection.class);

    // Utilize um Scanner para obter entrada do usuário
    Scanner scanner = new Scanner(System.in);
    System.out.println("Seu nome");
    String nome0 = scanner.nextLine();
    rabbitMQConnection.setOrigem(nome0);
    while (true) {
      System.out.println("Destinatario");
      String nome1 = scanner.nextLine();
      rabbitMQConnection.setDestino(nome1);
      System.out.println("Digite a mensagem");
      String menssagem = scanner.nextLine();
      rabbitMQConnection.enviarMensagemParaFila(menssagem);
      rabbitMQConnection.receberMensagemDaFila();

    }
  }
}
