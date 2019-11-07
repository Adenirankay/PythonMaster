  @Bean
    Binding binding() {
        return BindingBuilder.bind(incomingQueue()).to(exchange()).with(routingKey);
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(Constatnts.DEAD_LETTER_QUEUE_NAME).build();
    }


    //FormA updateTms with finacle values
    @Bean
    DirectExchange updateTmsFormAexchange() {
        return new DirectExchange(updateTmsFormAExchangeName);
    }

    @Bean
    Queue updateTmsFormAincomingQueue() {
        return QueueBuilder.durable(updateTmsFormAIncomingQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", Constatnts.UPDATE_TMS_FORMA_DEAD_LETTER_QUEUE_NAME)
                .build();
    }

    @Bean
    Binding updateTmsFormAbinding() {
        return BindingBuilder.bind(updateTmsFormAincomingQueue()).to(updateTmsFormAexchange()).with(updateTmsFormARoutingKey);

    }

    @Bean
    Queue updateTmsFormAdeadLetterQueue() {
        return QueueBuilder.durable(Constatnts.UPDATE_TMS_FORMA_DEAD_LETTER_QUEUE_NAME).build();
    }
