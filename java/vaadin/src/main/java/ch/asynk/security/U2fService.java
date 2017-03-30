package ch.asynk.security;

import java.util.List;

import com.yubico.u2f.data.DeviceRegistration;

public interface U2fService
{
    public boolean hasDeviceRegistrations(final String userId);
    public List<DeviceRegistration> getDeviceRegistrations(final String userId);
    public void addDeviceRegistration(final String userId, final DeviceRegistration deviceRegistration);
}
