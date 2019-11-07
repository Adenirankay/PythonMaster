 //    formNxp configuration
    @Bean
    DirectExchange formNxpExchange() {
        return new DirectExchange(formNxpExchangeName);
    }

    @Bean
    Queue formNxpIncomingQueue() {
        return QueueBuilder.durable(formNxpIncomingQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", Constatnts.FORM_Q_DEAD_LETTER_QUEUE_NAME)
                .build();
    }
    @Bean
    Binding formNxpBinding() {
        return BindingBuilder.bind(formNxpIncomingQueue()).to(formNxpExchange()).with(formNxpRoutingKey);
    }

    @Bean
    Queue formNxpDeadLetterQueue() {
        return QueueBuilder.durable(Constatnts.FORM_Q_DEAD_LETTER_QUEUE_NAME).build();
    }


    //FormQ updateTms with finacle values
    @Bean
    DirectExchange updateTmsFormNxpexchange() {
        return new DirectExchange(updateTmsFormNxpExchangeName);
    }

    @Bean
    Queue updateTmsFormNxpincomingQueue() {
        return QueueBuilder.durable(updateTmsFormNxpIncomingQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", Constatnts.UPDATE_TMS_FORM_NXP_DEAD_LETTER_QUEUE_NAME)
                .build();
    }

    @Bean
    Binding updateTmsFormNxpbinding() {
        return BindingBuilder.bind(updateTmsFormNxpincomingQueue()).to(updateTmsFormNxpexchange()).with(updateTmsFormNxpRoutingKey);

    }

    @Bean
    Queue updateTmsFormNxpdeadLetterQueue() {
        return QueueBuilder.durable(Constatnts.UPDATE_TMS_FORM_NXP_DEAD_LETTER_QUEUE_NAME).build();
    }
