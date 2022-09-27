package alcemirjunior.github.customer;

import alcemirjunior.github.clients.fraud.FraudCheckResponse;
import alcemirjunior.github.clients.fraud.FraudClient;
import alcemirjunior.github.clients.notification.NotificationClient;
import alcemirjunior.github.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(
        CustomerRepository customerRepository,
        RestTemplate restTemplate,
        FraudClient fraudClient,
        NotificationClient notificationClient
) {

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        //todo check if email valid
        //todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalArgumentException("fraudster");
        }
        //todo: tornar assincrono, adicionar uma fila
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, testing my message", customer.getFirstName())
                )
        );
    }
}
