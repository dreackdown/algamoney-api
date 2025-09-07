package dev.hugofaria.algamoney.api.config;

import dev.hugofaria.algamoney.api.config.property.AlgamoneyApiProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private final AlgamoneyApiProperty property;

    public MailConfig(AlgamoneyApiProperty property) {
        this.property = property;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.connectiontimeout", 10000);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(props);
        mailSender.setHost(property.getMail().getHost());
        mailSender.setPort(property.getMail().getPort());
        mailSender.setUsername(property.getMail().getUsername());
        mailSender.setPassword(property.getMail().getPassword());

        return mailSender;
    }
}