package ir.eyrsa.app.openai.Model;

public class BodySendModel {
    String model,prompt;
    int max_tokens;
    double temperature;

    public BodySendModel(String model, String prompt, int max_tokens, double temperature) {
        this.model = model;
        this.prompt = prompt;
        this.max_tokens = max_tokens;
        this.temperature = temperature;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
