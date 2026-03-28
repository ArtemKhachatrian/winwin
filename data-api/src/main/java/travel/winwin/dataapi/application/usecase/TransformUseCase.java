package travel.winwin.dataapi.application.usecase;

import org.springframework.stereotype.Component;

@Component
public class TransformUseCase {

    public String execute(String text) {
        return text.toLowerCase().replaceAll("\\s", "-");
    }
}
