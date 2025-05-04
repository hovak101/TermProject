package s25.cs151.application.MODEL;

import java.util.List;

public interface SemesterTimeSlotsDaoInt {
    void storeTimeSlot(String fromHour, String toHour);
    List<SemesterTimeSlotsBean> getTimeSlots();
}
