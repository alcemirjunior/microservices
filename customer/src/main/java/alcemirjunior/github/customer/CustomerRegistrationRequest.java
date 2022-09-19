package alcemirjunior.github.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
