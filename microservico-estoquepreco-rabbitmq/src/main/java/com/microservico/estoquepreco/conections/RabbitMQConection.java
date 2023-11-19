package com.microservico.estoquepreco.conections;
import com.microservico.estoquepreco.constantes.RabbitmqConstantes;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RabbitMQConection {
  private static final String NOME_EXCHANGE = "amq.direct";

  private final AmqpAdmin amqpAdmin;

  private String origemNome;

  private String destinoNome;

  @Autowired
  public RabbitMQConection(AmqpAdmin amqpAdmin) {
    this.amqpAdmin = amqpAdmin;

  }

  @Autowired
  private RabbitTemplate rabbitTemplate;

  private Queue fila(String nomeFila) {
    return new Queue(nomeFila, false, false, false);
  }

  public void enviarMensagemParaFila(String mensagem) {
    rabbitTemplate.convertAndSend(NOME_EXCHANGE, this.destinoNome, mensagem);
    //System.out.println("Mensagem enviada para a fila " + this.destinoNome + ": " + mensagem);
  }

  public String receberMensagemDaFila() {
    // Receber mensagem da fila associada a this.destinoNome
    Message mensagem = rabbitTemplate.receive(this.origemNome);

    // Verificar se a mensagem não é nula
    if (mensagem != null) {
      // Converter a mensagem para uma string (ou conforme necessário)
      String mensagemRecebida = new String(mensagem.getBody());
      System.out.println("Mensagem recebida da fila " + mensagemRecebida);
      return mensagemRecebida;
    } else {
      return null;
    }
  }

  private DirectExchange trocaDireta() {
    return new DirectExchange(NOME_EXCHANGE);
  }

  private Binding relacionamento(Queue fila, DirectExchange troca) {
    return new Binding(fila.getName(), Binding.DestinationType.QUEUE, troca.getName(), fila.getName(), null);
  }

  @PostConstruct public void nada(){
    System.out.println("Chat inciado...");

  }

  public void setDestino(String destinoNome){
    this.destinoNome = destinoNome;

    Queue filaDestino = this.fila(destinoNome);
    DirectExchange troca = this.trocaDireta();
    Binding ligacaoDestino = this.relacionamento(filaDestino, troca);

    // Criando as filas no rabbitmq
    this.amqpAdmin.declareQueue(filaDestino);

    this.amqpAdmin.declareExchange(troca);

    this.amqpAdmin.declareBinding(ligacaoDestino);

  }

  public void setOrigem(String origemNome){
    this.origemNome = origemNome;
  }


}
