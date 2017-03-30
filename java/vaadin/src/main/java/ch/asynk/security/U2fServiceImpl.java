package ch.asynk.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yubico.u2f.data.DeviceRegistration;

public class U2fServiceImpl implements U2fService
{
    private HashMap<String, List<DeviceRegistration>> store;

    public U2fServiceImpl()
    {
        store = new HashMap<String, List<DeviceRegistration>>();
    }

    public boolean hasDeviceRegistrations(final String userId)
    {
        return store.containsKey(userId);
    }

    public List<DeviceRegistration> getDeviceRegistrations(final String userId)
    {
        return get(userId);
    }

    public void addDeviceRegistration(final String userId, final DeviceRegistration deviceRegistration)
    {
        List<DeviceRegistration> registrations = get(userId);
        registrations.add(deviceRegistration);
        store.put(userId, registrations);
    }

    private List<DeviceRegistration> get(final String userId)
    {
        List<DeviceRegistration> registrations = store.get(userId);
        if (registrations == null)
            registrations = new ArrayList<DeviceRegistration>();
        return registrations;
    }
}
