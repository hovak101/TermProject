package s25.cs151.application;

public class SemesterTimeSlotsBean {
    private final String fromHour; // Format: HH:mm
    private final String toHour;

    public SemesterTimeSlotsBean(String fromHour, String toHour) {
        // Validate that both times are in HH:mm format
        if (!fromHour.matches("\\d{1,2}:\\d{2} [APM]{2}") || !toHour.matches("\\d{1,2}:\\d{2} [APM]{2}")) {
            throw new IllegalArgumentException("Time must be in HH:mm AM/PM format.");
        }
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public String getFromHour() { return fromHour; }
    public String getToHour() { return toHour; }
}