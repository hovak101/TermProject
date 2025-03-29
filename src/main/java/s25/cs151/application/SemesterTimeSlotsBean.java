package s25.cs151.application;

public class SemesterTimeSlotsBean {
    private String fromHour; // Format: HH:mm
    private String toHour;

    public SemesterTimeSlotsBean(String fromHour, String toHour) {
        if (!fromHour.matches("\\d{2}:\\d{2}") || !toHour.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Time must be in HH:mm format.");
        }
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public String getFromHour() { return fromHour; }
    public String getToHour() { return toHour; }

    public void setFromHour(String fromHour) { this.fromHour = fromHour; }
    public void setToHour(String toHour) { this.toHour = toHour; }
}